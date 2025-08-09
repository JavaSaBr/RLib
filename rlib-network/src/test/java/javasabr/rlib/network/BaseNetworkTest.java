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

  protected TestNetwork<StringDataConnection> buildStringNetwork() {
    return buildStringNetwork(
        ServerNetworkConfig.DEFAULT_SERVER,
        new DefaultBufferAllocator(ServerNetworkConfig.DEFAULT_SERVER),
        NetworkConfig.DEFAULT_CLIENT,
        new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT));
  }

  protected TestNetwork<StringDataSSLConnection> buildStringSSLNetwork(
      SSLContext serverSSLContext,
      SSLContext clientSSLContext) {
    return buildStringSSLNetwork(
        ServerNetworkConfig.DEFAULT_SERVER,
        new DefaultBufferAllocator(ServerNetworkConfig.DEFAULT_SERVER),
        serverSSLContext,
        NetworkConfig.DEFAULT_CLIENT,
        new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT),
        clientSSLContext);
  }

  protected TestNetwork<StringDataConnection> buildStringNetwork(
      BufferAllocator serverBufferAllocator,
      BufferAllocator clientBufferAllocator) {
    return buildStringNetwork(
        ServerNetworkConfig.DEFAULT_SERVER,
        serverBufferAllocator,
        NetworkConfig.DEFAULT_CLIENT,
        clientBufferAllocator);
  }

  protected TestNetwork<StringDataConnection> buildStringNetwork(
      ServerNetworkConfig serverNetworkConfig,
      BufferAllocator serverBufferAllocator) {
    return buildStringNetwork(
        serverNetworkConfig,
        serverBufferAllocator,
        NetworkConfig.DEFAULT_CLIENT,
        new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT));
  }

  protected TestNetwork<StringDataConnection> buildStringNetwork(
      ServerNetworkConfig serverNetworkConfig,
      BufferAllocator serverBufferAllocator,
      NetworkConfig clientNetworkConfig,
      BufferAllocator clientBufferAllocator) {

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

  protected TestNetwork<StringDataSSLConnection> buildStringSSLNetwork(
      ServerNetworkConfig serverNetworkConfig,
      BufferAllocator serverBufferAllocator,
      SSLContext serverSSLContext,
      NetworkConfig clientNetworkConfig,
      BufferAllocator clientBufferAllocator,
      SSLContext clientSSLContext) {

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

  protected TestNetwork<DefaultConnection> buildDefaultNetwork(
      ReadablePacketRegistry<DefaultReadablePacket> serverPacketRegistry,
      ReadablePacketRegistry<DefaultReadablePacket> clientPacketRegistry) {
    return buildDefaultNetwork(
        ServerNetworkConfig.DEFAULT_SERVER,
        new DefaultBufferAllocator(ServerNetworkConfig.DEFAULT_SERVER),
        serverPacketRegistry,
        NetworkConfig.DEFAULT_CLIENT,
        new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT),
        clientPacketRegistry);
  }

  protected TestNetwork<DefaultConnection> buildDefaultNetwork(
      BufferAllocator serverBufferAllocator,
      ReadablePacketRegistry<DefaultReadablePacket> serverPacketRegistry,
      BufferAllocator clientBufferAllocator,
      ReadablePacketRegistry<DefaultReadablePacket> clientPacketRegistry) {
    return buildDefaultNetwork(
        ServerNetworkConfig.DEFAULT_SERVER,
        serverBufferAllocator,
        serverPacketRegistry,
        NetworkConfig.DEFAULT_CLIENT,
        clientBufferAllocator,
        clientPacketRegistry);
  }

  protected TestNetwork<DefaultConnection> buildDefaultNetwork(
      ServerNetworkConfig serverNetworkConfig,
      BufferAllocator serverBufferAllocator,
      ReadablePacketRegistry<DefaultReadablePacket> serverPacketRegistry,
      NetworkConfig clientNetworkConfig,
      BufferAllocator clientBufferAllocator,
      ReadablePacketRegistry<DefaultReadablePacket> clientPacketRegistry) {

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
