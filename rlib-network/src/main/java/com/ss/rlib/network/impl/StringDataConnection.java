package com.ss.rlib.network.impl;

import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.NetworkCryptor;
import com.ss.rlib.network.packet.impl.StringReadablePacket;
import com.ss.rlib.network.packet.impl.StringWritablePacket;
import org.jetbrains.annotations.NotNull;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author JavaSaBr
 */
public class StringDataConnection extends DefaultDataConnection<StringReadablePacket, StringWritablePacket> {

    public StringDataConnection(
        @NotNull Network<? extends Connection<StringReadablePacket, StringWritablePacket>> network,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator
    ) {
        super(network, channel, NetworkCryptor.NULL, bufferAllocator, 100, 2);
    }

    @Override
    protected @NotNull StringReadablePacket createReadablePacket() {
        return new StringReadablePacket();
    }
}
