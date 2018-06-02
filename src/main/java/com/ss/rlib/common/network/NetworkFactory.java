package com.ss.rlib.common.network;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.ss.rlib.common.network.client.ClientNetwork;
import com.ss.rlib.common.network.client.ConnectHandler;
import com.ss.rlib.common.network.client.impl.DefaultClientNetwork;
import com.ss.rlib.common.network.packet.ReadablePacketRegistry;
import com.ss.rlib.common.network.server.AcceptHandler;
import com.ss.rlib.common.network.server.ServerNetwork;
import com.ss.rlib.common.network.server.client.Client;
import com.ss.rlib.common.network.server.impl.DefaultServerNetwork;

/**
 * The network factory.
 *
 * @author JavaSaBr
 */
public final class NetworkFactory {

    /**
     * Create a default asynchronous client network.
     *
     * @param registry the readable packet registry.
     * @return the client network.
     */
    public static @NotNull ClientNetwork newDefaultAsyncClientNetwork(@NotNull ReadablePacketRegistry registry) {
        return newDefaultAsyncClientNetwork(NetworkConfig.DEFAULT_CLIENT, registry, ConnectHandler.newDefault());
    }

    /**
     * Create a default asynchronous client network.
     *
     * @param config         the network config.
     * @param registry       the readable packet registry.
     * @param connectHandler the connect handler.
     * @return the client network.
     */
    public static @NotNull ClientNetwork newDefaultAsyncClientNetwork(
            @NotNull NetworkConfig config,
            @NotNull ReadablePacketRegistry registry,
            @NotNull ConnectHandler connectHandler
    ) {
        try {
            return new DefaultClientNetwork(config, registry, connectHandler);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Create a default asynchronous server network.
     *
     * @param registry the readable packet registry.
     * @return the client network.
     */
    public static @NotNull ServerNetwork newDefaultAsyncServerNetwork(@NotNull ReadablePacketRegistry registry) {
        return newDefaultAsyncServerNetwork(NetworkConfig.DEFAULT_SERVER, registry, AcceptHandler.newDefault());
    }

    /**
     * Create a default asynchronous server network.
     *
     * @param registry       the readable packet registry.
     * @param clientConsumer the client consumer.
     * @return the client network.
     */
    public static @NotNull ServerNetwork newDefaultAsyncServerNetwork(
            @NotNull ReadablePacketRegistry registry,
            @NotNull Consumer<@NotNull Client> clientConsumer
    ) {
        return newDefaultAsyncServerNetwork(NetworkConfig.DEFAULT_SERVER, registry,
                AcceptHandler.newDefault(clientConsumer));
    }

    /**
     * Create a default asynchronous server network.
     *
     * @param registry      the readable packet registry.
     * @param acceptHandler the accept handler.
     * @return the client network.
     */
    public static @NotNull ServerNetwork newDefaultAsyncServerNetwork(
            @NotNull ReadablePacketRegistry registry,
            @NotNull AcceptHandler acceptHandler
    ) {
        return newDefaultAsyncServerNetwork(NetworkConfig.DEFAULT_SERVER, registry, acceptHandler);
    }

    /**
     * Create a default asynchronous server network.
     *
     * @param config        the network config.
     * @param registry      the readable packet registry.
     * @param acceptHandler the accept handler.
     * @return the client network.
     */
    public static @NotNull ServerNetwork newDefaultAsyncServerNetwork(
            @NotNull NetworkConfig config,
            @NotNull ReadablePacketRegistry registry,
            @NotNull AcceptHandler acceptHandler
    ) {
        try {
            return new DefaultServerNetwork(config, registry, acceptHandler);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private NetworkFactory() throws Exception {
        throw new Exception("no permission");
    }
}
