package rlib.network.server.client.impl;

import org.jetbrains.annotations.NotNull;
import rlib.network.impl.AbstractAsyncConnection;
import rlib.network.packet.SendablePacket;
import rlib.network.server.ServerNetwork;
import rlib.network.server.client.ClientConnection;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * The base implementation of a client connection.
 *
 * @author JavaSaBr
 */
public abstract class AbstractClientConnection extends AbstractAsyncConnection implements ClientConnection {

    public AbstractClientConnection(@NotNull final ServerNetwork network,
                                    @NotNull final AsynchronousSocketChannel channel,
                                    @NotNull final Class<? extends SendablePacket> sendableType) {
        super(network, channel, sendableType);
    }
}
