package com.ss.rlib.network.impl;

import static com.ss.rlib.common.util.Utils.unchecked;
import static com.ss.rlib.network.util.NetworkUtils.getSocketAddress;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.linkedlist.LinkedList;
import com.ss.rlib.common.util.linkedlist.LinkedListFactory;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.NetworkCryptor;
import com.ss.rlib.network.packet.PacketReader;
import com.ss.rlib.network.packet.PacketWriter;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.WritablePacket;
import com.ss.rlib.network.packet.impl.WritablePacketWrapper;
import com.ss.rlib.network.util.NetworkUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CompletableFuture;
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

    private static class WritablePacketWithFeedback<W extends WritablePacket> extends
        WritablePacketWrapper<CompletableFuture<Boolean>, W> {

        public WritablePacketWithFeedback(@NotNull CompletableFuture<Boolean> attachment, @NotNull W packet) {
            super(attachment, packet);
        }
    }

    protected final @Getter NetworkCryptor crypt;
    protected final @Getter String remoteAddress;

    protected final Network<? extends Connection<R, W>> network;
    protected final BufferAllocator bufferAllocator;
    protected final AsynchronousSocketChannel channel;
    protected final LinkedList<WritablePacket> pendingPackets;
    protected final StampedLock lock;

    protected final AtomicBoolean isWriting;
    protected final AtomicBoolean closed;

    protected final Array<BiConsumer<? super Connection<R, W>, ? super R>> subscribers;

    protected final int maxPacketsByRead;

    protected volatile @Getter long lastActivity;

    public AbstractConnection(
        @NotNull Network<? extends Connection<R, W>> network,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull NetworkCryptor crypt,
        @NotNull BufferAllocator bufferAllocator,
        int maxPacketsByRead
    ) {
        this.bufferAllocator = bufferAllocator;
        this.maxPacketsByRead = maxPacketsByRead;
        this.lock = new StampedLock();
        this.crypt = crypt;
        this.channel = channel;
        this.pendingPackets = LinkedListFactory.newLinkedList(WritablePacket.class);
        this.network = network;
        this.isWriting = new AtomicBoolean(false);
        this.closed = new AtomicBoolean(false);
        this.subscribers = ArrayFactory.newCopyOnModifyArray(BiConsumer.class);
        this.remoteAddress = String.valueOf(NetworkUtils.getSocketAddress(channel));
    }

    protected abstract @NotNull PacketReader getPacketReader();

    protected abstract @NotNull PacketWriter getPacketWriter();

    protected void handleReadPacket(@NotNull R packet) {
        LOGGER.debug(channel, packet, (ch, pck) -> "Handle read packet: " + pck + " from: " + getSocketAddress(ch));
        subscribers.forEach(this, packet, BiConsumer::accept);
    }

    @Override
    public void onReceive(@NotNull BiConsumer<? super Connection<R, W>, ? super R> consumer) {
        subscribers.add(consumer);
        getPacketReader().startRead();
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

        BiConsumer<Connection<R, W>, R> listener = (connection, packet) -> sink.next(packet);

        onReceive(listener);

        sink.onDispose(() -> subscribers.remove(listener));
    }

    protected @Nullable WritablePacket nextPacketToWrite() {
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

        if (channel.isOpen()) {
            unchecked(channel, AsynchronousChannel::close);
        }

        clearWaitPackets();

        getPacketReader().close();
        getPacketWriter().close();
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

    protected void onWrittenPacket(@NotNull WritablePacket packet) { }

    protected void onSentPacket(@NotNull WritablePacket packet, @NotNull Boolean result) {
        if (packet instanceof WritablePacketWithFeedback) {
            ((WritablePacketWithFeedback<W>) packet).getAttachment().complete(result);
        }
    }

    @Override
    public final void send(@NotNull W packet) {
        sendImpl(packet);
    }

    protected void sendImpl(@NotNull WritablePacket packet) {

        if (isClosed()) {
            return;
        }

        long stamp = lock.writeLock();
        try {
            pendingPackets.add(packet);
        } finally {
            lock.unlockWrite(stamp);
        }

        getPacketWriter().writeNextPacket();
    }

    @Override
    public @NotNull CompletableFuture<Boolean> sendWithFeedback(@NotNull W packet) {

        var asyncResult = new CompletableFuture<Boolean>();

        sendImpl(new WritablePacketWithFeedback<>(asyncResult, packet));

        if (isClosed()) {
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }

        return asyncResult;
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

        for (var pendingPacket : pendingPackets) {
            onSentPacket(pendingPacket, Boolean.FALSE);
        }

        pendingPackets.clear();
    }
}
