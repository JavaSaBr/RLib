package com.ss.rlib.common.network.client.server.impl;

import com.ss.rlib.common.network.client.ClientNetwork;
import com.ss.rlib.common.network.impl.AbstractAsyncConnection;
import com.ss.rlib.common.network.packet.WritablePacket;
import com.ss.rlib.common.network.client.ClientNetwork;
import com.ss.rlib.common.network.client.server.Server;
import com.ss.rlib.common.network.client.server.ServerConnection;
import com.ss.rlib.common.network.impl.AbstractAsyncConnection;
import com.ss.rlib.common.network.packet.WritablePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * The base implementation of {@link ServerConnection}.
 *
 * @author JavaSaBr
 */
public class DefaultServerConnection extends AbstractAsyncConnection implements ServerConnection {

    public DefaultServerConnection(@NotNull final ClientNetwork network,
                                   @NotNull final AsynchronousSocketChannel channel) {
        super(network, channel, WritablePacket.class);
    }

    public DefaultServerConnection(@NotNull final ClientNetwork network,
                                   @NotNull final AsynchronousSocketChannel channel,
                                   @NotNull final Class<? extends WritablePacket> sendableType) {
        super(network, channel, sendableType);
    }

    @Override
    public @Nullable Server getOwner() {
        return (Server) super.getOwner();
    }

    @Override
    protected void doClose() throws IOException {
        super.doClose();
        final ClientNetwork clientNetwork = (ClientNetwork) getNetwork();
        if (clientNetwork.getCurrentServer() == getOwner()) {
            clientNetwork.setCurrentServer(null);
        }
    }
}
