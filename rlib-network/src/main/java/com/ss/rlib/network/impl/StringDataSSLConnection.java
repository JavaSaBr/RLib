package com.ss.rlib.network.impl;

import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.packet.impl.StringReadablePacket;
import com.ss.rlib.network.packet.impl.StringWritablePacket;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author JavaSaBr
 */
public class StringDataSSLConnection extends DefaultDataSSLConnection<StringReadablePacket, StringWritablePacket> {

    public StringDataSSLConnection(
        @NotNull Network<? extends Connection<StringReadablePacket, StringWritablePacket>> network,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull SSLContext sslContext,
        boolean clientMode
    ) {
        super(network, channel, bufferAllocator, sslContext, 100, 2, clientMode);
    }

    @Override
    protected @NotNull StringReadablePacket createReadablePacket() {
        return new StringReadablePacket();
    }
}
