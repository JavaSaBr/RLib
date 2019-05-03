package com.ss.rlib.network.test;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.rlib.common.util.Utils;
import com.ss.rlib.network.NetworkFactory;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.client.server.Server;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.impl.simple.StringReadablePacket;
import com.ss.rlib.network.packet.impl.simple.StringWritablePacket;
import com.ss.rlib.network.server.ServerNetwork;
import com.ss.rlib.network.server.client.Client;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The tests of string based network.
 *
 * @author JavaSaBr
 */
public class StringNetworkTest {

    private static final CompletableFuture<Client> CONNECTED_CLIENT = new CompletableFuture<>();

    private static volatile ServerNetwork serverNetwork;
    private static volatile ClientNetwork clientNetwork;

    @BeforeAll
    public static void createNetwork() throws IOException {
        var address = new InetSocketAddress(Utils.getFreePort(1100));
        serverNetwork = NetworkFactory.newStringAsyncServerNetwork(CONNECTED_CLIENT::complete);
        serverNetwork.bind(address);
        clientNetwork = NetworkFactory.newStringAsyncClientNetwork();
        clientNetwork.connect(address);
    }

    @Test
    void testSendMessageToServer() {

        for (int i = 1, attempts = 10; i <= attempts; i++) {

            var receivedPacketFromClient = new CompletableFuture<StringReadablePacket>();
            var clientOnServer = CONNECTED_CLIENT.join();
            var handler = clientOnServer.addPacketHandler(receivedPacketFromClient::complete);

            var clientsServer = notNull(clientNetwork.getCurrentServer());
            clientsServer.getConnection()
                .sendPacket(new StringWritablePacket("message from client " + i));

            Assertions.assertEquals("message from client " + i, receivedPacketFromClient.join().getData());

            clientOnServer.removePacketHandler(handler);
        }
    }

    @Test
    void testSendMessageToClient() {

        for (int i = 1, attempts = 10; i <= attempts; i++) {

            var receivedPacketFromServer = new CompletableFuture<StringReadablePacket>();
            var clientsServer = clientNetwork.requireCurrentServer();
            var handler = clientsServer.addPacketHandler(receivedPacketFromServer::complete);

            var clientOnServer = CONNECTED_CLIENT.join();
            clientOnServer.getConnection()
                .sendPacket(new StringWritablePacket("message from server " + i));

            Assertions.assertEquals("message from server " + i, receivedPacketFromServer.join().getData());

            clientsServer.removePacketHandler(handler);
        }
    }

    @AfterAll
    public static void shutdownNetwork() {
        clientNetwork.shutdown();
        serverNetwork.shutdown();
    }
}
