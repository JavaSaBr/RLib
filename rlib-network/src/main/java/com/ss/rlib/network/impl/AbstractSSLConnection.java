package com.ss.rlib.network.impl;

import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.WritablePacket;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import java.nio.channels.AsynchronousSocketChannel;

public abstract class AbstractSSLConnection<R extends ReadablePacket, W extends WritablePacket> extends
    AbstractConnection<R, W> {

    protected final @NotNull SSLEngine sslEngine;

    public AbstractSSLConnection(
        @NotNull Network<? extends Connection<R, W>> network,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull SSLContext sslContext,
        int maxPacketsByRead,
        boolean clientMode
    ) {
        super(network, channel, bufferAllocator, maxPacketsByRead);
        this.sslEngine = sslContext.createSSLEngine();
        this.sslEngine.setUseClientMode(clientMode);
        try {
            this.sslEngine.beginHandshake();
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }
    }
}
