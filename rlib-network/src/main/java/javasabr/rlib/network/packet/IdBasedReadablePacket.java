package javasabr.rlib.network.packet;

import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.network.Connection;

/**
 * @author JavaSaBr
 */
public interface IdBasedReadablePacket<S extends IdBasedReadablePacket<S>> extends ReadablePacket, IdBasedPacket {

  /**
   * Create a new instance of this type.
   *
   * @return the new instance of this type.
   */
  default S newInstance() {
    return ClassUtils.newInstance(getClass());
  }

  /**
   * Execute a logic of this packet.
   *
   * @param connection the owner's connection.
   */
  default void execute(Connection<?, ?> connection) {

  }
}
