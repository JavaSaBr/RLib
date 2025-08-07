package javasabr.rlib.network.packet;

import java.nio.ByteBuffer;
import javasabr.rlib.network.Connection;
import org.jetbrains.annotations.NotNull;

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
  boolean read(@NotNull Connection<?, ?> connection, @NotNull ByteBuffer buffer, int length);
}
