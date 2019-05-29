package com.ss.rlib.network.impl.simple;

import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.NetworkCryptor;
import com.ss.rlib.network.impl.AbstractConnection;
import com.ss.rlib.network.packet.PacketReader;
import com.ss.rlib.network.packet.impl.DefaultPacketReader;
import com.ss.rlib.network.packet.impl.simple.StringReadablePacket;
import com.ss.rlib.network.packet.impl.simple.StringWritablePacket;
import org.jetbrains.annotations.NotNull;

import java.nio.channels.AsynchronousSocketChannel;

public class StringDataConnection extends AbstractConnection<StringReadablePacket, StringWritablePacket> {

    public StringDataConnection(
        @NotNull Network<? extends Connection<StringReadablePacket, StringWritablePacket>> network,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator
    ) {
        this(network, channel, NetworkCryptor.NULL, bufferAllocator, 100, 2);
    }

    public StringDataConnection(
        @NotNull Network<? extends Connection<StringReadablePacket, StringWritablePacket>> network,
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
            readBuffer,
            readPendingBuffer,
            readDecryptedBuffer,
            this::updateLastActivity,
            this::handleReadPacket,
            value -> new StringReadablePacket(),
            maxPacketsByRead,
            packetLengthHeaderSize
        );
    }
}
