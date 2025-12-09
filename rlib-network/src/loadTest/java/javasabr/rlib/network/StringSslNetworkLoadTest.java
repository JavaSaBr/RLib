package javasabr.rlib.network;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAccumulator;
import javasabr.rlib.common.util.StringUtils;
import javasabr.rlib.common.util.ThreadUtils;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerManager;
import javasabr.rlib.network.ServerNetworkConfig.SimpleServerNetworkConfig;
import javasabr.rlib.network.client.ClientNetwork;
import javasabr.rlib.network.impl.DefaultBufferAllocator;
import javasabr.rlib.network.impl.StringDataSslConnection;
import javasabr.rlib.network.packet.impl.StringReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.StringWritableNetworkPacket;
import javasabr.rlib.network.server.ServerNetwork;
import javasabr.rlib.network.util.NetworkUtils;
import javax.net.ssl.SSLContext;
import lombok.CustomLog;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@CustomLog
public class StringSslNetworkLoadTest {

  public static final int MAX_ITERATIONS = 10;
  public static final int MAX_SEND_DELAY = 20_000;

  private static class TestClient implements AutoCloseable {

    private static final AtomicInteger ID_FACTORY = new AtomicInteger();

    String clientId = "Client_%s".formatted(ID_FACTORY.incrementAndGet());
    NetworkConfig networkConfig = NetworkConfig.SimpleNetworkConfig.builder()
        .threadGroupName(clientId)
        .writeBufferSize(256)
        .readBufferSize(256)
        .pendingBufferSize(512)
        .build();

    BufferAllocator clientAllocator = new DefaultBufferAllocator(networkConfig);
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    ClientNetwork<StringDataSslConnection> network = NetworkFactory
        .stringDataSslClientNetwork(networkConfig, clientAllocator, NetworkUtils.createAllTrustedClientSslContext());
    StatisticsCollector statistics;

    private TestClient(StatisticsCollector statistics) {
      this.statistics = statistics;
    }

    void connectAndSendMessages(
        InetSocketAddress serverAddress,
        int messages) {
      Thread thread = new Thread(() -> {

        ThreadLocalRandom random = ThreadLocalRandom.current();

        ThreadUtils.sleep(random.nextInt(5000));
        StringDataSslConnection connection = network.connect(serverAddress);

        connection.onReceiveValidPacket((serverConnection, packet) -> statistics
            .receivedServerPackersPerSecond()
            .accumulate(1));

        var tasks = new ArrayList<ScheduledFuture<?>>(messages);

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
          for (int m = 0; m < messages; m++) {
            int delay = random.nextInt(MAX_SEND_DELAY);
            ScheduledFuture<?> schedule = executor.schedule(
                () -> {
                  var message = newMessage(10, 10240); // 10240
                  connection.sendInBackground(message);
                }, delay, TimeUnit.MILLISECONDS);
            tasks.add(schedule);
          }

          for (ScheduledFuture<?> task : tasks) {
            try {
              task.get();
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          }

          tasks.clear();
        }
      });
      thread.setName(clientId);
      thread.setUncaughtExceptionHandler((t, e) -> log.error(e));
      thread.start();
    }

    @Override
    public void close() {
      network.shutdown();
    }
  }

  @Getter
  @Accessors(fluent = true)
  private static class StatisticsCollector {
    LongAccumulator receivedClientPackersPerSecond = new LongAccumulator(Long::sum, 0);
    LongAccumulator sentEchoPackersPerSecond = new LongAccumulator(Long::sum, 0);
    LongAccumulator receivedServerPackersPerSecond = new LongAccumulator(Long::sum, 0);
  }

  @Test
  @SneakyThrows
  void testServerWithMultiplyClients() {
    LoggerManager.enable(StringSslNetworkLoadTest.class, LoggerLevel.INFO);
    //LoggerManager.enable(AbstractSslNetworkPacketReader.class, LoggerLevel.DEBUG);
    //LoggerManager.enable(AbstractSslNetworkPacketWriter.class, LoggerLevel.DEBUG);

    var serverConfig = SimpleServerNetworkConfig
        .builder()
        .threadGroupMaxSize(10)
        .writeBufferSize(1024)
        .readBufferSize(1024)
        .pendingBufferSize(2048)
        .build();

    var serverAllocator = new DefaultBufferAllocator(serverConfig);
    ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    int clientCount = 100;
    int messagesPerIteration = 2000;
    int expectedMessages = clientCount * messagesPerIteration * MAX_ITERATIONS;

    var finalWaiter = new CountDownLatch(2);
    var statistics = new StatisticsCollector();

    InputStream keystoreFile = StringSslNetworkLoadTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
    SSLContext serverSslContext = NetworkUtils.createSslContext(keystoreFile, "test");

    ServerNetwork<StringDataSslConnection> serverNetwork = NetworkFactory
        .stringDataSslServerNetwork(serverConfig, serverAllocator, serverSslContext);
    InetSocketAddress serverAddress = serverNetwork.start();

    serverNetwork.onAccept(accepted -> accepted
        .onReceiveValidPacket((connection, packet) -> {
          StringReadableNetworkPacket<StringDataSslConnection> receivedPacket = (StringReadableNetworkPacket<StringDataSslConnection>) packet;
          statistics
              .receivedClientPackersPerSecond()
              .accumulate(1);
          connection.sendInBackground(new StringWritableNetworkPacket<>("Echo: " + receivedPacket.data()));
          statistics
              .sentEchoPackersPerSecond()
              .accumulate(1);
        }));

    initReceivedMessagesTracker(
        scheduledExecutor,
        statistics,
        finalWaiter);

    var clients = new ArrayList<TestClient>();

    for(int c = 0; c < clientCount; c++) {
      TestClient testClient = new TestClient(statistics);
      testClient.connectAndSendMessages(serverAddress, messagesPerIteration);
    }

    ThreadUtils.sleep(30000);
    finalWaiter.countDown();

    Assertions
        .assertTrue(finalWaiter.await(300_000, TimeUnit.MILLISECONDS),
            "Still not received [%s] -> [%s] messages".formatted(expectedMessages, finalWaiter.getCount()));

    for (TestClient client : clients) {
      client.close();
    }

    serverNetwork.shutdown();
  }

  private static void initReceivedMessagesTracker(
      ScheduledExecutorService scheduledExecutor,
      StatisticsCollector statistics,
      CountDownLatch finalWaiter) {
    scheduledExecutor.scheduleAtFixedRate(() -> {
      long received = statistics
          .receivedClientPackersPerSecond()
          .getThenReset();
      log.info(received, "Server receiving [%s] messages/sec"::formatted);
      if (received == 0) {
       finalWaiter.countDown();
      }
    }, 1, 1, TimeUnit.SECONDS);
    scheduledExecutor.scheduleAtFixedRate(() -> {
      long received = statistics
          .sentEchoPackersPerSecond()
          .getThenReset();
      log.info(received, "Sent echo packets [%s] messages/sec"::formatted);
    }, 1, 1, TimeUnit.SECONDS);
    scheduledExecutor.scheduleAtFixedRate(() -> {
      long received = statistics
          .receivedServerPackersPerSecond()
          .getThenReset();
      log.info(received, "Clients receiving [%s] echo messages/sec"::formatted);
    }, 1, 1, TimeUnit.SECONDS);
  }

  private static StringWritableNetworkPacket<StringDataSslConnection> newMessage(
      int minMessageLength,
      int maxMessageLength) {
    return new StringWritableNetworkPacket<>(StringUtils.generate(minMessageLength, maxMessageLength));
  }
}
