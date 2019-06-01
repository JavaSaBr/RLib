package com.ss.rlib.network.test;

import static java.util.stream.Collectors.toList;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.Utils;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.NetworkFactory;
import com.ss.rlib.network.ServerNetworkConfig;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.impl.simple.StringDataConnection;
import com.ss.rlib.network.packet.impl.simple.StringReadablePacket;
import com.ss.rlib.network.packet.impl.simple.StringWritablePacket;
import com.ss.rlib.network.server.ServerNetwork;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 * The tests of string based network.
 *
 * @author JavaSaBr
 */
public class StringNetworkTest {

    private static final CompletableFuture<StringDataConnection> CON_CLIENT_TO_SERVER = new CompletableFuture<>();
    private static final CompletableFuture<StringDataConnection> CON_SERVER_TO_CLIENT = new CompletableFuture<>();

    private static volatile ServerNetwork<StringDataConnection> serverNetwork;
    private static volatile ClientNetwork<StringDataConnection> clientNetwork;

    @BeforeAll
    static void createNetwork() {

        var serverAddress = new InetSocketAddress(Utils.getFreePort(10000));

        serverNetwork = NetworkFactory.newStringDataServerNetwork();
        serverNetwork.start(serverAddress);
        serverNetwork.onAccept(CON_SERVER_TO_CLIENT::complete);

        clientNetwork = NetworkFactory.newStringDataClientNetwork();
        clientNetwork.connect(serverAddress)
            .thenApply(CON_CLIENT_TO_SERVER::complete);
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
    void shouldSendBiggerPacketThanWriteBuffer() {

        var clientToServer = CON_CLIENT_TO_SERVER.join();
        var serverToClient = CON_SERVER_TO_CLIENT.join();

        var pendingPacketsOnServer = serverToClient.receive()
            .buffer(1);

        var networkConfig = NetworkConfig.DEFAULT_CLIENT;
        var message = StringUtils.generate(networkConfig.getWriteBufferSize());

        clientToServer.send(new StringWritablePacket(message));

        var received = pendingPacketsOnServer.blockFirst(Duration.ofSeconds(1));

        Assertions.assertEquals(received.get(0).getData(), message);
    }

    private static @NotNull String generateMessage() {
        var networkConfig = ServerNetworkConfig.DEFAULT_SERVER;
        return StringUtils.generate((int) (networkConfig.getReadBufferSize() * 1.3));
    }

    @AfterAll
    static void shutdownNetwork() {
        clientNetwork.shutdown();
        serverNetwork.shutdown();
    }
}
