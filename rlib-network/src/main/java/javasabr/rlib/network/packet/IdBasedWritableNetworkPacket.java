package javasabr.rlib.network.packet;

import javasabr.rlib.network.Connection;

/**
 * Interface for ID-based writable network packets.
 *
 * @param <C> the connection type
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface IdBasedWritableNetworkPacket<C extends Connection<C>>
    extends WritableNetworkPacket<C>, IdBasedNetworkPacket<C> {
}
