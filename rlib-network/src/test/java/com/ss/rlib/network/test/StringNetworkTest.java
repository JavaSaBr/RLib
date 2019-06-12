package com.ss.rlib.network.test;

import static java.util.stream.Collectors.toList;
import com.ss.rlib.common.concurrent.atomic.AtomicInteger;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerLevel;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.*;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.impl.DefaultBufferAllocator;
import com.ss.rlib.network.impl.StringDataConnection;
import com.ss.rlib.network.packet.impl.StringWritablePacket;
import com.ss.rlib.network.server.ServerNetwork;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.MappedByteBuffer;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * The tests of string based network.
 *
 * @author JavaSaBr
 */
public class StringNetworkTest extends BaseNetworkTest {

    private static final Logger LOGGER = LoggerManager.getLogger(StringNetworkTest.class);

    @BeforeAll
    static void beforeAll() {
        LoggerLevel.DEBUG.setEnabled(true);
        //LoggerManager.getLogger(DefaultBufferAllocator.class)
        //    .setEnabled(LoggerLevel.DEBUG, false);
    }

    @Test
    @SneakyThrows
    void echoNetworkTest() {

        var serverNetwork = NetworkFactory.newStringDataServerNetwork();
        var serverAddress = serverNetwork.start();
        var counter = new CountDownLatch(90);

        serverNetwork.accepted()
            .flatMap(Connection::receivedEvents)
            .subscribe(event -> {
                var message = event.packet.getData();
                LOGGER.info("Received from client: " + message);
                event.connection.send(new StringWritablePacket("Echo: " + message));
            });

        var clientNetwork = NetworkFactory.newStringDataClientNetwork();
        clientNetwork.connected(serverAddress)
            .doOnNext(connection -> IntStream.range(10, 100)
                .forEach(length -> connection.send(new StringWritablePacket(StringUtils.generate(length)))))
            .flatMapMany(Connection::receivedEvents)
            .subscribe(event -> {
                LOGGER.info("Received from server: " + event.packet.getData());
                counter.countDown();
            });

        Assertions.assertTrue(
            counter.await(10000, TimeUnit.MILLISECONDS),
            "Still wait for " + counter.getCount() + " packets..."
        );

        clientNetwork.shutdown();
        serverNetwork.shutdown();
    }

    @Test
    void shouldNotUseMappedBuffers() {

        var serverAllocator = new DefaultBufferAllocator(ServerNetworkConfig.DEFAULT_SERVER) {

            @Override
            public @NotNull MappedByteBuffer takeMappedBuffer(int size) {
                throw new RuntimeException();
            }
        };

        var clientAllocator = new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT) {

            @Override
            public @NotNull MappedByteBuffer takeMappedBuffer(int size) {
                throw new RuntimeException();
            }
        };

        int packetCount = 200;

        try(var testNetwork = buildStringNetwork(serverAllocator, clientAllocator)) {

            var bufferSize = testNetwork.serverNetworkConfig
                .getReadBufferSize() / 3;

            var random = ThreadLocalRandom.current();

            var clientToServer = testNetwork.clientToServer;
            var serverToClient = testNetwork.serverToClient;

            var pendingPacketsOnServer = serverToClient.receivedPackets()
                .buffer(packetCount);

            var messages = IntStream.range(0, packetCount)
                .mapToObj(value -> StringUtils.generate(random.nextInt(0, bufferSize)))
                .peek(message -> clientToServer.send(new StringWritablePacket(message)))
                .collect(toList());

            var receivedPackets = ObjectUtils.notNull(pendingPacketsOnServer.blockFirst(Duration.ofSeconds(5)));

            Assertions.assertEquals(receivedPackets.size(), packetCount, "Didn't receive all packets");

            var wrongPacket = receivedPackets.stream()
                .filter(packet -> messages.stream()
                    .noneMatch(message -> message.equals(packet.getData())))
                .findFirst()
                .orElse(null);

            Assertions.assertNull(wrongPacket, () -> "Wrong received packet: " + wrongPacket);
        }
    }

    @Test
    void shouldReceiveManyPacketsFromSmallToBigSize() {

        int packetCount = 200;

        try(var testNetwork = buildStringNetwork()) {

            var bufferSize = testNetwork.serverNetworkConfig
                .getReadBufferSize();

            var random = ThreadLocalRandom.current();

            var clientToServer = testNetwork.clientToServer;
            var serverToClient = testNetwork.serverToClient;

            var pendingPacketsOnServer = serverToClient.receivedPackets()
                .buffer(packetCount);

            var messages = IntStream.range(0, packetCount)
                .mapToObj(value -> {
                    var length = value % 3 == 0 ? bufferSize : random.nextInt(0, bufferSize / 2 - 1);
                    return StringUtils.generate(length);
                })
                .peek(message -> clientToServer.send(new StringWritablePacket(message)))
                .collect(toList());

            var receivedPackets = ObjectUtils.notNull(pendingPacketsOnServer.blockFirst(Duration.ofSeconds(5)));

            Assertions.assertEquals(receivedPackets.size(), packetCount, "Didn't receive all packets");

            var wrongPacket = receivedPackets.stream()
                .filter(packet -> messages.stream()
                    .noneMatch(message -> message.equals(packet.getData())))
                .findFirst()
                .orElse(null);

            Assertions.assertNull(wrongPacket, () -> "Wrong received packet: " + wrongPacket);
        }
    }

    @Test
    void shouldSendBiggerPacketThanWriteBuffer() {

        int packetCount = 100;

        try(var testNetwork = buildStringNetwork()) {

            var bufferSize = testNetwork.clientNetworkConfig
                .getWriteBufferSize();

            var random = ThreadLocalRandom.current();

            var clientToServer = testNetwork.clientToServer;
            var serverToClient = testNetwork.serverToClient;

            var pendingPacketsOnServer = serverToClient.receivedPackets()
                .buffer(packetCount);

            var messages = IntStream.range(0, packetCount)
                .mapToObj(value -> {
                    var length = value % 2 == 0 ? bufferSize : random.nextInt(0, bufferSize / 2 - 1);
                    return StringUtils.generate(length);
                })
                .peek(message -> clientToServer.send(new StringWritablePacket(message)))
                .collect(toList());

            var receivedPackets = ObjectUtils.notNull(pendingPacketsOnServer.blockFirst(Duration.ofSeconds(5)));

            Assertions.assertEquals(receivedPackets.size(), packetCount, "Didn't receive all packets");

            var wrongPacket = receivedPackets.stream()
                .filter(packet -> messages.stream()
                    .noneMatch(message -> message.equals(packet.getData())))
                .findFirst()
                .orElse(null);

            Assertions.assertNull(wrongPacket, () -> "Wrong received packet: " + wrongPacket);
        }
    }

    @Test
    @SneakyThrows
    void testServerWithMultiplyClients() {

        var serverNetwork = NetworkFactory.newStringDataServerNetwork(new ServerNetworkConfig() {

            @Override
            public int getGroupSize() {
                return 1;
            }
        });
        var serverAddress = serverNetwork.start();
        var clientCount = 5;
        var packetsPerClient = 10;
        var counter = new CountDownLatch(clientCount * packetsPerClient);
        var minMessageLength = 10;
        var maxMessageLength = ServerNetworkConfig.DEFAULT_SERVER.getReadBufferSize() * 3;
        var sentPacketsToServer = new AtomicInteger();
        var receivedPacketsOnServer = new AtomicInteger();
        var receivedPacketsOnClients = new AtomicInteger();

        serverNetwork.accepted()
            .flatMap(Connection::receivedEvents)
            .subscribe(event -> {
                receivedPacketsOnServer.incrementAndGet();
                event.connection.send(
                    new StringWritablePacket(StringUtils.generate(minMessageLength, maxMessageLength))
                );
            });

        var clients = IntStream.range(0, clientCount)
            .mapToObj(value -> NetworkFactory.newStringDataClientNetwork())
            .collect(toList());

        clients.forEach(clientNetwork -> clientNetwork.connected(serverAddress)
            .doOnNext(connection -> IntStream.range(0, packetsPerClient)
                .forEach(val -> {
                    connection.send(new StringWritablePacket(StringUtils.generate(minMessageLength, maxMessageLength)));
                    sentPacketsToServer.incrementAndGet();
                }))
            .flatMapMany(Connection::receivedEvents)
            .subscribe(event -> {
                receivedPacketsOnClients.incrementAndGet();
                counter.countDown();
            }));

        Assertions.assertTrue(
            counter.await(10000, TimeUnit.MILLISECONDS),
            "Still wait for " + counter.getCount() + " packets... " +
                "Sent packets to server: " + sentPacketsToServer + ", " +
                "Received packets on server: " +  receivedPacketsOnServer + ", " +
                "Received packets on clients: " + receivedPacketsOnClients
        );

        clients.forEach(Network::shutdown);
        serverNetwork.shutdown();
    }

    @AfterAll
    static void afterAll() {
        //LoggerLevel.DEBUG.setEnabled(false);
    }
}
