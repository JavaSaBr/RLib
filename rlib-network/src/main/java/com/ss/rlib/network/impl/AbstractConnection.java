package com.ss.rlib.network.impl;

import static com.ss.rlib.common.util.Utils.safeGet;
import static com.ss.rlib.common.util.Utils.unchecked;
import static com.ss.rlib.network.util.NetworkUtils.getSocketAddress;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.*;
import com.ss.rlib.network.packet.*;
import com.ss.rlib.common.util.linkedlist.LinkedList;
import com.ss.rlib.common.util.linkedlist.LinkedListFactory;
import com.ss.rlib.network.packet.impl.DefaultPacketWriter;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.StampedLock;
import java.util.function.BiConsumer;

/**
 * The base implementation of {@link Connection}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractConnection<R extends ReadablePacket, W extends WritablePacket> implements
    Connection<R, W> {

    private static final Logger LOGGER = LoggerManager.getLogger(AbstractConnection.class);

    @Getter
    protected final NetworkCryptor crypt;

    protected final Network<? extends Connection<R, W>> network;
    protected final BufferAllocator bufferAllocator;

    protected final AsynchronousSocketChannel channel;
    protected final PacketReader packetReader;
    protected final PacketWriter packetWriter;

    protected final AtomicBoolean isWriting;
    protected final AtomicBoolean closed;

    protected final LinkedList<W> pendingPackets;
    protected final StampedLock lock;

    protected final Array<BiConsumer<? super Connection<R, W>, ? super R>> subscribers;

    protected final int maxPacketsByRead;
    protected final int packetLengthHeaderSize;

    @Getter
    protected volatile long lastActivity;

    public AbstractConnection(
        @NotNull Network<? extends Connection<R, W>> network,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull NetworkCryptor crypt,
        @NotNull BufferAllocator bufferAllocator,
        int maxPacketsByRead,
        int packetLengthHeaderSize
    ) {
        this.bufferAllocator = bufferAllocator;
        this.maxPacketsByRead = maxPacketsByRead;
        this.packetLengthHeaderSize = packetLengthHeaderSize;
        this.lock = new StampedLock();
        this.crypt = crypt;
        this.channel = channel;
        this.pendingPackets = LinkedListFactory.newLinkedList(WritablePacket.class);
        this.network = network;
        this.isWriting = new AtomicBoolean(false);
        this.closed = new AtomicBoolean(false);
        this.packetReader = createPacketReader();
        this.packetWriter = createPacketWriter();
        this.subscribers = ArrayFactory.newCopyOnModifyArray(BiConsumer.class);
    }

    protected abstract @NotNull PacketReader createPacketReader();

    protected void handleReadPacket(@NotNull R packet) {
        LOGGER.debug(channel, packet, (ch, pck) -> "Handle read packet: " + pck + " from: " + getSocketAddress(ch));
        subscribers.forEach(this, packet, BiConsumer::accept);
    }

    @Override
    public void onReceive(@NotNull BiConsumer<? super Connection<R, W>, ? super R> consumer) {
        subscribers.add(consumer);
        packetReader.startRead();
    }

    @Override
    public @NotNull Flux<ReceivedPacketEvent<? extends Connection<R, W>, ? extends R>> receivedEvents() {
        return Flux.create(this::registerFluxOnReceivedEvents);
    }

    @Override
    public @NotNull Flux<? extends R> receivedPackets() {
        return Flux.create(this::registerFluxOnReceivedPackets);
    }

    protected void registerFluxOnReceivedEvents(
        @NotNull FluxSink<ReceivedPacketEvent<? extends Connection<R, W>, ? extends R>> sink
    ) {

        BiConsumer<Connection<R, W>, R> listener =
            (connection, packet) -> sink.next(new ReceivedPacketEvent<>(connection, packet));

        onReceive(listener);

        sink.onDispose(() -> subscribers.remove(listener));
    }

    protected void registerFluxOnReceivedPackets(@NotNull FluxSink<? super R> sink) {

        BiConsumer<Connection<R, W>, R> listener =
            (connection, packet) -> sink.next(packet);

        onReceive(listener);

        sink.onDispose(() -> subscribers.remove(listener));
    }

    protected @NotNull PacketWriter createPacketWriter() {
        return new DefaultPacketWriter<W, Connection<R, W>>(
            this,
            channel,
            bufferAllocator,
            this::updateLastActivity,
            this::nextPacketToWrite,
            packetLengthHeaderSize
        );
    }

    protected @Nullable W nextPacketToWrite() {
        long stamp = lock.writeLock();
        try {
            return pendingPackets.poll();
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            doClose();
        }
    }

    /**
     * Does the process of closing this connection.
     */
    protected void doClose() {

        unchecked(channel, AsynchronousChannel::close);

        clearWaitPackets();

        packetReader.close();
        packetWriter.close();
    }

    @Override
    public @NotNull String getRemoteAddress() {
        return safeGet(channel, arg -> String.valueOf(arg.getRemoteAddress()), "unknown");
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
    public final void send(@NotNull W packet) {

        if (isClosed()) {
            return;
        }

        long stamp = lock.writeLock();
        try {
            pendingPackets.add(packet);
        } finally {
            lock.unlockWrite(stamp);
        }

        packetWriter.writeNextPacket();
    }

    /**
     * Clear waited packets.
     */
    protected void clearWaitPackets() {

        long stamp = lock.writeLock();
        try {
            doClearWaitPackets();
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    protected void doClearWaitPackets() {
        pendingPackets.clear();
    }
}
