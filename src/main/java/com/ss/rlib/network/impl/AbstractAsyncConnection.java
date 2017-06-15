package com.ss.rlib.network.impl;

import static com.ss.rlib.network.packet.ReadablePacketType.getPacketType;
import static java.util.Objects.requireNonNull;
import com.ss.rlib.concurrent.lock.LockFactory;
import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.network.AsyncConnection;
import com.ss.rlib.network.AsynchronousNetwork;
import com.ss.rlib.network.ConnectionOwner;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.ReadablePacketType;
import com.ss.rlib.network.packet.SendablePacket;
import com.ss.rlib.network.packet.impl.AbstractReusableSendablePacket;
import com.ss.rlib.util.ClassUtils;
import com.ss.rlib.util.linkedlist.LinkedList;
import com.ss.rlib.util.linkedlist.LinkedListFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

/**
 * The base implementation of the Async Connection.
 *
 * @author JavaSaBr
 */
public abstract class AbstractAsyncConnection implements AsyncConnection {

    /**
     * The constant LOGGER.
     */
    protected static final Logger LOGGER = LoggerManager.getLogger(AsyncConnection.class);

    /**
     * The constant SIZE_BYTES_SIZE.
     */
    protected static final int SIZE_BYTES_SIZE = 2;
    /**
     * The constant READ_PACKET_LIMIT.
     */
    protected static final int READ_PACKET_LIMIT = 1000;
    /**
     * The constant WAIT_SEGMENT_LIMIT.
     */
    protected static final int WAIT_SEGMENT_LIMIT = 10;

    /**
     * The network.
     */
    @NotNull
    protected final AsynchronousNetwork network;

    /**
     * The list of waited packets.
     */
    @NotNull
    protected final LinkedList<SendablePacket> waitPackets;

    /**
     * The channel.
     */
    @NotNull
    protected final AsynchronousSocketChannel channel;

    /**
     * The read buffer.
     */
    @NotNull
    protected final ByteBuffer readBuffer;

    /**
     * The write buffer.
     */
    @NotNull
    protected final ByteBuffer writeBuffer;

    /**
     * The wait buffer to receive a large packet.
     */
    @NotNull
    protected final ByteBuffer waitBuffer;

    /**
     * The count of received segments of a large packet.
     */
    @NotNull
    protected final AtomicInteger waitCount;

    /**
     * The state of writing.
     */
    @NotNull
    protected final AtomicBoolean isWriting;

    /**
     * The state of closed.
     */
    @NotNull
    protected final AtomicBoolean closed;

    /**
     * The config.
     */
    @NotNull
    protected final NetworkConfig config;

    /**
     * The locker.
     */
    @NotNull
    protected final Lock lock;

    /**
     * The connection's owner.
     */
    @Nullable
    protected volatile ConnectionOwner owner;

    /**
     * The time of lst activity.
     */
    protected volatile long lastActivity;

    /**
     * The read handler.
     */
    @NotNull
    private final CompletionHandler<Integer, AbstractAsyncConnection> readHandler = new CompletionHandler<Integer, AbstractAsyncConnection>() {

        @Override
        public void completed(@NotNull final Integer result, @NotNull final AbstractAsyncConnection connection) {
            setLastActivity(System.currentTimeMillis());

            if (result == -1) {
                finish();
                return;
            }

            final ByteBuffer buffer = getReadBuffer();
            buffer.flip();
            try {
                if (isReady(buffer)) readPacket(buffer);
            } catch (final Exception e) {
                LOGGER.error(this, e);
            } finally {
                buffer.clear();
            }

            startRead();
        }

        @Override
        public void failed(@NotNull final Throwable exc, @NotNull final AbstractAsyncConnection attachment) {
            if (config.isVisibleReadException()) LOGGER.warning(this, exc);
            if (!isClosed()) finish();
        }
    };

    /**
     * The write handler.
     */
    @NotNull
    private final CompletionHandler<Integer, SendablePacket> writeHandler = new CompletionHandler<Integer, SendablePacket>() {

        @Override
        public void completed(@NotNull final Integer result, @NotNull final SendablePacket packet) {
            setLastActivity(System.currentTimeMillis());

            if (result == -1) {
                finish();
                return;
            }

            final ByteBuffer buffer = getWriteBuffer();

            if (buffer.remaining() > 0) {
                channel.write(buffer, packet, this);
                return;
            }

            if (isWriting.compareAndSet(true, false)) writeNextPacket();
        }

        @Override
        public void failed(@NotNull final Throwable exc, @NotNull final SendablePacket packet) {

            if (config.isVisibleWriteException()) {
                LOGGER.warning(this, new Exception("incorrect write packet " + packet, exc));
            }

            if (isClosed()) return;
            if (isWriting.compareAndSet(true, false)) writeNextPacket();
        }
    };

    /**
     * Instantiates a new Abstract async connection.
     *
     * @param network      the network
     * @param channel      the channel
     * @param sendableType the sendable type
     */
    public AbstractAsyncConnection(@NotNull final AsynchronousNetwork network, @NotNull final AsynchronousSocketChannel channel,
                                   @NotNull final Class<? extends SendablePacket> sendableType) {
        this.lock = LockFactory.newReentrantLock();
        this.channel = channel;
        this.waitPackets = LinkedListFactory.newLinkedList(sendableType);
        this.network = network;
        this.readBuffer = network.takeReadBuffer();
        this.readBuffer.clear();
        this.writeBuffer = network.takeWriteBuffer();
        this.config = network.getConfig();
        this.isWriting = new AtomicBoolean(false);
        this.closed = new AtomicBoolean(false);
        this.waitBuffer = network.takeReadBuffer();
        this.waitCount = new AtomicInteger();
    }

    /**
     * Clear waited packets.
     */
    protected void clearWaitPackets() {

        waitPackets.forEach(packet -> packet instanceof AbstractReusableSendablePacket,
                packet -> ((AbstractReusableSendablePacket) packet).forceComplete());

        waitPackets.clear();
    }

    @Override
    public void setOwner(@Nullable final ConnectionOwner owner) {
        this.owner = owner;
    }

    @Override
    @Nullable
    public ConnectionOwner getOwner() {
        return owner;
    }

    @Override
    public void close() {
        lock();
        try {

            if (isClosed()) return;
            setClosed(true);

            final AsynchronousSocketChannel channel = getChannel();
            if (channel.isOpen()) channel.close();

            clearWaitPackets();

            final AsynchronousNetwork network = getNetwork();
            network.putReadBuffer(getReadBuffer());
            network.putWriteBuffer(getWriteBuffer());
            network.putReadBuffer(getWaitBuffer());

        } catch (final IOException e) {
            LOGGER.warning(this, e);
        } finally {
            unlock();
        }
    }

    /**
     * Handle finish of this connection.
     */
    protected void finish() {
        final ConnectionOwner owner = getOwner();
        if (owner != null) owner.close();
    }

    /**
     * Gets wait buffer.
     *
     * @return the wait buffer to receive a large packet.
     */
    @NotNull
    protected ByteBuffer getWaitBuffer() {
        return waitBuffer;
    }

    /**
     * Gets remote address.
     *
     * @return the remote address.
     */
    @NotNull
    public String getRemoteAddress() {

        final AsynchronousSocketChannel channel = getChannel();
        try {
            return String.valueOf(channel.getRemoteAddress());
        } catch (final IOException e) {
            LOGGER.warning(this, e);
        }

        return "unknown";
    }

    /**
     * Get data size of the packet.
     *
     * @param buffer the packet data buffer.
     * @return the packet size.
     */
    protected int getPacketSize(@NotNull final ByteBuffer buffer) {
        return buffer.getShort() & 0xFFFF;
    }

    /**
     * Gets wait count.
     *
     * @return the count of received segments of a large packet.
     */
    @NotNull
    protected AtomicInteger getWaitCount() {
        return waitCount;
    }

    /**
     * Gets channel.
     *
     * @return the channel.
     */
    @NotNull
    protected AsynchronousSocketChannel getChannel() {
        return channel;
    }

    @Override
    public final long getLastActivity() {
        return lastActivity;
    }

    @Override
    public final void setLastActivity(final long lastActivity) {
        this.lastActivity = lastActivity;
    }

    /**
     * Gets lock.
     *
     * @return the locker.
     */
    @NotNull
    public Lock getLock() {
        return lock;
    }

    /**
     * Gets network.
     *
     * @return the network.
     */
    @NotNull
    protected AsynchronousNetwork getNetwork() {
        return network;
    }

    /**
     * Gets read buffer.
     *
     * @return the read buffer.
     */
    @NotNull
    protected final ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    /**
     * Gets read handler.
     *
     * @return the read handler.
     */
    @NotNull
    protected CompletionHandler<Integer, AbstractAsyncConnection> getReadHandler() {
        return readHandler;
    }

    /**
     * Gets wait packets.
     *
     * @return the list of waited packets.
     */
    @NotNull
    protected LinkedList<SendablePacket> getWaitPackets() {
        return waitPackets;
    }

    /**
     * Gets write buffer.
     *
     * @return the write buffer.
     */
    @NotNull
    protected final ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }

    /**
     * Gets write handler.
     *
     * @return the write handler.
     */
    @NotNull
    protected CompletionHandler<Integer, SendablePacket> getWriteHandler() {
        return writeHandler;
    }

    @Override
    public final boolean isClosed() {
        return closed.get();
    }

    /**
     * Sets closed.
     *
     * @param closed закрыт ли конект.
     */
    protected final void setClosed(final boolean closed) {
        this.closed.getAndSet(closed);
    }

    /**
     * Check a buffer on ready to read.
     *
     * @param buffer the read buffer.
     * @return true if the buffer is ready.
     */
    protected boolean isReady(@NotNull final ByteBuffer buffer) {
        return true;
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    /**
     * Read the buffer with received data.
     *
     * @param buffer the buffer with received data
     */
    protected void readPacket(@NotNull final ByteBuffer buffer) {

        final ConnectionOwner owner = requireNonNull(getOwner());
        final ByteBuffer waitBuffer = getWaitBuffer();
        final AtomicInteger waitCount = getWaitCount();

        int currentCount = waitCount.get();

        if (currentCount > WAIT_SEGMENT_LIMIT) {
            waitBuffer.clear();
            LOGGER.error(this, "crowded limit segments.");
        }

        // если есть кусок пакета ожидающего
        if (waitBuffer.position() > 0) {

            // вливаем весь новый буффер
            waitBuffer.put(buffer.array(), buffer.position(), buffer.limit() - buffer.position());
            waitBuffer.flip();

            // очищаем пришедший буффер и вливаем в него новый итоговый буффер
            buffer.clear();
            buffer.put(waitBuffer.array(), 0, waitBuffer.limit());
            buffer.flip();

            // очищаем ожидающий буффер
            waitBuffer.clear();
        }

        for (int i = 0, limit = 0, size = 0; buffer.remaining() >= SIZE_BYTES_SIZE && i < READ_PACKET_LIMIT; i++) {

            size = getPacketSize(buffer);

            limit += size;

            // если пакет не вместился в этот буффер, складываем его в ожидающий
            // и выходим из цикла
            if (limit > buffer.limit()) {

                final int offset = buffer.position() - SIZE_BYTES_SIZE;
                final int length = buffer.limit() - offset;

                waitBuffer.put(buffer.array(), offset, length);
                waitCount.incrementAndGet();
                return;
            }

            owner.decrypt(buffer, buffer.position(), size - SIZE_BYTES_SIZE);

            final ReadablePacket packet = createPacketFor(buffer);

            if (currentCount > 0) {
                waitCount.getAndSet(0);
                currentCount = 0;
            }

            if (packet != null) {
                owner.readPacket(packet, buffer);
            }

            buffer.position(limit);
        }
    }

    /**
     * Write a packet to a write buffer.
     *
     * @param packet the packet.
     * @param buffer the write buffer.
     * @return the write buffer.
     */
    @NotNull
    protected ByteBuffer writePacketToBuffer(@NotNull final SendablePacket packet, final @NotNull ByteBuffer buffer) {
        buffer.clear();

        packet.prepareWritePosition(buffer);
        packet.write(buffer);

        buffer.flip();

        packet.writePacketSize(buffer, buffer.limit());

        final ConnectionOwner owner = requireNonNull(getOwner());
        owner.encrypt(buffer, SIZE_BYTES_SIZE, buffer.limit() - SIZE_BYTES_SIZE);

        return buffer;
    }

    /**
     * Create a packet to read the data buffer.
     *
     * @param buffer the buffer
     * @return the readable packet
     */
    @Nullable
    protected ReadablePacket createPacketFor(@NotNull final ByteBuffer buffer) {
        if (buffer.remaining() < SIZE_BYTES_SIZE) return null;

        final int packetTypeId = buffer.getShort() & 0xFFFF;
        final ReadablePacketType<ReadablePacket> packetType = getPacketType(packetTypeId);

        return ClassUtils.unsafeCast(packetType.newInstance());
    }

    /**
     * Handle a completed packet.
     *
     * @param packet the sent packet.
     */
    protected void completed(@NotNull final SendablePacket packet) {
        if (packet instanceof AbstractReusableSendablePacket) {
            ((AbstractReusableSendablePacket) packet).complete();
        }
    }

    @Override
    public final void sendPacket(@NotNull final SendablePacket packet) {
        if (isClosed()) return;

        lock();
        try {
            if (isClosed()) return;
            waitPackets.add(packet);
        } finally {
            unlock();
        }

        writeNextPacket();
    }

    @Override
    public final void startRead() {
        channel.read(readBuffer, this, readHandler);
    }

    /**
     * Write a next packet.
     */
    protected final void writeNextPacket() {
        lock();
        try {

            if (isClosed() || !isWriting.compareAndSet(false, true)) {
                return;
            }

            final SendablePacket waitPacket = waitPackets.poll();

            if (waitPacket == null) {
                isWriting.set(false);
                return;
            }

            final AsynchronousSocketChannel channel = getChannel();
            channel.write(writePacketToBuffer(waitPacket, getWriteBuffer()), waitPacket, getWriteHandler());

            completed(waitPacket);

        } finally {
            unlock();
        }
    }

    @Override
    public String toString() {
        return "AbstractAsyncConnection{" + "network=" + network + ", waitPackets=" + waitPackets + ", channel=" +
                channel + ", isWriting=" + isWriting + ", closed=" + closed + ", lastActivity=" + lastActivity + '}';
    }
}
