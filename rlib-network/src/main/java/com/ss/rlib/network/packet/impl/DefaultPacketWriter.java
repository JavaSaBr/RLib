package com.ss.rlib.network.packet.impl;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.packet.PacketWriter;
import com.ss.rlib.network.packet.ReusableWritablePacket;
import com.ss.rlib.network.packet.WritablePacket;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class DefaultPacketWriter<W extends WritablePacket, C extends Connection<?, W>> implements PacketWriter {

    private static final Logger LOGGER = LoggerManager.getLogger(DefaultPacketWriter.class);

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
    protected final ByteBuffer writeBuffer;
    protected final ByteBuffer encryptedBuffer;

    protected final Runnable updateActivityFunction;
    protected final Supplier<@Nullable W> nextWritePacketSupplier;
    protected final int packetLengthHeaderSize;

    @Override
    public final void writeNextPacket() {

        if (connection.isClosed() || !isWriting.compareAndSet(false, true)) {
            return;
        }

        var waitPacket = nextWritePacketSupplier.get();

        if (waitPacket == null) {
            isWriting.set(false);
            return;
        }

        channel.write(serialize(waitPacket), waitPacket, writeHandler);

        completed(waitPacket);
    }

    protected @NotNull ByteBuffer serialize(@NotNull W packet) {

        writeBuffer.clear();
        writeBuffer.position(packetLengthHeaderSize);

        doWrite(packet);

        writeBuffer.flip();
        writeBuffer.position(packetLengthHeaderSize);

        var crypt = connection.getCrypt();
        var encrypted = crypt.encrypt(writeBuffer, writeBuffer.limit() - packetLengthHeaderSize, encryptedBuffer.clear());

        // nothing to encrypt
        if (encrypted == null) {
            return writePacketLength(writeBuffer, writeBuffer.limit());
        }

        writeBuffer.clear();
        writeBuffer.position(packetLengthHeaderSize);
        writeBuffer.put(encrypted);
        writeBuffer.flip();

        return writePacketLength(writeBuffer, writeBuffer.limit());
    }

    protected void doWrite(@NotNull W packet) {
        packet.write(writeBuffer);
    }

    protected @NotNull ByteBuffer writePacketLength(@NotNull ByteBuffer buffer, int dataLength) {
        return writeHeader(buffer, 0, dataLength, packetLengthHeaderSize);
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
        if (packet instanceof ReusableWritablePacket) {
            ((ReusableWritablePacket) packet).complete();
        }
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

        if (writeBuffer.remaining() > 0) {
            channel.write(writeBuffer, packet, writeHandler);
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
    protected void handleFailedWriting(@NotNull Throwable exception, @NotNull W packet) {
        LOGGER.error(this, new RuntimeException("Failed writing packet: " + packet, exception));

        if (!connection.isClosed()) {
            if (isWriting.compareAndSet(true, false)) {
                writeNextPacket();
            }
        }
    }
}
