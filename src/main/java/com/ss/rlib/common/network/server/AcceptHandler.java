package com.ss.rlib.common.network.server;

import com.ss.rlib.common.network.server.client.Client;
import com.ss.rlib.common.network.server.client.ClientConnection;
import com.ss.rlib.common.network.server.client.impl.DefaultClient;
import com.ss.rlib.common.network.server.client.impl.DefaultClientConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The interface to implement a handler of accepted connections.
 *
 * @author JavaSaBr
 */
public interface AcceptHandler extends CompletionHandler<AsynchronousSocketChannel, ServerNetwork> {

    /**
     * Create a simple accept handler.
     *
     * @param connectionFactory the connection factory.
     * @param clientFactory     the client factory.
     * @param clientConsumer    the client consumer.
     * @return the accept handler.
     */
    static @NotNull AcceptHandler newSimple(
            @NotNull BiFunction<ServerNetwork, AsynchronousSocketChannel, ClientConnection> connectionFactory,
            @NotNull Function<ClientConnection, Client> clientFactory,
            @Nullable Consumer<Client> clientConsumer
    ) {
        return (channel, network) -> {

            try {
                channel.setOption(StandardSocketOptions.SO_SNDBUF, 12000);
                channel.setOption(StandardSocketOptions.SO_RCVBUF, 24000);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ClientConnection connection = connectionFactory.apply(network, channel);
            Client client = clientFactory.apply(connection);
            connection.setOwner(client);
            client.notifyConnected();
            connection.startRead();

            if (clientConsumer != null) {
                clientConsumer.accept(client);
            }
        };
    }

    /**
     * Create a default accept handler.
     *
     * @return the accept handler.
     */
    static @NotNull AcceptHandler newDefault() {
        return newSimple(DefaultClientConnection::new, DefaultClient::new, null);
    }

    /**
     * Create a default accept handler.
     *
     * @param consumer the client consumer.
     * @return the accept handler.
     */
    static @NotNull AcceptHandler newDefault(@NotNull Consumer<Client> consumer) {
        return newSimple(DefaultClientConnection::new, DefaultClient::new, consumer);
    }

    @Override
    default void completed(@NotNull AsynchronousSocketChannel result, @NotNull ServerNetwork network) {
        network.accept(network, this);
        onAccept(result, network);
    }

    @Override
    default void failed(@NotNull Throwable exc, @NotNull ServerNetwork network) {
        network.accept(network, this);
        onFailed(exc);
    }

    /**
     * Handle the new client connection.
     *
     * @param channel the client channel.
     * @param network the server network.
     */
    void onAccept(@NotNull AsynchronousSocketChannel channel, @NotNull ServerNetwork network);

    /**
     * Handle the exception.
     *
     * @param exception the exception.
     */
    default void onFailed(@NotNull Throwable exception) {
    }
}
