package javasabr.rlib.network.packet.impl;

import java.nio.ByteBuffer;
import javasabr.rlib.network.packet.WritablePacket;

/**
 * The base implementation of the {@link WritablePacket}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractWritablePacket extends AbstractPacket implements WritablePacket {

  @Override
  public boolean write(ByteBuffer buffer) {
    try {
      writeImpl(buffer);
      return true;
    } catch (Exception e) {
      handleException(buffer, e);
      return false;
    }
  }

  /**
   * The process of writing this packet to the buffer.
   *
   * @param buffer the buffer
   */
  protected void writeImpl(ByteBuffer buffer) {}
}
