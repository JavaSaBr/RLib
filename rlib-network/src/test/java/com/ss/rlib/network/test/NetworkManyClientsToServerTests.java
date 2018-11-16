package com.ss.rlib.network.test;

import com.ss.rlib.common.concurrent.util.ThreadUtils;
import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.Utils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import com.ss.rlib.network.AsyncNetwork;
import com.ss.rlib.network.ConnectionOwner;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.NetworkFactory;
import com.ss.rlib.network.annotation.PacketDescription;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.packet.ReadablePacketRegistry;
import com.ss.rlib.network.packet.impl.AbstractReadablePacket;
import com.ss.rlib.network.packet.impl.AbstractWritablePacket;
import com.ss.rlib.network.server.AcceptHandler;
import com.ss.rlib.network.server.ServerNetwork;
import com.ss.rlib.network.server.client.Client;
import com.ss.rlib.network.server.client.impl.DefaultClient;
import com.ss.rlib.network.server.client.impl.DefaultClientConnection;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Test to check creating many clients to one server.
 *
 * @author JavaSaBr
 */
public class NetworkManyClientsToServerTests extends NetworkTestConfig {

    private static final Logger LOGGER = LoggerManager.getLogger(NetworkManyClientsToServerTests.class);

    private static final InetSocketAddress SERVER_ADDRESS = new InetSocketAddress(Utils.getFreePort(3344));

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);

    private static final int CLIENT_COUNT = 20000;
    private static final int PACKETS_FOR_CLIENT = 100;

    private static final AtomicLong WROTE_SERVER_PACKETS = new AtomicLong(0);
    private static final AtomicLong RECEIVED_CLIENT_PACKETS = new AtomicLong(0);

    public static class ServerPackets {

        @PacketDescription(id = 2)
        public static class Message extends AbstractWritablePacket {

            @NotNull
            private final String message;

            public Message(@NotNull String message) {
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

        @PacketDescription(id = 2)
        public static class Message extends AbstractReadablePacket {

            @Override
            protected void readImpl(@NotNull ConnectionOwner owner, @NotNull ByteBuffer buffer) {
                LOGGER.info("received: " + readString(buffer));
                RECEIVED_CLIENT_PACKETS.incrementAndGet();
            }
        }
    }

    private static final ConcurrentArray<ClientNetwork> CLIENT_NETWORKS =
        ArrayFactory.newConcurrentStampedLockArray(ClientNetwork.class);

    private static final ConcurrentArray<Client> AVAILABLE_CLIENTS =
        ArrayFactory.newConcurrentStampedLockArray(Client.class);

    private static ServerNetwork serverNetwork;

    @BeforeAll
    public static void createNetworks() throws IOException {

        var clientChannelGroup = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(4));
        var clientRegistry = ReadablePacketRegistry.of(ClientPackets.Message.class);

        serverNetwork = NetworkFactory.newDefaultAsyncServerNetwork(
            NetworkConfig.DEFAULT_SERVER,
            ReadablePacketRegistry.empty(),
            AcceptHandler.newSimple(DefaultClientConnection::new, DefaultClient::new,
                client -> AVAILABLE_CLIENTS.runInWriteLock(client, Array::add)
            )
        );

        serverNetwork.bind(SERVER_ADDRESS);
        serverNetwork.setDestroyedHandler(client -> AVAILABLE_CLIENTS.runInWriteLock(client, Array::fastRemove));

        for (int i = 0; i < CLIENT_COUNT; i++) {

            var clientNetwork = NetworkFactory.newDefaultAsyncClientNetwork(
                clientChannelGroup,
                clientRegistry
            );

            clientNetwork.connect(SERVER_ADDRESS);

            CLIENT_NETWORKS.add(clientNetwork);
        }

        while (AVAILABLE_CLIENTS.size() < CLIENT_COUNT) {
            LOGGER.info("connected clients: " + AVAILABLE_CLIENTS.size());
            ThreadUtils.sleep(1000);
        }
    }

    @Test
    public void sendPackets() {

        for (var client : AVAILABLE_CLIENTS) {

            EXECUTOR_SERVICE.execute(() -> {

                var random = ThreadLocalRandom.current();

                for (int i = 0; i < PACKETS_FOR_CLIENT; i++) {
                    client.sendPacket(new ServerPackets.Message(
                        StringUtils.generate(random.nextInt(10, 70))
                    ));
                }
            });
        }

        var totalReceivedPackets = CLIENT_COUNT * PACKETS_FOR_CLIENT;

        for (int i = 0; i < 100; i++) {

            ThreadUtils.sleep(500);

            var receiverClientPackets = RECEIVED_CLIENT_PACKETS.get();

            LOGGER.info("received: " + receiverClientPackets + " packets...");
            LOGGER.info("wrote: " + WROTE_SERVER_PACKETS + " packets...");

            if (receiverClientPackets < totalReceivedPackets) {
                continue;
            }

            break;
        }

        Assertions.assertEquals(totalReceivedPackets, RECEIVED_CLIENT_PACKETS.get(),
                "Expected received packets from server: " + totalReceivedPackets + ", " +
                    "received: " + RECEIVED_CLIENT_PACKETS.get());
    }

    @AfterAll
    public static void shutdownNetwork() {
        CLIENT_NETWORKS.forEach(AsyncNetwork::shutdown);
        serverNetwork.shutdown();
    }
}
