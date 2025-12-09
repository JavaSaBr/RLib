package javasabr.rlib.network;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerManager;
import javasabr.rlib.network.annotation.NetworkPacketDescription;
import javasabr.rlib.network.impl.DefaultConnection;
import javasabr.rlib.network.packet.impl.DefaultReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.DefaultWritableNetworkPacket;
import javasabr.rlib.network.packet.registry.ReadableNetworkPacketRegistry;
import javasabr.rlib.network.server.ServerNetwork;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@CustomLog
public class HandlingValidAndInvalidReceivedPacketsTest extends BaseNetworkTest {

  static {
    LoggerManager.enable(HandlingValidAndInvalidReceivedPacketsTest.class, LoggerLevel.INFO);
    //LoggerManager.enable(AbstractConnection.class, LoggerLevel.DEBUG);
  }

  // client packets
  interface ClientPackets {

    @RequiredArgsConstructor
    @NetworkPacketDescription(id = 1)
    class TestValidatablePacket extends DefaultWritableNetworkPacket<DefaultConnection> {

      private final boolean valid;

      @Override
      protected void writeImpl(DefaultConnection connection, ByteBuffer buffer) {
        super.writeImpl(connection, buffer);
        writeByte(buffer, valid ? 1 : 0);
      }
    }

    @NetworkPacketDescription(id = 2)
    class MockReadablePacket extends DefaultReadableNetworkPacket<DefaultConnection> {}
  }

  // server packets
  interface ServerPackets {

    @NetworkPacketDescription(id = 1)
    class TestValidatablePacket extends DefaultReadableNetworkPacket<DefaultConnection> {

      @Override
      protected void readImpl(DefaultConnection connection, ByteBuffer buffer) {
        super.readImpl(connection, buffer);
        if (readByte(buffer) == 0) {
          throw new RuntimeException("Received invalid packet");
        }
      }
    }
  }

  @Test
  void shouldCorrectlyReceiveValidAndInvalidPackets() throws InterruptedException {
    // given:
    ReadableNetworkPacketRegistry<DefaultReadableNetworkPacket<DefaultConnection>, DefaultConnection> serverPackets =
        ReadableNetworkPacketRegistry.of(DefaultReadableNetworkPacket.class, DefaultConnection.class, ServerPackets.TestValidatablePacket.class);
    ReadableNetworkPacketRegistry<DefaultReadableNetworkPacket<DefaultConnection>, DefaultConnection> clientPackets =
        ReadableNetworkPacketRegistry.of(DefaultReadableNetworkPacket.class, DefaultConnection.class, ClientPackets.MockReadablePacket.class);

    ServerNetwork<DefaultConnection> serverNetwork = NetworkFactory.defaultServerNetwork(serverPackets);
    InetSocketAddress serverAddress = serverNetwork.start();
    List<ServerPackets.TestValidatablePacket> receivedValidPackets = Collections
        .synchronizedList(new ArrayList<>());
    List<ServerPackets.TestValidatablePacket> receivedInvalidPackets = Collections
        .synchronizedList(new ArrayList<>());

    var counter = new CountDownLatch(30);

    // when:
    serverNetwork
        .accepted()
        .flatMap(connection -> connection.receivedEvents(ServerPackets.TestValidatablePacket.class))
        .doOnNext(event -> {
          var packet = event.packet();
          if (event.valid()) {
            receivedValidPackets.add(packet);
          } else {
            receivedInvalidPackets.add(packet);
          }
          counter.countDown();
        })
        .subscribe(event -> log.info(event, "Received from client:[%s]"::formatted));

    var clientNetwork = NetworkFactory.defaultClientNetwork(clientPackets);
    clientNetwork
        .connectReactive(serverAddress)
        .doOnNext(connection -> IntStream
            .range(0, 30)
            .forEach(length -> {
              if (length % 5 == 0) {
                connection.sendInBackground(new ClientPackets.TestValidatablePacket(false));
              } else {
                connection.sendInBackground(new ClientPackets.TestValidatablePacket(true));
              }
            }))
        .subscribe();

    Assertions.assertTrue(
        counter.await(10000, TimeUnit.MILLISECONDS),
        "Still wait for " + counter.getCount() + " packets...");

    clientNetwork.shutdown();
    serverNetwork.shutdown();

    // then:
    assertThat(receivedInvalidPackets).hasSize(6);
    assertThat(receivedValidPackets).hasSize(24);
  }
}
