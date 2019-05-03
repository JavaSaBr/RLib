package com.ss.rlib.network.impl;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.*;
import com.ss.rlib.network.packet.*;
import com.ss.rlib.network.packet.impl.AbstractReusableWritablePacket;
import com.ss.rlib.common.util.linkedlist.LinkedList;
import com.ss.rlib.common.util.linkedlist.LinkedListFactory;
import com.ss.rlib.network.packet.impl.DefaultPacketReader;
import com.ss.rlib.network.packet.impl.DefaultPacketWriter;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.StampedLock;

/**
 * The base implementation of {@link AsyncConnection}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractAsyncConnection implements AsyncConnection {

    protected static final Logger LOGGER = LoggerManager.getLogger(AsyncNetwork.class);

    protected static final int PACKET_SIZE_BYTES_COUNT = 2;

    @Getter
    protected final AsyncNetwork network;

    protected final AsynchronousSocketChannel channel;
    protected final PacketReader packetReader;
    protected final PacketWriter packetWriter;
    protected final NetworkCrypt crypt;

    protected final ByteBuffer readBuffer;
    protected final ByteBuffer swapBuffer;
    protected final ByteBuffer writeBuffer;
    protected final ByteBuffer waitBuffer;

    protected final AtomicBoolean isWriting;
    protected final AtomicBoolean closed;
    protected final NetworkConfig config;

    protected final LinkedList<WritablePacket> waitPackets;
    protected final StampedLock lock;

    @Getter
    protected final ConnectionOwner owner;

    @Getter
    protected volatile long lastActivity;

    public AbstractAsyncConnection(
        @NotNull AsyncNetwork network,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull Class<? extends WritablePacket> writablePacketType,
        @NotNull ConnectionOwner owner
    ) {
        this.owner = owner;
        this.lock = new StampedLock();
        this.channel = channel;
        this.waitPackets = LinkedListFactory.newLinkedList(writablePacketType);
        this.network = network;
        this.readBuffer = network.takeReadBuffer();
        this.readBuffer.clear();
        this.writeBuffer = network.takeWriteBuffer();
        this.config = network.getConfig();
        this.isWriting = new AtomicBoolean(false);
        this.closed = new AtomicBoolean(false);
        this.waitBuffer = network.takeWaitBuffer();
        this.waitBuffer.flip();
        this.swapBuffer = network.takeWaitBuffer();
        this.swapBuffer.flip();
        this.packetReader = createPacketReader();
        this.packetWriter = createPacketWriter();
    }

    protected @NotNull PacketReader createPacketReader() {
        return new DefaultPacketReader(
            readBuffer,
            swapBuffer,
            waitBuffer,
            this,
            this::updateLastActivity
        );
    }

    protected @NotNull PacketWriter createPacketWriter() {
        return new DefaultPacketWriter(
            this,
            channel,
            writeBuffer,
            waitBuffer,
            this::updateLastActivity,
            this::nextPacketToWrite
        );
    }

    protected @Nullable WritablePacket nextPacketToWrite() {
        long stamp = lock.writeLock();
        try {
            return waitPackets.poll();
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Override
    public int getPacketSizeByteCount() {
        return PACKET_SIZE_BYTES_COUNT;
    }

    @Override
    public void close() {

        if (!closed.compareAndSet(false, true)) {
            return;
        }

        try {
            doClose();
        } catch (IOException e) {
            LOGGER.warning(this, e);
        }
    }

    /**
     * Does the process of closing this connection.
     *
     * @throws IOException if found some problem during closing a channel.
     */
    protected void doClose() throws IOException {

        if (channel.isOpen()) {
            channel.close();
        }

        clearWaitPackets();
        getNetwork().putReadBuffer(readBuffer)
            .putWriteBuffer(writeBuffer)
            .putWaitBuffer(waitBuffer)
            .putWaitBuffer(swapBuffer);

        owner.connectionWasClosed();
    }

    @Override
    public @NotNull String getRemoteAddress() {

        try {
            return String.valueOf(channel.getRemoteAddress());
        } catch (IOException e) {
            LOGGER.warning(this, e);
        }

        return "unknown";
    }

    /**
     * Update the time of last activity.
     */
    protected void updateLastActivity() {
        this.lastActivity = System.currentTimeMillis();
    }

    @Override
    public boolean isClosed() {
        return closed.get();
    }

    @Override
    public final void sendPacket(@NotNull WritablePacket packet) {

        if (isClosed()) {
            return;
        }

        long stamp = lock.writeLock();
        try {
            waitPackets.add(packet);
        } finally {
            lock.unlockWrite(stamp);
        }

        packetWriter.writeNextPacket();
    }

    @Override
    public final void startRead() {
        packetReader.startRead();
    }

    @Override
    public @NotNull NetworkCrypt getCrypt() {
        return crypt;
    }

    /**
     * Clear waited packets.
     */
    protected void clearWaitPackets() {

        long stamp = lock.writeLock();
        try {

            waitPackets.applyIfType(
                AbstractReusableWritablePacket.class,
                AbstractReusableWritablePacket::forceComplete
            );

            waitPackets.clear();

        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "network=" + network + ", waitPackets=" + waitPackets + ", channel=" +
                channel + ", isWriting=" + isWriting + ", closed=" + closed + ", lastActivity=" + lastActivity + '}';
    }
}
