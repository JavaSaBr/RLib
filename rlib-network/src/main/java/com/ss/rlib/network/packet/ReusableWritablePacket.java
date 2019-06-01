package com.ss.rlib.network.packet;

import com.ss.rlib.common.util.pools.Pool;
import com.ss.rlib.common.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a reusable writable packet.
 *
 * @author JavaSaBr
 */
public interface ReusableWritablePacket extends WritablePacket, Reusable {

    /**
     * Handle completion of packet sending.
     */
    void complete();

    /**
     * Force complete this packet.
     */
    void forceComplete();

    /**
     * Decrease sending count.
     */
    void decreaseSends();

    /**
     * Decrease sending count.
     *
     * @param count the count.
     */
    void decreaseSends(int count);

    /**
     * Increase sending count.
     */
    void increaseSends();

    /**
     * Increase sending count.
     *
     * @param count the count.
     */
    void increaseSends(int count);

    /**
     * Set the pool.
     *
     * @param pool the pool to store used packet.
     */
    void setPool(@NotNull Pool<ReusableWritablePacket> pool);

    default void notifyAddedToSend() {
        increaseSends();
    }
}
