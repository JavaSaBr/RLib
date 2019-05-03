package com.ss.rlib.network;

import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.client.ConnectHandler;
import com.ss.rlib.network.client.impl.DefaultClientNetwork;
import com.ss.rlib.network.packet.registry.ReadablePacketRegistry;
import com.ss.rlib.network.packet.registry.impl.StringPacketRegistry;
import com.ss.rlib.network.server.AcceptHandler;
import com.ss.rlib.network.server.ServerNetwork;
import com.ss.rlib.network.server.client.Client;
import com.ss.rlib.network.server.impl.DefaultServerNetwork;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.function.Consumer;

/**
 * Class with factory methods to build client/server networks.
 *
 * @author JavaSaBr
 */
public final class NetworkFactory {

    /**
     * Create a string packet based asynchronous client network.
     *
     * @return the client network.
     */
    public static @NotNull ClientNetwork newStringAsyncClientNetwork() {
        return newDefaultAsyncClientNetwork(
            NetworkConfig.DEFAULT_CLIENT,
            StringPacketRegistry.getInstance(),
            ConnectHandler.newDefault()
        );
    }

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
     * @param registry     the readable packet registry.
     * @param channelGroup the channel group.
     * @return the client network.
     */
    public static @NotNull ClientNetwork newDefaultAsyncClientNetwork(
        @NotNull AsynchronousChannelGroup channelGroup,
        @NotNull ReadablePacketRegistry registry
    ) {
        return newDefaultAsyncClientNetwork(
            NetworkConfig.DEFAULT_CLIENT,
            channelGroup,
            registry,
            ConnectHandler.newDefault()
        );
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
     * Create a default asynchronous client network.
     *
     * @param config         the network config.
     * @param channelGroup   the channel group.
     * @param registry       the readable packet registry.
     * @param connectHandler the connect handler.
     * @return the client network.
     */
    public static @NotNull ClientNetwork newDefaultAsyncClientNetwork(
        @NotNull NetworkConfig config,
        @NotNull AsynchronousChannelGroup channelGroup,
        @NotNull ReadablePacketRegistry registry,
        @NotNull ConnectHandler connectHandler
    ) {
        try {
            return new DefaultClientNetwork(config, channelGroup, registry, connectHandler);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Create a default asynchronous client network.
     *
     * @param config       the network config.
     * @param channelGroup the channel group.
     * @param registry     the readable packet registry.
     * @return the client network.
     */
    public static @NotNull ClientNetwork newDefaultAsyncClientNetwork(
        @NotNull NetworkConfig config,
        @NotNull AsynchronousChannelGroup channelGroup,
        @NotNull ReadablePacketRegistry registry
    ) {
        try {
            return new DefaultClientNetwork(config, channelGroup, registry, ConnectHandler.newDefault());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Create string packet based asynchronous server network.
     *
     * @return the server network.
     */
    public static @NotNull ServerNetwork newStringAsyncServerNetwork() {
        return newDefaultAsyncServerNetwork(
            NetworkConfig.DEFAULT_SERVER,
            StringPacketRegistry.getInstance(),
            AcceptHandler.newDefault()
        );
    }

    /**
     * Create string packet based asynchronous server network.
     *
     * @param clientConsumer the client consumer.
     * @return the server network.
     */
    public static @NotNull ServerNetwork newStringAsyncServerNetwork(@NotNull Consumer<Client> clientConsumer) {
        return newDefaultAsyncServerNetwork(
            NetworkConfig.DEFAULT_SERVER,
            StringPacketRegistry.getInstance(),
            AcceptHandler.newDefault(clientConsumer)
        );
    }

    /**
     * Create a default asynchronous server network.
     *
     * @param registry the readable packet registry.
     * @return the server network.
     */
    public static @NotNull ServerNetwork newDefaultAsyncServerNetwork(@NotNull ReadablePacketRegistry registry) {
        return newDefaultAsyncServerNetwork(NetworkConfig.DEFAULT_SERVER, registry, AcceptHandler.newDefault());
    }

    /**
     * Create a default asynchronous server network.
     *
     * @param registry       the readable packet registry.
     * @param clientConsumer the client consumer.
     * @return the server network.
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
     * @return the server network.
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
     * @return the server network.
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
