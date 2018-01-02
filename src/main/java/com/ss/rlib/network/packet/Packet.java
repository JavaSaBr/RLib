package com.ss.rlib.network.packet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface to implement a network packet.
 *
 * @author JavaSaBr
 */
public interface Packet {

    /**
     * Get the name.
     *
     * @return the name of this packet.
     */
    @NotNull String getName();

    /**
     * Get the owner.
     *
     * @return the owner of this packet.
     */
    @Nullable Object getOwner();

    /**
     * Set the new owner of this packet.
     *
     * @param owner the new owner of this packet.
     */
    void setOwner(@Nullable Object owner);
}
