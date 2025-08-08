package javasabr.rlib.network.packet;

import java.nio.ByteBuffer;
import javasabr.rlib.network.Connection;

/**
 * The interface to implement a readable network packet.
 *
 * @author JavaSaBr
 */
public interface ReadablePacket extends Packet {

  /**
   * Read packet's data from byte buffer.
   *
   * @param connection the network connection.
   * @param buffer the buffer with received data.
   * @param length the data length.
   * @return true if reading was success.
   */
  boolean read(Connection<?, ?> connection, ByteBuffer buffer, int length);
}
