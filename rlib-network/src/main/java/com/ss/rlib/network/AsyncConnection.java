package com.ss.rlib.network;

import com.ss.rlib.network.packet.WritablePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface to implement an async connection.
 *
 * @author JavaSaBr
 */
public interface AsyncConnection {

    @Nullable ConnectionOwner getOwner();

    void setOwner(@Nullable ConnectionOwner owner);

    @NotNull AsyncNetwork getNetwork();


    void close();

    /**
     * Get time of last activity.
     *
     * @return the time of last activity.
     */
    long getLastActivity();

    boolean isClosed();

    /**
     * Send the packet to connection owner.
     *
     * @param packet the sendable packet.
     */
    void sendPacket(@NotNull WritablePacket packet);

    /**
     * Activate the process of receiving packets.
     */
    void startRead();
}
