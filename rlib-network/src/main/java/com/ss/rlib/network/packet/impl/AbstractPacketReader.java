package com.ss.rlib.network.packet.impl;

import static com.ss.rlib.network.util.NetworkUtils.getSocketAddress;
import com.ss.rlib.common.util.BufferUtils;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.packet.PacketReader;
import com.ss.rlib.network.packet.ReadablePacket;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
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

    private final CompletionHandler<Integer, Void> readHandler = new CompletionHandler<>() {

        @Override
        public void completed(@NotNull Integer result, @Nullable Void attachment) {
            handleReadData(result);
        }

        @Override
        public void failed(@NotNull Throwable exc, @Nullable Void attachment) {
            handleFailedRead(exc);
        }
    };

    protected final AtomicBoolean isReading = new AtomicBoolean(false);

    protected final C connection;
    protected final AsynchronousSocketChannel channel;
    protected final BufferAllocator bufferAllocator;

    protected final ByteBuffer readBuffer;
    protected final ByteBuffer pendingBuffer;
    protected final ByteBuffer decryptedBuffer;

    protected final Runnable updateActivityFunction;
    protected final Consumer<? super R> readPacketHandler;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    protected volatile ByteBuffer readTempBuffer;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    protected volatile ByteBuffer decryptedTempBuffer;

    protected final int maxPacketsByRead;

    protected AbstractPacketReader(
        @NotNull C connection,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull Runnable updateActivityFunction,
        @NotNull Consumer<? super R> readPacketHandler,
        int maxPacketsByRead
    ) {
        this.connection = connection;
        this.channel = channel;
        this.bufferAllocator = bufferAllocator;
        this.readBuffer = bufferAllocator.takeReadBuffer();
        this.pendingBuffer = bufferAllocator.takePendingBuffer();
        this.decryptedBuffer = bufferAllocator.takePendingBuffer();
        this.updateActivityFunction = updateActivityFunction;
        this.readPacketHandler = readPacketHandler;
        this.maxPacketsByRead = maxPacketsByRead;
    }

    @Override
    public void startRead() {
        if (isReading.compareAndSet(false, true)) {
            channel.read(readBuffer, null, readHandler);
        }
    }

    /**
     * Read packets from the buffer with received data.
     *
     * @param receivedBuffer the buffer with received data.
     * @param pendingBuffer  the buffer with pending data from prev. received buffer.
     * @return count of read packets.
     */
    protected int readPackets(@NotNull ByteBuffer receivedBuffer, @NotNull ByteBuffer pendingBuffer) {

        LOGGER.debug(receivedBuffer, buf -> "Start reading packets from received buffer: " + buf);

        var crypt = connection.getCrypt();

        var waitedBytes = pendingBuffer.position();
        var bufferToRead = receivedBuffer;
        var bufferToDecrypt = decryptedBuffer;
        var readTempBuffer = getReadTempBuffer();

        // if we have read mapped buffer it means that we are reading a really big packet now
        if (readTempBuffer != null) {

            if (readTempBuffer.remaining() < receivedBuffer.remaining()) {
                reAllocTempBuffers(readTempBuffer.flip(), readTempBuffer.capacity());
                readTempBuffer = this.readTempBuffer;
            }

            LOGGER.debug(receivedBuffer, readTempBuffer,
                (buf, mappedBuf) -> "Put received buffer: " + buf + " to read mapped buffer: " + mappedBuf);

            bufferToRead = BufferUtils.putToAndFlip(readTempBuffer, receivedBuffer);
            bufferToDecrypt = decryptedTempBuffer;
        }
        // if we have some pending data we need to append the received buffer to the pending buffer
        // and start to read pending buffer with result received data
        else if (waitedBytes > 0) {

            if (pendingBuffer.remaining() < receivedBuffer.remaining()) {

                LOGGER.debug(pendingBuffer, receivedBuffer,
                    (penBuf, buf) -> "Pending buffer: " + penBuf + " is too small to append received buffer: " +
                        buf + ", allocate mapped buffer for this");

                allocTempBuffers(pendingBuffer.flip(), pendingBuffer.capacity());

                LOGGER.debug(pendingBuffer, buf -> "Clear pending buffer: " + buf);

                pendingBuffer.clear();

                readTempBuffer = this.readTempBuffer;
                bufferToDecrypt = decryptedTempBuffer;

                LOGGER.debug(receivedBuffer, readTempBuffer,
                    (buf, mappedBuf) -> "Put received buffer: " + buf + " to mapped buffer: " + mappedBuf);

                bufferToRead = BufferUtils.putToAndFlip(readTempBuffer, receivedBuffer);

            } else {

                LOGGER.debug(receivedBuffer, pendingBuffer,
                    (buf, penBuf) -> "Put received buffer: " + buf + " to pending buffer: " + penBuf);

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
            var dataLength = calcDataLength(packetLength, bufferToRead.position() - endPosition, bufferToRead);

            LOGGER.debug(packetLength, positionBeforeRead,
                (length, pos) -> "Find next packet from position: " + pos + " with length: " + length);

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
                        LOGGER.debug(pendingBuffer,
                            buf -> "Put pending data form received buffer to pending buffer: " + buf);
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

                } else if (bufferToRead == readTempBuffer) {

                    // if not read data is less than pending buffer then we can switch to use the pending buffer
                    if (Math.max(packetLength, readTempBuffer.remaining()) <= pendingBuffer.capacity()) {

                        pendingBuffer.clear()
                            .put(readTempBuffer);

                        LOGGER.debug(pendingBuffer,
                            buf -> "Moved pending data from mapped buffer to pending buffer: " + buf);

                        freeTempBuffers();
                    }
                    // if a new packet is bigger than current read mapped buffer
                    else if (packetLength > readTempBuffer.capacity()) {
                        reAllocTempBuffers(readTempBuffer, packetLength);
                    }
                    // or just compact this current mapped buffer
                    else {
                        readTempBuffer.compact();
                        LOGGER.debug(readTempBuffer, buf -> "Compact mapped buffer: " + buf);
                    }
                }

                LOGGER.debug(channel, readPackets,
                    (ch, count) -> "Read " + count + " packet(s) from buffered data of " + getSocketAddress(ch) + ", " +
                        "but 1 packet is still waiting for receiving additional data.");

                return readPackets;
            }

            var decryptedData = crypt.decrypt(
                bufferToRead,
                dataLength,
                bufferToDecrypt.clear()
            );

            R packet;

            if (decryptedData != null) {
                packet = createPacketFor(decryptedData, positionBeforeRead, packetLength, dataLength);
            } else {
                packet = createPacketFor(bufferToRead, positionBeforeRead, packetLength, dataLength);
            }

            if (packet != null) {
                LOGGER.debug(packet, pck -> "Created instance of packet to read data: " + pck);
                packet.read(connection, bufferToRead, dataLength);
                readPacketHandler.accept(packet);
                LOGGER.debug(packet, pck -> "Read data of packet: " + pck);
                readPackets++;
            } else {
                LOGGER.warning("Cannot create any instance of packet to read data.");
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
        } else if (readTempBuffer != null) {
            freeTempBuffers();
        }

        LOGGER.debug(channel, readPackets,
            (ch, count) -> "Read " + count + " packet(s) from buffered data of " + getSocketAddress(ch) + ".");

        return readPackets;
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
    protected int calcDataLength(int packetLength, int readBytes, @NotNull ByteBuffer buffer) {
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

        LOGGER.debug(sourceBuffer.capacity(), packetLength, (currentSize, newSize) ->
            "Resize read temp buffer from: " + currentSize + " to: " + newSize);

        var newReadTempBuffer = bufferAllocator.takeBuffer(packetLength + readBuffer.capacity());

        LOGGER.debug(sourceBuffer, newReadTempBuffer,
            (old, buf) -> "Moved pending data from old temp buffer: " + old + " to new temp buffer: " + buf);

        newReadTempBuffer.put(sourceBuffer);

        freeTempBuffers();

        this.readTempBuffer = newReadTempBuffer;
        this.decryptedTempBuffer = bufferAllocator.takeBuffer(newReadTempBuffer.capacity());
    }

    protected void allocTempBuffers(@NotNull ByteBuffer sourceBuffer, int packetLength) {

        LOGGER.debug(packetLength, sourceBuffer.remaining(), (length, part) ->
            "Request temp buffer to store a part: " + part + " of big packet with length: " + length);

        var readTempBuffer = bufferAllocator.takeBuffer(packetLength + readBuffer.capacity());

        LOGGER.debug(sourceBuffer, readTempBuffer,
            (recBuf, buf) -> "Put the part of packet: " + recBuf + " to mapped buffer: " + buf);

        readTempBuffer.put(sourceBuffer);

        this.readTempBuffer = readTempBuffer;
        this.decryptedTempBuffer = bufferAllocator.takeBuffer(readTempBuffer.capacity());
    }

    protected void freeTempBuffers() {

        var readTempBuffer = getReadTempBuffer();
        var decryptedTempBuffer = getDecryptedTempBuffer();

        if (readTempBuffer != null) {
            setReadTempBuffer(null);
            bufferAllocator.putBuffer(readTempBuffer);
        }

        if (decryptedTempBuffer != null) {
            setDecryptedTempBuffer(null);
            bufferAllocator.putBuffer(decryptedTempBuffer);
        }
    }

    /**
     * Handle read data.
     *
     * @param result the count of read bytes.
     */
    protected void handleReadData(@NotNull Integer result) {
        updateActivityFunction.run();

        if (result == -1) {
            connection.close();
            return;
        }

        readBuffer.flip();
        try {
            readPackets(readBuffer, pendingBuffer);
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            readBuffer.clear();
        }

        if (isReading.compareAndSet(true, false)) {
            startRead();
        }
    }

    /**
     * Handle the exception during reading data.
     *
     * @param exception the exception.
     */
    protected void handleFailedRead(@NotNull Throwable exception) {
        LOGGER.error(exception);
        connection.close();
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
