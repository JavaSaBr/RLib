package com.ss.rlib.network;

import com.ss.rlib.network.packet.ReadablePacket;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

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
     * Handle the closed connection to this connection's owner.
     */
    void connectionWasClosed();

    /**
     * Add the handler of each read packet from this owner.
     *
     * @param handler the handler.
     * @param <P>     the readable packet's type.
     * @return the added handler.
     */
    <P extends ReadablePacket> @NotNull Consumer<P> addPacketHandler(@NotNull Consumer<P> handler);

    /**
     * Remove the handler.
     *
     * @param handler the handler.
     */
    void removePacketHandler(@NotNull Consumer<? extends ReadablePacket> handler);
}
