package rlib.network.client.server.impl;

import org.jetbrains.annotations.NotNull;
import rlib.network.client.ClientNetwork;
import rlib.network.client.server.ServerConnection;
import rlib.network.impl.AbstractAsyncConnection;
import rlib.network.packet.SendablePacket;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * The base implementation of a server connection.
 *
 * @author JavaSaBr
 */
public abstract class AbstractServerConnection extends AbstractAsyncConnection implements ServerConnection {

    public AbstractServerConnection(@NotNull final ClientNetwork network,
                                    @NotNull final AsynchronousSocketChannel channel,
                                    @NotNull final Class<? extends SendablePacket> sendableType) {
        super(network, channel, sendableType);
    }
}
