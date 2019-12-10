package com.ss.rlib.network.impl;

import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.packet.PacketReader;
import com.ss.rlib.network.packet.PacketWriter;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.WritablePacket;
import com.ss.rlib.network.packet.impl.DefaultPacketReader;
import com.ss.rlib.network.packet.impl.DefaultPacketWriter;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author JavaSaBr
 */
@Getter(AccessLevel.PROTECTED)
public abstract class DefaultDataConnection<R extends ReadablePacket, W extends WritablePacket> extends
    AbstractConnection<R, W> {

    private final @NotNull PacketReader packetReader;
    private final @NotNull PacketWriter packetWriter;

    private final int packetLengthHeaderSize;

    public DefaultDataConnection(
        @NotNull Network<? extends Connection<R, W>> network,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        int maxPacketsByRead,
        int packetLengthHeaderSize
    ) {
        super(network, channel, bufferAllocator, maxPacketsByRead);
        this.packetLengthHeaderSize = packetLengthHeaderSize;
        this.packetReader = createPacketReader();
        this.packetWriter = createPacketWriter();
    }

    protected @NotNull PacketReader createPacketReader() {
        return new DefaultPacketReader<>(
            this,
            channel,
            bufferAllocator,
            this::updateLastActivity,
            this::handleReadPacket,
            value -> createReadablePacket(),
            packetLengthHeaderSize,
            maxPacketsByRead
        );
    }

    protected @NotNull PacketWriter createPacketWriter() {
        return new DefaultPacketWriter<W, Connection<R, W>>(
            this,
            channel,
            bufferAllocator,
            this::updateLastActivity,
            this::nextPacketToWrite,
            this::onWrittenPacket,
            this::onSentPacket,
            packetLengthHeaderSize
        );
    }

    protected abstract @NotNull R createReadablePacket();
}
