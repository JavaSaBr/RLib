package com.ss.rlib.network.impl;

import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.NetworkCryptor;
import com.ss.rlib.network.packet.IdBasedReadablePacket;
import com.ss.rlib.network.packet.IdBasedWritablePacket;
import com.ss.rlib.network.packet.PacketReader;
import com.ss.rlib.network.packet.PacketWriter;
import com.ss.rlib.network.packet.impl.IdBasedPacketReader;
import com.ss.rlib.network.packet.impl.IdBasedPacketWriter;
import com.ss.rlib.network.packet.registry.ReadablePacketRegistry;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author JavaSaBr
 */
public class IdBasedPacketConnection<R extends IdBasedReadablePacket<R>, W extends IdBasedWritablePacket> extends
    AbstractConnection<R, W> {

    @Getter(AccessLevel.PROTECTED)
    private final PacketReader packetReader;

    @Getter(AccessLevel.PROTECTED)
    private final PacketWriter packetWriter;

    private final ReadablePacketRegistry<R> packetRegistry;
    private final int packetIdHeaderSize;

    public IdBasedPacketConnection(
        @NotNull Network<? extends Connection<R, W>> network,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull NetworkCryptor crypt,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull ReadablePacketRegistry<R> packetRegistry,
        int maxPacketsByRead,
        int packetLengthHeaderSize,
        int packetIdHeaderSize
    ) {
        super(network, channel, crypt, bufferAllocator, maxPacketsByRead, packetLengthHeaderSize);
        this.packetRegistry = packetRegistry;
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
            this::handleReadPacket,
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
            packetLengthHeaderSize,
            packetIdHeaderSize
        );
    }
}
