package rlib.network.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import rlib.network.AsynchronousNetwork;

/**
 * The interface to implement a server network.
 *
 * @author JavaSaBr
 */
public interface ServerNetwork extends AsynchronousNetwork {

    /**
     * Put a handler to wait for a new connection.
     *
     * @param attachment the additional argument.
     * @param handler    the handler.
     */
    <A> void accept(@Nullable A attachment, @NotNull CompletionHandler<AsynchronousSocketChannel, ? super A> handler);

    /**
     * Start a server using a socket address.
     *
     * @param address the socket address.
     */
    void bind(@NotNull SocketAddress address) throws IOException;
}
