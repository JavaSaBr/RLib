package com.ss.rlib.network.client;

import com.ss.rlib.network.AsyncNetwork;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * The interface to implement a client network.
 *
 * @author JavaSaBr
 */
public interface ClientNetwork extends AsyncNetwork {

    /**
     * Connect to a server by the address.
     *
     * @param serverAddress the sever address.
     */
    void connect(@NotNull InetSocketAddress serverAddress);

    /**
     * Get the network channel to a server.
     *
     * @return the network channel to a server.
     */
    @Nullable AsynchronousSocketChannel getChannel();
}
