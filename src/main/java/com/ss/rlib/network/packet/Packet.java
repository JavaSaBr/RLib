package com.ss.rlib.network.packet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface for implementing a network packet.
 *
 * @author JavaSaBr
 */
public interface Packet {

    /**
     * Gets name.
     *
     * @return the name of this packet.
     */
    @NotNull
    String getName();

    /**
     * Gets owner.
     *
     * @return the owner of this packet.
     */
    @Nullable
    Object getOwner();

    /**
     * Sets the new owner of this packet.
     *
     * @param owner the new owner of this packet.
     */
    void setOwner(@Nullable Object owner);
}
