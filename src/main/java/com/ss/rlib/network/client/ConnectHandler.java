package com.ss.rlib.network.client;

import static com.ss.rlib.util.ObjectUtils.notNull;
import com.ss.rlib.network.client.server.Server;
import com.ss.rlib.network.client.server.ServerConnection;
import com.ss.rlib.network.client.server.impl.DefaultServer;
import com.ss.rlib.network.client.server.impl.DefaultServerConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The interface to implement a connection handler.
 *
 * @author JavaSaBr
 */
public interface ConnectHandler extends CompletionHandler<Void, ClientNetwork> {

    /**
     * Create a simple connection handler.
     *
     * @param connectionFactory the connection factory.
     * @param serverFactory     the server factory.
     * @return the connection handler.
     */
    static @NotNull ConnectHandler newSimple(@NotNull final BiFunction<@NotNull ClientNetwork, @NotNull AsynchronousSocketChannel, @NotNull ServerConnection> connectionFactory,
                                             @NotNull final Function<@NotNull ServerConnection, @NotNull Server> serverFactory) {
        return (network) -> {
            final AsynchronousSocketChannel channel = notNull(network.getChannel());
            final ServerConnection connection = connectionFactory.apply(network, channel);
            final Server server = serverFactory.apply(connection);
            connection.setOwner(server);
            connection.startRead();
        };
    }

    /**
     * Create a default accept handler.
     *
     * @return the accept handler.
     */
    static @NotNull ConnectHandler newDefault() {
        return newSimple(DefaultServerConnection::new, DefaultServer::new);
    }

    @Override
    default void completed(@Nullable final Void result, @NotNull final ClientNetwork network) {
        onConnect(network);
    }

    @Override
    default void failed(@NotNull final Throwable exc, @NotNull final ClientNetwork network) {
        onFailed(exc);
    }

    /**
     * Handle a new connection.
     *
     * @param network the network.
     */
    void onConnect(@NotNull final ClientNetwork network);

    /**
     * Handle an exception.
     *
     * @param exc the exception.
     */
    default void onFailed(@NotNull final Throwable exc) {
    }
}
