package com.ss.rlib.network.test;

/**
 * The test ot test performance of reusable packets of network.
 *
 * @author JavaSaBr
 */
public class NetworkReusablePerformanceTests {

   /* private static final InetSocketAddress SERVER_ADDRESS = new InetSocketAddress(3434);

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

    private static final AtomicLong RECEIVED_SERVER_PACKETS = new AtomicLong(0);
    private static final AtomicLong RECEIVED_CLIENT_PACKETS = new AtomicLong(0);

    public static class ServerPackets {

        @PacketDescription(id = 1)
        public static class MessageRequest extends AbstractReadablePacket {

            @Override
            protected void readImpl(@NotNull ConnectionOwner owner, @NotNull ByteBuffer buffer) {

                String message = readString(buffer);

                RECEIVED_CLIENT_PACKETS.incrementAndGet();

                EXECUTOR_SERVICE.execute(() -> {

                    MessageResponse response = MessageResponse.newInstance(message);
                    response.increaseSends();

                    AVAILABLE_CLIENTS.runInReadLock(response, (clients, packet) -> {
                        packet.increaseSends(clients.size());
                        clients.forEach(packet, (client, messageResponse) -> client.getConnection().sendPacket(messageResponse));
                    });

                    response.complete();
                });
            }
        }

        @PacketDescription(id = 2)
        public static class MessageResponse extends AbstractReusableWritablePacket {

            public static final AtomicLong INSTANCES = new AtomicLong(0);

            private static final MessageResponse EXAMPLE = new MessageResponse();

            public static @NotNull MessageResponse newInstance(@NotNull String message) {

                MessageResponse packet = EXAMPLE.newInstance();
                packet.notifyFinishedPreparing();
                packet.message = message;
                packet.notifyFinishedPreparing();

                return packet;
            }

            @NotNull
            private volatile String message;

            public MessageResponse() {
                INSTANCES.incrementAndGet();
                message = "";
            }

            @Override
            protected void writeImpl(@NotNull ByteBuffer buffer) {
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

        @PacketDescription(id = 1)
        public static class MessageRequest extends AbstractReusableWritablePacket {

            private static final MessageRequest EXAMPLE = new MessageRequest();

            public static @NotNull MessageRequest newInstance(@NotNull String message) {

                MessageRequest packet = EXAMPLE.newInstance();
                packet.notifyStartedPreparing();
                packet.message = message;
                packet.notifyFinishedPreparing();

                return packet;
            }

            @NotNull
            private String message;

            public MessageRequest() {
                message = "";
            }

            @Override
            protected void writeImpl(@NotNull ByteBuffer buffer) {
                super.writeImpl(buffer);
                writeString(buffer, message);
            }

            @Override
            public void free() {
                message = "";
            }
        }

        @PacketDescription(id = 1)
        public static class MessageNotReusableRequest extends AbstractWritablePacket {

            @NotNull
            private final String message;

            public MessageNotReusableRequest(@NotNull String message) {
                this.message = message;
            }

            @Override
            protected void writeImpl(@NotNull ByteBuffer buffer) {
                super.writeImpl(buffer);
                writeString(buffer, message);
            }
        }

        @PacketDescription(id = 2)
        public static class MessageResponse extends AbstractReadablePacket {

            @Override
            protected void readImpl(@NotNull ConnectionOwner owner, @NotNull ByteBuffer buffer) {
                readString(buffer);
                RECEIVED_SERVER_PACKETS.getAndIncrement();
            }
        }
    }

    private static final Array<ClientNetwork> CLIENT_NETWORKS =
            ArrayFactory.newArray(ClientNetwork.class);

    private static final ConcurrentArray<Client> AVAILABLE_CLIENTS =
            ArrayFactory.newConcurrentStampedLockArray(Client.class);

    private static ServerNetwork serverNetwork;

   // @BeforeAll
    public static void createNetworks() throws IOException {

        ReadablePacketRegistry serverRegistry = ReadablePacketRegistry.of(ServerPackets.MessageRequest.class);
        ReadablePacketRegistry clientRegistry = ReadablePacketRegistry.of(ClientPackets.MessageResponse.class);

        serverNetwork = NetworkFactory.newDefaultAsyncServerNetwork(SERVER_CONFIG, serverRegistry,
                AcceptHandler.newDefault(client -> AVAILABLE_CLIENTS.runInWriteLock(client, Array::add)));
        serverNetwork.bind(SERVER_ADDRESS);
        serverNetwork.setDestroyedHandler(client -> AVAILABLE_CLIENTS.runInWriteLock(client, Array::fastRemove));

        for (int i = 0; i < CLIENT_COUNT; i++) {

            ClientNetwork clientNetwork = NetworkFactory.newDefaultAsyncClientNetwork(clientRegistry);
            clientNetwork.asyncConnect(SERVER_ADDRESS);

            CLIENT_NETWORKS.add(clientNetwork);
        }

        while (AVAILABLE_CLIENTS.size() < CLIENT_COUNT) {
            ThreadUtils.sleep(1000);
        }
    }

   // @Test
    public void sendPackets() {

        int order = 1;

        for (ClientNetwork clientNetwork : CLIENT_NETWORKS) {

            final Thread thread = new Thread(() -> {

                Server server = ObjectUtils.notNull(clientNetwork.getCurrentServer());
                ThreadLocalRandom random = ThreadLocalRandom.current();

                for (int i = 0; i < CLIENT_PACKETS_PER_CLIENT; i++) {
                    //server.sendPacket(ClientPackets.MessageRequest.newInstance(StringUtils.generate(random.nextInt(10, 70))));
                    server.getConnection().sendPacket(new ClientPackets.MessageNotReusableRequest(StringUtils.generate(random.nextInt(10, 70))));
                }

            }, "SendPacketThread_" + order++);
            thread.start();
        }

        int totalClientPackets = CLIENT_COUNT * CLIENT_PACKETS_PER_CLIENT;
        int totalServerPackets = CLIENT_COUNT * CLIENT_PACKETS_PER_CLIENT * CLIENT_COUNT;

        for (int i = 0; i < 100; i++) {
            ThreadUtils.sleep(500);

            long receiverClientPackets = RECEIVED_CLIENT_PACKETS.get();

            if (receiverClientPackets < totalClientPackets) {
                continue;
            }

            long receivedServerPackets = RECEIVED_SERVER_PACKETS.get();

            if (receivedServerPackets < totalServerPackets) {
                continue;
            }

            break;
        }

        long instances = ServerPackets.MessageResponse.INSTANCES.get();

        System.out.println("Instances: " + instances + ", server packets: " + RECEIVED_SERVER_PACKETS);

        Assertions.assertEquals(totalClientPackets, RECEIVED_CLIENT_PACKETS.get(),
                "Expected packets from clients: " + totalClientPackets + ", received: " + RECEIVED_CLIENT_PACKETS);
        Assertions.assertEquals(totalServerPackets, RECEIVED_SERVER_PACKETS.get(),
                "Expected packets from server: " + totalServerPackets + ", received: " + RECEIVED_SERVER_PACKETS);
    }

   // @AfterAll
    public static void shutdownNetwork() {
        CLIENT_NETWORKS.forEach(Network::shutdown);
        serverNetwork.shutdown();
    }*/
}
