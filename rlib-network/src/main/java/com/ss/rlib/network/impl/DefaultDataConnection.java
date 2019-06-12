package com.ss.rlib.network.impl;

import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.NetworkCryptor;
import com.ss.rlib.network.packet.PacketReader;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.WritablePacket;
import com.ss.rlib.network.packet.impl.DefaultPacketReader;
import org.jetbrains.annotations.NotNull;

import java.nio.channels.AsynchronousSocketChannel;

public abstract class DefaultDataConnection<R extends ReadablePacket, W extends WritablePacket> extends
    AbstractConnection<R, W> {

    public DefaultDataConnection(
        @NotNull Network<? extends Connection<R, W>> network,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull NetworkCryptor crypt,
        @NotNull BufferAllocator bufferAllocator,
        int maxPacketsByRead,
        int packetLengthHeaderSize
    ) {
        super(network, channel, crypt, bufferAllocator, maxPacketsByRead, packetLengthHeaderSize);
    }

    @Override
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

    protected abstract @NotNull R createReadablePacket();
}
