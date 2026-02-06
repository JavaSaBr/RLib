package javasabr.rlib.network.packet;

import javasabr.rlib.network.Connection;
import javasabr.rlib.network.annotation.NetworkPacketDescription;

/**
 * Interface for network packets that have a unique identifier.
 *
 * @param <C> the connection type
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface IdBasedNetworkPacket<C extends Connection<C>> extends NetworkPacket<C> {

  /**
   * Gets the ID of this packet from its {@link NetworkPacketDescription} annotation.
   *
   * @return the packet type's ID
   * @since 10.0.0
   */
  default int packetId() {
    return getClass()
        .getAnnotation(NetworkPacketDescription.class)
        .id();
  }
}
