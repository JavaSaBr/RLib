package com.ss.rlib.network.client;

import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * Interface to implement a client network.
 *
 * @author JavaSaBr
 */
public interface ClientNetwork<C extends Connection<?, ?>> extends Network<C> {

    /**
     * Connect to a server by the address.
     *
     * @param serverAddress the sever address.
     * @return the future with result connection.
     */
    @NotNull CompletableFuture<C> connect(@NotNull InetSocketAddress serverAddress);

    /**
     * Get a current connection to a server or null.
     *
     * @return the current connection or null.
     */
    @Nullable C getCurrentConnection();
}
