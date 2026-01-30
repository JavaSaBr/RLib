package javasabr.rlib.network;

import static javasabr.rlib.network.ServerNetworkConfig.DEFAULT_SERVER;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import javasabr.rlib.common.util.ObjectUtils;
import javasabr.rlib.common.util.StringUtils;
import javasabr.rlib.network.annotation.NetworkPacketDescription;
import javasabr.rlib.network.impl.DefaultBufferAllocator;
import javasabr.rlib.network.impl.DefaultConnection;
import javasabr.rlib.network.packet.MarkerNetworkPacket;
import javasabr.rlib.network.packet.ReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.DefaultReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.DefaultWritableNetworkPacket;
import javasabr.rlib.network.packet.registry.ReadableNetworkPacketRegistry;
import javasabr.rlib.network.server.ServerNetwork;
import lombok.CustomLog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * The tests of default network.
 *
 * @author JavaSaBr
 */
@CustomLog
public class DefaultNetworkTest extends BaseNetworkTest {

  // client packets
  interface ClientPackets {

    @RequiredArgsConstructor
    @NetworkPacketDescription(id = 1)
    class RequestEchoMessage extends DefaultWritableNetworkPacket<DefaultConnection> {

      private final String message;

      @Override
      protected void writeImpl(DefaultConnection connection, ByteBuffer buffer) {
        super.writeImpl(connection, buffer);
        writeString(buffer, message);
      }
    }

    @NetworkPacketDescription(id = 2)
    class RequestServerTime extends DefaultWritableNetworkPacket<DefaultConnection>
        implements MarkerNetworkPacket {}

    @RequiredArgsConstructor
    @NetworkPacketDescription(id = 3)
    class ResponseEchoMessage extends DefaultReadableNetworkPacket<DefaultConnection> {

      @Getter
      @Nullable
      private volatile String message;

      @Override
      protected void readImpl(DefaultConnection connection, ByteBuffer buffer) {
        message = readString(buffer, Integer.MAX_VALUE);
      }

      @Override
      public String toString() {
        return "Message:[" + message + "]";
      }
    }

    @NetworkPacketDescription(id = 4)
    class ResponseServerTime extends DefaultReadableNetworkPacket<DefaultConnection> {

      @Getter
      @Nullable
      private volatile LocalDateTime localDateTime;

      @Override
      protected void readImpl(DefaultConnection connection, ByteBuffer buffer) {
        super.readImpl(connection, buffer);
        localDateTime = LocalDateTime.ofEpochSecond(readLong(buffer), 0, ZoneOffset.ofTotalSeconds(readInt(buffer)));
      }

      @Override
      public String toString() {
        return "ResponseServerTime";
      }
    }
  }

  // server packets
  interface ServerPackets {

    @NetworkPacketDescription(id = 1)
    class RequestEchoMessage extends DefaultReadableNetworkPacket<DefaultConnection> {

      private volatile String message;

      @Override
      protected void readImpl(DefaultConnection connection, ByteBuffer buffer) {
        super.readImpl(connection, buffer);
        message = readString(buffer, Integer.MAX_VALUE);
      }

      @Override
      public String toString() {
        return "Message:[" + message + "]";
      }
    }

    @NetworkPacketDescription(id = 2)
    class RequestServerTime extends DefaultReadableNetworkPacket<DefaultConnection>
        implements MarkerNetworkPacket {

      @Override
      public String toString() {
        return "RequestServerTime";
      }
    }

    @RequiredArgsConstructor
    @NetworkPacketDescription(id = 3)
    class ResponseEchoMessage extends DefaultWritableNetworkPacket<DefaultConnection> {

      private final String message;

      @Override
      protected void writeImpl(DefaultConnection connection, ByteBuffer buffer) {
        super.writeImpl(connection, buffer);
        writeString(buffer, "Echo: " + message);
      }
    }

    @NetworkPacketDescription(id = 4)
    class ResponseServerTime extends DefaultWritableNetworkPacket<DefaultConnection> {

      @Override
      protected void writeImpl(DefaultConnection connection, ByteBuffer buffer) {
        super.writeImpl(connection, buffer);
        var dateTime = ZonedDateTime.now();
        writeLong(buffer, dateTime.toEpochSecond());
        writeInt(buffer, dateTime.getOffset().getTotalSeconds());
      }
    }
  }

  @Test
  @SneakyThrows
  void echoNetworkTest() {
    //LoggerManager.enable(DefaultNetworkTest.class, LoggerLevel.DEBUG);
    //LoggerManager.enable(DefaultNetworkTest.class, LoggerLevel.INFO);
    //LoggerManager.enable(AbstractNetworkPacketReader.class, LoggerLevel.DEBUG);

    var serverPackets = ReadableNetworkPacketRegistry.of(
        DefaultReadableNetworkPacket.class,
        DefaultConnection.class,
        ServerPackets.RequestEchoMessage.class,
        ServerPackets.RequestServerTime.class);
    var clientPackets = ReadableNetworkPacketRegistry.of(
        DefaultReadableNetworkPacket.class,
        DefaultConnection.class,
        ClientPackets.ResponseEchoMessage.class,
        ClientPackets.ResponseServerTime.class);

    ServerNetwork<DefaultConnection> serverNetwork = NetworkFactory.defaultServerNetwork(serverPackets);
    InetSocketAddress serverAddress = serverNetwork.start();

    var counter = new CountDownLatch(90);

    serverNetwork
        .accepted()
        .flatMap(Connection::receivedEvents)
        .doOnNext(event -> {
          var connection = event.connection();
          var packet = event.packet();
          if (packet instanceof ServerPackets.RequestEchoMessage request) {
            connection.sendInBackground(new ServerPackets.ResponseEchoMessage(request.message));
          } else if (packet instanceof ServerPackets.RequestServerTime request) {
            connection.sendInBackground(new ServerPackets.ResponseServerTime());
          }
        })
        .subscribe(event -> log.info(event, "Received from client:[%s]"::formatted));

    var clientNetwork = NetworkFactory.defaultClientNetwork(clientPackets);
    clientNetwork
        .connectReactive(serverAddress)
        .doOnNext(connection -> IntStream
            .range(10, 100)
            .forEach(length -> {
              if (length % 2 == 0) {
                connection.sendInBackground(new ClientPackets.RequestServerTime());
              } else {
                connection.sendInBackground(new ClientPackets.RequestEchoMessage(StringUtils.generate(length)));
              }
            }))
        .flatMapMany(Connection::receivedEvents)
        .subscribe(event -> {
          log.info(event, "Received from server:[%s]"::formatted);
          counter.countDown();
        });

    
    assertThat(counter.await(10000, TimeUnit.MILLISECONDS))
        .as(() -> "Still wait for " + counter.getCount() + " packets...")
        .isTrue();

    clientNetwork.shutdown();
    serverNetwork.shutdown();
  }

  @Test
  void shouldNotUseMappedBuffers() {

    var serverPacketRegistry = ReadableNetworkPacketRegistry.of(
        DefaultReadableNetworkPacket.class,
        DefaultConnection.class,
        ServerPackets.RequestEchoMessage.class,
        ServerPackets.RequestServerTime.class);
    var clientPacketRegistry = ReadableNetworkPacketRegistry.of(
        DefaultReadableNetworkPacket.class,
        DefaultConnection.class,
        ClientPackets.ResponseEchoMessage.class,
        ClientPackets.ResponseServerTime.class);

    var serverAllocator = new DefaultBufferAllocator(DEFAULT_SERVER) {

      @Override
      public ByteBuffer takeBuffer(int bufferSize) {
        throw new RuntimeException();
      }
    };

    var clientAllocator = new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT) {

      @Override
      public ByteBuffer takeBuffer(int bufferSize) {
        throw new RuntimeException();
      }
    };

    int packetCount = 200;

    try (TestNetwork<DefaultConnection> testNetwork = buildDefaultNetwork(
        serverAllocator,
        serverPacketRegistry,
        clientAllocator,
        clientPacketRegistry)) {

      int bufferSize = testNetwork.serverNetworkConfig.readBufferSize() / 3;
      var random = ThreadLocalRandom.current();

      DefaultConnection clientToServer = testNetwork.clientToServer;
      DefaultConnection serverToClient = testNetwork.serverToClient;

      var pendingPacketsOnServer = serverToClient
          .receivedValidPackets()
          .buffer(packetCount);

      List<String> messages = IntStream
          .range(0, packetCount)
          .mapToObj(value -> StringUtils.generate(random.nextInt(0, bufferSize)))
          .peek(message -> clientToServer.sendInBackground(new ClientPackets.RequestEchoMessage(message)))
          .toList();

      List<? extends ReadableNetworkPacket<DefaultConnection>> receivedPackets =
          ObjectUtils.notNull(pendingPacketsOnServer.blockFirst(Duration.ofSeconds(5)));

      assertThat(receivedPackets.size())
          .as("Didn't receive all packets")
          .isEqualTo(packetCount);

      var wrongPacket = receivedPackets
          .stream()
          .filter(ClientPackets.ResponseEchoMessage.class::isInstance)
          .map(ClientPackets.ResponseEchoMessage.class::cast)
          .filter(packet -> messages
              .stream()
              .noneMatch(message -> message.equals(packet.getMessage())))
          .findFirst()
          .orElse(null);

      assertThat(wrongPacket)
          .as("Wrong received packet: " + wrongPacket)
          .isNull();
    }
  }
}
