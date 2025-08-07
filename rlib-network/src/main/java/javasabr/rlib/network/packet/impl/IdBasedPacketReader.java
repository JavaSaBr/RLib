package javasabr.rlib.network.packet.impl;

import javasabr.rlib.common.function.NotNullConsumer;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.packet.IdBasedReadablePacket;
import javasabr.rlib.network.packet.registry.ReadablePacketRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @param <R> the readable packet's type.
 * @param <C> the connection's type.
 * @author JavaSaBr
 */
public class IdBasedPacketReader<R extends IdBasedReadablePacket<R>, C extends Connection<R, ?>> extends
    AbstractPacketReader<R, C> {

    private final ReadablePacketRegistry<R> packetRegistry;
    private final int packetLengthHeaderSize;
    private final int packetIdHeaderSize;

    public IdBasedPacketReader(
        @NotNull C connection,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull Runnable updateActivityFunction,
        @NotNull NotNullConsumer<R> readPacketHandler,
        int packetLengthHeaderSize,
        int maxPacketsByRead,
        int packetIdHeaderSize,
        @NotNull ReadablePacketRegistry<R> packetRegistry
    ) {
        super(
            connection,
            channel,
            bufferAllocator,
            updateActivityFunction,
            readPacketHandler,
            maxPacketsByRead
        );
        this.packetLengthHeaderSize = packetLengthHeaderSize;
        this.packetIdHeaderSize = packetIdHeaderSize;
        this.packetRegistry = packetRegistry;
    }

    @Override
    protected boolean canStartReadPacket(@NotNull ByteBuffer buffer) {
        return buffer.remaining() > packetLengthHeaderSize;
    }

    @Override
    protected int readPacketLength(@NotNull ByteBuffer buffer) {
        return readHeader(buffer, packetLengthHeaderSize);
    }

    @Override
    protected @Nullable R createPacketFor(
        @NotNull ByteBuffer buffer,
        int startPacketPosition,
        int packetLength,
        int dataLength
    ) {
        return packetRegistry.findById(readHeader(buffer, packetIdHeaderSize))
            .newInstance();
    }
}
