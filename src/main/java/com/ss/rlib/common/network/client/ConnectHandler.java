package com.ss.rlib.common.network.client;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.rlib.common.network.client.server.Server;
import com.ss.rlib.common.network.client.server.ServerConnection;
import com.ss.rlib.common.network.client.server.impl.DefaultServer;
import com.ss.rlib.common.network.client.server.impl.DefaultServerConnection;
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
    static @NotNull ConnectHandler newSimple(
            @NotNull BiFunction<ClientNetwork, AsynchronousSocketChannel, ServerConnection> connectionFactory,
            @NotNull Function<ServerConnection, Server> serverFactory
    ) {
        return (network) -> {
            AsynchronousSocketChannel channel = notNull(network.getChannel());
            ServerConnection connection = connectionFactory.apply(network, channel);
            Server server = serverFactory.apply(connection);
            connection.setOwner(server);
            connection.startRead();
            network.setCurrentServer(server);
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
    default void completed(@Nullable Void result, @NotNull ClientNetwork network) {
        onConnect(network);
    }

    @Override
    default void failed(@NotNull Throwable exc, @NotNull ClientNetwork network) {
        onFailed(exc);
    }

    /**
     * Handle a new connection.
     *
     * @param network the network.
     */
    void onConnect(@NotNull ClientNetwork network);

    /**
     * Handle an exception.
     *
     * @param exc the exception.
     */
    default void onFailed(@NotNull Throwable exc) {
    }
}
