package javasabr.rlib.network.packet.impl;

import static javasabr.rlib.common.util.ObjectUtils.notNull;
import static javasabr.rlib.network.util.NetworkUtils.getRemoteAddress;
import javasabr.rlib.common.function.NotNullConsumer;
import javasabr.rlib.common.util.BufferUtils;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.packet.PacketReader;
import javasabr.rlib.network.packet.ReadablePacket;
import javasabr.rlib.network.util.NetworkUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @param <R> the readable packet's type.
 * @param <C> the connection's type.
 * @author JavaSaBr
 */
public abstract class AbstractPacketReader<R extends ReadablePacket, C extends Connection<R, ?>> implements
    PacketReader {

    private static final Logger LOGGER = LoggerManager.getLogger(AbstractPacketReader.class);

    private final CompletionHandler<Integer, ByteBuffer> readHandler = new CompletionHandler<>() {

        @Override
        public void completed(@NotNull Integer receivedBytes, @NotNull ByteBuffer readingBuffer) {
            handleReceivedData(receivedBytes, readingBuffer);
        }

        @Override
        public void failed(@NotNull Throwable exc, @NotNull ByteBuffer readingBuffer) {
            handleFailedReceiving(exc, readingBuffer);
        }
    };

    protected final @NotNull AtomicBoolean isReading = new AtomicBoolean(false);

    protected final @NotNull C connection;
    protected final @NotNull AsynchronousSocketChannel channel;
    protected final @NotNull BufferAllocator bufferAllocator;

    protected final @NotNull ByteBuffer readBuffer;
    protected final @NotNull ByteBuffer pendingBuffer;

    protected final @NotNull Runnable updateActivityFunction;
    protected final @NotNull Consumer<? super R> readPacketHandler;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    protected volatile @Nullable ByteBuffer tempPendingBuffer;

    protected final int maxPacketsByRead;

    protected AbstractPacketReader(
        @NotNull C connection,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull Runnable updateActivityFunction,
        @NotNull NotNullConsumer<? super R> readPacketHandler,
        int maxPacketsByRead
    ) {
        this.connection = connection;
        this.channel = channel;
        this.bufferAllocator = bufferAllocator;
        this.readBuffer = bufferAllocator.takeReadBuffer();
        this.pendingBuffer = bufferAllocator.takePendingBuffer();
        this.updateActivityFunction = updateActivityFunction;
        this.readPacketHandler = readPacketHandler;
        this.maxPacketsByRead = maxPacketsByRead;
    }

    protected @NotNull ByteBuffer getBufferToReadFromChannel() {
        return readBuffer;
    }

    @Override
    public void startRead() {

        if (!isReading.compareAndSet(false, true)) {
            return;
        }

        LOGGER.debug(channel, ch -> "Start waiting for new data from channel \"" + getRemoteAddress(ch) + "\"");

        var buffer = getBufferToReadFromChannel();

        channel.read(buffer, buffer, readHandler);
    }

    /**
     * Read packets from the buffer with received data.
     *
     * @param receivedBuffer the buffer with received data.
     * @return count of read packets.
     */
    protected int readPackets(@NotNull ByteBuffer receivedBuffer) {
        return readPackets(receivedBuffer, pendingBuffer);
    }

    /**
     * Read packets from the buffer with received data.
     *
     * @param receivedBuffer the buffer with received data.
     * @param pendingBuffer  the buffer with pending data from prev. received buffer.
     * @return count of read packets.
     */
    protected int readPackets(@NotNull ByteBuffer receivedBuffer, @NotNull ByteBuffer pendingBuffer) {

        LOGGER.debug(receivedBuffer, buf -> "Start reading packets from received buffer " + buf);

        var waitedBytes = pendingBuffer.position();
        var bufferToRead = receivedBuffer;
        var tempPendingBuffer = getTempPendingBuffer();

        // if we have a temp buffer it means that we are reading a really big packet now
        if (tempPendingBuffer != null) {

            if (tempPendingBuffer.remaining() < receivedBuffer.remaining()) {
                reAllocTempBuffers(tempPendingBuffer.flip(), tempPendingBuffer.capacity());
                tempPendingBuffer = notNull(getTempPendingBuffer());
            }

            LOGGER.debug(
                receivedBuffer,
                tempPendingBuffer,
                (buf, mappedBuf) -> "Put received buffer " + buf + " to read mapped buffer " + mappedBuf
            );

            bufferToRead = BufferUtils.putToAndFlip(tempPendingBuffer, receivedBuffer);
        }
        // if we have some pending data we need to append the received buffer to the pending buffer
        // and start to read pending buffer with result received data
        else if (waitedBytes > 0) {

            if (pendingBuffer.remaining() < receivedBuffer.remaining()) {

                LOGGER.debug(
                    pendingBuffer,
                    receivedBuffer,
                    (penBuf, buf) -> "Pending buffer " + penBuf + " is too small to append received buffer " +
                        buf + ", will allocate new temp buffer for this"
                );

                allocTempBuffers(pendingBuffer.flip(), pendingBuffer.capacity());

                LOGGER.debug(pendingBuffer, buf -> "Clear pending buffer: " + buf);

                pendingBuffer.clear();

                tempPendingBuffer = notNull(getTempPendingBuffer());

                LOGGER.debugNullable(
                    receivedBuffer,
                    tempPendingBuffer,
                    (buf, mappedBuf) -> "Put received buffer: " + buf + " to mapped buffer: " + mappedBuf
                );

                bufferToRead = BufferUtils.putToAndFlip(tempPendingBuffer, receivedBuffer);

            } else {

                LOGGER.debug(
                    receivedBuffer,
                    pendingBuffer,
                    (buf, penBuf) -> "Put received buffer: " + buf + " to pending buffer: " + penBuf
                );

                bufferToRead = BufferUtils.putToAndFlip(pendingBuffer, receivedBuffer);
            }
        }

        var maxPacketsByRead = getMaxPacketsByRead();

        var readPackets = 0;
        var endPosition = 0;

        while (canStartReadPacket(bufferToRead) && readPackets < maxPacketsByRead) {

            // set position of start a next packet
            bufferToRead.position(endPosition);

            var positionBeforeRead = endPosition;
            var packetLength = readPacketLength(bufferToRead);
            var dataLength = getDataLength(packetLength, bufferToRead.position() - endPosition, bufferToRead);

            LOGGER.debug(
                packetLength,
                positionBeforeRead,
                (length, pos) -> "Find next packet from position: " + pos + " with length: " + length
            );

            // calculate position of end the next packet
            endPosition += packetLength;

            // if the packet isn't full presented in this buffer
            if (packetLength == -1 || endPosition > bufferToRead.limit()) {

                bufferToRead.position(positionBeforeRead);

                // if we read the received buffer we need to put
                // not read data to the pending buffer or big mapped byte buffer
                if (bufferToRead == receivedBuffer) {
                    if (packetLength <= pendingBuffer.capacity()) {
                        pendingBuffer.put(receivedBuffer);
                        LOGGER.debug(
                            pendingBuffer,
                            buf -> "Put pending data form received buffer to pending buffer: " + buf
                        );
                    } else {
                        allocTempBuffers(receivedBuffer, packetLength);
                    }
                }
                // if we already read this pending buffer we need to compact it
                else if (bufferToRead == pendingBuffer) {

                    if (packetLength <= pendingBuffer.capacity()) {
                        pendingBuffer.compact();
                        LOGGER.debug(pendingBuffer, buf -> "Compact pending buffer: " + buf);
                    } else {
                        allocTempBuffers(pendingBuffer, packetLength);
                        LOGGER.debug(pendingBuffer, buf -> "Clear pending buffer: " + buf);
                        pendingBuffer.clear();
                    }

                } else if (bufferToRead == tempPendingBuffer) {

                    // if not read data is less than pending buffer then we can switch to use the pending buffer
                    if (Math.max(packetLength, tempPendingBuffer.remaining()) <= pendingBuffer.capacity()) {

                        pendingBuffer
                            .clear()
                            .put(tempPendingBuffer);

                        LOGGER.debug(
                            pendingBuffer,
                            buf -> "Moved pending data from mapped buffer to pending buffer: " + buf
                        );

                        freeTempBuffers();
                    }
                    // if a new packet is bigger than current read mapped buffer
                    else if (packetLength > tempPendingBuffer.capacity()) {
                        reAllocTempBuffers(tempPendingBuffer, packetLength);
                    }
                    // or just compact this current mapped buffer
                    else {
                        tempPendingBuffer.compact();
                        LOGGER.debug(tempPendingBuffer, buf -> "Compact mapped buffer: " + buf);
                    }
                }

                LOGGER.debug(
                    channel,
                    readPackets,
                    (ch, count) -> "Read " + count + " packets from received buffer of " + getRemoteAddress(ch) + ", " +
                        "but 1 packet is still waiting for receiving additional data."
                );

                receivedBuffer.clear();
                return readPackets;
            }

            R packet = createPacketFor(bufferToRead, positionBeforeRead, packetLength, dataLength);

            if (packet != null) {
                LOGGER.debug(packet, pck -> "Created instance of packet to read data: " + pck);
                readAndHandlePacket(bufferToRead, dataLength, packet);
                LOGGER.debug(packet, pck -> "Finished reading data of packet: " + pck);
                readPackets++;
            } else {
                LOGGER.warning("Cannot create any instance of packet to read data");
            }

            bufferToRead.position(endPosition);
        }

        if (bufferToRead.hasRemaining()) {

            if (bufferToRead == receivedBuffer) {
                pendingBuffer.put(receivedBuffer);
                LOGGER.debug("Found not yet read data from receive buffer, will put it to pending buffer.");
            } else {
                bufferToRead.compact();
            }

        } else if (bufferToRead == pendingBuffer) {
            pendingBuffer.clear();
        } else if (tempPendingBuffer != null) {
            freeTempBuffers();
        }

        LOGGER.debug(
            channel,
            readPackets,
            (ch, count) -> "Read " + count + " packets from received buffer of " + getRemoteAddress(ch) + "."
        );

        receivedBuffer.clear();
        return readPackets;
    }

    protected void readAndHandlePacket(@NotNull ByteBuffer bufferToRead, int dataLength, @NotNull R packet) {
        if (packet.read(connection, bufferToRead, dataLength)) {
            readPacketHandler.accept(packet);
        } else {
            LOGGER.error("Packet " + packet + " was read incorrectly");
        }
    }

    /**
     * Check buffer's data.
     *
     * @param buffer the buffer to read.
     * @return true if this buffer has enough data to start initial reading.
     */
    protected abstract boolean canStartReadPacket(@NotNull ByteBuffer buffer);

    /**
     * Calculate size of packet data.
     *
     * @param packetLength the full packet length.
     * @param readBytes    the count of already read bytes from buffer to get packet length.
     * @param buffer       the buffer.
     * @return the length of packet data part.
     */
    protected int getDataLength(int packetLength, int readBytes, @NotNull ByteBuffer buffer) {
        return packetLength - readBytes;
    }

    /**
     * Get the packet's data length of next packet in the buffer.
     *
     * @param buffer the buffer with received data.
     * @return the packet length or -1 if we have no enough data to read length.
     */
    protected abstract int readPacketLength(@NotNull ByteBuffer buffer);

    protected void reAllocTempBuffers(
        @NotNull ByteBuffer sourceBuffer,
        int packetLength
    ) {

        LOGGER.debug(
            sourceBuffer.capacity(),
            packetLength,
            (currentSize, newSize) -> "Resize read temp buffer from " + currentSize + " to " + newSize
        );

        var newReadTempBuffer = bufferAllocator.takeBuffer(packetLength + readBuffer.capacity());

        LOGGER.debug(
            sourceBuffer,
            newReadTempBuffer,
            (old, buf) -> "Moved pending data from old temp buffer " + old + " to new temp buffer " + buf
        );

        newReadTempBuffer.put(sourceBuffer);

        freeTempBuffers();

        this.tempPendingBuffer = newReadTempBuffer;
    }

    protected void allocTempBuffers(@NotNull ByteBuffer sourceBuffer, int packetLength) {

        LOGGER.debug(
            packetLength,
            sourceBuffer.remaining(),
            (length, part) -> "Request temp buffer to store a part: " + part + " of big packet with length: " + length
        );

        var readTempBuffer = bufferAllocator.takeBuffer(packetLength + readBuffer.capacity());

        LOGGER.debug(
            sourceBuffer,
            readTempBuffer,
            (recBuf, buf) -> "Put the part of packet: " + recBuf + " to mapped buffer: " + buf
        );

        readTempBuffer.put(sourceBuffer);

        this.tempPendingBuffer = readTempBuffer;
    }

    protected void freeTempBuffers() {

        var readTempBuffer = getTempPendingBuffer();

        if (readTempBuffer != null) {
            setTempPendingBuffer(null);
            bufferAllocator.putBuffer(readTempBuffer);
        }
    }

    /**
     * Handle received data.
     *
     * @param receivedBytes the count of received bytes.
     * @param readingBuffer the currently reading buffer.
     */
    protected void handleReceivedData(@NotNull Integer receivedBytes, @NotNull ByteBuffer readingBuffer) {
        updateActivityFunction.run();

        if (receivedBytes == -1) {
            connection.close();
            return;
        }

        LOGGER.debug(
            receivedBytes,
            channel,
            (bytes, ch) -> "Received " + bytes + " bytes from channel \"" + NetworkUtils.getRemoteAddress(ch) + "\""
        );

        readingBuffer.flip();
        try {
            readPackets(readingBuffer);
        } catch (Exception e) {
            LOGGER.error(e);
        }

        if (isReading.compareAndSet(true, false)) {
            startRead();
        }
    }

    /**
     * Handle the exception during receiving data.
     *
     * @param exception     the exception.
     * @param readingBuffer the currently reading buffer.
     */
    protected void handleFailedReceiving(@NotNull Throwable exception, @NotNull ByteBuffer readingBuffer) {
        if (exception instanceof AsynchronousCloseException) {
            LOGGER.info(connection, cn -> "Connection " + cn.getRemoteAddress() + " was closed.");
        } else {
            LOGGER.error(exception);
            connection.close();
        }
    }

    /**
     * Get the how many packets can be read by the one method call {@link #readPackets(ByteBuffer, ByteBuffer)}}.
     *
     * @return the how many packets can be read.
     */
    protected int getMaxPacketsByRead() {
        return maxPacketsByRead;
    }

    protected int readHeader(@NotNull ByteBuffer buffer, int headerSize) {
        switch (headerSize) {
            case 1:
                return buffer.get() & 0xFF;
            case 2:
                return buffer.getShort() & 0xFFFF;
            case 4:
                return buffer.getInt();
            default:
                throw new IllegalStateException("Wrong packet's header size: " + headerSize);
        }
    }

    /**
     * Create a packet to read received data.
     *
     * @param buffer              the buffer with received data.
     * @param startPacketPosition the start position of the packet in the buffer.
     * @param packetLength        the length of packet.
     * @param dataLength          length of packet's data.
     * @return the readable packet.
     */
    protected abstract @Nullable R createPacketFor(
        @NotNull ByteBuffer buffer,
        int startPacketPosition,
        int packetLength,
        int dataLength
    );

    @Override
    public void close() {

        bufferAllocator
            .putReadBuffer(readBuffer)
            .putPendingBuffer(pendingBuffer);

        freeTempBuffers();
    }
}
