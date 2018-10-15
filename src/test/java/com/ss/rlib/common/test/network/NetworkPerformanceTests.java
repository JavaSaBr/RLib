package com.ss.rlib.common.test.network;

import com.ss.rlib.common.concurrent.util.ThreadUtils;
import com.ss.rlib.common.network.AsyncNetwork;
import com.ss.rlib.common.network.ConnectionOwner;
import com.ss.rlib.common.network.NetworkConfig;
import com.ss.rlib.common.network.NetworkFactory;
import com.ss.rlib.common.network.annotation.PacketDescription;
import com.ss.rlib.common.network.client.ClientNetwork;
import com.ss.rlib.common.network.client.ConnectHandler;
import com.ss.rlib.common.network.packet.ReadablePacketRegistry;
import com.ss.rlib.common.network.packet.impl.AbstractReadablePacket;
import com.ss.rlib.common.network.packet.impl.AbstractWritablePacket;
import com.ss.rlib.common.network.server.AcceptHandler;
import com.ss.rlib.common.network.server.ServerNetwork;
import com.ss.rlib.common.network.server.client.Client;
import com.ss.rlib.common.network.server.client.ClientConnection;
import com.ss.rlib.common.network.server.client.impl.DefaultClient;
import com.ss.rlib.common.network.server.client.impl.DefaultClientConnection;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.Utils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The test ot test performance of reusable packets of network.
 *
 * @author JavaSaBr
 */
public class NetworkPerformanceTests {

    private static final InetSocketAddress SERVER_ADDRESS = new InetSocketAddress(Utils.getFreePort(3344));

    private static class TestClient extends DefaultClient {

        public TestClient(@NotNull ClientConnection connection) {
            super(connection);
        }
    }

    private static class TestClientConnection extends DefaultClientConnection {

        public TestClientConnection(@NotNull ServerNetwork network, @NotNull AsynchronousSocketChannel channel) {
            super(network, channel);
        }
    }

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);

    private static final int CLIENT_COUNT = 120;
    private static final int CLIENT_PACKETS_PER_CLIENT = 1400;

    private static final AtomicLong RECEIVED_SERVER_PACKETS = new AtomicLong(0);
    private static final AtomicLong SENT_SERVER_PACKETS = new AtomicLong(0);
    private static final AtomicLong WROTE_SERVER_PACKETS = new AtomicLong(0);
    private static final AtomicLong RECEIVED_CLIENT_PACKETS = new AtomicLong(0);

    public static class ServerPackets {

        /**
         * It's a packet which a server receives from a client.
         */
        @PacketDescription(id = 1)
        public static class MessageRequest extends AbstractReadablePacket {

            @Override
            protected void readImpl(@NotNull ConnectionOwner owner, @NotNull ByteBuffer buffer) {
                var message = readString(buffer);
                RECEIVED_CLIENT_PACKETS.incrementAndGet();
                EXECUTOR_SERVICE.execute(() -> {
                    AVAILABLE_CLIENTS.runInReadLock(new MessageResponse(message), (clients, packet) -> {
                        clients.forEach(packet, (client, messageResponse) -> {
                            client.sendPacket(messageResponse);
                            SENT_SERVER_PACKETS.incrementAndGet();
                        });
                    });
                });
            }
        }

        /**
         * It's a packet which a server sends to a client.
         */
        @PacketDescription(id = 2)
        public static class MessageResponse extends AbstractWritablePacket {

            @NotNull
            private final String message;

            public MessageResponse(@NotNull String message) {
                this.message = message;
            }

            @Override
            protected void writeImpl(@NotNull ByteBuffer buffer) {
                super.writeImpl(buffer);
                writeString(buffer, message);
                WROTE_SERVER_PACKETS.incrementAndGet();
            }
        }
    }

    public static class ClientPackets {

        /**
         * It's a packet which a client sends to a server.
         */
        @PacketDescription(id = 1)
        public static class MessageRequest extends AbstractWritablePacket {

            @NotNull
            private final String message;

            public MessageRequest(@NotNull String message) {
                this.message = message;
            }

            @Override
            protected void writeImpl(@NotNull ByteBuffer buffer) {
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
            protected void readImpl(@NotNull ConnectionOwner owner, @NotNull ByteBuffer buffer) {
                readString(buffer);
                RECEIVED_SERVER_PACKETS.incrementAndGet();
            }
        }
    }

    private static final Array<ClientNetwork> CLIENT_NETWORKS = ArrayFactory.newArray(ClientNetwork.class);
    private static final ConcurrentArray<Client> AVAILABLE_CLIENTS = ArrayFactory.newConcurrentStampedLockArray(Client.class);

    private static ServerNetwork serverNetwork;

    @BeforeAll
    public static void createNetworks() throws IOException {

        var serverRegistry = ReadablePacketRegistry.of(ServerPackets.MessageRequest.class);
        var clientRegistry = ReadablePacketRegistry.of(ClientPackets.MessageResponse.class);

        serverNetwork = NetworkFactory.newDefaultAsyncServerNetwork(NetworkConfig.DEFAULT_SERVER, serverRegistry,
                AcceptHandler.newSimple(TestClientConnection::new, TestClient::new,
                        client -> AVAILABLE_CLIENTS.runInWriteLock(client, Array::add)));

        serverNetwork.bind(SERVER_ADDRESS);
        serverNetwork.setDestroyedHandler(client -> AVAILABLE_CLIENTS.runInWriteLock(client, Array::fastRemove));

        for (int i = 0; i < CLIENT_COUNT; i++) {

            var clientNetwork = NetworkFactory.newDefaultAsyncClientNetwork(NetworkConfig.DEFAULT_CLIENT,
                    clientRegistry, ConnectHandler.newDefault());

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

        for (var clientNetwork : CLIENT_NETWORKS) {

            var thread = new Thread(() -> {

                var server = ObjectUtils.notNull(clientNetwork.getCurrentServer());
                var random = ThreadLocalRandom.current();

                for (int i = 0; i < CLIENT_PACKETS_PER_CLIENT; i++) {
                    server.sendPacket(new ClientPackets.MessageRequest(StringUtils.generate(random.nextInt(10, 70))));
                }

            }, "SendPacketThread_" + order++);
            thread.start();
        }

        var totalClientPackets = CLIENT_COUNT * CLIENT_PACKETS_PER_CLIENT;
        var totalServerPackets = CLIENT_COUNT * CLIENT_PACKETS_PER_CLIENT * CLIENT_COUNT;

        for (int i = 0; i < 100; i++) {

            ThreadUtils.sleep(500);

            var receiverClientPackets = RECEIVED_CLIENT_PACKETS.get();
            if (receiverClientPackets < totalClientPackets) {
                continue;
            }

            var receivedServerPackets = RECEIVED_SERVER_PACKETS.get();
            if (receivedServerPackets < totalServerPackets) {
               continue;
            }

            break;
        }

        Assertions.assertEquals(totalClientPackets, RECEIVED_CLIENT_PACKETS.get(),
                "Expected packets from clients: " + totalClientPackets + ", received: " + RECEIVED_CLIENT_PACKETS);
        Assertions.assertEquals(totalServerPackets, RECEIVED_SERVER_PACKETS.get(),
                "Expected packets from server: " + totalServerPackets + ", received: " + RECEIVED_SERVER_PACKETS +
                        ", sent : " + SENT_SERVER_PACKETS + ", wrote " + WROTE_SERVER_PACKETS.get());
    }

    @AfterAll
    public static void shutdownNetwork() {
        CLIENT_NETWORKS.forEach(AsyncNetwork::shutdown);
        serverNetwork.shutdown();
    }
}
