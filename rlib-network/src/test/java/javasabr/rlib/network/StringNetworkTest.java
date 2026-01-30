package javasabr.rlib.network;

import static javasabr.rlib.network.ServerNetworkConfig.DEFAULT_SERVER;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import javasabr.rlib.common.util.ObjectUtils;
import javasabr.rlib.common.util.StringUtils;
import javasabr.rlib.network.ServerNetworkConfig.SimpleServerNetworkConfig;
import javasabr.rlib.network.client.ClientNetwork;
import javasabr.rlib.network.impl.DefaultBufferAllocator;
import javasabr.rlib.network.impl.StringDataConnection;
import javasabr.rlib.network.packet.impl.StringReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.StringWritableNetworkPacket;
import javasabr.rlib.network.server.ServerNetwork;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

/**
 * The tests of string based network.
 *
 * @author JavaSaBr
 */
@CustomLog
public class StringNetworkTest extends BaseNetworkTest {

  @Test
  @SneakyThrows
  void echoNetworkTest() {
    //LoggerManager.enable(StringNetworkTest.class, LoggerLevel.INFO);
    //LoggerManager.enable(AbstractNetworkPacketReader.class, LoggerLevel.INFO);
    //LoggerManager.enable(AbstractNetworkPacketReader.class, LoggerLevel.DEBUG);

    ServerNetwork<StringDataConnection> serverNetwork = NetworkFactory.stringDataServerNetwork();
    InetSocketAddress serverAddress = serverNetwork.start();

    log.info(serverAddress, "Server address:[%s]"::formatted);

    var counter = new CountDownLatch(190);

    serverNetwork
        .accepted()
        .flatMap(connection -> connection.receivedEvents(RECEIVED_PACKET_TYPE))
        .subscribe(event -> {
          String message = event.packet().data();
          log.info(message, "Received from client:[%s]"::formatted);
          event.connection().sendInBackground(new StringWritableNetworkPacket<>("Echo: " + message));
        });

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    var clientNetwork = NetworkFactory.stringDataClientNetwork();
    clientNetwork
        .connectReactive(serverAddress)
        .doOnNext(connection -> IntStream
            .range(10, 200)
            .forEach(length -> {
              var packet = new StringWritableNetworkPacket<StringDataConnection>(StringUtils.generate(length));
              int delay = ThreadLocalRandom
                  .current()
                  .nextInt(50);
              executor.schedule(() -> connection.sendInBackground(packet), delay, TimeUnit.MILLISECONDS);
            }))
        .flatMapMany(connection -> connection.receivedEvents(RECEIVED_PACKET_TYPE))
        .subscribe(event -> {
          log.info(event.packet().data(), "Received from server:[%s]"::formatted);
          counter.countDown();
          log.info(counter.getCount(), "Still wait for:[%s]"::formatted);
        });

    assertThat(counter.await(10000, TimeUnit.MINUTES))
        .as(() -> "Still wait for " + counter.getCount() + " packets...")
        .isTrue();

    clientNetwork.shutdown();
    serverNetwork.shutdown();
    executor.shutdown();
  }

  @Test
  void shouldNotUseTempBuffers() {

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

    try (TestNetwork<StringDataConnection> testNetwork = buildStringNetwork(serverAllocator, clientAllocator)) {
      int bufferSize = testNetwork.serverNetworkConfig.readBufferSize() / 3;
      var random = ThreadLocalRandom.current();

      StringDataConnection clientToServer = testNetwork.clientToServer;
      StringDataConnection serverToClient = testNetwork.serverToClient;

      var pendingPacketsOnServer = serverToClient
          .receivedValidPackets(RECEIVED_PACKET_TYPE)
          .buffer(packetCount);

      List<String> messages = IntStream
          .range(0, packetCount)
          .mapToObj(value -> StringUtils.generate(random.nextInt(0, bufferSize)))
          .peek(message -> {
            log.info(message.length(), "Send [%s] symbols to server"::formatted);
            clientToServer.sendInBackground(new StringWritableNetworkPacket<>(message));
          })
          .toList();

      List<StringReadableNetworkPacket<StringDataConnection>> receivedPackets = ObjectUtils
          .notNull(pendingPacketsOnServer.blockFirst(Duration.ofSeconds(5)));

      log.info(receivedPackets.size(), "Received [%s] packets from client"::formatted);

      assertThat(receivedPackets.size())
          .as("Didn't receive all packets")
          .isEqualTo(packetCount);

      var wrongPacket = receivedPackets
          .stream()
          .filter(packet -> messages
              .stream()
              .noneMatch(message -> message.equals(packet.data())))
          .findFirst()
          .orElse(null);

      assertThat(wrongPacket)
          .as("Wrong received packet: " + wrongPacket)
          .isNull();
    }
  }

  @Test
  void shouldReceiveManyPacketsFromSmallToBigSize() {
    //LoggerManager.enable(StringNetworkTest.class, LoggerLevel.INFO);

    int packetCount = 2000;

    try (TestNetwork<StringDataConnection> testNetwork = buildStringNetwork();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1)) {
      int bufferSize = testNetwork.serverNetworkConfig.readBufferSize();
      var random = ThreadLocalRandom.current();

      StringDataConnection clientToServer = testNetwork.clientToServer;
      StringDataConnection serverToClient = testNetwork.serverToClient;

      var pendingPacketsOnServer = serverToClient
          .receivedValidPackets(RECEIVED_PACKET_TYPE)
          .doOnNext(packet ->
              log.info(packet.data().length(), "Received [%s] symbols from client"::formatted))
          .buffer(packetCount);

      var messages = IntStream
          .range(0, packetCount)
          .mapToObj(value -> {
            var length = value % 3 == 0 ? bufferSize : random.nextInt(0, bufferSize / 2 - 1);
            return StringUtils.generate(length);
          })
          .peek(message -> {
            var packet = new StringWritableNetworkPacket<StringDataConnection>(message);
            int delay = ThreadLocalRandom
                .current()
                .nextInt(15);
            executor.schedule(
                () -> {
                  clientToServer.sendInBackground(packet);
                  log.info(message.length(), "Send [%s] symbols to server"::formatted);
                }, delay, TimeUnit.MILLISECONDS);
          })
          .toList();

      List<? extends StringReadableNetworkPacket<StringDataConnection>> receivedPackets =
          ObjectUtils.notNull(pendingPacketsOnServer.blockFirst(Duration.ofSeconds(5)));

      log.info(receivedPackets.size(), "Received [%s] packets from client"::formatted);

      assertThat(receivedPackets.size())
          .as("Didn't receive all packets")
          .isEqualTo(packetCount);

      var wrongPacket = receivedPackets
          .stream()
          .filter(packet -> messages
              .stream()
              .noneMatch(message -> message.equals(packet.data())))
          .findFirst()
          .orElse(null);

      assertThat(wrongPacket)
          .as("Wrong received packet: " + wrongPacket)
          .isNull();
    }
  }

  @Test
  void shouldSendBiggerPacketThanWriteBuffer() {
    //LoggerManager.enable(StringNetworkTest.class, LoggerLevel.INFO);

    int packetCount = 10_000;

    try (TestNetwork<StringDataConnection> testNetwork = buildStringNetwork();
         ScheduledExecutorService executor = Executors.newScheduledThreadPool(1)) {
      int bufferSize = testNetwork.clientNetworkConfig.writeBufferSize();
      var random = ThreadLocalRandom.current();

      StringDataConnection clientToServer = testNetwork.clientToServer;
      StringDataConnection serverToClient = testNetwork.serverToClient;

      var pendingPacketsOnServer = serverToClient
          .receivedValidPackets(RECEIVED_PACKET_TYPE)
          .doOnNext(packet ->
              log.info(packet.data().length(), "Received [%s] symbols from client"::formatted))
          .buffer(packetCount);

      List<String> messages = IntStream
          .range(0, packetCount)
          .mapToObj(value -> {
            var length = random.nextBoolean() ? random.nextInt(bufferSize, bufferSize * 10) : random.nextInt(0, 200);
            return StringUtils.generate(length);
          })
          .peek(message -> {
            var packet = new StringWritableNetworkPacket<StringDataConnection>(message);
            int delay = ThreadLocalRandom
                .current()
                .nextInt(15);
            executor.schedule(
                () -> {
                  clientToServer.sendInBackground(packet);
                  log.info(message.length(), "Send [%s] symbols to server"::formatted);
                }, delay, TimeUnit.MILLISECONDS);
          })
          .toList();

      List<? extends StringReadableNetworkPacket<StringDataConnection>> receivedPackets =
          ObjectUtils.notNull(pendingPacketsOnServer.blockFirst(Duration.ofSeconds(5)));

      log.info(receivedPackets.size(), "Received [%s] packets from client"::formatted);

      assertThat(receivedPackets.size())
          .as("Didn't receive all packets")
          .isEqualTo(packetCount);

      var wrongPacket = receivedPackets
          .stream()
          .filter(packet -> messages
              .stream()
              .noneMatch(message -> message.equals(packet.data())))
          .findFirst()
          .orElse(null);

      assertThat(wrongPacket)
          .as("Wrong received packet: " + wrongPacket)
          .isNull();
    }
  }

  @Test
  @SneakyThrows
  void testServerWithMultiplyClients() {
    //LoggerManager.enable(AbstractPacketWriter.class, LoggerLevel.DEBUG);

    var serverConfig = SimpleServerNetworkConfig
        .builder()
        .threadGroupMaxSize(10)
        .build();

    var serverAllocator = new DefaultBufferAllocator(serverConfig);
    var clientAllocator = new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT);

    int clientCount = 100;
    int packetsPerClient = 100;
    int minMessageLength = 10;
    int maxMessageLength = (int) (DEFAULT_SERVER.readBufferSize() * 1.5);

    var counter = new CountDownLatch(clientCount * packetsPerClient);
    var sentPacketsToServer = new AtomicInteger();
    var receivedPacketsOnServer = new AtomicInteger();
    var receivedPacketsOnClients = new AtomicInteger();

    ServerNetwork<StringDataConnection> serverNetwork = NetworkFactory.stringDataServerNetwork(serverConfig, serverAllocator);
    InetSocketAddress serverAddress = serverNetwork.start();

    serverNetwork
        .accepted()
        .flatMap(Connection::receivedEvents)
        .doOnNext(event -> receivedPacketsOnServer.incrementAndGet())
        .subscribe(event -> event.connection().sendInBackground(newMessage(minMessageLength, maxMessageLength)));

    Flux
        .fromStream(IntStream
            .range(0, clientCount)
            .mapToObj(value -> NetworkFactory.stringDataClientNetwork(NetworkConfig.DEFAULT_CLIENT, clientAllocator)))
        .doOnDiscard(ClientNetwork.class, Network::shutdown)
        .flatMap(client -> client.connectReactive(serverAddress))
        .flatMap(connection -> {

          var receivedEvents = connection.receivedEvents();

          for (int i = 0; i < packetsPerClient; i++) {
            connection.sendInBackground(newMessage(minMessageLength, maxMessageLength));
            sentPacketsToServer.incrementAndGet();
          }

          return receivedEvents;
        })
        .subscribe(event -> {
          receivedPacketsOnClients.incrementAndGet();
          counter.countDown();
        });
    
    assertThat(counter.await(10000, TimeUnit.MILLISECONDS))
        .as(() -> "Still wait for " + counter.getCount() + " packets... " + "Sent packets to server: " + sentPacketsToServer
            + ", " + "Received packets on server: " + receivedPacketsOnServer + ", " + "Received packets on clients: "
            + receivedPacketsOnClients)
        .isTrue();

    serverNetwork.shutdown();
  }

  @Test
  @SneakyThrows
  void testServerWithMultiplyClientsUsingOldApi() {

    var serverNetwork = NetworkFactory.stringDataServerNetwork(SimpleServerNetworkConfig
        .builder()
        .threadGroupMaxSize(10)
        .build());

    InetSocketAddress serverAddress = serverNetwork.start();

    int clientCount = 100;
    int packetsPerClient = 1000;
    int minMessageLength = 10;
    int maxMessageLength = (int) (DEFAULT_SERVER.readBufferSize() * 1.5);

    var counter = new CountDownLatch(clientCount * packetsPerClient);
    var sentPacketsToServer = new AtomicInteger();
    var receivedPacketsOnServer = new AtomicInteger();
    var receivedPacketsOnClients = new AtomicInteger();
    var connectedClients = new CountDownLatch(clientCount);

    serverNetwork.onAccept(connection -> {
      connection.onReceiveValidPacket((con, packet) -> {
        receivedPacketsOnServer.incrementAndGet();
        con.sendInBackground(newMessage(minMessageLength, maxMessageLength));
      });
      connectedClients.countDown();
    });

    List<ClientNetwork<StringDataConnection>> clients = IntStream
        .range(0, clientCount)
        .mapToObj(value -> NetworkFactory.stringDataClientNetwork())
        .peek(client -> client.connect(serverAddress))
        .toList();

    connectedClients.await();

    clients
        .stream()
        .map(ClientNetwork::currentConnection)
        .filter(Objects::nonNull)
        .peek(connection -> connection.onReceiveValidPacket((con, packet) -> {
          receivedPacketsOnClients.incrementAndGet();
          counter.countDown();
        }))
        .forEach(connection -> IntStream
            .range(0, packetsPerClient)
            .peek(value -> connection.sendInBackground(newMessage(minMessageLength, maxMessageLength)))
            .forEach(val -> sentPacketsToServer.incrementAndGet()));

    assertThat(counter.await(5, TimeUnit.SECONDS))
        .as("Still wait for " + counter.getCount() + " packets... " + "Sent packets to server: " + sentPacketsToServer
            + ", " + "Received packets on server: " + receivedPacketsOnServer + ", " + "Received packets on clients: "
            + receivedPacketsOnClients)
        .isTrue();

    clients.forEach(Network::shutdown);
    serverNetwork.shutdown();
  }

  @Test
  void shouldGetAllPacketWithFeedback() {

    int packetCount = 200;

    try (TestNetwork<StringDataConnection> testNetwork = buildStringNetwork()) {
      int bufferSize = testNetwork.serverNetworkConfig.readBufferSize() / 3;
      var random = ThreadLocalRandom.current();

      StringDataConnection clientToServer = testNetwork.clientToServer;
      StringDataConnection serverToClient = testNetwork.serverToClient;

      var pendingPacketsOnServer = serverToClient
          .receivedValidPackets()
          .buffer(packetCount);

      List<CompletableFuture<Boolean>> asyncResults = IntStream
          .range(0, packetCount)
          .mapToObj(value -> StringUtils.generate(random.nextInt(0, bufferSize)))
          .map(message -> clientToServer.sendAsync(new StringWritableNetworkPacket<>(message)))
          .toList();

      CompletableFuture
          .allOf(asyncResults.toArray(CompletableFuture[]::new))
          .join();

      var notSentPacket = asyncResults
          .stream()
          .map(CompletableFuture::join)
          .filter(sent -> !sent)
          .findFirst()
          .orElse(null);

      assertThat(notSentPacket)
          .as("Found not sent packets...")
          .isNull();

      // so all packets are already sent, we should not wait for long time to get result
      var receivedPackets = ObjectUtils.notNull(pendingPacketsOnServer.blockFirst(Duration.ofMillis(100)));

      assertThat(receivedPackets.size())
          .as("Didn't receive all packets")
          .isEqualTo(packetCount);
    }
  }

  private static StringWritableNetworkPacket<StringDataConnection> newMessage(int minMessageLength, int maxMessageLength) {
    return new StringWritableNetworkPacket<>(StringUtils.generate(minMessageLength, maxMessageLength));
  }
}
