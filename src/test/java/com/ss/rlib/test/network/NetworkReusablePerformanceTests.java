package com.ss.rlib.test.network;

import static com.ss.rlib.util.ObjectUtils.notNull;
import com.ss.rlib.concurrent.util.ThreadUtils;
import com.ss.rlib.network.AsyncNetwork;
import com.ss.rlib.network.ConnectionOwner;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.NetworkFactory;
import com.ss.rlib.network.annotation.PacketDescription;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.client.server.Server;
import com.ss.rlib.network.packet.ReadablePacketRegistry;
import com.ss.rlib.network.packet.impl.AbstractReadablePacket;
import com.ss.rlib.network.packet.impl.AbstractReusableWritablePacket;
import com.ss.rlib.network.packet.impl.AbstractWritablePacket;
import com.ss.rlib.network.server.AcceptHandler;
import com.ss.rlib.network.server.ServerNetwork;
import com.ss.rlib.network.server.client.Client;
import com.ss.rlib.util.ArrayUtils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import com.ss.rlib.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The test ot test performance of reusable packets of network.
 *
 * @author JavaSaBr
 */
public class NetworkReusablePerformanceTests {

    @NotNull
    private static final InetSocketAddress SERVER_ADDRESS = new InetSocketAddress(3434);

    private static final NetworkConfig SERVER_CONFIG = new NetworkConfig() {
        @Override
        public int getGroupSize() {
            return 10;
        }

        @Override
        public boolean isVisibleReadException() {
            return true;
        }

        @Override
        public boolean isVisibleWriteException() {
            return true;
        }
    };

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);

    private static final int CLIENT_COUNT = 100;
    private static final int CLIENT_PACKETS_PER_CLIENT = 1000;

    @NotNull
    private static final AtomicLong RECEIVED_SERVER_PACKETS = new AtomicLong(0);

    @NotNull
    private static final AtomicLong RECEIVED_CLIENT_PACKETS = new AtomicLong(0);

    public static class ServerPackets {

        /**
         * It's a packet which a server receives from a client.
         */
        @PacketDescription(id = 1)
        public static class MessageRequest extends AbstractReadablePacket {

            @Override
            protected void readImpl(@NotNull final ConnectionOwner owner, @NotNull final ByteBuffer buffer) {
                final String message = readString(buffer);
                RECEIVED_CLIENT_PACKETS.incrementAndGet();
                /*EXECUTOR_SERVICE.execute(() -> {

                    final MessageResponse response = MessageResponse.newInstance(message);
                    response.increaseSends();

                    ArrayUtils.runInReadLock(AVAILABLE_CLIENTS, response, (clients, packet) -> {
                        packet.increaseSends(clients.size());
                        clients.forEach(packet, ConnectionOwner::sendPacket);
                    });

                    response.complete();
                });*/
            }
        }

        /**
         * It's a packet which a server sends to a client.
         */
        @PacketDescription(id = 2)
        public static class MessageResponse extends AbstractReusableWritablePacket {

            private static final MessageResponse EXAMPLE = new MessageResponse();

            public static @NotNull MessageResponse newInstance(@NotNull final String message) {
                final MessageResponse packet = EXAMPLE.newInstance();
                packet.notifyFinishedPreparing();
                packet.message = message;
                packet.notifyFinishedPreparing();
                return packet;
            }

            @NotNull
            private volatile String message;

            public MessageResponse() {
                message = "";
            }

            @Override
            protected void writeImpl(@NotNull final ByteBuffer buffer) {
                super.writeImpl(buffer);
                writeString(buffer, message);
            }

            @Override
            public void free() {
                message = "";
            }
        }
    }

    public static class ClientPackets {

        /**
         * It's a packet which a client sends to a server.
         */
        @PacketDescription(id = 1)
        public static class MessageRequest extends AbstractReusableWritablePacket {

            public static final AtomicLong INSTANCES = new AtomicLong(0);

            private static final MessageRequest EXAMPLE = new MessageRequest();

            public static @NotNull MessageRequest newInstance(@NotNull final String message) {
                final MessageRequest packet = EXAMPLE.newInstance();
                packet.notifyStartedPreparing();
                packet.message = message;
                packet.notifyFinishedPreparing();
                return packet;
            }

            @NotNull
            private String message;

            public MessageRequest() {
                INSTANCES.incrementAndGet();
                message = "";
            }

            @Override
            protected void writeImpl(@NotNull final ByteBuffer buffer) {
                super.writeImpl(buffer);
                writeString(buffer, message);
            }

            @Override
            public void free() {
                message = "";
            }
        }

        /**
         * It's a packet which a client sends to a server.
         */
        @PacketDescription(id = 1)
        public static class MessageNotReusableRequest extends AbstractWritablePacket {

            @NotNull
            private final String message;

            public MessageNotReusableRequest(@NotNull final String message) {
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
            protected void readImpl(@NotNull final ConnectionOwner owner, @NotNull final ByteBuffer buffer) {
                readString(buffer);
                RECEIVED_SERVER_PACKETS.getAndIncrement();
            }
        }
    }

    private static final Array<ClientNetwork> CLIENT_NETWORKS = ArrayFactory.newArray(ClientNetwork.class);
    private static final ConcurrentArray<Client> AVAILABLE_CLIENTS = ArrayFactory.newConcurrentStampedLockArray(Client.class);

    private static ServerNetwork serverNetwork;

    @BeforeAll
    public static void createNetworks() throws IOException {

        final ReadablePacketRegistry serverRegistry = ReadablePacketRegistry.of(ServerPackets.MessageRequest.class);
        final ReadablePacketRegistry clientRegistry = ReadablePacketRegistry.of(ClientPackets.MessageResponse.class);

        serverNetwork = NetworkFactory.newDefaultAsyncServerNetwork(SERVER_CONFIG, serverRegistry,
                AcceptHandler.newDefault(client -> ArrayUtils.runInWriteLock(AVAILABLE_CLIENTS, client, Array::add)));
        serverNetwork.bind(SERVER_ADDRESS);
        serverNetwork.setDestroyedHandler(client -> ArrayUtils.runInWriteLock(AVAILABLE_CLIENTS, client, Array::fastRemove));

        for (int i = 0; i < CLIENT_COUNT; i++) {

            final ClientNetwork clientNetwork = NetworkFactory.newDefaultAsyncClientNetwork(clientRegistry);
            clientNetwork.asyncConnect(SERVER_ADDRESS);

            CLIENT_NETWORKS.add(clientNetwork);
        }

        while (AVAILABLE_CLIENTS.size() < CLIENT_COUNT) {
            ThreadUtils.sleep(1000);
        }
    }

    @Test
    public void sendPackets() {

        int order = 1;

        for (final ClientNetwork clientNetwork : CLIENT_NETWORKS) {

            final Thread thread = new Thread(() -> {

                final Server server = notNull(clientNetwork.getCurrentServer());

                for (int i = 0; i < CLIENT_PACKETS_PER_CLIENT; i++) {
                    //server.sendPacket(ClientPackets.MessageRequest.newInstance("Message_" + i));
                    server.sendPacket(new ClientPackets.MessageNotReusableRequest("Message"));
                }

            }, "SendPacketThread_" + order++);
            thread.start();
        }

        final int totalClientPackets = CLIENT_COUNT * CLIENT_PACKETS_PER_CLIENT;
        final int totalServerPackets = CLIENT_COUNT * CLIENT_PACKETS_PER_CLIENT * CLIENT_COUNT;

        for (int i = 0; i < 30; i++) {
            ThreadUtils.sleep(500);

            final long receiverClientPackets = RECEIVED_CLIENT_PACKETS.get();
            if (receiverClientPackets < totalClientPackets) {
                continue;
            }

            final long receivedServerPackets = RECEIVED_SERVER_PACKETS.get();
            if (receivedServerPackets < totalServerPackets) {
               // continue;
            }

            break;
        }

        final long instances = ClientPackets.MessageRequest.INSTANCES.get();

        Assertions.assertEquals(totalClientPackets, RECEIVED_CLIENT_PACKETS.get(),
                "Expected packets from clients: " + totalClientPackets + ", received: " + RECEIVED_CLIENT_PACKETS);
        Assertions.assertEquals(totalServerPackets, RECEIVED_SERVER_PACKETS.get(),
                "Expected packets from server: " + totalServerPackets + ", received: " + RECEIVED_SERVER_PACKETS);
    }

    @AfterAll
    public static void shutdownNetwork() {
        CLIENT_NETWORKS.forEach(AsyncNetwork::shutdown);
        serverNetwork.shutdown();
    }
}
