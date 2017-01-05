package rlib.network.packet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface for implementing a network packet.
 *
 * @author JavaSaBr
 */
public interface Packet {

    /**
     * @return the name of this packet.
     */
    @NotNull
    String getName();

    /**
     * @return the owner of this packet.
     */
    @Nullable
    Object getOwner();

    /**
     * Sets the new owner of this packet.
     *
     * @param owner the new owner of this packet.
     */
    void setOwner(@NotNull Object owner);
}
