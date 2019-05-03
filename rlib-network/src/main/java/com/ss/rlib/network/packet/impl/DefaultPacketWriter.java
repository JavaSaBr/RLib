package com.ss.rlib.network.packet.impl;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.AsyncConnection;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.NetworkCrypt;
import com.ss.rlib.network.packet.PacketWriter;
import com.ss.rlib.network.packet.ReusableWritablePacket;
import com.ss.rlib.network.packet.WritablePacket;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class DefaultPacketWriter implements PacketWriter {

    protected static final Logger LOGGER = LoggerManager.getLogger(DefaultPacketWriter.class);

    /**
     * The write handler.
     */
    @NotNull @Getter
    private final CompletionHandler<Integer, WritablePacket> writeHandler = new CompletionHandler<>() {

        @Override
        public void completed(@NotNull Integer result, @NotNull WritablePacket packet) {
            handleWroteData(result, packet);
        }

        @Override
        public void failed(@NotNull Throwable exc, @NotNull WritablePacket packet) {
            handleFailedWrite(exc, packet);
        }
    };

    protected final AsyncConnection connection;
    protected final AsynchronousSocketChannel channel;
    protected final ByteBuffer writeBuffer;
    protected final ByteBuffer waitBuffer;

    protected final Runnable updateActivityFunction;
    protected final Supplier<@Nullable WritablePacket> nextWritePacketFunction;

    protected final AtomicBoolean isWriting;

    public DefaultPacketWriter(
        @NotNull AsyncConnection connection,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull ByteBuffer writeBuffer,
        @NotNull ByteBuffer waitBuffer,
        @NotNull Runnable updateActivityFunction,
        @NotNull Supplier<@Nullable WritablePacket> nextWritePacketFunction
    ) {
        this.connection = connection;
        this.channel = channel;
        this.writeBuffer = writeBuffer;
        this.waitBuffer = waitBuffer;
        this.updateActivityFunction = updateActivityFunction;
        this.nextWritePacketFunction = nextWritePacketFunction;
        this.isWriting = new AtomicBoolean();
    }

    /**
     * Write a next packet.
     */
    protected final void writeNextPacket() {

        if (connection.isClosed() || !isWriting.compareAndSet(false, true)) {
            return;
        }

        var waitPacket = nextWritePacketFunction.get();

        if (waitPacket == null) {
            isWriting.set(false);
            return;
        }

        channel.write(
            writePacketToBuffer(waitPacket, waitBuffer),
            waitPacket,
            writeHandler
        );

        completed(waitPacket);
    }

    /**
     * Write the packet to the write buffer.
     *
     * @param packet the packet.
     * @param buffer the write buffer.
     * @return the write buffer.
     */
    protected @NotNull ByteBuffer writePacketToBuffer(@NotNull WritablePacket packet, @NotNull ByteBuffer buffer) {

        buffer.clear();
        packet.prepareWritePosition(buffer);
        packet.write(buffer);
        buffer.flip();
        packet.writePacketSize(buffer, buffer.limit());

        var crypt = connection.getCrypt();
        var sizeByteCount = connection.getPacketSizeByteCount();
        var length = buffer.limit() - sizeByteCount;

        // FIXME
        encrypt(buffer, crypt, sizeByteCount, length);

        return buffer;
    }

    /**
     * Handle a completed packet.
     *
     * @param packet the writable packet.
     */
    protected void completed(@NotNull WritablePacket packet) {
        if (packet instanceof ReusableWritablePacket) {
            ((ReusableWritablePacket) packet).complete();
        }
    }

    /**
     * Encrypt data using the crypt of the connection owner.
     *
     * @param buffer the data buffer.
     * @param crypt  the crypt.
     * @param offset the offset.
     * @param length the length.
     * @return the encrypted data or null if this crypt implementation does encrypting inside the passed byte array.
     */
    protected @Nullable byte[] encrypt(@NotNull ByteBuffer buffer, @NotNull NetworkCrypt crypt, int offset, int length) {

        if (crypt.isNull()) {
            return null;
        }

        return crypt.encrypt(buffer.array(), offset, length);
    }

    /**

     * Handle wrote data.
     *
     * @param result the count of wrote bytes.
     * @param packet the sent packet.
     */
    protected void handleWroteData(@NotNull Integer result, @NotNull WritablePacket packet) {
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
    protected void handleFailedWrite(@NotNull Throwable exception, @NotNull WritablePacket packet) {

        var config = connection.getNetwork()
            .getConfig();

        if (config.isVisibleWriteException()) {
            LOGGER.warning(this, new Exception("incorrect write packet " + packet, exception));
        }

        if (connection.isClosed()) {
            return;
        }

        if (isWriting.compareAndSet(true, false)) {
            writeNextPacket();
        }
    }
}
