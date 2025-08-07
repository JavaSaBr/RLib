package javasabr.rlib.network.impl;

import java.nio.channels.AsynchronousSocketChannel;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.packet.IdBasedReadablePacket;
import javasabr.rlib.network.packet.IdBasedWritablePacket;
import javasabr.rlib.network.packet.PacketReader;
import javasabr.rlib.network.packet.PacketWriter;
import javasabr.rlib.network.packet.impl.IdBasedPacketReader;
import javasabr.rlib.network.packet.impl.IdBasedPacketWriter;
import javasabr.rlib.network.packet.registry.ReadablePacketRegistry;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
@Getter(AccessLevel.PROTECTED)
public class IdBasedPacketConnection<R extends IdBasedReadablePacket<R>, W extends IdBasedWritablePacket> extends
    AbstractConnection<R, W> {

    private final @NotNull PacketReader packetReader;
    private final @NotNull PacketWriter packetWriter;
    private final @NotNull ReadablePacketRegistry<R> packetRegistry;

    private final int packetLengthHeaderSize;
    private final int packetIdHeaderSize;

    public IdBasedPacketConnection(
        @NotNull Network<? extends Connection<R, W>> network,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull ReadablePacketRegistry<R> packetRegistry,
        int maxPacketsByRead,
        int packetLengthHeaderSize,
        int packetIdHeaderSize
    ) {
        super(network, channel, bufferAllocator, maxPacketsByRead);
        this.packetRegistry = packetRegistry;
        this.packetLengthHeaderSize = packetLengthHeaderSize;
        this.packetIdHeaderSize = packetIdHeaderSize;
        this.packetReader = createPacketReader();
        this.packetWriter = createPacketWriter();
    }

    protected @NotNull PacketReader createPacketReader() {
        return new IdBasedPacketReader<>(
            this,
            channel,
            bufferAllocator,
            this::updateLastActivity,
            this::handleReceivedPacket,
            packetLengthHeaderSize,
            maxPacketsByRead,
            packetIdHeaderSize,
            packetRegistry
        );
    }

    protected @NotNull PacketWriter createPacketWriter() {
        return new IdBasedPacketWriter<>(
            this,
            channel,
            bufferAllocator,
            this::updateLastActivity,
            this::nextPacketToWrite,
            this::onWrittenPacket,
            this::onSentPacket,
            packetLengthHeaderSize,
            packetIdHeaderSize
        );
    }
}
