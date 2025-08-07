package javasabr.rlib.network;

import java.util.concurrent.CompletableFuture;
import javasabr.rlib.network.client.ClientNetwork;
import javasabr.rlib.network.impl.DefaultBufferAllocator;
import javasabr.rlib.network.impl.DefaultConnection;
import javasabr.rlib.network.impl.StringDataConnection;
import javasabr.rlib.network.impl.StringDataSSLConnection;
import javasabr.rlib.network.packet.impl.DefaultReadablePacket;
import javasabr.rlib.network.packet.registry.ReadablePacketRegistry;
import javasabr.rlib.network.server.ServerNetwork;
import javax.net.ssl.SSLContext;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

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
        new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT));
  }

  protected @NotNull TestNetwork<StringDataSSLConnection> buildStringSSLNetwork(
      @NotNull SSLContext serverSSLContext,
      @NotNull SSLContext clientSSLContext) {
    return buildStringSSLNetwork(
        ServerNetworkConfig.DEFAULT_SERVER,
        new DefaultBufferAllocator(ServerNetworkConfig.DEFAULT_SERVER),
        serverSSLContext,
        NetworkConfig.DEFAULT_CLIENT,
        new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT),
        clientSSLContext);
  }

  protected @NotNull TestNetwork<StringDataConnection> buildStringNetwork(
      @NotNull BufferAllocator serverBufferAllocator,
      @NotNull BufferAllocator clientBufferAllocator) {
    return buildStringNetwork(
        ServerNetworkConfig.DEFAULT_SERVER,
        serverBufferAllocator,
        NetworkConfig.DEFAULT_CLIENT,
        clientBufferAllocator);
  }

  protected @NotNull TestNetwork<StringDataConnection> buildStringNetwork(
      @NotNull ServerNetworkConfig serverNetworkConfig,
      @NotNull BufferAllocator serverBufferAllocator) {
    return buildStringNetwork(
        serverNetworkConfig,
        serverBufferAllocator,
        NetworkConfig.DEFAULT_CLIENT,
        new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT));
  }

  protected @NotNull TestNetwork<StringDataConnection> buildStringNetwork(
      @NotNull ServerNetworkConfig serverNetworkConfig,
      @NotNull BufferAllocator serverBufferAllocator,
      @NotNull NetworkConfig clientNetworkConfig,
      @NotNull BufferAllocator clientBufferAllocator) {

    var asyncClientToServer = new CompletableFuture<StringDataConnection>();
    var asyncServerToClient = new CompletableFuture<StringDataConnection>();

    var serverNetwork = NetworkFactory.newStringDataServerNetwork(serverNetworkConfig, serverBufferAllocator);
    var serverAddress = serverNetwork.start();

    serverNetwork.onAccept(asyncServerToClient::complete);

    var clientNetwork = NetworkFactory.newStringDataClientNetwork(clientNetworkConfig, clientBufferAllocator);
    clientNetwork
        .connect(serverAddress)
        .thenApply(asyncClientToServer::complete);

    return new TestNetwork<>(
        serverNetworkConfig,
        clientNetworkConfig,
        asyncClientToServer.join(),
        asyncServerToClient.join(),
        serverNetwork,
        clientNetwork);
  }

  protected @NotNull TestNetwork<StringDataSSLConnection> buildStringSSLNetwork(
      @NotNull ServerNetworkConfig serverNetworkConfig,
      @NotNull BufferAllocator serverBufferAllocator,
      @NotNull SSLContext serverSSLContext,
      @NotNull NetworkConfig clientNetworkConfig,
      @NotNull BufferAllocator clientBufferAllocator,
      @NotNull SSLContext clientSSLContext) {

    var asyncClientToServer = new CompletableFuture<StringDataSSLConnection>();
    var asyncServerToClient = new CompletableFuture<StringDataSSLConnection>();

    var serverNetwork = NetworkFactory.newStringDataSSLServerNetwork(
        serverNetworkConfig,
        serverBufferAllocator,
        serverSSLContext);

    var serverAddress = serverNetwork.start();
    serverNetwork.onAccept(asyncServerToClient::complete);

    var clientNetwork = NetworkFactory.newStringDataSSLClientNetwork(
        clientNetworkConfig,
        clientBufferAllocator,
        clientSSLContext);

    clientNetwork
        .connect(serverAddress)
        .thenApply(asyncClientToServer::complete);

    return new TestNetwork<>(
        serverNetworkConfig,
        clientNetworkConfig,
        asyncClientToServer.join(),
        asyncServerToClient.join(),
        serverNetwork,
        clientNetwork);
  }

  protected @NotNull TestNetwork<DefaultConnection> buildDefaultNetwork(
      @NotNull ReadablePacketRegistry<DefaultReadablePacket> serverPacketRegistry,
      @NotNull ReadablePacketRegistry<DefaultReadablePacket> clientPacketRegistry) {
    return buildDefaultNetwork(
        ServerNetworkConfig.DEFAULT_SERVER,
        new DefaultBufferAllocator(ServerNetworkConfig.DEFAULT_SERVER),
        serverPacketRegistry,
        NetworkConfig.DEFAULT_CLIENT,
        new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT),
        clientPacketRegistry);
  }

  protected @NotNull TestNetwork<DefaultConnection> buildDefaultNetwork(
      @NotNull BufferAllocator serverBufferAllocator,
      @NotNull ReadablePacketRegistry<DefaultReadablePacket> serverPacketRegistry,
      @NotNull BufferAllocator clientBufferAllocator,
      @NotNull ReadablePacketRegistry<DefaultReadablePacket> clientPacketRegistry) {
    return buildDefaultNetwork(
        ServerNetworkConfig.DEFAULT_SERVER,
        serverBufferAllocator,
        serverPacketRegistry,
        NetworkConfig.DEFAULT_CLIENT,
        clientBufferAllocator,
        clientPacketRegistry);
  }

  protected @NotNull TestNetwork<DefaultConnection> buildDefaultNetwork(
      @NotNull ServerNetworkConfig serverNetworkConfig,
      @NotNull BufferAllocator serverBufferAllocator,
      @NotNull ReadablePacketRegistry<DefaultReadablePacket> serverPacketRegistry,
      @NotNull NetworkConfig clientNetworkConfig,
      @NotNull BufferAllocator clientBufferAllocator,
      @NotNull ReadablePacketRegistry<DefaultReadablePacket> clientPacketRegistry) {

    var asyncClientToServer = new CompletableFuture<DefaultConnection>();
    var asyncServerToClient = new CompletableFuture<DefaultConnection>();

    var serverNetwork = NetworkFactory.newDefaultServerNetwork(
        serverNetworkConfig,
        serverBufferAllocator,
        serverPacketRegistry);
    var serverAddress = serverNetwork.start();

    serverNetwork.onAccept(asyncServerToClient::complete);

    var clientNetwork = NetworkFactory.newDefaultClientNetwork(
        clientNetworkConfig,
        clientBufferAllocator,
        clientPacketRegistry);
    clientNetwork
        .connect(serverAddress)
        .thenApply(asyncClientToServer::complete);

    return new TestNetwork<>(
        serverNetworkConfig,
        clientNetworkConfig,
        asyncClientToServer.join(),
        asyncServerToClient.join(),
        serverNetwork,
        clientNetwork);
  }
}
