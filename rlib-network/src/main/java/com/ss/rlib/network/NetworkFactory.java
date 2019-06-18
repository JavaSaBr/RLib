package com.ss.rlib.network;

import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.client.impl.DefaultClientNetwork;
import com.ss.rlib.network.impl.DefaultBufferAllocator;
import com.ss.rlib.network.impl.DefaultConnection;
import com.ss.rlib.network.impl.StringDataConnection;
import com.ss.rlib.network.packet.impl.DefaultReadablePacket;
import com.ss.rlib.network.packet.registry.ReadablePacketRegistry;
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
        return newStringDataClientNetwork(
            networkConfig,
            new DefaultBufferAllocator(networkConfig)
        );
    }

    /**
     * Create a string packet based asynchronous client network.
     *
     * @param networkConfig   the network config.
     * @param bufferAllocator the buffer allocator.
     * @return the client network.
     */
    public static @NotNull ClientNetwork<StringDataConnection> newStringDataClientNetwork(
        @NotNull NetworkConfig networkConfig,
        @NotNull BufferAllocator bufferAllocator
    ) {
        return newClientNetwork(
            networkConfig,
            (network, channel) -> new StringDataConnection(network, channel, bufferAllocator)
        );
    }

    /**
     * Create id based packet default asynchronous client network.
     *
     * @param networkConfig   the network config.
     * @param bufferAllocator the buffer allocator.
     * @param packetRegistry  the readable packet registry.
     * @return the server network.
     */
    public static @NotNull ClientNetwork<DefaultConnection> newDefaultClientNetwork(
        @NotNull NetworkConfig networkConfig,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull ReadablePacketRegistry<DefaultReadablePacket> packetRegistry
    ) {
        return newClientNetwork(
            networkConfig,
            (network, channel) -> new DefaultConnection(network,
                channel,
                NetworkCryptor.NULL,
                bufferAllocator,
                packetRegistry
            )
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
        return newStringDataServerNetwork(
            networkConfig,
            new DefaultBufferAllocator(networkConfig)
        );
    }

    /**
     * Create string packet based asynchronous server network.
     *
     * @param networkConfig   the network config.
     * @param bufferAllocator the buffer allocator.
     * @return the server network.
     */
    public static @NotNull ServerNetwork<StringDataConnection> newStringDataServerNetwork(
        @NotNull ServerNetworkConfig networkConfig,
        @NotNull BufferAllocator bufferAllocator
    ) {
        return newServerNetwork(
            networkConfig,
            (network, channel) -> new StringDataConnection(network, channel, bufferAllocator)
        );
    }

    /**
     * Create id based packet default asynchronous server network.
     *
     * @param networkConfig   the network config.
     * @param bufferAllocator the buffer allocator.
     * @param packetRegistry  the readable packet registry.
     * @return the server network.
     */
    public static @NotNull ServerNetwork<DefaultConnection> newDefaultServerNetwork(
        @NotNull ServerNetworkConfig networkConfig,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull ReadablePacketRegistry<DefaultReadablePacket> packetRegistry
    ) {
        return newServerNetwork(
            networkConfig,
            (network, channel) -> new DefaultConnection(network,
                channel,
                NetworkCryptor.NULL,
                bufferAllocator,
                packetRegistry
            )
        );
    }

    private NetworkFactory() throws Exception {
        throw new Exception("no permission");
    }
}
