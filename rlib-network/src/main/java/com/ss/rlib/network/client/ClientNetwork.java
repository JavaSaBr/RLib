package com.ss.rlib.network.client;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.network.AsyncNetwork;
import com.ss.rlib.network.client.server.Server;
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
     * Async connect to a server by the address.
     *
     * @param serverAddress the sever address.
     */
    void asyncConnect(@NotNull InetSocketAddress serverAddress);

    /**
     * Connect to a server by the address.
     *
     * @param serverAddress the sever address.
     * @return the connected server.
     */
    @NotNull Server connect(@NotNull InetSocketAddress serverAddress);

    /**
     * Get the network channel to a server.
     *
     * @return the network channel to a server.
     */
    @Nullable AsynchronousSocketChannel getChannel();

    /**
     * Get the current server.
     *
     * @return the current server or null.
     */
    @Nullable Server getCurrentServer();

    /**
     * Required the current server.
     *
     * @return the current server.
     */
    default @NotNull Server requireCurrentServer() {
        return notNull(getCurrentServer());
    }

    /**
     * Set the current server.
     *
     * @param server the current server.
     */
    void setCurrentServer(@Nullable Server server);
}
