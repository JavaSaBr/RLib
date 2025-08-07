package com.ss.rlib.network.test;

import static com.ss.rlib.network.NetworkFactory.*;
import static java.util.stream.Collectors.toList;
import javasabr.rlib.common.util.ObjectUtils;
import javasabr.rlib.common.util.StringUtils;
import javasabr.rlib.common.util.Utils;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.ServerNetworkConfig;
import com.ss.rlib.network.impl.DefaultBufferAllocator;
import com.ss.rlib.network.packet.impl.*;
import com.ss.rlib.network.util.NetworkUtils;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.net.ssl.*;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * The tests of string based network.
 *
 * @author JavaSaBr
 */
public class StringSSLNetworkTest extends BaseNetworkTest {

    private static final Logger LOGGER = LoggerManager.getLogger(StringSSLNetworkTest.class);

    @Test
    @SneakyThrows
    void certificatesTest() {

        var keystoreFile = StringSSLNetworkTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
        var sslContext = NetworkUtils.createSslContext(keystoreFile, "test");
        var clientSSLContext = NetworkUtils.createAllTrustedClientSslContext();

        var serverPort = NetworkUtils.getAvailablePort(10000);

        var serverSocketFactory = sslContext.getServerSocketFactory();
        var serverSocket = serverSocketFactory.createServerSocket(serverPort);

        var clientSocketFactory = clientSSLContext.getSocketFactory();
        var clientSocket = (SSLSocket) clientSocketFactory.createSocket("localhost", serverPort);

        var clientSocketOnServer = serverSocket.accept();

        new Thread(() -> Utils.unchecked(() -> {
            var clientOutStream = new PrintWriter(clientSocket.getOutputStream());
            clientOutStream.println("Hello SSL");
            clientOutStream.flush();
        })).start();

        var serverIn = new Scanner(clientSocketOnServer.getInputStream());
        var receivedOnServer = serverIn.next() + " " + serverIn.next();

        Assertions.assertEquals("Hello SSL", receivedOnServer);
    }

    @Test
    @SneakyThrows
    void serverSSLNetworkTest() {

        var keystoreFile = StringSSLNetworkTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
        var sslContext = NetworkUtils.createSslContext(keystoreFile, "test");

        var serverNetwork = newStringDataSSLServerNetwork(
            ServerNetworkConfig.DEFAULT_SERVER,
            new DefaultBufferAllocator(ServerNetworkConfig.DEFAULT_CLIENT),
            sslContext
        );

        var serverAddress = serverNetwork.start();

        serverNetwork.accepted()
            .flatMap(Connection::receivedEvents)
            .subscribe(event -> {
                var message = event.packet.getData();
                LOGGER.info("Received from client: " + message);
                event.connection.send(new StringWritablePacket("Echo: " + message));
            });

        var clientSslContext = NetworkUtils.createAllTrustedClientSslContext();
        var sslSocketFactory = clientSslContext.getSocketFactory();
        var sslSocket = (SSLSocket) sslSocketFactory.createSocket(serverAddress.getHostName(), serverAddress.getPort());

        var buffer = ByteBuffer.allocate(1024);
        buffer.position(2);

        new StringWritablePacket("Hello SSL").write(buffer);

        buffer.putShort(0, (short) buffer.position());
        buffer.flip();

        var out = sslSocket.getOutputStream();
        out.write(buffer.array(), 0, buffer.limit());
        out.flush();

        buffer.clear();

        var in = sslSocket.getInputStream();
        var readBytes = in.read(buffer.array());

        buffer.position(readBytes).flip();
        var packetLength = buffer.getShort();

        var response = new StringReadablePacket();
        response.read(null, buffer, packetLength - 2);

        LOGGER.info("Response: " + response.getData());

        serverNetwork.shutdown();
    }

    @Test
    @SneakyThrows
    void clientSSLNetworkTest() {

        System.setProperty("javax.net.debug", "all");
        //LoggerManager.enable(AbstractSSLPacketWriter.class, LoggerLevel.DEBUG);
        //LoggerManager.enable(AbstractSSLPacketReader.class, LoggerLevel.DEBUG);

        var keystoreFile = StringSSLNetworkTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
        var sslContext = NetworkUtils.createSslContext(keystoreFile, "test");

        var serverPort = NetworkUtils.getAvailablePort(1000);

        var serverSocketFactory = sslContext.getServerSocketFactory();
        var serverSocket = serverSocketFactory.createServerSocket(serverPort);
        var counter = new CountDownLatch(1);

        var clientSslContext = NetworkUtils.createAllTrustedClientSslContext();
        var clientNetwork = newStringDataSSLClientNetwork(
            NetworkConfig.DEFAULT_CLIENT,
            new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT),
            clientSslContext
        );

        clientNetwork.connected(new InetSocketAddress("localhost", serverPort))
            .doOnNext(connection -> connection.send(new StringWritablePacket("Hello SSL")))
            .doOnError(Throwable::printStackTrace)
            .flatMapMany(Connection::receivedEvents)
            .subscribe(event -> {
                LOGGER.info("Received from server: " + event.packet.getData());
                counter.countDown();
            });

        var acceptedClientSocket = serverSocket.accept();

        var buffer = ByteBuffer.allocate(512);

        var clientIn = acceptedClientSocket.getInputStream();
        var readBytes = clientIn.read(buffer.array());

        buffer.position(readBytes).flip();

        var dataLength = buffer.getShort();

        var receivedPacket = new StringReadablePacket();
        receivedPacket.read(null, buffer, dataLength);

        Assertions.assertEquals("Hello SSL", receivedPacket.getData());

        LOGGER.info("Received from client: " + receivedPacket.getData());

        buffer.clear();
        buffer.position(2);

        new StringWritablePacket("Echo: Hello SSL").write(buffer);

        buffer.putShort(0, (short) buffer.position());
        buffer.flip();

        var out = acceptedClientSocket.getOutputStream();
        out.write(buffer.array(), 0, buffer.limit());
        out.flush();

        buffer.clear();

        Assertions.assertTrue(
            counter.await(100_000, TimeUnit.MILLISECONDS),
            "Still wait for " + counter.getCount() + " packets..."
        );

        clientNetwork.shutdown();
        serverSocket.close();
    }

    @Test
    @SneakyThrows
    void echoNetworkTest() {

        //System.setProperty("javax.net.debug", "all");
        //LoggerManager.enable(AbstractPacketWriter.class, LoggerLevel.DEBUG);
        //LoggerManager.enable(AbstractSSLPacketWriter.class, LoggerLevel.DEBUG);
        //LoggerManager.enable(AbstractSSLPacketReader.class, LoggerLevel.DEBUG);

        var keystoreFile = StringSSLNetworkTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
        var serverSSLContext = NetworkUtils.createSslContext(keystoreFile, "test");

        var serverNetwork = newStringDataSSLServerNetwork(
            ServerNetworkConfig.DEFAULT_SERVER,
            new DefaultBufferAllocator(ServerNetworkConfig.DEFAULT_CLIENT),
            serverSSLContext
        );

        var expectedReceivedPackets = 90;
        var serverAddress = serverNetwork.start();
        var counter = new CountDownLatch(expectedReceivedPackets);

        serverNetwork.accepted()
            .flatMap(Connection::receivedEvents)
            .subscribe(event -> {
                var message = event.packet.getData();
                LOGGER.info("Received from client: " + message);
                event.connection.send(new StringWritablePacket("Echo: " + message));
            });

        var clientSslContext = NetworkUtils.createAllTrustedClientSslContext();
        var clientNetwork = newStringDataSSLClientNetwork(
            NetworkConfig.DEFAULT_CLIENT,
            new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT),
            clientSslContext
        );

        clientNetwork.connected(serverAddress)
            .doOnNext(connection -> IntStream.range(10, expectedReceivedPackets + 10)
                .forEach(length -> connection.send(newMessage(9, length))))
            .doOnError(Throwable::printStackTrace)
            .flatMapMany(Connection::receivedEvents)
            .subscribe(event -> {
                LOGGER.info("Received from server: " + event.packet.getData());
                counter.countDown();
            });

        Assertions.assertTrue(
            counter.await(1000, TimeUnit.MILLISECONDS),
            "Still wait for " + counter.getCount() + " packets..."
        );

        serverNetwork.shutdown();
        clientNetwork.shutdown();

        //LoggerManager.disable(AbstractSSLPacketWriter.class, LoggerLevel.DEBUG);
        //LoggerManager.disable(AbstractSSLPacketReader.class, LoggerLevel.DEBUG);
        //LoggerManager.disable(AbstractPacketWriter.class, LoggerLevel.DEBUG);
    }

    @Test
    void shouldReceiveManyPacketsFromSmallToBigSize() {

        //System.setProperty("javax.net.debug", "all");
        //LoggerManager.enable(AbstractPacketReader.class, LoggerLevel.DEBUG);
        //LoggerManager.enable(AbstractPacketWriter.class, LoggerLevel.DEBUG);
        LoggerManager.enable(AbstractSSLPacketWriter.class, LoggerLevel.DEBUG);
        LoggerManager.enable(AbstractSSLPacketReader.class, LoggerLevel.DEBUG);

        var keystoreFile = StringSSLNetworkTest.class.getResourceAsStream("/ssl/rlib_test_cert.p12");
        var serverSSLContext = NetworkUtils.createSslContext(keystoreFile, "test");
        var clientSSLContext = NetworkUtils.createAllTrustedClientSslContext();

        int packetCount = 10;

        try(var testNetwork = buildStringSSLNetwork(serverSSLContext, clientSSLContext)) {

            var bufferSize = testNetwork.serverNetworkConfig
                .getReadBufferSize();

            var random = ThreadLocalRandom.current();

            var clientToServer = testNetwork.clientToServer;
            var serverToClient = testNetwork.serverToClient;

            var pendingPacketsOnServer = serverToClient.receivedPackets()
                .doOnNext(packet -> LOGGER.info("Received from client: " + packet.getData()))
                .buffer(packetCount);

            var messages = IntStream.range(0, packetCount)
                .mapToObj(value -> {
                    var length = value % 3 == 0 ? bufferSize : random.nextInt(0, bufferSize / 2 - 1);
                    return StringUtils.generate(length);
                })
                .peek(message -> clientToServer.send(new StringWritablePacket(message)))
                .collect(toList());

            var receivedPackets = ObjectUtils.notNull(pendingPacketsOnServer.blockFirst(Duration.ofSeconds(5000)));

            Assertions.assertEquals(receivedPackets.size(), packetCount, "Didn't receive all packets");

            var wrongPacket = receivedPackets.stream()
                .filter(packet -> messages.stream()
                    .noneMatch(message -> message.equals(packet.getData())))
                .findFirst()
                .orElse(null);

            Assertions.assertNull(wrongPacket, () -> "Wrong received packet: " + wrongPacket);
        }
    }

    private static @NotNull StringWritablePacket newMessage(int minMessageLength, int maxMessageLength) {
        return new StringWritablePacket(StringUtils.generate(minMessageLength, maxMessageLength));
    }
}
