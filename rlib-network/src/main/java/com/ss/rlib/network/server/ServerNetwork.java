package com.ss.rlib.network.server;

import com.ss.rlib.network.AsyncNetwork;
import com.ss.rlib.network.server.client.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.Consumer;

/**
 * The interface to implement a server network.
 *
 * @author JavaSaBr
 */
public interface ServerNetwork extends AsyncNetwork {

    /**
     * Set the handler to handle destroyed clients.
     *
     * @param handler the handler.
     */
    void setDestroyedHandler(@NotNull Consumer<@NotNull Client> handler);

    /**
     * Put a handler to wait for a new connection.
     *
     * @param <A>        the type of an attachment.
     * @param attachment the additional argument.
     * @param handler    the handler.
     */
    <A> void accept(
        @Nullable A attachment,
        @NotNull CompletionHandler<@NotNull AsynchronousSocketChannel, @Nullable ? super A> handler
    );

    /**
     * Start a server using the socket address.
     *
     * @param address the socket address.
     * @throws IOException if a socket can't be created.
     */
    void bind(@NotNull SocketAddress address) throws IOException;

    /**
     * Notify the server about destroying the client.
     *
     * @param client the destroyed client.
     */
    void onDestroyed(@NotNull Client client);
}
