package com.ss.rlib.test.network;

import com.ss.rlib.network.NetworkFactory;
import com.ss.rlib.network.annotation.PacketDescription;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.client.server.Server;
import com.ss.rlib.network.packet.ReadablePacketRegistry;
import com.ss.rlib.network.packet.impl.AbstractReadablePacket;
import com.ss.rlib.network.packet.impl.AbstractSendablePacket;
import com.ss.rlib.network.server.AcceptHandler;
import com.ss.rlib.network.server.ServerNetwork;
import com.ss.rlib.network.server.client.Client;
import com.ss.rlib.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

/**
 * The test ot test network.
 *
 * @author JavaSaBr
 */
public class NetworkTests {

    @NotNull
    private static final InetSocketAddress SERVER_ADDRESS = new InetSocketAddress(2222);

    @NotNull
    private static final CompletableFuture<Client> WAITED_CLIENT = new CompletableFuture<>();

    private static volatile CompletableFuture<String> checkClientMessage;
    private static volatile CompletableFuture<String> checkServerMessage;

    public static class ServerPackets {

        /**
         * It's a packet which a server receives from a client.
         */
        @PacketDescription(id = 1)
        public static class MessageRequest extends AbstractReadablePacket {

            @Override
            protected void readImpl(@NotNull final ByteBuffer buffer) {
                final String message = readString(buffer);
                System.out.println("Server: received \"" + message + "\"");
                checkClientMessage.complete(message);
            }
        }

        /**
         * It's a packet which a server sends to a client.
         */
        @PacketDescription(id = 2)
        public static class MessageResponse extends AbstractSendablePacket {

            @NotNull
            private final String message;

            public MessageResponse(@NotNull final String message) {
                this.message = message;
            }

            @Override
            protected void writeImpl(@NotNull final ByteBuffer buffer) {
                super.writeImpl(buffer);
                writeString(buffer, message);
            }
        }
    }

    public static class ClientPackets {

        /**
         * It's a packet which a client sends to a server.
         */
        @PacketDescription(id = 1)
        public static class MessageRequest extends AbstractSendablePacket {

            @NotNull
            private final String message;

            public MessageRequest(@NotNull final String message) {
                this.message = message;
            }

            @Override
            protected void writeImpl(@NotNull final ByteBuffer buffer) {
                super.writeImpl(buffer);
                writeString(buffer, message);
            }
        }

        /**
         * It's a packet which a client receives from a server.
         */
        @PacketDescription(id = 2)
        public static class MessageResponse extends AbstractReadablePacket {

            @Override
            protected void readImpl(@NotNull final ByteBuffer buffer) {
                final String message = readString(buffer);
                System.out.println("client: received \"" + message + "\"");
                checkServerMessage.complete(message);
            }
        }
    }

    private static ServerNetwork serverNetwork;
    private static ClientNetwork clientNetwork;

    @BeforeAll
    public static void createNetwork() throws IOException {
        final ReadablePacketRegistry packetRegistry = ReadablePacketRegistry.of(ServerPackets.MessageRequest.class);
        serverNetwork = NetworkFactory.newDefaultAsyncServerNetwork(packetRegistry, AcceptHandler.newDefault(WAITED_CLIENT::complete));
        serverNetwork.bind(SERVER_ADDRESS);
        clientNetwork = NetworkFactory.newDefaultAsyncClientNetwork(ReadablePacketRegistry.of(ClientPackets.MessageResponse.class));
        clientNetwork.asyncConnect(SERVER_ADDRESS);
    }

    @Test
    public void sendFromClientToServer() {

        final Server server = clientNetwork.getCurrentServer();

        Assertions.assertNotNull(server);

        checkClientMessage = new CompletableFuture<>();

        server.sendPacket(new ClientPackets.MessageRequest("Test client message"));

        final String received = Utils.get(() -> checkClientMessage.get());

        Assertions.assertEquals("Test client message", received);
    }

    @Test
    public void sendFromServerToClient() {

        final Client client = Utils.get(WAITED_CLIENT::get);

        checkServerMessage = new CompletableFuture<>();

        client.sendPacket(new ServerPackets.MessageResponse("Test server message"));

        final String received = Utils.get(() -> checkServerMessage.get());

        Assertions.assertEquals("Test server message", received);
    }

    @AfterAll
    public static void shutdownNetwork() {
        clientNetwork.shutdown();
        serverNetwork.shutdown();
    }
}
