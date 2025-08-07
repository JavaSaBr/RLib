package javasabr.rlib.network;

import static java.util.stream.Collectors.toList;
import static javasabr.rlib.network.NetworkFactory.newDefaultClientNetwork;
import static javasabr.rlib.network.NetworkFactory.newDefaultServerNetwork;
import static javasabr.rlib.network.ServerNetworkConfig.DEFAULT_SERVER;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import javasabr.rlib.common.util.ObjectUtils;
import javasabr.rlib.common.util.StringUtils;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import javasabr.rlib.network.annotation.PacketDescription;
import javasabr.rlib.network.impl.DefaultBufferAllocator;
import javasabr.rlib.network.impl.DefaultConnection;
import javasabr.rlib.network.packet.impl.DefaultReadablePacket;
import javasabr.rlib.network.packet.impl.DefaultWritablePacket;
import javasabr.rlib.network.packet.registry.ReadablePacketRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The tests of default network.
 *
 * @author JavaSaBr
 */
public class DefaultNetworkTest extends BaseNetworkTest {

  private static final Logger LOGGER = LoggerManager.getLogger(DefaultNetworkTest.class);

  // client packets
  interface ClientPackets {

    @RequiredArgsConstructor
    @PacketDescription(id = 1)
    class RequestEchoMessage extends DefaultWritablePacket {

      private final String message;

      @Override
      protected void writeImpl(@NotNull ByteBuffer buffer) {
        super.writeImpl(buffer);
        writeString(buffer, message);
      }
    }

    @PacketDescription(id = 2)
    class RequestServerTime extends DefaultWritablePacket {}

    @ToString
    @RequiredArgsConstructor
    @PacketDescription(id = 3)
    class ResponseEchoMessage extends DefaultReadablePacket {

      @Getter
      private volatile String message;

      @Override
      protected void readImpl(@NotNull DefaultConnection connection, @NotNull ByteBuffer buffer) {
        super.readImpl(connection, buffer);
        message = readString(buffer);
      }
    }

    @ToString
    @PacketDescription(id = 4)
    class ResponseServerTime extends DefaultReadablePacket {

      @Getter
      private volatile LocalDateTime localDateTime;

      @Override
      protected void readImpl(@NotNull DefaultConnection connection, @NotNull ByteBuffer buffer) {
        super.readImpl(connection, buffer);
        localDateTime = LocalDateTime.ofEpochSecond(readLong(buffer), 0, ZoneOffset.ofTotalSeconds(readInt(buffer)));
      }
    }
  }

  // server packets
  interface ServerPackets {

    @ToString
    @PacketDescription(id = 1)
    class RequestEchoMessage extends DefaultReadablePacket {

      private volatile String message;

      @Override
      protected void readImpl(@NotNull DefaultConnection connection, @NotNull ByteBuffer buffer) {
        super.readImpl(connection, buffer);
        message = readString(buffer);
      }

      @Override
      protected void executeImpl(@NotNull DefaultConnection connection) {
        super.executeImpl(connection);
        connection.send(new ResponseEchoMessage(message));
      }
    }

    @ToString
    @PacketDescription(id = 2)
    class RequestServerTime extends DefaultReadablePacket {

      @Override
      protected void readImpl(@NotNull DefaultConnection connection, @NotNull ByteBuffer buffer) {
        super.readImpl(connection, buffer);
      }

      @Override
      protected void executeImpl(@NotNull DefaultConnection connection) {
        super.executeImpl(connection);
        connection.send(new ResponseServerTime());
      }
    }

    @RequiredArgsConstructor
    @PacketDescription(id = 3)
    class ResponseEchoMessage extends DefaultWritablePacket {

      private final String message;

      @Override
      protected void writeImpl(@NotNull ByteBuffer buffer) {
        super.writeImpl(buffer);
        writeString(buffer, "Echo: " + message);
      }
    }

    @PacketDescription(id = 4)
    class ResponseServerTime extends DefaultWritablePacket {

      @Override
      protected void writeImpl(@NotNull ByteBuffer buffer) {
        super.writeImpl(buffer);
        var dateTime = ZonedDateTime.now();
        writeLong(buffer, dateTime.toEpochSecond());
        writeInt(
            buffer,
            dateTime
                .getOffset()
                .getTotalSeconds());
      }
    }
  }

  @Test
  @SneakyThrows
  void echoNetworkTest() {

    var serverNetwork = newDefaultServerNetwork(ReadablePacketRegistry.of(
        DefaultReadablePacket.class,
        ServerPackets.RequestEchoMessage.class,
        ServerPackets.RequestServerTime.class));
    var serverAddress = serverNetwork.start();
    var counter = new CountDownLatch(90);

    serverNetwork
        .accepted()
        .flatMap(Connection::receivedEvents)
        .doOnNext(event -> event.packet.execute(event.connection))
        .subscribe(event -> LOGGER.info("Received from client: " + event.packet));

    var clientNetwork = newDefaultClientNetwork(ReadablePacketRegistry.of(
        DefaultReadablePacket.class,
        ClientPackets.ResponseEchoMessage.class,
        ClientPackets.ResponseServerTime.class));
    clientNetwork
        .connected(serverAddress)
        .doOnNext(connection -> IntStream
            .range(10, 100)
            .forEach(length -> {
              if (length % 2 == 0) {
                connection.send(new ClientPackets.RequestServerTime());
              } else {
                connection.send(new ClientPackets.RequestEchoMessage(StringUtils.generate(length)));
              }
            }))
        .flatMapMany(Connection::receivedEvents)
        .subscribe(event -> {
          LOGGER.info("Received from server: " + event.packet);
          counter.countDown();
        });

    Assertions.assertTrue(
        counter.await(10000, TimeUnit.MILLISECONDS),
        "Still wait for " + counter.getCount() + " packets...");

    clientNetwork.shutdown();
    serverNetwork.shutdown();
  }

  @Test
  void shouldNotUseMappedBuffers() {

    var serverPacketRegistry = ReadablePacketRegistry.of(
        DefaultReadablePacket.class,
        ServerPackets.RequestEchoMessage.class,
        ServerPackets.RequestServerTime.class);
    var clientPacketRegistry = ReadablePacketRegistry.of(
        DefaultReadablePacket.class,
        ClientPackets.ResponseEchoMessage.class,
        ClientPackets.ResponseServerTime.class);

    var serverAllocator = new DefaultBufferAllocator(DEFAULT_SERVER) {

      @Override
      public @NotNull ByteBuffer takeBuffer(int bufferSize) {
        throw new RuntimeException();
      }
    };

    var clientAllocator = new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT) {

      @Override
      public @NotNull ByteBuffer takeBuffer(int bufferSize) {
        throw new RuntimeException();
      }
    };

    int packetCount = 200;

    try (var testNetwork = buildDefaultNetwork(
        serverAllocator,
        serverPacketRegistry,
        clientAllocator,
        clientPacketRegistry)) {

      var bufferSize = testNetwork.serverNetworkConfig.getReadBufferSize() / 3;

      var random = ThreadLocalRandom.current();

      var clientToServer = testNetwork.clientToServer;
      var serverToClient = testNetwork.serverToClient;

      var pendingPacketsOnServer = serverToClient
          .receivedPackets()
          .buffer(packetCount);

      var messages = IntStream
          .range(0, packetCount)
          .mapToObj(value -> StringUtils.generate(random.nextInt(0, bufferSize)))
          .peek(message -> clientToServer.send(new ClientPackets.RequestEchoMessage(message)))
          .collect(toList());

      var receivedPackets = ObjectUtils.notNull(pendingPacketsOnServer.blockFirst(Duration.ofSeconds(5)));

      Assertions.assertEquals(receivedPackets.size(), packetCount, "Didn't receive all packets");

      var wrongPacket = receivedPackets
          .stream()
          .filter(ClientPackets.ResponseEchoMessage.class::isInstance)
          .map(ClientPackets.ResponseEchoMessage.class::cast)
          .filter(packet -> messages
              .stream()
              .noneMatch(message -> message.equals(packet.getMessage())))
          .findFirst()
          .orElse(null);

      Assertions.assertNull(wrongPacket, () -> "Wrong received packet: " + wrongPacket);
    }
  }
}
