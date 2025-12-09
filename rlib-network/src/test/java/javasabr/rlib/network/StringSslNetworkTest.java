package javasabr.rlib.network;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import javasabr.rlib.common.util.ObjectUtils;
import javasabr.rlib.common.util.StringUtils;
import javasabr.rlib.common.util.Utils;
import javasabr.rlib.network.client.ClientNetwork;
import javasabr.rlib.network.impl.DefaultBufferAllocator;
import javasabr.rlib.network.impl.StringDataSslConnection;
import javasabr.rlib.network.packet.ReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.StringReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.StringWritableNetworkPacket;
import javasabr.rlib.network.server.ServerNetwork;
import javasabr.rlib.network.util.NetworkUtils;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The tests of string based network.
 *
 * @author JavaSaBr
 */
@CustomLog
public class StringSslNetworkTest extends BaseNetworkTest {

  @Test
  @SneakyThrows
  void certificatesTest() {

    InputStream keystoreFile = StringSslNetworkTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
    SSLContext sslContext = NetworkUtils.createSslContext(keystoreFile, "test");
    SSLContext clientSslContext = NetworkUtils.createAllTrustedClientSslContext();

    int serverPort = NetworkUtils.getAvailablePort(10000);

    SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
    ServerSocket serverSocket = serverSocketFactory.createServerSocket(serverPort);

    SSLSocketFactory clientSocketFactory = clientSslContext.getSocketFactory();
    SSLSocket clientSocket = (SSLSocket) clientSocketFactory.createSocket("localhost", serverPort);

    var clientSocketOnServer = serverSocket.accept();

    new Thread(() -> Utils.unchecked(() -> {
      var clientOutStream = new PrintWriter(clientSocket.getOutputStream());
      clientOutStream.println("Hello SSL");
      clientOutStream.flush();
    })).start();

    Scanner serverIn = new Scanner(clientSocketOnServer.getInputStream());
    String receivedOnServer = serverIn.next() + " " + serverIn.next();

    Assertions.assertEquals("Hello SSL", receivedOnServer);
  }

  @Test
  @SneakyThrows
  void serverSslNetworkTest() {
    //LoggerManager.enable(StringSslNetworkTest.class, LoggerLevel.INFO);

    InputStream keystoreFile = StringSslNetworkTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
    SSLContext sslContext = NetworkUtils.createSslContext(keystoreFile, "test");

    ServerNetwork<StringDataSslConnection> serverNetwork = NetworkFactory.stringDataSslServerNetwork(
        ServerNetworkConfig.DEFAULT_SERVER,
        new DefaultBufferAllocator(ServerNetworkConfig.DEFAULT_CLIENT),
        sslContext);

    InetSocketAddress serverAddress = serverNetwork.start();

    serverNetwork
        .accepted()
        .flatMap(Connection::receivedEvents)
        .subscribe(event -> {
          var message = ((StringReadableNetworkPacket<?>) event.packet()).data();
          log.info(message, "Received from client:[%s]"::formatted);
          event.connection().sendInBackground(new StringWritableNetworkPacket<>("Echo: " + message));
        });

    SSLContext clientSslContext = NetworkUtils.createAllTrustedClientSslContext();
    SSLSocketFactory sslSocketFactory = clientSslContext.getSocketFactory();
    SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(serverAddress.getHostName(), serverAddress.getPort());

    var writableNetworkPacket = new StringWritableNetworkPacket<StringDataSslConnection>("Hello SSL");
    var buffer = ByteBuffer.allocate(1024);
    buffer.position(2);
    writableNetworkPacket.write(null, buffer);
    buffer.putShort(0, (short) buffer.position());
    buffer.flip();

    var out = sslSocket.getOutputStream();
    out.write(buffer.array(), 0, buffer.limit());
    out.flush();

    buffer.clear();

    InputStream in = sslSocket.getInputStream();
    int readBytes = in.read(buffer.array());

    buffer
        .position(readBytes)
        .flip();
    short packetLength = buffer.getShort();

    var response = new StringReadableNetworkPacket<StringDataSslConnection>();
    response.read(null, buffer, packetLength - 2);

    log.info(response.data(), "Response:[%s]"::formatted);

    serverNetwork.shutdown();
  }

  @Test
  @SneakyThrows
  void clientSslNetworkTest() {
    //System.setProperty("javax.net.debug", "all");
    //LoggerManager.enable(StringSslNetworkTest.class, LoggerLevel.INFO);

    InputStream keystoreFile = StringSslNetworkTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
    SSLContext sslContext = NetworkUtils.createSslContext(keystoreFile, "test");

    int serverPort = NetworkUtils.getAvailablePort(1000);

    SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
    ServerSocket serverSocket = serverSocketFactory.createServerSocket(serverPort);
    CountDownLatch counter = new CountDownLatch(1);

    SSLContext clientSslContext = NetworkUtils.createAllTrustedClientSslContext();
    ClientNetwork<StringDataSslConnection> clientNetwork = NetworkFactory.stringDataSslClientNetwork(
        NetworkConfig.DEFAULT_CLIENT,
        new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT),
        clientSslContext);

    clientNetwork
        .connectReactive(new InetSocketAddress("localhost", serverPort))
        .doOnNext(connection -> connection.sendInBackground(new StringWritableNetworkPacket<>("Hello SSL")))
        .doOnError(Throwable::printStackTrace)
        .flatMapMany(Connection::receivedEvents)
        .subscribe(event -> {
          var message = ((StringReadableNetworkPacket<?>) event.packet()).data();
          log.info(message, "Received from server:[%s]"::formatted);
          counter.countDown();
        });

    Socket acceptedClientSocket = serverSocket.accept();

    var buffer = ByteBuffer.allocate(512);

    InputStream clientIn = acceptedClientSocket.getInputStream();
    int readBytes = clientIn.read(buffer.array());

    buffer
        .position(readBytes)
        .flip();

    var dataLength = buffer.getShort();

    var receivedPacket = new StringReadableNetworkPacket<StringDataSslConnection>();
    receivedPacket.read(null, buffer, dataLength);

    Assertions.assertEquals("Hello SSL", receivedPacket.data());

    log.info(receivedPacket.data(), "Received from client:[%s]"::formatted);

    var writableNetworkPacket = new StringWritableNetworkPacket<StringDataSslConnection>("Echo: Hello SSL");
    buffer.clear();
    buffer.position(2);
    writableNetworkPacket.write(null, buffer);
    buffer.putShort(0, (short) buffer.position());
    buffer.flip();

    OutputStream out = acceptedClientSocket.getOutputStream();
    out.write(buffer.array(), 0, buffer.limit());
    out.flush();

    buffer.clear();

    Assertions.assertTrue(
        counter.await(100_000, TimeUnit.MILLISECONDS),
        "Still wait for " + counter.getCount() + " packets...");

    clientNetwork.shutdown();
    serverSocket.close();
  }

  @Test
  @SneakyThrows
  void echoNetworkTest() {
    //System.setProperty("javax.net.debug", "all");
    //LoggerManager.enable(StringSslNetworkTest.class, LoggerLevel.INFO);

    InputStream keystoreFile = StringSslNetworkTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
    SSLContext serverSSLContext = NetworkUtils.createSslContext(keystoreFile, "test");
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    ServerNetwork<StringDataSslConnection> serverNetwork = NetworkFactory.stringDataSslServerNetwork(
        ServerNetworkConfig.DEFAULT_SERVER,
        new DefaultBufferAllocator(ServerNetworkConfig.DEFAULT_CLIENT),
        serverSSLContext);

    InetSocketAddress serverAddress = serverNetwork.start();
    int expectedReceivedPackets = 90;

    var counter = new CountDownLatch(expectedReceivedPackets);

    serverNetwork
        .accepted()
        .flatMap(Connection::receivedEvents)
        .subscribe(event -> {
          var message = ((StringReadableNetworkPacket<?>) event.packet()).data();
          log.info(message, "Received from client:[%s]"::formatted);
          event.connection().sendInBackground(new StringWritableNetworkPacket<>("Echo: " + message));
        });

    SSLContext clientSslContext = NetworkUtils.createAllTrustedClientSslContext();
    ClientNetwork<StringDataSslConnection> clientNetwork = NetworkFactory.stringDataSslClientNetwork(
        NetworkConfig.DEFAULT_CLIENT,
        new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT),
        clientSslContext);

    clientNetwork
        .connectReactive(serverAddress)
        .doOnNext(connection -> IntStream
            .range(10, expectedReceivedPackets + 10)
            .forEach(length -> {
              var packet = newMessage(9, length);
              int delay = ThreadLocalRandom
                  .current()
                  .nextInt(2000);
              executor.schedule(
                  () -> {
                    connection.sendInBackground(packet);
                    log.info(packet.data().length(), "Send [%s] symbols to server"::formatted);
                  }, delay, TimeUnit.MILLISECONDS);
            }))
        .doOnError(Throwable::printStackTrace)
        .flatMapMany(Connection::receivedEvents)
        .subscribe(event -> {
          var message = ((StringReadableNetworkPacket<?>) event.packet()).data();
          log.info(message, "Received from server:[%s]"::formatted);
          counter.countDown();
        });

    Assertions.assertTrue(
        counter.await(60, TimeUnit.SECONDS),
        "Still wait for " + counter.getCount() + " packets...");

    serverNetwork.shutdown();
    clientNetwork.shutdown();
  }

  @Test
  void shouldReceiveManyPacketsFromSmallToBigSize() {

    //System.setProperty("javax.net.debug", "all");
    //LoggerManager.enable(AbstractPacketReader.class, LoggerLevel.DEBUG);
    //LoggerManager.enable(AbstractPacketWriter.class, LoggerLevel.DEBUG);
    //LoggerManager.enable(AbstractSslNetworkPacketWriter.class, LoggerLevel.DEBUG);
    //LoggerManager.enable(AbstractSslNetworkPacketReader.class, LoggerLevel.DEBUG);

    InputStream keystoreFile = StringSslNetworkTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
    SSLContext serverSSLContext = NetworkUtils.createSslContext(keystoreFile, "test");
    SSLContext clientSSLContext = NetworkUtils.createAllTrustedClientSslContext();

    int packetCount = 10;

    try (TestNetwork<StringDataSslConnection> testNetwork = buildStringSSLNetwork(serverSSLContext, clientSSLContext)) {
      var bufferSize = testNetwork.serverNetworkConfig.readBufferSize();
      var random = ThreadLocalRandom.current();

      StringDataSslConnection clientToServer = testNetwork.clientToServer;
      StringDataSslConnection serverToClient = testNetwork.serverToClient;

      var pendingPacketsOnServer = serverToClient
          .receivedValidPackets()
          .doOnNext(packet -> log.info("Received from client: " + packet))
          .buffer(packetCount);

      var messages = IntStream
          .range(0, packetCount)
          .mapToObj(value -> {
            var length = value % 3 == 0 ? bufferSize : random.nextInt(0, bufferSize / 2 - 1);
            return StringUtils.generate(length);
          })
          .peek(message -> clientToServer.sendInBackground(new StringWritableNetworkPacket<>(message)))
          .toList();

      List<? extends ReadableNetworkPacket<StringDataSslConnection>> receivedPackets =
          ObjectUtils.notNull(pendingPacketsOnServer.blockFirst(Duration.ofSeconds(5000)));

      Assertions.assertEquals(packetCount, receivedPackets.size(), "Didn't receive all packets");

      var wrongPacket = receivedPackets
          .stream()
          .filter(packet -> messages
              .stream()
              .noneMatch(message -> message.equals(((StringReadableNetworkPacket<?>) packet).data())))
          .findFirst()
          .orElse(null);

      Assertions.assertNull(wrongPacket, () -> "Wrong received packet: " + wrongPacket);
    }
  }

  private static StringWritableNetworkPacket<StringDataSslConnection> newMessage(int minMessageLength, int maxMessageLength) {
    return new StringWritableNetworkPacket<>(StringUtils.generate(minMessageLength, maxMessageLength));
  }
}
