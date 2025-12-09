package javasabr.rlib.network;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.network.client.ClientNetwork;
import javasabr.rlib.network.impl.DefaultBufferAllocator;
import javasabr.rlib.network.impl.DefaultConnection;
import javasabr.rlib.network.impl.StringDataConnection;
import javasabr.rlib.network.impl.StringDataSslConnection;
import javasabr.rlib.network.packet.ReadableNetworkPacket;
import javasabr.rlib.network.packet.WritableNetworkPacket;
import javasabr.rlib.network.packet.impl.DefaultReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.StringReadableNetworkPacket;
import javasabr.rlib.network.packet.registry.ReadableNetworkPacketRegistry;
import javasabr.rlib.network.server.ServerNetwork;
import javax.net.ssl.SSLContext;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

/**
 * @author JavaSaBr
 */
public class BaseNetworkTest {

  protected static final Class<StringReadableNetworkPacket<StringDataConnection>> RECEIVED_PACKET_TYPE =
      ClassUtils.unsafeCast(StringReadableNetworkPacket.class);

  public static class MockConnection implements Connection<MockConnection> {
    @Override
    public String remoteAddress() {
      return "";
    }

    @Override
    public long lastActivity() {
      return 0;
    }

    @Override
    public void close() {}

    @Override
    public boolean closed() {
      return false;
    }

    @Override
    public Flux<ReceivedPacketEvent<MockConnection, ? extends ReadableNetworkPacket<MockConnection>>> receivedEvents() {
      return Flux.empty();
    }

    @Override
    public void sendInBackground(WritableNetworkPacket packet) {}

    @Override
    public CompletableFuture<Boolean> sendAsync(WritableNetworkPacket packet) {
      return CompletableFuture.completedFuture(false);
    }

    @Override
    public Flux<? extends ReadableNetworkPacket<MockConnection>> receivedValidPackets() {
      return Flux.empty();
    }

    @Override
    public Flux<? extends ReadableNetworkPacket<MockConnection>> receivedInvalidPackets() {
      return Flux.empty();
    }

    @Override
    public void onReceiveValidPacket(BiConsumer<MockConnection, ? super ReadableNetworkPacket<MockConnection>> consumer) {}

    @Override
    public void onReceiveInvalidPacket(BiConsumer<MockConnection, ? super ReadableNetworkPacket<MockConnection>> consumer) {}
  }

  public static final MockConnection MOCK_CONNECTION = new MockConnection();

  @AllArgsConstructor
  public static class TestNetwork<C extends Connection<C>> implements AutoCloseable {

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

  protected TestNetwork<StringDataSslConnection> buildStringSSLNetwork(
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

    var serverNetwork = NetworkFactory.stringDataServerNetwork(serverNetworkConfig, serverBufferAllocator);
    var serverAddress = serverNetwork.start();

    serverNetwork.onAccept(asyncServerToClient::complete);

    var clientNetwork = NetworkFactory.stringDataClientNetwork(clientNetworkConfig, clientBufferAllocator);
    clientNetwork
        .connectAsync(serverAddress)
        .thenApply(asyncClientToServer::complete);

    return new TestNetwork<>(
        serverNetworkConfig,
        clientNetworkConfig,
        asyncClientToServer.join(),
        asyncServerToClient.join(),
        serverNetwork,
        clientNetwork);
  }

  protected TestNetwork<StringDataSslConnection> buildStringSSLNetwork(
      ServerNetworkConfig serverNetworkConfig,
      BufferAllocator serverBufferAllocator,
      SSLContext serverSSLContext,
      NetworkConfig clientNetworkConfig,
      BufferAllocator clientBufferAllocator,
      SSLContext clientSSLContext) {

    var asyncClientToServer = new CompletableFuture<StringDataSslConnection>();
    var asyncServerToClient = new CompletableFuture<StringDataSslConnection>();

    var serverNetwork = NetworkFactory.stringDataSslServerNetwork(
        serverNetworkConfig,
        serverBufferAllocator,
        serverSSLContext);

    var serverAddress = serverNetwork.start();
    serverNetwork.onAccept(asyncServerToClient::complete);

    var clientNetwork = NetworkFactory.stringDataSslClientNetwork(
        clientNetworkConfig,
        clientBufferAllocator,
        clientSSLContext);

    clientNetwork
        .connectAsync(serverAddress)
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
      ReadableNetworkPacketRegistry<DefaultReadableNetworkPacket<DefaultConnection>, DefaultConnection> serverPacketRegistry,
      ReadableNetworkPacketRegistry<DefaultReadableNetworkPacket<DefaultConnection>, DefaultConnection> clientPacketRegistry) {
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
      ReadableNetworkPacketRegistry<DefaultReadableNetworkPacket<DefaultConnection>, DefaultConnection> serverPacketRegistry,
      BufferAllocator clientBufferAllocator,
      ReadableNetworkPacketRegistry<DefaultReadableNetworkPacket<DefaultConnection>, DefaultConnection> clientPacketRegistry) {
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
      ReadableNetworkPacketRegistry<DefaultReadableNetworkPacket<DefaultConnection>, DefaultConnection> serverPacketRegistry,
      NetworkConfig clientNetworkConfig,
      BufferAllocator clientBufferAllocator,
      ReadableNetworkPacketRegistry<DefaultReadableNetworkPacket<DefaultConnection>, DefaultConnection> clientPacketRegistry) {

    var asyncClientToServer = new CompletableFuture<DefaultConnection>();
    var asyncServerToClient = new CompletableFuture<DefaultConnection>();

    var serverNetwork = NetworkFactory.defaultServerNetwork(
        serverNetworkConfig,
        serverBufferAllocator,
        serverPacketRegistry);
    var serverAddress = serverNetwork.start();

    serverNetwork.onAccept(asyncServerToClient::complete);

    var clientNetwork = NetworkFactory.defaultClientNetwork(
        clientNetworkConfig,
        clientBufferAllocator,
        clientPacketRegistry);
    clientNetwork
        .connectAsync(serverAddress)
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
