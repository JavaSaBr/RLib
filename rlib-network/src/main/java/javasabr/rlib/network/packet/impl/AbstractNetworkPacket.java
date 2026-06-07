package javasabr.rlib.network.packet.impl;

import static javasabr.rlib.network.util.NetworkUtils.hexDump;

import java.nio.ByteBuffer;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.packet.NetworkPacket;
import lombok.CustomLog;

/**
 * The base implementation of {@link NetworkPacket}.
 *
 * @author JavaSaBr
 */
@CustomLog
public abstract class AbstractNetworkPacket<C extends Connection<C>> implements NetworkPacket<C> {

  /**
   * Handles packet data exception.
   */
  protected void handleException(C connection, ByteBuffer buffer, Exception exception) {
    log.warn(exception);
    if (!log.warnEnabled()) {
      return;
    }

    String hexDump;

    if (buffer.isDirect()) {
      var array = new byte[buffer.remaining()];
      buffer.get(array, buffer.position(), buffer.limit());
      hexDump = hexDump(array, array.length);
    } else {
      hexDump = hexDump(buffer.array(), buffer.position(), buffer.limit());
    }

    log.warn(connection.remoteAddress(), name(), buffer, hexDump,
        "[%s] Hexdump for:[%s] -> buffer:[%s]\n[%s]"::formatted);
  }

  @Override
  public String name() {
    return getClass().getSimpleName();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" + "name='" + name() + '\'' + '}';
  }
}
