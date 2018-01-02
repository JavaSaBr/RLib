package com.ss.rlib.network.client;

import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

import com.ss.rlib.network.AsyncNetwork;

/**
 * The interface to implement a client network.
 *
 * @author JavaSaBr
 */
public interface ClientNetwork extends AsyncNetwork {

    /**
     * Connect a client to a server.
     *
     * @param serverAddress the sever address.
     */
    void connect(@NotNull InetSocketAddress serverAddress);
}
