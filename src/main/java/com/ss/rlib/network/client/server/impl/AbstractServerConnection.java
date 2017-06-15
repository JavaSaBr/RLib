package com.ss.rlib.network.client.server.impl;

import com.ss.rlib.network.client.ClientNetwork;
import org.jetbrains.annotations.NotNull;
import com.ss.rlib.network.client.server.ServerConnection;
import com.ss.rlib.network.impl.AbstractAsyncConnection;
import com.ss.rlib.network.packet.SendablePacket;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * The base implementation of a server connection.
 *
 * @author JavaSaBr
 */
public abstract class AbstractServerConnection extends AbstractAsyncConnection implements ServerConnection {

    /**
     * Instantiates a new Abstract server connection.
     *
     * @param network      the network
     * @param channel      the channel
     * @param sendableType the sendable type
     */
    public AbstractServerConnection(@NotNull final ClientNetwork network,
                                    @NotNull final AsynchronousSocketChannel channel,
                                    @NotNull final Class<? extends SendablePacket> sendableType) {
        super(network, channel, sendableType);
    }
}
