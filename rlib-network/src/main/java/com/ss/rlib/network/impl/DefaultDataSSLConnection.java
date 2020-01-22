package com.ss.rlib.network.impl;

import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.packet.PacketReader;
import com.ss.rlib.network.packet.PacketWriter;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.WritablePacket;
import com.ss.rlib.network.packet.impl.DefaultSSLPacketReader;
import com.ss.rlib.network.packet.impl.DefaultSSLPacketWriter;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author JavaSaBr
 */
@Getter(AccessLevel.PROTECTED)
public abstract class DefaultDataSSLConnection<R extends ReadablePacket, W extends WritablePacket> extends
    AbstractSSLConnection<R, W> {

    private final @NotNull PacketReader packetReader;
    private final @NotNull PacketWriter packetWriter;

    private final int packetLengthHeaderSize;

    public DefaultDataSSLConnection(
        @NotNull Network<? extends Connection<R, W>> network,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull SSLContext sslContext,
        int maxPacketsByRead,
        int packetLengthHeaderSize,
        boolean clientMode
    ) {
        super(network, channel, bufferAllocator, sslContext, maxPacketsByRead, clientMode);
        this.packetLengthHeaderSize = packetLengthHeaderSize;
        this.packetReader = createPacketReader();
        this.packetWriter = createPacketWriter();
    }

    protected @NotNull PacketReader createPacketReader() {
        return new DefaultSSLPacketReader<>(
            this,
            channel,
            bufferAllocator,
            this::updateLastActivity,
            this::handleReceivedPacket,
            value -> createReadablePacket(),
            sslEngine,
            this::sendImpl,
            packetLengthHeaderSize,
            maxPacketsByRead
        );
    }

    protected @NotNull PacketWriter createPacketWriter() {
        return new DefaultSSLPacketWriter<W, Connection<R, W>>(
            this,
            channel,
            bufferAllocator,
            this::updateLastActivity,
            this::nextPacketToWrite,
            this::onWrittenPacket,
            this::onSentPacket,
            sslEngine,
            this::sendImpl,
            this::queueAtFirst,
            packetLengthHeaderSize
        );
    }

    protected abstract @NotNull R createReadablePacket();
}
