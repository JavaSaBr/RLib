package com.ss.rlib.network.test;

import com.ss.rlib.common.util.Utils;
import com.ss.rlib.network.NetworkFactory;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.impl.simple.StringDataConnection;
import com.ss.rlib.network.packet.impl.simple.StringReadablePacket;
import com.ss.rlib.network.packet.impl.simple.StringWritablePacket;
import com.ss.rlib.network.server.ServerNetwork;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

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
            .filter(StringReadablePacket.class::isInstance)
            .map(StringReadablePacket.class::cast)
            .buffer(10);

        for (int i = 1, attempts = 10; i <= attempts; i++) {
            clientToServer.send(new StringWritablePacket("message from client " + i));
        }

        var receivedPackets = pendingPackets.blockFirst(Duration.ofSeconds(1));

        Assertions.assertNotNull(receivedPackets);

        for (int i = 0; i < receivedPackets.size(); i++) {
            Assertions.assertEquals("message from client " + i, receivedPackets.get(i).getData());
        }
    }

    @Test
    void testSendMessageToClient() {

        var clientToServer = CON_CLIENT_TO_SERVER.join();
        var serverToClient = CON_SERVER_TO_CLIENT.join();

        var pendingPackets = clientToServer.receive()
            .filter(StringReadablePacket.class::isInstance)
            .map(StringReadablePacket.class::cast)
            .buffer(10);

        for (int i = 1, attempts = 10; i <= attempts; i++) {
            serverToClient.send(new StringWritablePacket("message from client " + i));
        }

        var receivedPackets = pendingPackets.blockFirst(Duration.ofSeconds(1));

        Assertions.assertNotNull(receivedPackets);

        for (int i = 0; i < receivedPackets.size(); i++) {
            Assertions.assertEquals("message from server " + i, receivedPackets.get(i).getData());
        }
    }

    @AfterAll
    static void shutdownNetwork() {
        clientNetwork.shutdown();
        serverNetwork.shutdown();
    }
}
