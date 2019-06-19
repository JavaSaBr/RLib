package com.ss.rlib.network.test;

import com.ss.rlib.network.*;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.impl.DefaultBufferAllocator;
import com.ss.rlib.network.impl.DefaultConnection;
import com.ss.rlib.network.impl.StringDataConnection;
import com.ss.rlib.network.packet.impl.DefaultReadablePacket;
import com.ss.rlib.network.packet.registry.ReadablePacketRegistry;
import com.ss.rlib.network.server.ServerNetwork;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * @author JavaSaBr
 */
public class BaseNetworkTest {

    @AllArgsConstructor
    public static class TestNetwork<C extends Connection<?, ?>> implements AutoCloseable {

        public final ServerNetworkConfig serverNetworkConfig;
        public final NetworkConfig clientNetworkConfig;

        public final C clientToServer;
        public final C serverToClient;

        private final ServerNetwork<C> serverNetwork;
        private final ClientNetwork<C> clientNetwork;

        @Override
        public void close() {
            shutdown();
        }

        public void shutdown() {
            serverNetwork.shutdown();
            clientNetwork.shutdown();
        }
    }

    protected @NotNull TestNetwork<StringDataConnection> buildStringNetwork() {
        return buildStringNetwork(
            ServerNetworkConfig.DEFAULT_SERVER,
            new DefaultBufferAllocator(ServerNetworkConfig.DEFAULT_SERVER),
            NetworkConfig.DEFAULT_CLIENT,
            new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT)
        );
    }

    protected @NotNull TestNetwork<StringDataConnection> buildStringNetwork(
        @NotNull BufferAllocator serverBufferAllocator,
        @NotNull BufferAllocator clientBufferAllocator
    ) {
        return buildStringNetwork(
            ServerNetworkConfig.DEFAULT_SERVER,
            serverBufferAllocator,
            NetworkConfig.DEFAULT_CLIENT,
            clientBufferAllocator
        );
    }

    protected @NotNull TestNetwork<StringDataConnection> buildStringNetwork(
        @NotNull ServerNetworkConfig serverNetworkConfig,
        @NotNull BufferAllocator serverBufferAllocator
    ) {
        return buildStringNetwork(
            serverNetworkConfig,
            serverBufferAllocator,
            NetworkConfig.DEFAULT_CLIENT,
            new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT)
        );
    }

    protected @NotNull TestNetwork<StringDataConnection> buildStringNetwork(
        @NotNull ServerNetworkConfig serverNetworkConfig,
        @NotNull BufferAllocator serverBufferAllocator,
        @NotNull NetworkConfig clientNetworkConfig,
        @NotNull BufferAllocator clientBufferAllocator
    ) {

        var asyncClientToServer = new CompletableFuture<StringDataConnection>();
        var asyncServerToClient = new CompletableFuture<StringDataConnection>();

        var serverNetwork = NetworkFactory.newStringDataServerNetwork(serverNetworkConfig, serverBufferAllocator);
        var serverAddress = serverNetwork.start();

        serverNetwork.onAccept(asyncServerToClient::complete);

        var clientNetwork = NetworkFactory.newStringDataClientNetwork(clientNetworkConfig, clientBufferAllocator);
        clientNetwork.connect(serverAddress)
            .thenApply(asyncClientToServer::complete);

        return new TestNetwork<>(
            serverNetworkConfig,
            clientNetworkConfig,
            asyncClientToServer.join(),
            asyncServerToClient.join(),
            serverNetwork,
            clientNetwork
        );
    }

    protected @NotNull TestNetwork<DefaultConnection> buildDefaultNetwork(
        @NotNull ReadablePacketRegistry<DefaultReadablePacket> serverPacketRegistry,
        @NotNull ReadablePacketRegistry<DefaultReadablePacket> clientPacketRegistry
    ) {
        return buildDefaultNetwork(
            ServerNetworkConfig.DEFAULT_SERVER,
            new DefaultBufferAllocator(ServerNetworkConfig.DEFAULT_SERVER),
            serverPacketRegistry,
            NetworkConfig.DEFAULT_CLIENT,
            new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT),
            clientPacketRegistry
        );
    }

    protected @NotNull TestNetwork<DefaultConnection> buildDefaultNetwork(
        @NotNull BufferAllocator serverBufferAllocator,
        @NotNull ReadablePacketRegistry<DefaultReadablePacket> serverPacketRegistry,
        @NotNull BufferAllocator clientBufferAllocator,
        @NotNull ReadablePacketRegistry<DefaultReadablePacket> clientPacketRegistry
    ) {
        return buildDefaultNetwork(
            ServerNetworkConfig.DEFAULT_SERVER,
            serverBufferAllocator,
            serverPacketRegistry,
            NetworkConfig.DEFAULT_CLIENT,
            clientBufferAllocator,
            clientPacketRegistry
        );
    }

    protected @NotNull TestNetwork<DefaultConnection> buildDefaultNetwork(
        @NotNull ServerNetworkConfig serverNetworkConfig,
        @NotNull BufferAllocator serverBufferAllocator,
        @NotNull ReadablePacketRegistry<DefaultReadablePacket> serverPacketRegistry,
        @NotNull NetworkConfig clientNetworkConfig,
        @NotNull BufferAllocator clientBufferAllocator,
        @NotNull ReadablePacketRegistry<DefaultReadablePacket> clientPacketRegistry
    ) {

        var asyncClientToServer = new CompletableFuture<DefaultConnection>();
        var asyncServerToClient = new CompletableFuture<DefaultConnection>();

        var serverNetwork = NetworkFactory.newDefaultServerNetwork(
            serverNetworkConfig,
            serverBufferAllocator,
            serverPacketRegistry
        );
        var serverAddress = serverNetwork.start();

        serverNetwork.onAccept(asyncServerToClient::complete);

        var clientNetwork = NetworkFactory.newDefaultClientNetwork(
            clientNetworkConfig,
            clientBufferAllocator,
            clientPacketRegistry
        );
        clientNetwork.connect(serverAddress)
            .thenApply(asyncClientToServer::complete);

        return new TestNetwork<>(
            serverNetworkConfig,
            clientNetworkConfig,
            asyncClientToServer.join(),
            asyncServerToClient.join(),
            serverNetwork,
            clientNetwork
        );
    }
}
