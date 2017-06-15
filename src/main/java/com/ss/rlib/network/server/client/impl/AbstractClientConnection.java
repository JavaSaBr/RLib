package com.ss.rlib.network.server.client.impl;

import org.jetbrains.annotations.NotNull;
import com.ss.rlib.network.impl.AbstractAsyncConnection;
import com.ss.rlib.network.packet.SendablePacket;
import com.ss.rlib.network.server.ServerNetwork;
import com.ss.rlib.network.server.client.ClientConnection;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * The base implementation of a client connection.
 *
 * @author JavaSaBr
 */
public abstract class AbstractClientConnection extends AbstractAsyncConnection implements ClientConnection {

    /**
     * Instantiates a new Abstract client connection.
     *
     * @param network      the network
     * @param channel      the channel
     * @param sendableType the sendable type
     */
    public AbstractClientConnection(@NotNull final ServerNetwork network,
                                    @NotNull final AsynchronousSocketChannel channel,
                                    @NotNull final Class<? extends SendablePacket> sendableType) {
        super(network, channel, sendableType);
    }
}
