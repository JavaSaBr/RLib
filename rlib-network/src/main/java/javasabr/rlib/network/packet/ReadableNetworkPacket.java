package javasabr.rlib.network.packet;

import java.nio.ByteBuffer;
import javasabr.rlib.network.Connection;

/**
 * Interface for network packets that can be read from a byte buffer.
 *
 * @param <C> the connection type
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface ReadableNetworkPacket<C extends Connection<C>> extends NetworkPacket<C> {

  /**
   * Reads packet data from the byte buffer.
   *
   * @param connection the connection this packet was received from
   * @param buffer the buffer with received data
   * @param remainingDataLength the expected remaining data length
   * @return true if reading was successful
   * @since 10.0.0
   */
  boolean read(C connection, ByteBuffer buffer, int remainingDataLength);
}
