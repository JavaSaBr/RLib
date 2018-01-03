package com.ss.rlib.network.server.client.impl;

import com.ss.rlib.network.impl.AbstractAsyncConnection;
import com.ss.rlib.network.packet.SendablePacket;
import com.ss.rlib.network.server.ServerNetwork;
import com.ss.rlib.network.server.client.Client;
import com.ss.rlib.network.server.client.ClientConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * The base implementation of {@link ClientConnection}.
 *
 * @author JavaSaBr
 */
public class DefaultClientConnection extends AbstractAsyncConnection implements ClientConnection {

    public DefaultClientConnection(@NotNull final ServerNetwork network,
                                   @NotNull final AsynchronousSocketChannel channel) {
        super(network, channel, SendablePacket.class);
    }

    public DefaultClientConnection(@NotNull final ServerNetwork network,
                                   @NotNull final AsynchronousSocketChannel channel,
                                   @NotNull final Class<? extends SendablePacket> sendableType) {
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
