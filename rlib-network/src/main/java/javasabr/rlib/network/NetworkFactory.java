package javasabr.rlib.network;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.function.BiFunction;
import javasabr.rlib.network.client.ClientNetwork;
import javasabr.rlib.network.client.impl.DefaultClientNetwork;
import javasabr.rlib.network.impl.DefaultBufferAllocator;
import javasabr.rlib.network.impl.DefaultConnection;
import javasabr.rlib.network.impl.StringDataConnection;
import javasabr.rlib.network.impl.StringDataMtlsServerConnection;
import javasabr.rlib.network.impl.StringDataSslConnection;
import javasabr.rlib.network.packet.impl.DefaultReadableNetworkPacket;
import javasabr.rlib.network.packet.registry.ReadableNetworkPacketRegistry;
import javasabr.rlib.network.server.ServerNetwork;
import javasabr.rlib.network.server.impl.DefaultServerNetwork;
import javax.net.ssl.SSLContext;
import lombok.experimental.UtilityClass;

/**
 * Factory class with methods to build client and server networks.
 *
 * @author JavaSaBr
 * @since 10.0.0
 */
@UtilityClass
public final class NetworkFactory {

  /**
   * Creates a new client network with custom connection factory.
   *
   * @param <C> the connection type
   * @param networkConfig the network configuration
   * @param channelToConnection the function to create connections from channels
   * @return a new client network
   * @since 10.0.0
   */
  public static <C extends UnsafeConnection<C>> ClientNetwork<C> clientNetwork(
      NetworkConfig networkConfig,
      BiFunction<Network<C>, AsynchronousSocketChannel, C> channelToConnection) {
    return new DefaultClientNetwork<>(networkConfig, channelToConnection);
  }

  /**
   * Creates a new server network with custom connection factory.
   *
   * @param <C> the connection type
   * @param networkConfig the server network configuration
   * @param channelToConnection the function to create connections from channels
   * @return a new server network
   * @since 10.0.0
   */
  public static <C extends UnsafeConnection<C>> ServerNetwork<C> serverNetwork(
      ServerNetworkConfig networkConfig,
      BiFunction<Network<C>, AsynchronousSocketChannel, C> channelToConnection) {
    return new DefaultServerNetwork<>(networkConfig, channelToConnection);
  }

  /**
   * Creates a string packet based asynchronous client network with default configuration.
   *
   * @return a new string data client network
   * @since 10.0.0
   */
  public static ClientNetwork<StringDataConnection> stringDataClientNetwork() {
    return stringDataClientNetwork(NetworkConfig.DEFAULT_CLIENT);
  }

  /**
   * Creates a string packet based asynchronous client network with custom configuration.
   *
   * @param networkConfig the network configuration
   * @return a new string data client network
   * @since 10.0.0
   */
  public static ClientNetwork<StringDataConnection> stringDataClientNetwork(
      NetworkConfig networkConfig) {
    return stringDataClientNetwork(networkConfig, new DefaultBufferAllocator(networkConfig));
  }

  /**
   * Creates a string packet based asynchronous client network with custom configuration and allocator.
   *
   * @param networkConfig the network configuration
   * @param bufferAllocator the buffer allocator
   * @return a new string data client network
   * @since 10.0.0
   */
  public static ClientNetwork<StringDataConnection> stringDataClientNetwork(
      NetworkConfig networkConfig,
      BufferAllocator bufferAllocator) {
    return clientNetwork(
        networkConfig,
        (network, channel) -> new StringDataConnection(network, channel, bufferAllocator));
  }

  /**
   * Creates an ID-based packet default asynchronous client network.
   *
   * @param packetRegistry the packet registry
   * @return a new default client network
   * @since 10.0.0
   */
  public static ClientNetwork<DefaultConnection> defaultClientNetwork(
      ReadableNetworkPacketRegistry<DefaultReadableNetworkPacket<DefaultConnection>, DefaultConnection> packetRegistry) {
    return defaultClientNetwork(
        NetworkConfig.DEFAULT_CLIENT,
        new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT),
        packetRegistry);
  }

  /**
   * Creates an ID-based packet default asynchronous client network with custom configuration.
   *
   * @param networkConfig the network configuration
   * @param bufferAllocator the buffer allocator
   * @param packetRegistry the packet registry
   * @return a new default client network
   * @since 10.0.0
   */
  public static ClientNetwork<DefaultConnection> defaultClientNetwork(
      NetworkConfig networkConfig,
      BufferAllocator bufferAllocator,
      ReadableNetworkPacketRegistry<DefaultReadableNetworkPacket<DefaultConnection>, DefaultConnection> packetRegistry) {
    return NetworkFactory.clientNetwork(
        networkConfig,
        (network, channel) -> new DefaultConnection(network, channel, bufferAllocator, packetRegistry));
  }

  /**
   * Creates a string packet based asynchronous secure client network.
   *
   * @param networkConfig the network configuration
   * @param bufferAllocator the buffer allocator
   * @param sslContext the SSL context
   * @return a new SSL string data client network
   * @since 10.0.0
   */
  public static ClientNetwork<StringDataSslConnection> stringDataSslClientNetwork(
      NetworkConfig networkConfig,
      BufferAllocator bufferAllocator,
      SSLContext sslContext) {
    return clientNetwork(
        networkConfig,
        (network, channel) -> {
          StringDataSslConnection connection = new StringDataSslConnection(network, channel, bufferAllocator, sslContext, true);
          connection.beginHandshake();
          return connection;
        });
  }

  /**
   * Creates a string packet based asynchronous server network with default configuration.
   *
   * @return a new string data server network
   * @since 10.0.0
   */
  public static ServerNetwork<StringDataConnection> stringDataServerNetwork() {
    return stringDataServerNetwork(ServerNetworkConfig.DEFAULT_SERVER);
  }

  /**
   * Creates a string packet based asynchronous server network with custom configuration.
   *
   * @param networkConfig the server network configuration
   * @return a new string data server network
   * @since 10.0.0
   */
  public static ServerNetwork<StringDataConnection> stringDataServerNetwork(
      ServerNetworkConfig networkConfig) {
    return stringDataServerNetwork(networkConfig, new DefaultBufferAllocator(networkConfig));
  }

  /**
   * Creates a string packet based asynchronous server network with custom configuration and allocator.
   *
   * @param networkConfig the server network configuration
   * @param bufferAllocator the buffer allocator
   * @return a new string data server network
   * @since 10.0.0
   */
  public static ServerNetwork<StringDataConnection> stringDataServerNetwork(
      ServerNetworkConfig networkConfig,
      BufferAllocator bufferAllocator) {
    return serverNetwork(
        networkConfig,
        (network, channel) -> new StringDataConnection(network, channel, bufferAllocator));
  }

  /**
   * Creates a string packet based asynchronous secure server network.
   *
   * @param networkConfig the server network configuration
   * @param bufferAllocator the buffer allocator
   * @param sslContext the SSL context
   * @return a new SSL string data server network
   * @since 10.0.0
   */
  public static ServerNetwork<StringDataSslConnection> stringDataSslServerNetwork(
      ServerNetworkConfig networkConfig,
      BufferAllocator bufferAllocator,
      SSLContext sslContext) {
    return serverNetwork(
        networkConfig,
        (network, channel) -> {
          StringDataSslConnection connection = new StringDataSslConnection(network, channel, bufferAllocator, sslContext, false);
          connection.beginHandshake();
          return connection;
        });
  }

  /**
   * Creates an ID-based packet default asynchronous server network.
   *
   * @param packetRegistry the packet registry
   * @return a new default server network
   * @since 10.0.0
   */
  public static ServerNetwork<DefaultConnection> defaultServerNetwork(
      ReadableNetworkPacketRegistry<DefaultReadableNetworkPacket<DefaultConnection>, DefaultConnection> packetRegistry) {
    return defaultServerNetwork(
        ServerNetworkConfig.DEFAULT_SERVER,
        new DefaultBufferAllocator(ServerNetworkConfig.DEFAULT_SERVER),
        packetRegistry);
  }

  /**
   * Creates an ID-based packet default asynchronous server network with custom configuration.
   *
   * @param networkConfig the server network configuration
   * @param bufferAllocator the buffer allocator
   * @param packetRegistry the packet registry
   * @return a new default server network
   * @since 10.0.0
   */
  public static ServerNetwork<DefaultConnection> defaultServerNetwork(
      ServerNetworkConfig networkConfig,
      BufferAllocator bufferAllocator,
      ReadableNetworkPacketRegistry<DefaultReadableNetworkPacket<DefaultConnection>, DefaultConnection> packetRegistry) {
    return serverNetwork(
        networkConfig,
        (network, channel) -> new DefaultConnection(network, channel, bufferAllocator, packetRegistry));
  }

  /**
   * Create string packet based asynchronous Mutual TLS server network.
   *
   * @param networkConfig the server network configuration
   * @param bufferAllocator the buffer allocator
   * @param sslContext SSL context
   * @return a new mTLS server network
   * @since 10.0.0
   */
  public static ServerNetwork<StringDataMtlsServerConnection> stringDataMtlsServerNetwork(
      ServerNetworkConfig networkConfig,
      BufferAllocator bufferAllocator,
      SSLContext sslContext) {
    return serverNetwork(
        networkConfig,
        (network, channel) -> {
          StringDataMtlsServerConnection connection = new StringDataMtlsServerConnection(network, channel, bufferAllocator, sslContext);
          connection.beginHandshake();
          return connection;
        });
  }
}
