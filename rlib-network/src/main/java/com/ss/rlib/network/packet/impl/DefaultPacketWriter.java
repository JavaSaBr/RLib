package com.ss.rlib.network.packet.impl;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.packet.PacketWriter;
import com.ss.rlib.network.packet.WritablePacket;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class DefaultPacketWriter<W extends WritablePacket, C extends Connection<?, W>> implements PacketWriter {

    private static final Logger LOGGER = LoggerManager.getLogger(DefaultPacketWriter.class);

    private static final VarHandle WTB_CAS;
    private static final VarHandle ETB_CAS;

    static {
        try {
            WTB_CAS = MethodHandles.lookup()
                .findVarHandle(DefaultPacketWriter.class, "writeTempBuffer", ByteBuffer.class);
            ETB_CAS = MethodHandles.lookup()
                .findVarHandle(DefaultPacketWriter.class, "encryptedTempBuffer", ByteBuffer.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final CompletionHandler<Integer, W> writeHandler = new CompletionHandler<>() {

        @Override
        public void completed(@NotNull Integer result, @NotNull W packet) {
            handleSuccessfulWriting(result, packet);
        }

        @Override
        public void failed(@NotNull Throwable exc, @NotNull W packet) {
            handleFailedWriting(exc, packet);
        }
    };

    protected final AtomicBoolean isWriting = new AtomicBoolean();

    protected final C connection;
    protected final AsynchronousSocketChannel channel;
    protected final BufferAllocator bufferAllocator;
    protected final ByteBuffer writeBuffer;
    protected final ByteBuffer encryptedBuffer;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    protected volatile ByteBuffer writeTempBuffer;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    protected volatile ByteBuffer encryptedTempBuffer;

    protected final Runnable updateActivityFunction;
    protected final Supplier<@Nullable W> nextWritePacketSupplier;
    protected final int packetLengthHeaderSize;

    public DefaultPacketWriter(
        @NotNull C connection,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull Runnable updateActivityFunction,
        @NotNull Supplier<@Nullable W> nextWritePacketSupplier,
        int packetLengthHeaderSize
    ) {
        this.connection = connection;
        this.channel = channel;
        this.bufferAllocator = bufferAllocator;
        this.writeBuffer = bufferAllocator.takeWriteBuffer();
        this.encryptedBuffer = bufferAllocator.takeWriteBuffer();
        this.updateActivityFunction = updateActivityFunction;
        this.nextWritePacketSupplier = nextWritePacketSupplier;
        this.packetLengthHeaderSize = packetLengthHeaderSize;
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

        completed(waitPacket);
    }

    protected @NotNull ByteBuffer serialize(@NotNull W packet) {

        var expectedLength = packet.getExpectedLength();
        var totalSize = expectedLength + packetLengthHeaderSize;

        // if the packet is too big to use a write buffer
        if (expectedLength != -1 && totalSize > writeBuffer.capacity()) {
            writeTempBuffer = bufferAllocator.takeBuffer(totalSize);
            encryptedTempBuffer = bufferAllocator.takeBuffer(totalSize);
            return serialize(packet, writeTempBuffer, encryptedTempBuffer);
        } else {
            return serialize(packet, writeBuffer, encryptedBuffer);
        }
    }

    protected @NotNull ByteBuffer serialize(
        @NotNull W packet,
        @NotNull ByteBuffer buffer,
        @NotNull ByteBuffer encryptedBuffer
    ) {

        buffer.clear();
        buffer.position(packetLengthHeaderSize);

        if (!doWrite(buffer, packet)) {
            return buffer.clear()
                .limit(0);
        }

        buffer.flip();
        buffer.position(packetLengthHeaderSize);

        var crypt = connection.getCrypt();
        var encrypted = crypt.encrypt(buffer, buffer.limit() - packetLengthHeaderSize, encryptedBuffer.clear());

        // nothing to encrypt
        if (encrypted == null) {
            return writePacketLength(buffer, buffer.limit())
                .position(0);
        }

        buffer.clear();
        buffer.position(packetLengthHeaderSize);
        buffer.put(encrypted);
        buffer.flip();

        return writePacketLength(buffer, buffer.limit())
            .position(0);
    }

    protected boolean doWrite(@NotNull ByteBuffer buffer, @NotNull W packet) {
        return packet.write(buffer);
    }

    protected @NotNull ByteBuffer writePacketLength(@NotNull ByteBuffer buffer, int packetLength) {
        return writeHeader(buffer, 0, packetLength, packetLengthHeaderSize);
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

    /**
     * Handle a completed packet.
     *
     * @param packet the writable packet.
     */
    protected void completed(@NotNull W packet) {
    }

    /**
     * Handle successful wrote data.
     *
     * @param result the count of wrote bytes.
     * @param packet the sent packet.
     */
    protected void handleSuccessfulWriting(@NotNull Integer result, @NotNull W packet) {
        updateActivityFunction.run();

        if (result == -1) {
            connection.close();
            return;
        }

        var writeTempBuffer = this.writeTempBuffer;

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
            if (writeBuffer.remaining() > 0) {
                channel.write(writeBuffer, packet, writeHandler);
                return;
            }
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
    protected void handleFailedWriting(@NotNull Throwable exception, @NotNull W packet) {
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
            .putWriteBuffer(writeBuffer)
            .putWriteBuffer(encryptedBuffer);

        clearTempBuffers();
    }

    protected void clearTempBuffers() {

        var encryptedTempBuffer = getEncryptedTempBuffer();
        var writeTempBuffer = getWriteTempBuffer();

        if (encryptedTempBuffer != null) {
            setEncryptedTempBuffer(null);
            bufferAllocator.putBuffer(encryptedTempBuffer);
        }

        if (writeTempBuffer != null) {
            setWriteTempBuffer(null);
            bufferAllocator.putBuffer(writeTempBuffer);
        }
    }
}
