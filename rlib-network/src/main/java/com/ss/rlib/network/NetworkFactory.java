package com.ss.rlib.network;

import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.client.impl.DefaultClientNetwork;
import com.ss.rlib.network.impl.DefaultBufferAllocator;
import com.ss.rlib.network.impl.simple.StringDataConnection;
import com.ss.rlib.network.server.ServerNetwork;
import com.ss.rlib.network.server.impl.DefaultServerNetwork;
import org.jetbrains.annotations.NotNull;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.function.BiFunction;

/**
 * Class with factory methods to build client/server networks.
 *
 * @author JavaSaBr
 */
public final class NetworkFactory {

    public static <C extends Connection<?, ?>> @NotNull ClientNetwork<C> newClientNetwork(
        @NotNull NetworkConfig networkConfig,
        @NotNull BiFunction<Network<C>, AsynchronousSocketChannel, C> channelToConnection
    ) {
        return new DefaultClientNetwork<>(networkConfig, channelToConnection);
    }

    public static <C extends Connection<?, ?>> @NotNull ServerNetwork<C> newServerNetwork(
        @NotNull ServerNetworkConfig networkConfig,
        @NotNull BiFunction<Network<C>, AsynchronousSocketChannel, C> channelToConnection
    ) {
        return new DefaultServerNetwork<>(networkConfig, channelToConnection);
    }

    /**
     * Create a string packet based asynchronous client network.
     *
     * @return the client network.
     */
    public static @NotNull ClientNetwork<StringDataConnection> newStringDataClientNetwork() {
        return newStringDataClientNetwork(NetworkConfig.DEFAULT_CLIENT);
    }

    /**
     * Create a string packet based asynchronous client network.
     *
     * @param networkConfig the network config.
     * @return the client network.
     */
    public static @NotNull ClientNetwork<StringDataConnection> newStringDataClientNetwork(
        @NotNull NetworkConfig networkConfig
    ) {

        var allocator = new DefaultBufferAllocator(networkConfig);

        return newClientNetwork(
            networkConfig,
            (network, channel) -> new StringDataConnection(network, channel, allocator)
        );
    }

    /**
     * Create string packet based asynchronous server network.
     *
     * @return the server network.
     */
    public static @NotNull ServerNetwork<StringDataConnection> newStringDataServerNetwork() {
        return newStringDataServerNetwork(ServerNetworkConfig.DEFAULT_SERVER);
    }

    /**
     * Create string packet based asynchronous server network.
     *
     * @param networkConfig the network config.
     * @return the server network.
     */
    public static @NotNull ServerNetwork<StringDataConnection> newStringDataServerNetwork(
        @NotNull ServerNetworkConfig networkConfig
    ) {

        var allocator = new DefaultBufferAllocator(networkConfig);

        return newServerNetwork(
            networkConfig,
            (network, channel) -> new StringDataConnection(network, channel, allocator)
        );
    }

    private NetworkFactory() throws Exception {
        throw new Exception("no permission");
    }
}
