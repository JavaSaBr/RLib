package javasabr.rlib.network.packet;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a network packet.
 *
 * @author JavaSaBr
 */
public interface Packet {

    /**
     * Get packet's name.
     *
     * @return the packet's name.
     */
    @NotNull String getName();
}
