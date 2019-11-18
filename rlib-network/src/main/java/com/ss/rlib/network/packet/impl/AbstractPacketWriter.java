package com.ss.rlib.network.packet.impl;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.packet.PacketWriter;
import com.ss.rlib.network.packet.WritablePacket;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author JavaSaBr
 */
@RequiredArgsConstructor
public abstract class AbstractPacketWriter<W extends WritablePacket, C extends Connection<?, W>> implements
    PacketWriter {

    private static final Logger LOGGER = LoggerManager.getLogger(AbstractPacketWriter.class);

    private final CompletionHandler<Integer, WritablePacket> writeHandler = new CompletionHandler<>() {

        @Override
        public void completed(@NotNull Integer result, @NotNull WritablePacket packet) {
            handleSuccessfulWriting(result, packet);
        }

        @Override
        public void failed(@NotNull Throwable exc, @NotNull WritablePacket packet) {
            handleFailedWriting(exc, packet);
        }
    };

    protected final AtomicBoolean isWriting = new AtomicBoolean();

    protected final C connection;
    protected final AsynchronousSocketChannel channel;
    protected final BufferAllocator bufferAllocator;
    protected final ByteBuffer firstWriteBuffer;
    protected final ByteBuffer secondWriteBuffer;

    protected volatile ByteBuffer firstWriteTempBuffer;
    protected volatile ByteBuffer secondWriteTempBuffer;

    protected final @NotNull Runnable updateActivityFunction;
    protected final @NotNull Supplier<@Nullable WritablePacket> nextWritePacketSupplier;
    protected final @NotNull Consumer<@NotNull WritablePacket> writtenPacketHandler;
    protected final @NotNull BiConsumer<@NotNull WritablePacket, Boolean> sentPacketHandler;

    public AbstractPacketWriter(
        @NotNull C connection,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull Runnable updateActivityFunction,
        @NotNull Supplier<@Nullable WritablePacket> packetProvider,
        @NotNull Consumer<@NotNull WritablePacket> writtenPacketHandler,
        @NotNull BiConsumer<@NotNull WritablePacket, Boolean> sentPacketHandler
    ) {
        this.connection = connection;
        this.channel = channel;
        this.bufferAllocator = bufferAllocator;
        this.firstWriteBuffer = bufferAllocator.takeWriteBuffer();
        this.secondWriteBuffer = bufferAllocator.takeWriteBuffer();
        this.updateActivityFunction = updateActivityFunction;
        this.nextWritePacketSupplier = packetProvider;
        this.writtenPacketHandler = writtenPacketHandler;
        this.sentPacketHandler = sentPacketHandler;
    }

    @Override
    public void writeNextPacket() {

        if (connection.isClosed() || !isWriting.compareAndSet(false, true)) {
            return;
        }

        var waitPacket = nextWritePacketSupplier.get();

        if (waitPacket == null) {
            isWriting.set(false);
            return;
        }

        var byteBuffer = serialize(waitPacket);

        if (byteBuffer.limit() != 0) {
            channel.write(byteBuffer, waitPacket, writeHandler);
        } else {
            isWriting.set(false);
        }

        writtenPacketHandler.accept(waitPacket);
    }

    protected @NotNull ByteBuffer serialize(@NotNull WritablePacket packet) {

        if (packet instanceof WritablePacketWrapper) {
            packet = ((WritablePacketWrapper) packet).getPacket();
        }

        W resultPacket = (W) packet;

        var expectedLength = packet.getExpectedLength();
        var totalSize = getTotalSize(packet, expectedLength);

        // if the packet is too big to use a write buffer
        if (expectedLength != -1 && totalSize > firstWriteBuffer.capacity()) {
            firstWriteTempBuffer = bufferAllocator.takeBuffer(totalSize);
            secondWriteTempBuffer = bufferAllocator.takeBuffer(totalSize);
            return serialize(resultPacket, expectedLength, totalSize, firstWriteTempBuffer, secondWriteTempBuffer);
        } else {
            return serialize(resultPacket, expectedLength, totalSize, firstWriteBuffer, secondWriteBuffer);
        }
    }

    /**
     * Get a total size of packet if it possible.
     *
     * @param packet         the packet.
     * @param expectedLength the expected size.
     * @return the total size or -1.
     */
    protected abstract int getTotalSize(@NotNull WritablePacket packet, int expectedLength);

    /**
     * Serialize packet to byte buffer.
     *
     * @param packet         the packet to serialize.
     * @param expectedLength the packet's expected size.
     * @param totalSize      the packet's total size.
     * @param firstBuffer    the first byte buffer.
     * @param secondBuffer   the second byte buffer.
     * @return the buffer to write to channel.
     */
    protected @NotNull ByteBuffer serialize(
        @NotNull W packet,
        int expectedLength,
        int totalSize,
        @NotNull ByteBuffer firstBuffer,
        @NotNull ByteBuffer secondBuffer
    ) {

        if(!onBeforeWrite(packet, expectedLength, totalSize, firstBuffer, secondBuffer)) {
            return firstBuffer.clear().limit(0);
        } else if (!onWrite(packet, expectedLength, totalSize, firstBuffer, secondBuffer)) {
            return firstBuffer.clear().limit(0);
        } else if(!onAfterWrite(packet, expectedLength, totalSize, firstBuffer, secondBuffer)) {
            return firstBuffer.clear().limit(0);
        }

        // FIXME need to rewrite in more flexible style
        /*var crypt = connection.getCrypt();
        var encrypted = crypt.encrypt(buffer, buffer.limit() - packetLengthHeaderSize, encryptedBuffer.clear());

        // nothing to encrypt
        if (encrypted == null) {
            return onResult(packet, buffer);
        }

        buffer.clear();
        buffer.position(packetLengthHeaderSize);
        buffer.put(encrypted);
        buffer.flip();*/

        return onResult(packet, expectedLength, totalSize, firstBuffer, secondBuffer);
    }

    /**
     * Handle a byte buffer before writing packet's data.
     *
     * @param packet         the packet.
     * @param expectedLength the packet's expected size.
     * @param totalSize      the packet's total size.
     * @param firstBuffer    the first byte buffer.
     * @param secondBuffer   the second byte buffer.
     * @return true if handling was successful.
     */
    protected boolean onBeforeWrite(
        @NotNull W packet,
        int expectedLength,
        int totalSize,
        @NotNull ByteBuffer firstBuffer,
        @NotNull ByteBuffer secondBuffer
    ) {
        firstBuffer.clear();
        return true;
    }

    /**
     * Write a packet to byte buffer.
     *
     * @param packet         the packet.
     * @param expectedLength the packet's expected size.
     * @param totalSize      the packet's total size.
     * @param firstBuffer    the first byte buffer.
     * @param secondBuffer   the second byte buffer.
     * @return true if writing was successful.
     */
    protected boolean onWrite(
        @NotNull W packet,
        int expectedLength,
        int totalSize,
        @NotNull ByteBuffer firstBuffer,
        @NotNull ByteBuffer secondBuffer
    ) {
        return packet.write(firstBuffer);
    }

    /**
     * Handle a byte buffer after writing packet's data.
     *
     * @param packet         the packet.
     * @param expectedLength the packet's expected size.
     * @param totalSize      the packet's total size.
     * @param firstBuffer    the first byte buffer.
     * @param secondBuffer   the second byte buffer.
     * @return true if handling was successful.
     */
    protected boolean onAfterWrite(
        @NotNull W packet,
        int expectedLength,
        int totalSize,
        @NotNull ByteBuffer firstBuffer,
        @NotNull ByteBuffer secondBuffer
    ) {
        firstBuffer.flip();
        return true;
    }

    /**
     * Handle a final result byte buffer.
     *
     * @param packet         the packet.
     * @param expectedLength the packet's expected size.
     * @param totalSize      the packet's total size.
     * @param firstBuffer    the first byte buffer.
     * @param secondBuffer   the second byte buffer.
     * @return the same byte buffer.
     */
    protected @NotNull ByteBuffer onResult(
        @NotNull W packet,
        int expectedLength,
        int totalSize,
        @NotNull ByteBuffer firstBuffer,
        @NotNull ByteBuffer secondBuffer
    ) {
        return firstBuffer.position(0);
    }

    protected @NotNull ByteBuffer writeHeader(@NotNull ByteBuffer buffer, int position, int value, int headerSize) {

        switch (headerSize) {
            case 1:
                buffer.put(position, (byte) value);
                break;
            case 2:
                buffer.putShort(position, (short) value);
                break;
            case 4:
                buffer.putInt(position, value);
                break;
            default:
                throw new IllegalStateException("Wrong packet's header size: " + headerSize);
        }

        return buffer;
    }

    protected @NotNull ByteBuffer writeHeader(@NotNull ByteBuffer buffer, int value, int headerSize) {

        switch (headerSize) {
            case 1:
                buffer.put((byte) value);
                break;
            case 2:
                buffer.putShort((short) value);
                break;
            case 4:
                buffer.putInt(value);
                break;
            default:
                throw new IllegalStateException("Wrong packet's header size: " + headerSize);
        }

        return buffer;
    }

    /**
     * Handle successful wrote data.
     *
     * @param result the count of wrote bytes.
     * @param packet the sent packet.
     */
    protected void handleSuccessfulWriting(@NotNull Integer result, @NotNull WritablePacket packet) {
        updateActivityFunction.run();

        if (result == -1) {
            sentPacketHandler.accept(packet, Boolean.FALSE);
            connection.close();
            return;
        }

        var writeTempBuffer = this.firstWriteTempBuffer;

        // if we have data in temp write buffer we need to use it
        if (writeTempBuffer != null) {

            if (writeTempBuffer.remaining() > 0) {
                channel.write(writeTempBuffer, packet, writeHandler);
                return;
            }
            // if all data from temp buffers was written then we can remove it
            else {
                clearTempBuffers();
            }

        } else {
            if (firstWriteBuffer.remaining() > 0) {
                channel.write(firstWriteBuffer, packet, writeHandler);
                return;
            }
        }

        sentPacketHandler.accept(packet, Boolean.TRUE);

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
    protected void handleFailedWriting(@NotNull Throwable exception, @NotNull WritablePacket packet) {
        LOGGER.error(new RuntimeException("Failed writing packet: " + packet, exception));

        if (!connection.isClosed()) {
            if (isWriting.compareAndSet(true, false)) {
                writeNextPacket();
            }
        }
    }

    @Override
    public void close() {

        bufferAllocator
            .putWriteBuffer(firstWriteBuffer)
            .putWriteBuffer(secondWriteBuffer);

        clearTempBuffers();
    }

    protected void clearTempBuffers() {

        var secondWriteTempBuffer = this.secondWriteTempBuffer;
        var firstWriteTempBuffer = this.firstWriteTempBuffer;

        if (secondWriteTempBuffer != null) {
            this.secondWriteTempBuffer = null;
            bufferAllocator.putBuffer(secondWriteTempBuffer);
        }

        if (firstWriteTempBuffer != null) {
            this.firstWriteTempBuffer = null;
            bufferAllocator.putBuffer(firstWriteTempBuffer);
        }
    }
}
