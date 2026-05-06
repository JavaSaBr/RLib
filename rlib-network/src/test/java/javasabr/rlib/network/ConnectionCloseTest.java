package javasabr.rlib.network;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javasabr.rlib.network.exception.ConnectionClosedException;
import javasabr.rlib.network.impl.AbstractConnection;
import javasabr.rlib.network.impl.DefaultConnection;
import javasabr.rlib.network.packet.impl.DefaultReadableNetworkPacket;
import javasabr.rlib.network.packet.registry.ReadableNetworkPacketRegistry;
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
}
