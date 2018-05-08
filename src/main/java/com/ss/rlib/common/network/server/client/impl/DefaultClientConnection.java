package com.ss.rlib.common.network.server.client.impl;

import com.ss.rlib.common.network.impl.AbstractAsyncConnection;
import com.ss.rlib.common.network.packet.WritablePacket;
import com.ss.rlib.common.network.server.ServerNetwork;
import com.ss.rlib.common.network.server.client.Client;
import com.ss.rlib.common.network.server.client.ClientConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * The base implementation of {@link ClientConnection}.
 *
 * @author JavaSaBr
 */
public class DefaultClientConnection extends AbstractAsyncConnection implements ClientConnection {

    public DefaultClientConnection(@NotNull ServerNetwork network, @NotNull AsynchronousSocketChannel channel) {
        super(network, channel, WritablePacket.class);
    }

    public DefaultClientConnection(
            @NotNull ServerNetwork network,
            @NotNull AsynchronousSocketChannel channel,
            @NotNull Class<? extends WritablePacket> sendableType
    ) {
        super(network, channel, sendableType);
    }

    @Override
    public @Nullable Client getOwner() {
        return (Client) super.getOwner();
    }

    @Override
    public @NotNull ServerNetwork getNetwork() {
        return (ServerNetwork) super.getNetwork();
    }
}
