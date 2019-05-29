//package com.ss.rlib.network.impl;
//
//import com.ss.rlib.common.util.array.Array;
//import com.ss.rlib.common.util.array.ArrayFactory;
//import com.ss.rlib.common.util.linkedlist.LinkedList;
//import com.ss.rlib.common.util.linkedlist.LinkedListFactory;
//import com.ss.rlib.logger.api.Logger;
//import com.ss.rlib.logger.api.LoggerManager;
//import com.ss.rlib.network.*;
//import com.ss.rlib.network.packet.PacketReader;
//import com.ss.rlib.network.packet.PacketWriter;
//import com.ss.rlib.network.packet.ReadablePacket;
//import com.ss.rlib.network.packet.WritablePacket;
//import com.ss.rlib.network.packet.impl.AbstractPacketReader;
//import com.ss.rlib.network.packet.impl.DefaultPacketWriter;
//import lombok.Getter;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.channels.AsynchronousSocketChannel;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.locks.StampedLock;
//import java.util.function.Consumer;
//
///**
// * The base implementation of {@link Connection}.
// *
// * @author JavaSaBr
// */
//public abstract class AbstractConnectionTemp<R extends ReadablePacket, W extends WritablePacket> implements
//    Connection<R, W> {
//
//    private static final Logger LOGGER = LoggerManager.getLogger(Network.class);
//
//    protected static final int PACKET_DATA_LENGTH_HEADER_SIZE = 2;
//    protected static final int PACKET_ID_HEADER_SIZE = 2;
//
//    @Getter
//    protected final Network<? extends AbstractConnectionTemp<R, W>> network;
//
//    @Getter
//    protected final NetworkCryptor crypt;
//
//    protected final AsynchronousSocketChannel channel;
//    protected final PacketReader packetReader;
//    protected final PacketWriter packetWriter;
//
//    protected final ByteBuffer readBuffer;
//    protected final ByteBuffer readPendingBuffer;
//    protected final ByteBuffer readDecryptedBuffer;
//    protected final ByteBuffer writeBuffer;
//    protected final ByteBuffer writeEncryptedBuffer;
//
//    protected final AtomicBoolean isWriting;
//    protected final AtomicBoolean closed;
//    protected final NetworkConfig config;
//
//    protected final LinkedList<WritablePacket> pendingPackets;
//    protected final StampedLock lock;
//
//    protected final Array<Consumer<? extends ReadablePacket>> readPacketHandlers;
//
//    @Getter
//    protected volatile long lastActivity;
//
//    public AbstractConnectionTemp(
//        @NotNull Network<? extends AbstractConnectionTemp<R, W>> network,
//        @NotNull AsynchronousSocketChannel channel,
//        @NotNull NetworkCryptor crypt,
//        @NotNull NetworkConfig config,
//        @NotNull BufferAllocator allocator
//    ) {
//        this.config = config;
//        this.lock = new StampedLock();
//        this.crypt = crypt;
//        this.channel = channel;
//        this.pendingPackets = LinkedListFactory.newLinkedList(WritablePacket.class);
//        this.network = network;
//        this.isWriting = new AtomicBoolean(false);
//        this.closed = new AtomicBoolean(false);
//        this.readBuffer = allocator.takeReadBuffer();
//        this.readPendingBuffer = allocator.takePendingBuffer();
//        this.readDecryptedBuffer = allocator.takeReadBuffer();
//        this.writeBuffer = allocator.takeWriteBuffer();
//        this.writeEncryptedBuffer = allocator.takeWriteBuffer();
//        this.packetReader = createPacketReader();
//        this.packetWriter = createPacketWriter();
//        this.readPacketHandlers = ArrayFactory.newCopyOnModifyArray(Consumer.class);
//    }
//
//    protected @NotNull PacketReader createPacketReader() {
//        return new AbstractPacketReader(
//            this,
//            channel,
//            readBuffer,
//            readPendingBuffer,
//            readDecryptedBuffer,
//            getMaxPacketsByRead(),
//            this::updateLastActivity,
//            this::handleReadPacket
//        );
//    }
//
//    protected void handleReadPacket(@NotNull ReadablePacket packet, @NotNull ByteBuffer buffer) {
//
//        packet.read(owner, buffer);
//    }
//
//    protected int getMaxPacketsByRead() {
//        return 100;
//    }
//
//    protected @NotNull PacketWriter createPacketWriter() {
//        return new DefaultPacketWriter(
//            this,
//            channel,
//            writeBuffer,
//            writeEncryptedBuffer,
//            this::updateLastActivity,
//            this::nextPacketToWrite, packetLengthHeaderSize
//        );
//    }
//
//    protected @Nullable WritablePacket nextPacketToWrite() {
//        long stamp = lock.writeLock();
//        try {
//            return pendingPackets.poll();
//        } finally {
//            lock.unlockWrite(stamp);
//        }
//    }
//
//    @Override
//    public int getPacketLengthHeaderSize() {
//        return PACKET_DATA_LENGTH_HEADER_SIZE;
//    }
//
//    @Override
//    public int getPacketIdHeaderSize() {
//        return PACKET_ID_HEADER_SIZE;
//    }
//
//    @Override
//    public void close() {
//
//        if (!closed.compareAndSet(false, true)) {
//            return;
//        }
//
//        try {
//            doClose();
//        } catch (IOException e) {
//            LOGGER.warning(this, e);
//        }
//    }
//
//    /**
//     * Does the process of closing this connection.
//     *
//     * @throws IOException if found some problem during closing a channel.
//     */
//    protected void doClose() throws IOException {
//
//        if (channel.isOpen()) {
//            channel.close();
//        }
//
//        clearWaitPackets();
//
//        getNetwork().putReadBuffer(readBuffer)
//            .putReadBuffer(readDecryptedBuffer)
//            .putPendingBuffer(readPendingBuffer)
//            .putWriteBuffer(writeBuffer)
//            .putWriteBuffer(writeEncryptedBuffer);
//
//        owner.connectionWasClosed();
//    }
//
//    @Override
//    public @NotNull String getRemoteAddress() {
//
//        try {
//            return String.valueOf(channel.getRemoteAddress());
//        } catch (IOException e) {
//            LOGGER.warning(this, e);
//        }
//
//        return "unknown";
//    }
//
//    /**
//     * Update the time of last activity.
//     */
//    protected void updateLastActivity() {
//        this.lastActivity = System.currentTimeMillis();
//    }
//
//    @Override
//    public boolean isClosed() {
//        return closed.get();
//    }
//
//    @Override
//    public final void sendPacket(@NotNull WritablePacket packet) {
//
//        if (isClosed()) {
//            return;
//        }
//
//        long stamp = lock.writeLock();
//        try {
//            pendingPackets.add(packet);
//        } finally {
//            lock.unlockWrite(stamp);
//        }
//
//        packetWriter.writeNextPacket();
//    }
//
//    @Override
//    public void startRead() {
//        packetReader.startRead();
//    }
//
//    /**
//     * Clear waited packets.
//     */
//    protected void clearWaitPackets() {
//
//        long stamp = lock.writeLock();
//        try {
//            doClearWaitPackets();
//        } finally {
//            lock.unlockWrite(stamp);
//        }
//    }
//
//    protected void doClearWaitPackets() {
//        pendingPackets.clear();
//    }
//
//    @Override
//    public String toString() {
//        return getClass().getSimpleName() + "{" + "network=" + network + ", pendingPackets=" + pendingPackets + ", channel=" +
//                channel + ", isWriting=" + isWriting + ", closed=" + closed + ", lastActivity=" + lastActivity + '}';
//    }
//}
