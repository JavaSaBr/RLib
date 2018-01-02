package com.ss.rlib.test.network;

import com.ss.rlib.network.NetworkFactory;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.packet.ReadablePacketRegistry;
import com.ss.rlib.network.server.ServerNetwork;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * The test ot test network.
 *
 * @author JavaSaBr
 */
public class NetworkTests {

    @NotNull
    private static final InetSocketAddress SERVER_ADDRESS = new InetSocketAddress(2222);

    private ServerNetwork serverNetwork;
    private ClientNetwork clientNetwork;

    @BeforeAll
    public void createServerNetwork() throws IOException {
        serverNetwork = NetworkFactory.newDefaultAsyncServerNetwork(ReadablePacketRegistry.of());
        serverNetwork.bind(SERVER_ADDRESS);
        clientNetwork = NetworkFactory.newDefaultAsyncClientNetwork(ReadablePacketRegistry.of());
        clientNetwork.connect(SERVER_ADDRESS);
    }

    @BeforeAll
    public void createClientNetwork() {

    }

    @Test
    public void sendFromClientToServer() {

    }

    @Test
    public void sendFromServerToClient() {

    }

    @AfterAll
    public void shutdownServerNetwork() {
        serverNetwork.shutdown();
    }

    @AfterAll
    public void shutdownClientNetwork() {
        clientNetwork.shutdown();
    }
}
