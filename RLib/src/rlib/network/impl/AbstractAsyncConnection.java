package rlib.network.impl;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.lock.LockFactory;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.AsyncConnection;
import rlib.network.AsynchronousNetwork;
import rlib.network.NetworkConfig;
import rlib.util.linkedlist.LinkedList;
import rlib.util.linkedlist.LinkedListFactory;

/**
 * The base implementation of the Async Connection.
 *
 * @author JavaSaBr
 */
public abstract class AbstractAsyncConnection<N extends AsynchronousNetwork, R, S> implements AsyncConnection<R, S> {

    protected static final Logger LOGGER = LoggerManager.getLogger(AsyncConnection.class);

    /**
     * The network.
     */
    @NotNull
    protected final N network;

    /**
     * The list of waited packets.
     */
    @NotNull
    protected final LinkedList<S> waitPackets;

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
     * The time of lst activity.
     */
    protected volatile long lastActivity;

    /**
     * The read handler.
     */
    @NotNull
    private final CompletionHandler<Integer, AbstractAsyncConnection> readHandler = new CompletionHandler<Integer, AbstractAsyncConnection>() {

        @Override
        public void completed(final Integer result, final AbstractAsyncConnection connection) {
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
        public void failed(final Throwable exc, final AbstractAsyncConnection attachment) {
            if (config.isVisibleReadException()) LOGGER.warning(this, exc);
            if (!isClosed()) finish();
        }
    };

    /**
     * The write handler.
     */
    @NotNull
    private final CompletionHandler<Integer, S> writeHandler = new CompletionHandler<Integer, S>() {

        @Override
        public void completed(final Integer result, final S packet) {
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
        public void failed(final Throwable exc, final S packet) {

            if (config.isVisibleWriteException()) {
                LOGGER.warning(this, new Exception("incorrect write packet " + packet, exc));
            }

            if (isClosed()) return;
            if (isWriting.compareAndSet(true, false)) writeNextPacket();
        }
    };

    public AbstractAsyncConnection(@NotNull final N network, @NotNull final AsynchronousSocketChannel channel,
                                   @NotNull final Class<S> sendableType) {
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
    }

    /**
     * Clear waited packets.
     */
    protected void clearWaitPackets() {
        waitPackets.clear();
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

            final N network = getNetwork();
            network.putReadBuffer(getReadBuffer());
            network.putWriteBuffer(getWriteBuffer());

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
        close();
    }

    /**
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
     * @return the locker.
     */
    @NotNull
    public Lock getLock() {
        return lock;
    }

    /**
     * @return the network.
     */
    @NotNull
    protected N getNetwork() {
        return network;
    }

    /**
     * @return the read buffer.
     */
    @NotNull
    protected final ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    /**
     * @return the read handler.
     */
    @NotNull
    protected CompletionHandler<Integer, AbstractAsyncConnection> getReadHandler() {
        return readHandler;
    }

    /**
     * @return the list of waited packets.
     */
    @NotNull
    protected LinkedList<S> getWaitPackets() {
        return waitPackets;
    }

    /**
     * @return the write buffer.
     */
    @NotNull
    protected final ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }

    /**
     * @return the write handler.
     */
    @NotNull
    protected CompletionHandler<Integer, S> getWriteHandler() {
        return writeHandler;
    }

    @Override
    public final boolean isClosed() {
        return closed.get();
    }

    /**
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

    /**
     * Write a packet to a write buffer.
     *
     * @param packet the packet.
     * @param buffer the write buffer.
     * @return the write buffer.
     */
    @NotNull
    protected abstract ByteBuffer writePacketToBuffer(@NotNull S packet, @NotNull ByteBuffer buffer);

    /**
     * Handle a completed packet.
     *
     * @param packet the sent packet.
     */
    protected abstract void completed(@NotNull S packet);

    /**
     * Read and handle a packet.
     *
     * @param buffer the read buffer.
     */
    protected abstract void readPacket(@NotNull ByteBuffer buffer);

    @Override
    public final void sendPacket(@NotNull final S packet) {
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

    @Override
    public String toString() {
        return "AbstractAsyncConnection{" +
                "network=" + network +
                ", waitPackets=" + waitPackets +
                ", channel=" + channel +
                ", isWriting=" + isWriting +
                ", closed=" + closed +
                ", lastActivity=" + lastActivity +
                '}';
    }

    @Override
    public void unlock() {
        lock.unlock();
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

            final S waitPacket = waitPackets.poll();

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
}
