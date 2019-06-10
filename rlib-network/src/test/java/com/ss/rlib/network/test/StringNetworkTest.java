package com.ss.rlib.network.test;

import static java.util.stream.Collectors.toList;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.Utils;
import com.ss.rlib.logger.api.LoggerLevel;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.NetworkFactory;
import com.ss.rlib.network.ServerNetworkConfig;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.impl.DefaultBufferAllocator;
import com.ss.rlib.network.impl.simple.StringDataConnection;
import com.ss.rlib.network.packet.impl.simple.StringWritablePacket;
import com.ss.rlib.network.server.ServerNetwork;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * The tests of string based network.
 *
 * @author JavaSaBr
 */
public class StringNetworkTest extends BaseNetworkTest {

    private static final CompletableFuture<StringDataConnection> CON_CLIENT_TO_SERVER = new CompletableFuture<>();
    private static final CompletableFuture<StringDataConnection> CON_SERVER_TO_CLIENT = new CompletableFuture<>();

    private static volatile ServerNetwork<StringDataConnection> serverNetwork;
    private static volatile ClientNetwork<StringDataConnection> clientNetwork;

    @BeforeAll
    static void beforeAll() {
        //LoggerLevel.DEBUG.setEnabled(true);
        LoggerManager.getLogger(DefaultBufferAllocator.class)
            .setEnabled(LoggerLevel.DEBUG, false);
    }

    @Test
    void testSendMessageToServer() {

        var clientToServer = CON_CLIENT_TO_SERVER.join();
        var serverToClient = CON_SERVER_TO_CLIENT.join();

        var pendingPackets = serverToClient.receive()
            .buffer(10);

        for (int i = 1, attempts = 10; i <= attempts; i++) {
            clientToServer.send(new StringWritablePacket("message from client " + i));
        }

        var receivedPackets = pendingPackets.blockFirst(Duration.ofSeconds(1));

        Assertions.assertNotNull(receivedPackets);

        for (int i = 0; i < receivedPackets.size(); i++) {
            Assertions.assertEquals("message from client " + (i + 1), receivedPackets.get(i).getData());
        }
    }

    @Test
    void testSendMessageToClient() {

        var clientToServer = CON_CLIENT_TO_SERVER.join();
        var serverToClient = CON_SERVER_TO_CLIENT.join();

        var pendingPackets = clientToServer.receive()
            .buffer(10);

        for (int i = 1, attempts = 10; i <= attempts; i++) {
            serverToClient.send(new StringWritablePacket("message from client " + i));
        }

        var receivedPackets = pendingPackets.blockFirst(Duration.ofSeconds(1));

        Assertions.assertNotNull(receivedPackets);

        for (int i = 0; i < receivedPackets.size(); i++) {
            Assertions.assertEquals("message from server " + (i + 1), receivedPackets.get(i).getData());
        }
    }

    @Test
    void testSendTooBigMessageToServer() {

        var serverAddress = new InetSocketAddress(Utils.getFreePort(10000));

        var clientToServerAsync = new CompletableFuture<StringDataConnection>();
        var serverToClientAsync = new CompletableFuture<StringDataConnection>();

        var networkConfig = new NetworkConfig() {

            @Override
            public int getWriteBufferSize() {
                return (int) (ServerNetworkConfig.DEFAULT_SERVER.getReadBufferSize() * 1.5);
            }
        };

        var serverNetwork = NetworkFactory.newStringDataServerNetwork();
        serverNetwork.start(serverAddress);
        serverNetwork.onAccept(serverToClientAsync::complete);

        var clientNetwork = NetworkFactory.newStringDataClientNetwork(networkConfig);
        clientNetwork.connect(serverAddress)
            .thenApply(clientToServerAsync::complete);

        var messages = IntStream.range(0, 10)
            .mapToObj(value -> StringUtils.generate((int) (networkConfig.getWriteBufferSize() * 0.45)))
            .collect(toList());

        var serverToClient = serverToClientAsync.join();
        var clientToServer = clientToServerAsync.join();

        var pendingPackets = serverToClient.receive()
            .buffer(10);

        for (var message : messages) {
            clientToServer.send(new StringWritablePacket(message));
        }

        var receivedPackets = pendingPackets.blockFirst(Duration.ofSeconds(1));

        Assertions.assertNotNull(receivedPackets);

        for (int i = 0; i < receivedPackets.size(); i++) {
            Assertions.assertEquals(messages.get(i), receivedPackets.get(i).getData());
        }
    }

    @Test
    void shouldReceiveManyPacketsFromSmallToBigSize() {

        try(var testNetwork = buildStringNetwork()) {

            var bufferSize = testNetwork.clientNetworkConfig
                .getWriteBufferSize();

            var random = ThreadLocalRandom.current();

            var clientToServer = testNetwork.clientToServer;
            var serverToClient = testNetwork.serverToClient;

            var pendingPacketsOnServer = serverToClient.receive()
                .buffer(100);

            var messages = IntStream.range(0, 100)
                .mapToObj(value -> {
                    var length = value % 3 == 0 ? bufferSize : random.nextInt(0, (int) (bufferSize / 1.5F));
                    return StringUtils.generate(length);
                })
                .peek(message -> clientToServer.send(new StringWritablePacket(message)))
                .collect(toList());

            var received = pendingPacketsOnServer.blockFirst(Duration.ofSeconds(1));

            for (int i = 0; i < received.size(); i++) {
                Assertions.assertEquals(received.get(i).getData(), messages.get(i));
            }
        }
    }

    @Test
    void shouldSendBiggerPacketThanWriteBuffer() {
        testSendingToServer(100, 2, 2);
    }

    private void testSendingToServer(int packetCount, int extraSizeMod, int seconds) {

        try(var testNetwork = buildStringNetwork()) {

            var bufferSize = testNetwork.clientNetworkConfig
                .getWriteBufferSize();

            var random = ThreadLocalRandom.current();

            var clientToServer = testNetwork.clientToServer;
            var serverToClient = testNetwork.serverToClient;

            var pendingPacketsOnServer = serverToClient.receive()
                .buffer(packetCount);

            var messages = IntStream.range(0, packetCount)
                .mapToObj(value -> {
                    var length = value % extraSizeMod == 0 ? bufferSize : random.nextInt(0, bufferSize / 2 - 1);
                    return StringUtils.generate(length);
                })
                .peek(message -> clientToServer.send(new StringWritablePacket(message)))
                .collect(toList());

            var receivedPackets = ObjectUtils.notNull(pendingPacketsOnServer.blockFirst(Duration.ofSeconds(seconds)));

            Assertions.assertEquals(receivedPackets.size(), packetCount, "Didn't receive all packets");

            var wrongPacket = receivedPackets.stream()
                .filter(packet -> messages.stream()
                    .noneMatch(message -> message.equals(packet.getData())))
                .findFirst()
                .orElse(null);

            Assertions.assertNull(wrongPacket, () -> "Wrong received packet: " + wrongPacket);
        }
    }

    @AfterAll
    static void afterAll() {
        LoggerLevel.DEBUG.setEnabled(false);
    }
}
