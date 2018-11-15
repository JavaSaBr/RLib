package com.ss.rlib.network.packet;

import org.jetbrains.annotations.NotNull;

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
}
