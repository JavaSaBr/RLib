package com.ss.rlib.network.test;

import static com.ss.rlib.network.NetworkFactory.*;
import static com.ss.rlib.network.ServerNetworkConfig.DEFAULT_SERVER;
import static java.util.stream.Collectors.toList;
import com.ss.rlib.common.concurrent.atomic.AtomicInteger;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.ServerNetworkConfig.SimpleServerNetworkConfig;
import com.ss.rlib.network.annotation.PacketDescription;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.impl.DefaultBufferAllocator;
import com.ss.rlib.network.impl.DefaultConnection;
import com.ss.rlib.network.impl.ReuseBufferAllocator;
import com.ss.rlib.network.packet.impl.DefaultReadablePacket;
import com.ss.rlib.network.packet.impl.DefaultWritablePacket;
import com.ss.rlib.network.packet.impl.StringWritablePacket;
import com.ss.rlib.network.packet.registry.ReadablePacketRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.time.*;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

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
        class RequestServerTime extends DefaultWritablePacket {
        }

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
                localDateTime = LocalDateTime.ofEpochSecond(
                    readLong(buffer),
                    0,
                    ZoneOffset.ofTotalSeconds(readInt(buffer))
                );
            }
        }
    }

    // server packets
    interface ServerPackets {

        @ToString
        @PacketDescription(id = 1)
        class RequestEchoMessage extends DefaultReadablePacket {

            @Override
            protected void readImpl(@NotNull DefaultConnection connection, @NotNull ByteBuffer buffer) {
                super.readImpl(connection, buffer);
                connection.send(new ResponseEchoMessage(readString(buffer)));
            }
        }

        @ToString
        @PacketDescription(id = 2)
        class RequestServerTime extends DefaultReadablePacket {

            @Override
            protected void readImpl(@NotNull DefaultConnection connection, @NotNull ByteBuffer buffer) {
                super.readImpl(connection, buffer);
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
                writeInt(buffer, dateTime.getOffset().getTotalSeconds());
            }
        }
    }

    @Test
    @SneakyThrows
    void echoNetworkTest() {

        var serverNetwork = newDefaultServerNetwork(ReadablePacketRegistry.of(DefaultReadablePacket.class,
            ServerPackets.RequestEchoMessage.class,
            ServerPackets.RequestServerTime.class
        ));
        var serverAddress = serverNetwork.start();
        var counter = new CountDownLatch(90);

        serverNetwork.accepted()
            .flatMap(Connection::receivedEvents)
            .subscribe(event -> LOGGER.info("Received from client: " + event.packet));

        var clientNetwork = newDefaultClientNetwork(ReadablePacketRegistry.of(DefaultReadablePacket.class,
            ClientPackets.ResponseEchoMessage.class,
            ClientPackets.ResponseServerTime.class
        ));
        clientNetwork.connected(serverAddress)
            .doOnNext(connection -> IntStream.range(10, 100)
                .forEach(length -> {
                   if(length % 2 == 0) {
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
            "Still wait for " + counter.getCount() + " packets..."
        );

        clientNetwork.shutdown();
        serverNetwork.shutdown();
    }

    @Test
    void shouldNotUseMappedBuffers() {

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

        var serverConfig = SimpleServerNetworkConfig.builder()
            .groupSize(10)
            .build();

        var serverAllocator = new ReuseBufferAllocator(serverConfig);
        var clientAllocator = new ReuseBufferAllocator(NetworkConfig.DEFAULT_CLIENT);

        var clientCount = 100;
        var packetsPerClient = 100;
        var counter = new CountDownLatch(clientCount * packetsPerClient);
        var minMessageLength = 10;
        var maxMessageLength = (int) (DEFAULT_SERVER.getReadBufferSize() * 1.5);
        var sentPacketsToServer = new AtomicInteger();
        var receivedPacketsOnServer = new AtomicInteger();
        var receivedPacketsOnClients = new AtomicInteger();

        var serverNetwork = newStringDataServerNetwork(serverConfig, serverAllocator);
        var serverAddress = serverNetwork.start();

        serverNetwork.accepted()
            .flatMap(Connection::receivedEvents)
            .doOnNext(event -> receivedPacketsOnServer.incrementAndGet())
            .subscribe(event -> event.connection.send(newMessage(minMessageLength, maxMessageLength)));

        Flux.fromStream(IntStream.range(0, clientCount)
            .mapToObj(value -> newStringDataClientNetwork(NetworkConfig.DEFAULT_CLIENT, clientAllocator)))
            .doOnDiscard(ClientNetwork.class, Network::shutdown)
            .flatMap(client -> client.connected(serverAddress))
            .doOnNext(connection -> IntStream.range(0, packetsPerClient)
                .forEach(val -> {
                    connection.send(newMessage(minMessageLength, maxMessageLength));
                    sentPacketsToServer.incrementAndGet();
                }))
            .flatMap(Connection::receivedEvents)
            .subscribe(event -> {
                receivedPacketsOnClients.incrementAndGet();
                counter.countDown();
            });

        Assertions.assertTrue(
            counter.await(10000, TimeUnit.MILLISECONDS),
            "Still wait for " + counter.getCount() + " packets... " +
                "Sent packets to server: " + sentPacketsToServer + ", " +
                "Received packets on server: " +  receivedPacketsOnServer + ", " +
                "Received packets on clients: " + receivedPacketsOnClients
        );

        serverNetwork.shutdown();
    }

    @Test
    @SneakyThrows
    void testServerWithMultiplyClientsUsingOldApi() {

        var serverNetwork = newStringDataServerNetwork(SimpleServerNetworkConfig.builder()
            .groupSize(10)
            .build());

        var serverAddress = serverNetwork.start();
        var clientCount = 100;
        var packetsPerClient = 100;
        var counter = new CountDownLatch(clientCount * packetsPerClient);
        var minMessageLength = 10;
        var maxMessageLength = (int) (DEFAULT_SERVER.getReadBufferSize() * 1.5);
        var sentPacketsToServer = new AtomicInteger();
        var receivedPacketsOnServer = new AtomicInteger();
        var receivedPacketsOnClients = new AtomicInteger();
        var connectedClients = new CountDownLatch(clientCount);

        serverNetwork.onAccept(connection -> {
            connection.onReceive((con, packet) -> {
                receivedPacketsOnServer.incrementAndGet();
                con.send(newMessage(minMessageLength, maxMessageLength));
            });
            connectedClients.countDown();
        });

        var clients = IntStream.range(0, clientCount)
            .mapToObj(value -> newStringDataClientNetwork())
            .peek(client -> client.connect(serverAddress))
            .collect(toList());

        connectedClients.await();

        clients.stream()
            .map(ClientNetwork::getCurrentConnection)
            .filter(Objects::nonNull)
            .peek(connection -> connection.onReceive((con, packet) -> {
                receivedPacketsOnClients.incrementAndGet();
                counter.countDown();
            }))
            .forEach(connection -> IntStream.range(0, packetsPerClient)
                .peek(value -> connection.send(newMessage(minMessageLength, maxMessageLength)))
                .forEach(val -> sentPacketsToServer.incrementAndGet())
            );

        Assertions.assertTrue(
            counter.await(5, TimeUnit.SECONDS),
            "Still wait for " + counter.getCount() + " packets... " +
                "Sent packets to server: " + sentPacketsToServer + ", " +
                "Received packets on server: " +  receivedPacketsOnServer + ", " +
                "Received packets on clients: " + receivedPacketsOnClients
        );

        clients.forEach(Network::shutdown);
        serverNetwork.shutdown();
    }

    private static @NotNull StringWritablePacket newMessage(int minMessageLength, int maxMessageLength) {
        return new StringWritablePacket(StringUtils.generate(minMessageLength, maxMessageLength));
    }
}
