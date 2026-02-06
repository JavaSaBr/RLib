package javasabr.rlib.network.packet;

import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.network.Connection;

/**
 * Interface for ID-based readable network packets that can create new instances.
 *
 * @param <C> the connection type
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface IdBasedReadableNetworkPacket<C extends Connection<C>>
    extends ReadableNetworkPacket<C>, IdBasedNetworkPacket<C> {

  /**
   * Creates a new instance of this packet type.
   *
   * @return a new instance
   * @since 10.0.0
   */
  default IdBasedReadableNetworkPacket<C> newInstance() {
    return ClassUtils.newInstance(getClass());
  }
}
