package javasabr.rlib.network.packet;

import javasabr.rlib.network.Connection;

/**
 * Base interface for all network packets.
 *
 * @param <C> the connection type
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface NetworkPacket<C extends Connection<C>> {

  /**
   * Gets the name of this packet.
   *
   * @return the packet name
   * @since 10.0.0
   */
  String name();
}
