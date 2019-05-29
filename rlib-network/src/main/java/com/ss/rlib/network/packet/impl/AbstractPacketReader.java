package com.ss.rlib.network.packet.impl;

import com.ss.rlib.common.util.BufferUtils;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.packet.PacketReader;
import com.ss.rlib.network.packet.ReadablePacket;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class AbstractPacketReader<R extends ReadablePacket, C extends Connection<R, ?>> implements
    PacketReader {

    private static final Logger LOGGER = LoggerManager.getLogger(AbstractPacketReader.class);

    private final CompletionHandler<Integer, Void> readHandler = new CompletionHandler<>() {

        @Override
        public void completed(@NotNull Integer result, @NotNull Void attachment) {
            handleReadData(result);
        }

        @Override
        public void failed(@NotNull Throwable exc, @NotNull Void attachment) {
            handleFailedRead(exc);
        }
    };

    protected final AtomicBoolean isReading = new AtomicBoolean(false);

    protected final C connection;
    protected final AsynchronousSocketChannel channel;

    protected final ByteBuffer readBuffer;
    protected final ByteBuffer pendingBuffer;
    protected final ByteBuffer decryptedBuffer;

    protected final Runnable updateActivityFunction;
    protected final Consumer<? super R> readPacketHandler;

    protected final int packetLengthHeaderSize;
    protected final int maxPacketsByRead;

    @Override
    public void startRead() {
        if (isReading.compareAndSet(false, true)) {
            channel.read(readBuffer, null, readHandler);
        }
    }

    /**
     * Read packets from the buffer with received data.
     *
     * @param buffer the buffer with received data.
     * @return count of read packets.
     */
    protected int readPackets(@NotNull ByteBuffer buffer, @NotNull ByteBuffer pendingBuffer) {

        var crypt = connection.getCrypt();

        var readPackets = 0;
        var waitedBytes = pendingBuffer.remaining();

        // if we have some waited data we need take it to the reading now buffer
        if (waitedBytes > 0) {
            takeFromPendingBuffer(buffer, pendingBuffer);
        }

        var maxPacketsByRead = getMaxPacketsByRead();

        for (int i = 0, endPosition = 0; buffer.remaining() >= packetLengthHeaderSize && i < maxPacketsByRead; i++) {

            // set position of start a next packet
            buffer.position(endPosition);

            var packetLength = readPacketLength(buffer);
            var dataLength = packetLength - packetLengthHeaderSize;

            // calculate position of end the next packet
            endPosition += packetLength;

            // if the packet isn't full presented in this buffer
            if (endPosition > buffer.limit()) {
                BufferUtils.append(pendingBuffer, buffer.position(buffer.position() - packetLengthHeaderSize));
                break;
            }

            var decryptedData = crypt.decrypt(
                buffer,
                dataLength,
                decryptedBuffer.clear()
            );

            R packet;

            if (decryptedData != null) {
                packet = createPacketFor(decryptedData, dataLength);
            } else {
                packet = createPacketFor(buffer, dataLength);
            }

            if (packet != null) {
                packet.read(connection, buffer, dataLength);
                readPacketHandler.accept(packet);
                readPackets++;
            }
        }

        if (buffer.hasRemaining()) {
            BufferUtils.append(pendingBuffer, buffer);
        }

        return readPackets;
    }

    /**
     * Take pending data from a pending buffer.
     *
     * @param buffer the currently reading buffer.
     * @param buffer the pending buffer.
     */
    protected void takeFromPendingBuffer(@NotNull ByteBuffer buffer, @NotNull ByteBuffer pendingBuffer) {

        BufferUtils.append(pendingBuffer, buffer);
        BufferUtils.loadFrom(pendingBuffer, buffer);

        if (pendingBuffer.hasRemaining()) {
            pendingBuffer.compact();
            pendingBuffer.position(0);
        } else {
            pendingBuffer.clear();
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
            LOGGER.error(this, e);
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

    /**
     * Get the packet's data length of next packet in the buffer.
     *
     * @param buffer the buffer with received data.
     * @return the packet length.
     */
    protected int readPacketLength(@NotNull ByteBuffer buffer) {
        return readHeader(buffer, packetLengthHeaderSize);
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
     * @param buffer the buffer with received data.
     * @param length length of packet's data.
     * @return the readable packet.
     */
    protected abstract @Nullable R createPacketFor(@NotNull ByteBuffer buffer, int length);
}
