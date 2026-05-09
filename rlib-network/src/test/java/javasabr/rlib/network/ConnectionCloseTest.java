package javasabr.rlib.network;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javasabr.rlib.common.util.AwaitUtils;
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
    // given
    InputStream keystoreFile = ConnectionCloseTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
    SSLContext serverSslContext = NetworkUtils.createSslContext(keystoreFile, "test");
    SSLContext clientSslContext = NetworkUtils.createAllTrustedClientSslContext();
    try (var testNetwork = buildStringSSLNetwork(serverSslContext, clientSslContext)) {
      var serverConnection = testNetwork.serverToClient;
      var clientConnection = testNetwork.clientToServer;
      CountDownLatch dataReceivedLatch = new CountDownLatch(1);
      serverConnection.onReceiveValidPacket((conn, packet) -> dataReceivedLatch.countDown());
      clientConnection.sendInBackground(new StringWritableNetworkPacket<>("handshake"));
      dataReceivedLatch.await(5, TimeUnit.SECONDS);

      // when
      clientConnection.channel().close();
      assertThat(AwaitUtils.await(5000, ChronoUnit.MILLIS, clientConnection::closed))
          .as("Client connection should be closed prior server side verification").isTrue();

      // then
      assertThat(AwaitUtils.await(5000, ChronoUnit.MILLIS, serverConnection::closed))
          .as("Server connection should be closed after receiving EOF from abruptly closed client channel")
          .isTrue();
    }
  }
}
