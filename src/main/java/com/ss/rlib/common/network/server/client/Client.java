package com.ss.rlib.common.network.server.client;

import com.ss.rlib.common.network.ConnectionOwner;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a client.
 *
 * @author JavaSaBr
 */
public interface Client extends ConnectionOwner {

    /**
     * Handle successful connect.
     */
    void notifyConnected();

    @Override
    @NotNull ClientConnection getConnection();
}
