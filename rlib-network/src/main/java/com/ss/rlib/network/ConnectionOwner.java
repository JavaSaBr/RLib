package com.ss.rlib.network;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a connection's owner.
 *
 * @author JavaSaBr
 */
public interface ConnectionOwner {

    /**
     * Get a network connection to this owner.
     *
     * @return the network connection to this owner.
     */
    @NotNull AsyncConnection getConnection();

    /**
     * Set a network connection to this owner.
     *
     * @param connection the network connection to this owner.
     */
    void setConnection(@NotNull AsyncConnection connection);

    /**
     * Handle the closed connection to this connection's owner.
     */
    void connectionWasClosed();
}
