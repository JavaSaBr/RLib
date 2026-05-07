package javasabr.rlib.network;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javasabr.rlib.network.exception.ConnectionClosedException;
import javasabr.rlib.network.impl.AbstractConnection;
import javasabr.rlib.network.impl.DefaultConnection;
import javasabr.rlib.network.packet.impl.DefaultReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.StringWritableNetworkPacket;
import javasabr.rlib.network.packet.registry.ReadableNetworkPacketRegistry;
import javasabr.rlib.network.util.NetworkUtils;
import javax.net.ssl.SSLContext;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class ConnectionCloseTest extends BaseNetworkTest {

  @Test
  void shouldPropagateConnectionCloseToClient() throws InterruptedException {
    // given
    var packetRegistry = ReadableNetworkPacketRegistry.of(
        DefaultReadableNetworkPacket.class,
        DefaultConnection.class,
        DefaultNetworkTest.ServerPackets.RequestEchoMessage.class,
        DefaultNetworkTest.ServerPackets.RequestServerTime.class);
    var serverNetwork = NetworkFactory.defaultServerNetwork(packetRegistry);
    InetSocketAddress serverAddress = serverNetwork.start();
    serverNetwork.onAccept(AbstractConnection::close);
    var clientNetwork = NetworkFactory.defaultClientNetwork(packetRegistry);
    CountDownLatch closeLatch = new CountDownLatch(1);

    // when
    clientNetwork
        .connectReactive(serverAddress)
        .flatMapMany(AbstractConnection::receivedEvents)
        .doOnError(e -> {
          if (e instanceof ConnectionClosedException) {
            closeLatch.countDown();
          }
        })
        .subscribe();

    // then
    assertThat(closeLatch.await(5000, TimeUnit.MILLISECONDS))
        .as("Client should be notified that connection is closed")
        .isTrue();
    clientNetwork.shutdown();
    serverNetwork.shutdown();
  }

  @Test
  @SneakyThrows
  void shouldCloseServerConnectionWhenClientClosesTcpChannelAbruptly() {
    // Given: established SSL connection with completed handshake
    InputStream keystoreFile = ConnectionCloseTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
    SSLContext serverSslContext = NetworkUtils.createSslContext(keystoreFile, "test");
    SSLContext clientSslContext = NetworkUtils.createAllTrustedClientSslContext();

    try (var testNetwork = buildStringSSLNetwork(serverSslContext, clientSslContext)) {
      var serverConnection = testNetwork.serverToClient;
      var clientConnection = testNetwork.clientToServer;

      // Register handler to start reading on server side
      CountDownLatch dataReceivedLatch = new CountDownLatch(1);
      serverConnection.onReceiveValidPacket((conn, packet) -> dataReceivedLatch.countDown());

      // Send data to complete SSL handshake and deliver a packet
      clientConnection.sendInBackground(new StringWritableNetworkPacket<>("handshake"));

      // Wait for the handshake to complete and data to be received
      assertThat(dataReceivedLatch.await(5, TimeUnit.SECONDS))
          .as("SSL handshake should complete and data should be received by server")
          .isTrue();

      // When: close client's raw TCP channel without SSL close_notify
      clientConnection.channel().close();

      assertThat(awaitMillis(5000, serverConnection::closed))
          .as("Server connection should be closed after receiving EOF from abruptly closed client channel")
          .isTrue();
    }
  }

  private static boolean awaitMillis(long millis, Supplier<Boolean> a) throws InterruptedException {
    for (int i = 0; i < millis / 100; i++) {
      if (a.get()) {
        return true;
      }
      Thread.sleep(100);
    }
    return false;
  }
}
