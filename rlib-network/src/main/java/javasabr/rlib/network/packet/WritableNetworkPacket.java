package javasabr.rlib.network.packet;

import java.nio.ByteBuffer;
import javasabr.rlib.network.Connection;

/**
 * Interface for network packets that can be written to a byte buffer.
 *
 * @param <C> the connection type
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface WritableNetworkPacket<C extends Connection<C>> extends NetworkPacket<C> {

  /**
   * Constant indicating unknown expected packet length.
   *
   * @since 10.0.0
   */
  int UNKNOWN_EXPECTED_BYTES = -1;

  /**
   * Writes this packet to the buffer.
   *
   * @param connection the connection to write to
   * @param buffer the buffer to write to
   * @return true if writing was successful
   * @since 10.0.0
   */
  boolean write(C connection, ByteBuffer buffer);

  /**
   * Gets the expected data length of this packet.
   *
   * @param connection the connection
   * @return the expected length or {@link #UNKNOWN_EXPECTED_BYTES}
   * @since 10.0.0
   */
  default int expectedLength(C connection) {
    return UNKNOWN_EXPECTED_BYTES;
  }
}
