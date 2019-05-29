package com.ss.rlib.network.test;


/**
 * The test ot test network.
 *
 * @author JavaSaBr
 */
public class NetworkTests {

    /*@NotNull
    private static final InetSocketAddress SERVER_ADDRESS = new InetSocketAddress(2222);

    @NotNull
    private static final CompletableFuture<Client> WAITED_CLIENT = new CompletableFuture<>();

    private static volatile CompletableFuture<String> checkClientMessage;
    private static volatile CompletableFuture<String> checkServerMessage;

    public static class ServerPackets {

        @PacketDescription(id = 1)
        public static class MessageRequest extends AbstractReadablePacket {

            @Override
            protected void readImpl(@NotNull final ConnectionOwner owner, @NotNull final ByteBuffer buffer) {
                final String message = readString(buffer);
                System.out.println("Server: received \"" + message + "\"");
                checkClientMessage.complete(message);
            }
        }

        @PacketDescription(id = 2)
        public static class MessageResponse extends AbstractWritablePacket {

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

        @PacketDescription(id = 1)
        public static class MessageRequest extends AbstractWritablePacket {

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

        @PacketDescription(id = 2)
        public static class MessageResponse extends AbstractReadablePacket {

            @Override
            protected void readImpl(@NotNull final ConnectionOwner owner, @NotNull final ByteBuffer buffer) {
                final String message = readString(buffer);
                System.out.println("client: received \"" + message + "\"");
                checkServerMessage.complete(message);
            }
        }
    }

    private static ServerNetwork serverNetwork;
    private static ClientNetwork clientNetwork;

   // @BeforeAll
    public static void createNetwork() throws IOException {
        final ReadablePacketRegistry packetRegistry = ReadablePacketRegistry.of(ServerPackets.MessageRequest.class);
        serverNetwork = NetworkFactory.newDefaultAsyncServerNetwork(packetRegistry, WAITED_CLIENT::complete);
        serverNetwork.bind(SERVER_ADDRESS);
        clientNetwork = NetworkFactory.newDefaultAsyncClientNetwork(ReadablePacketRegistry.of(ClientPackets.MessageResponse.class));
        clientNetwork.connect(SERVER_ADDRESS);
    }

  //  @Test
    public void sendFromClientToServer() {

        final Server server = clientNetwork.getCurrentServer();

        Assertions.assertNotNull(server);

        checkClientMessage = new CompletableFuture<>();

        server.getConnection().sendPacket(new ClientPackets.MessageRequest("Test client message"));

        final String received = Utils.get(() -> checkClientMessage.get());

        Assertions.assertEquals("Test client message", received);
    }

 //   @Test
    public void sendFromServerToClient() {

        final Client client = Utils.get(WAITED_CLIENT::get);

        checkServerMessage = new CompletableFuture<>();

        client.getConnection().sendPacket(new ServerPackets.MessageResponse("Test server message"));

        final String received = Utils.get(() -> checkServerMessage.get());

        Assertions.assertEquals("Test server message", received);
    }

   // @AfterAll
    public static void shutdownNetwork() {
        clientNetwork.shutdown();
        serverNetwork.shutdown();
    }*/
}
