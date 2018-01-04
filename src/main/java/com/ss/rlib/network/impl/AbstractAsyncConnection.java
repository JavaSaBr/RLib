package com.ss.rlib.network.impl;

import static com.ss.rlib.util.ObjectUtils.notNull;
import static java.lang.Math.min;
import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.network.*;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.ReusableWritablePacket;
import com.ss.rlib.network.packet.WritablePacket;
import com.ss.rlib.network.packet.impl.AbstractReusableWritablePacket;
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
import java.util.concurrent.locks.StampedLock;

/**
 * The base implementation of {@link AsyncConnection}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractAsyncConnection implements AsyncConnection {

    /**
     * The constant LOGGER.
     */
    protected static final Logger LOGGER = LoggerManager.getLogger(AsyncNetwork.class);

    /**
     * The constant SIZE_BYTES_SIZE.
     */
    protected static final int SIZE_BYTES_SIZE = 2;

    /**
     * The constant READ_PACKET_LIMIT.
     */
    protected static final int READ_PACKET_LIMIT = Integer.parseInt(System.getProperty(
            AbstractAsyncConnection.class.getName() + ".readPacketLimit", "1000"));

    /**
     * The constant WAIT_SEGMENT_LIMIT.
     */
    protected static final int WAIT_SEGMENT_LIMIT = Integer.parseInt(System.getProperty(
            AbstractAsyncConnection.class.getName() + ".waitSegmentLimit", "10"));
    ;

    /**
     * The network.
     */
    @NotNull
    protected final AsyncNetwork network;

    /**
     * The list of waited packets.
     */
    @NotNull
    protected final LinkedList<WritablePacket> waitPackets;

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
     * The swap buffer.
     */
    @NotNull
    protected final ByteBuffer swapBuffer;

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
    protected final AtomicInteger waitCounter;

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
    protected final StampedLock lock;

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
            handleReadData(result);
        }

        @Override
        public void failed(@NotNull final Throwable exc, @NotNull final AbstractAsyncConnection attachment) {
            handleFailedRead(exc);
        }
    };

    /**
     * The write handler.
     */
    @NotNull
    private final CompletionHandler<Integer, WritablePacket> writeHandler = new CompletionHandler<Integer, WritablePacket>() {

        @Override
        public void completed(@NotNull final Integer result, @NotNull final WritablePacket packet) {
            handleWroteData(result, packet);
        }

        @Override
        public void failed(@NotNull final Throwable exc, @NotNull final WritablePacket packet) {
            handleFailedWrite(exc, packet);
        }
    };

    public AbstractAsyncConnection(@NotNull final AsyncNetwork network, @NotNull final AsynchronousSocketChannel channel,
                                   @NotNull final Class<? extends WritablePacket> sendableType) {
        this.lock = new StampedLock();
        this.channel = channel;
        this.waitPackets = LinkedListFactory.newLinkedList(sendableType);
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
        this.waitCounter = new AtomicInteger();
    }

    /**
     * Clear waited packets.
     */
    protected void clearWaitPackets() {
        waitPackets.forEach(AbstractReusableWritablePacket.class::isInstance,
                packet -> ((AbstractReusableWritablePacket) packet).forceComplete());
        waitPackets.clear();
    }

    @Override
    public void setOwner(@Nullable final ConnectionOwner owner) {
        this.owner = owner;
    }

    @Override
    public @Nullable ConnectionOwner getOwner() {
        return owner;
    }

    @Override
    public void close() {
        if (!closed.compareAndSet(false, true)) {
            return;
        }
        try {
            doClose();
        } catch (final IOException e) {
            LOGGER.warning(this, e);
        }
    }

    /**
     * Does the process of closing this connection.
     *
     * @throws IOException if found some problem during closing a channel.
     */
    protected void doClose() throws IOException {

        final AsynchronousSocketChannel channel = getChannel();
        if (channel.isOpen()) {
            channel.close();
        }

        clearWaitPackets();

        final AsyncNetwork network = getNetwork();
        network.putReadBuffer(getReadBuffer());
        network.putWriteBuffer(getWriteBuffer());
        network.putWaitBuffer(getWaitBuffer());
        network.putWaitBuffer(getSwapBuffer());
    }

    /**
     * Handle finish of this connection.
     */
    protected void finish() {
        final ConnectionOwner owner = getOwner();
        if (owner != null) {
            owner.destroy();
        }
    }

    /**
     * Get the wait buffer.
     *
     * @return the wait buffer to receive a large packet.
     */
    protected @NotNull ByteBuffer getWaitBuffer() {
        return waitBuffer;
    }

    /**
     * Get the swap buffer.
     *
     * @return the swap buffer.
     */
    protected @NotNull ByteBuffer getSwapBuffer() {
        return swapBuffer;
    }

    /**
     * Get the remote address.
     *
     * @return the remote address.
     */
    public @NotNull String getRemoteAddress() {
        final AsynchronousSocketChannel channel = getChannel();
        try {
            return String.valueOf(channel.getRemoteAddress());
        } catch (final IOException e) {
            LOGGER.warning(this, e);
        }
        return "unknown";
    }

    /**
     * Get the data size of the packet.
     *
     * @param buffer the packet data buffer.
     * @return the packet size.
     */
    protected int getPacketSize(@NotNull final ByteBuffer buffer) {
        return buffer.getShort() & 0xFFFF;
    }

    /**
     * Get the wait counter.
     *
     * @return the counter of received segments of a large packet.
     */
    protected @NotNull AtomicInteger getWaitCounter() {
        return waitCounter;
    }

    /**
     * Get the channel.
     *
     * @return the channel.
     */
    protected @NotNull AsynchronousSocketChannel getChannel() {
        return channel;
    }

    @Override
    public long getLastActivity() {
        return lastActivity;
    }

    /**
     * Set the time of last activity.
     *
     * @param lastActivity the time of last activity.
     */
    protected void setLastActivity(final long lastActivity) {
        this.lastActivity = lastActivity;
    }

    /**
     * Update the time of last activity.
     */
    protected void updateLastActivity() {
        setLastActivity(System.currentTimeMillis());
    }

    @Override
    public @NotNull AsyncNetwork getNetwork() {
        return network;
    }

    /**
     * Get the read buffer.
     *
     * @return the read buffer.
     */
    protected @NotNull ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    /**
     * Get the read handler.
     *
     * @return the read handler.
     */
    protected @NotNull CompletionHandler<Integer, AbstractAsyncConnection> getReadHandler() {
        return readHandler;
    }

    /**
     * Get the list of wait packets.
     *
     * @return the list of waited packets.
     */
    protected @NotNull LinkedList<WritablePacket> getWaitPackets() {
        return waitPackets;
    }

    /**
     * Get the write buffer.
     *
     * @return the write buffer.
     */
    protected @NotNull ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }

    /**
     * Get the write handler.
     *
     * @return the write handler.
     */
    protected @NotNull CompletionHandler<Integer, WritablePacket> getWriteHandler() {
        return writeHandler;
    }

    @Override
    public boolean isClosed() {
        return closed.get();
    }

    /**
     * Set true if this connection is closed.
     *
     * @param closed true if this connection is closed.
     */
    protected void setClosed(final boolean closed) {
        this.closed.getAndSet(closed);
    }

    /**
     * Check the buffer if this is ready to read.
     *
     * @param buffer the buffer.
     * @return true if the buffer is ready to read .
     */
    protected boolean isReadyToRead(@NotNull final ByteBuffer buffer) {
        return true;
    }

    /**
     * Read the buffer with received data.
     *
     * @param buffer the buffer with received data
     * @return count of read packets.
     */
    protected int readPacket(@NotNull final ByteBuffer buffer) {

        final ConnectionOwner owner = notNull(getOwner());
        final NetworkCrypt crypt = owner.getCrypt();
        final ByteBuffer waitBuffer = getWaitBuffer();
        final AtomicInteger waitCount = getWaitCounter();

        int currentCount = waitCount.get();
        int resultCount = 0;

        if (currentCount > WAIT_SEGMENT_LIMIT) {
            waitBuffer.clear();
            LOGGER.error(this, "crowded limit segments.");
        }

        final int waitedBytes = waitBuffer.remaining();

        // если есть кусок пакета ожидающего
        if (waitedBytes > 0) {

            final int prevLimit = waitBuffer.limit();
            final int prevPosition = waitBuffer.position();
            final int length = buffer.limit() - buffer.position();

            // вливаем весь новый буффер
            waitBuffer.clear();
            waitBuffer.position(prevLimit);
            waitBuffer.put(buffer.array(), buffer.position(), length);
            waitBuffer.flip();
            waitBuffer.position(prevPosition);

            // очищаем пришедший буффер и вливаем в него новый итоговый буффер
            buffer.clear();
            buffer.put(waitBuffer.array(), prevPosition, min(waitBuffer.remaining(), buffer.remaining()));
            buffer.flip();

            buffer.getShort();
            final int packetId = buffer.getShort() & 0xFFFF;
            buffer.position(buffer.position() - 4);

            // обновляем позицию в ожидаемом буфере после сброса части данных в обычный буффер
            waitBuffer.position(waitBuffer.position() + buffer.limit());

            // очищаем ожидающий буффер если мы его весь прочитали
            if (!waitBuffer.hasRemaining()) {
                waitBuffer.clear();
                waitBuffer.flip();
            }
        }

        for (int i = 0, limit = 0, size; buffer.remaining() >= SIZE_BYTES_SIZE && i < READ_PACKET_LIMIT; i++) {
            size = getPacketSize(buffer);
            limit += size;

            // если пакет не вместился в этот буффер
            if (limit > buffer.limit()) {

                final int missedBytes = limit - buffer.limit();

                // проверка остались ли данные в ожидаемом буффере для до прочтения и если да,
                // то достаточно ли там байт что бы дочитать пакет
                if (waitedBytes > 0 && waitBuffer.position() > 0 && waitBuffer.remaining() >= missedBytes) {

                    // делаем отступ назад на кол-во байт которых осталось непрочитанных в буффере
                    final int newPosition = waitBuffer.position() - buffer.remaining();
                    final int prevLimit = waitBuffer.limit();

                    waitBuffer.clear();
                    waitBuffer.position(newPosition);

                    // добавляем спереди непрочитанный кусок
                    waitBuffer.put(buffer.array(), buffer.position(), buffer.remaining());
                    waitBuffer.position(newPosition);
                    waitBuffer.limit(prevLimit);

                    buffer.clear();
                    buffer.put(waitBuffer.array(), newPosition, min(waitBuffer.remaining(), buffer.remaining()));
                    buffer.flip();

                    final int packetId = buffer.getShort() & 0xFFFF;
                    buffer.position(buffer.position() - 2);

                    // сдвигаем позицию на кол-во сколько скинули данных в буффер
                    waitBuffer.position(waitBuffer.position() + buffer.limit());

                    // очищаем ожидающий буффер если мы его весь прочитали
                    if (!waitBuffer.hasRemaining()) {
                        waitBuffer.clear();
                        waitBuffer.flip();
                    }

                    limit = size;

                } else {

                    final int offset = buffer.position() - SIZE_BYTES_SIZE;
                    final int length = buffer.limit() - offset;

                    // если ожидаеющий буффер фрагментировал
                    if (waitBuffer.position() > 0) {

                        final ByteBuffer swapBuffer = getSwapBuffer();
                        swapBuffer.clear();
                        swapBuffer.put(buffer.array(), offset, length);
                        swapBuffer.put(waitBuffer.array(), waitBuffer.position(), waitBuffer.remaining());
                        swapBuffer.flip();
                        waitBuffer.clear();
                        waitBuffer.put(swapBuffer.array(), 0, swapBuffer.remaining());
                        waitBuffer.flip();

                    } else {

                        final int prevLimit = waitBuffer.limit();
                        final int prevPosition = waitBuffer.position();

                        waitBuffer.clear();
                        waitBuffer.position(prevLimit);
                        waitBuffer.put(buffer.array(), offset, length);
                        waitBuffer.flip();
                        waitBuffer.position(prevPosition);
                    }

                    waitCount.incrementAndGet();
                    break;
                }
            }

            decrypt(buffer, crypt, buffer.position(), size - SIZE_BYTES_SIZE);

            final ReadablePacket packet = createPacketFor(buffer);

            if (currentCount > 0) {
                waitCount.getAndSet(0);
                currentCount = 0;
            }

            if (packet != null) {
                owner.readPacket(packet, buffer);
                resultCount++;
            }
        }

        return resultCount;
    }

    /**
     * Write the packet to the write buffer.
     *
     * @param packet the packet.
     * @param buffer the write buffer.
     * @return the write buffer.
     */
    protected @NotNull ByteBuffer writePacketToBuffer(@NotNull final WritablePacket packet, final @NotNull ByteBuffer buffer) {

        buffer.clear();
        packet.prepareWritePosition(buffer);
        packet.write(buffer);
        buffer.flip();
        packet.writePacketSize(buffer, buffer.limit());

        final ConnectionOwner owner = notNull(getOwner());
        final NetworkCrypt crypt = owner.getCrypt();

        encrypt(buffer, crypt, SIZE_BYTES_SIZE, buffer.limit() - SIZE_BYTES_SIZE);

        return buffer;
    }

    /**
     * Decrypt data using the crypt of the connection owner.
     *
     * @param buffer the data buffer.
     * @param crypt  the crypt.
     * @param offset the offset.
     * @param length the length.
     */
    protected void decrypt(@NotNull final ByteBuffer buffer, @NotNull final NetworkCrypt crypt, final int offset,
                           final int length) {
        if (crypt.isNull()) return;
        crypt.decrypt(buffer.array(), offset, length);
    }

    /**
     * Encrypt data using the crypt of the connection owner.
     *
     * @param buffer the data buffer.
     * @param crypt  the crypt.
     * @param offset the offset.
     * @param length the length.
     */
    protected void encrypt(@NotNull final ByteBuffer buffer, @NotNull final NetworkCrypt crypt, final int offset,
                           final int length) {
        if (crypt.isNull()) return;
        crypt.encrypt(buffer.array(), offset, length);
    }

    /**
     * Create a packet to read the data buffer.
     *
     * @param buffer the buffer
     * @return the readable packet
     */
    protected @Nullable ReadablePacket createPacketFor(@NotNull final ByteBuffer buffer) {
        if (buffer.remaining() < SIZE_BYTES_SIZE) return null;
        final int packetId = buffer.getShort() & 0xFFFF;
        return getNetwork().getPacketRegistry()
                .findById(packetId);
    }

    /**
     * Handle a completed packet.
     *
     * @param packet the sent packet.
     */
    protected void completed(@NotNull final WritablePacket packet) {
        if (packet instanceof ReusableWritablePacket) {
            ((ReusableWritablePacket) packet).complete();
        }
    }

    @Override
    public final void sendPacket(@NotNull final WritablePacket packet) {
        if (isClosed()) return;
        final long stamp = lock.writeLock();
        try {
            waitPackets.add(packet);
        } finally {
            lock.unlockWrite(stamp);
        }
        writeNextPacket();
    }

    @Override
    public final void startRead() {
        channel.read(readBuffer, this, getReadHandler());
    }

    /**
     * Write a next packet.
     */
    protected final void writeNextPacket() {
        if (isClosed() || !isWriting.compareAndSet(false, true)) {
            return;
        }

        WritablePacket waitPacket;

        final long stamp = lock.writeLock();
        try {
            waitPacket = waitPackets.poll();
        } finally {
            lock.unlockWrite(stamp);
        }

        if (waitPacket == null) {
            isWriting.set(false);
            return;
        }

        final AsynchronousSocketChannel channel = getChannel();
        channel.write(writePacketToBuffer(waitPacket, getWriteBuffer()), waitPacket, getWriteHandler());

        completed(waitPacket);
    }

    /**
     * Handle read data.
     *
     * @param result the count of read bytes.
     */
    protected void handleReadData(@NotNull final Integer result) {
        updateLastActivity();

        if (result == -1) {
            finish();
            return;
        }

        final ByteBuffer buffer = getReadBuffer();
        buffer.flip();
        try {
            if (isReadyToRead(buffer)) readPacket(buffer);
        } catch (final Exception e) {
            LOGGER.error(this, e);
        } finally {
            buffer.clear();
        }

        startRead();
    }

    /**
     * Handle the exception during reading data.
     *
     * @param exception the exception.
     */
    protected void handleFailedRead(@NotNull final Throwable exception) {
        if (config.isVisibleReadException()) {
            LOGGER.warning(this, exception);
        }
        if (!isClosed()) {
            finish();
        }
    }

    /**

     * Handle wrote data.
     *
     * @param result the count of wrote bytes.
     * @param packet the sent packet.
     */
    protected void handleWroteData(@NotNull final Integer result, @NotNull final WritablePacket packet) {
        updateLastActivity();

        if (result == -1) {
            finish();
            return;
        }

        final ByteBuffer buffer = getWriteBuffer();
        if (buffer.remaining() > 0) {
            channel.write(buffer, packet, getWriteHandler());
            return;
        }

        if (isWriting.compareAndSet(true, false)) {
            writeNextPacket();
        }
    }

    /**
     * Handle the exception during writing the packet.
     *
     * @param exception the exception.
     * @param packet    the packet.
     */
    protected void handleFailedWrite(@NotNull final Throwable exception, @NotNull final WritablePacket packet) {
        if (config.isVisibleWriteException()) {
            LOGGER.warning(this, new Exception("incorrect write packet " + packet, exception));
        }

        if (isClosed()) return;
        if (isWriting.compareAndSet(true, false)) {
            writeNextPacket();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "network=" + network + ", waitPackets=" + waitPackets + ", channel=" +
                channel + ", isWriting=" + isWriting + ", closed=" + closed + ", lastActivity=" + lastActivity + '}';
    }
}
