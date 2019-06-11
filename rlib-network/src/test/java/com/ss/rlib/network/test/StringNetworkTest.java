package com.ss.rlib.network.test;

import static java.util.stream.Collectors.toList;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.ServerNetworkConfig;
import com.ss.rlib.network.impl.DefaultBufferAllocator;
import com.ss.rlib.network.packet.impl.simple.StringWritablePacket;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.MappedByteBuffer;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * The tests of string based network.
 *
 * @author JavaSaBr
 */
public class StringNetworkTest extends BaseNetworkTest {

    @BeforeAll
    static void beforeAll() {
        //LoggerLevel.DEBUG.setEnabled(true);
        //LoggerManager.getLogger(DefaultBufferAllocator.class)
        //    .setEnabled(LoggerLevel.DEBUG, false);
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

            var pendingPacketsOnServer = serverToClient.receive()
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

            var pendingPacketsOnServer = serverToClient.receive()
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

            var pendingPacketsOnServer = serverToClient.receive()
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

    @AfterAll
    static void afterAll() {
        //LoggerLevel.DEBUG.setEnabled(false);
    }
}
