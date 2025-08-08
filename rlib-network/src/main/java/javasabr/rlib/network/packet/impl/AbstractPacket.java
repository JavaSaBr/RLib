package javasabr.rlib.network.packet.impl;

import static javasabr.rlib.network.util.NetworkUtils.hexDump;

import java.nio.ByteBuffer;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import javasabr.rlib.network.packet.Packet;

/**
 * The base implementation of {@link Packet}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractPacket implements Packet {

  protected static final Logger LOGGER = LoggerManager.getLogger(Packet.class);

  /**
   * Handle the exception.
   *
   * @param buffer the data buffer.
   * @param exception the exception.
   */
  protected void handleException(ByteBuffer buffer, Exception exception) {
    LOGGER.warning(exception);

    if (buffer.isDirect()) {
      var array = new byte[buffer.limit()];
      buffer.get(array, 0, buffer.limit());
      LOGGER.warning(getName() + " -> buffer: " + buffer + "\n" + hexDump(array, array.length));
    } else {
      LOGGER.warning(getName() + " -> buffer: " + buffer + "\n" + hexDump(buffer.array(), buffer.limit()));
    }
  }

  @Override
  public String getName() {
    return getClass().getSimpleName();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" + "name='" + getName() + '\'' + '}';
  }
}
