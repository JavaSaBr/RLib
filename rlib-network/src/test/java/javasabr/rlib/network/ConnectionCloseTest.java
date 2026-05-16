package javasabr.rlib.network;

import static java.util.function.Predicate.isEqual;
import static javasabr.rlib.network.util.NetworkUtils.createAllTrustedClientSslContext;
import static javasabr.rlib.network.util.NetworkUtils.createSslContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javasabr.rlib.network.exception.ConnectionClosedException;
import javasabr.rlib.network.impl.AbstractConnection;
import javasabr.rlib.network.impl.DefaultConnection;
import javasabr.rlib.network.packet.impl.DefaultReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.StringWritableNetworkPacket;
import javasabr.rlib.network.packet.registry.ReadableNetworkPacketRegistry;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * Checking that the connections are closed correctly
 *
 * @author crazyrokr
 */
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
    try {
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
    } finally {
      // cleanup
      clientNetwork.shutdown();
      serverNetwork.shutdown();
    }
  }

  @Test
  @SneakyThrows
  void shouldCloseServerConnectionWhenClientClosesTcpChannelAbruptly() {
    // given
    try (var keystoreFile = ConnectionCloseTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
         var testNetwork = buildStringSSLNetwork(
             createSslContext(keystoreFile, "test"),
             createAllTrustedClientSslContext())) {
      var serverConnection = testNetwork.serverToClient;
      var clientConnection = testNetwork.clientToServer;
      CountDownLatch dataReceivedLatch = new CountDownLatch(1);
      serverConnection.onReceiveValidPacket((conn, packet) -> dataReceivedLatch.countDown());
      clientConnection.sendInBackground(new StringWritableNetworkPacket<>("handshake"));
      assertThat(dataReceivedLatch.await(5, TimeUnit.SECONDS))
          .as("Client connection should be closed prior server side verification")
          .isTrue();

      // when
      clientConnection.channel().close();

      // then
      await()
          .alias("Client connection should be closed prior server side verification")
          .atMost(5, TimeUnit.SECONDS)
          .until(clientConnection::closed, isEqual(true));
      await()
          .alias("Server connection should be closed after receiving EOF from abruptly closed client channel")
          .atMost(5, TimeUnit.SECONDS)
          .until(serverConnection::closed, isEqual(true));
    }
  }
}
