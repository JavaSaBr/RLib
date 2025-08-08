package javasabr.rlib.network.packet;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import javasabr.rlib.logger.api.LoggerManager;

/**
 * Interface to implement a writable packet.
 *
 * @author JavaSaBr
 */
public interface WritablePacket extends Packet {

  /**
   * Write this packet to the buffer.
   *
   * @param buffer the buffer.
   * @return true if writing was successful.
   */
  boolean write(ByteBuffer buffer);

  /**
   * Return an expected data length of this packet or -1.
   *
   * @return expected data length of this packet or -1.
   */
  default int getExpectedLength() {
    return -1;
  }

  /**
   * Write 1 byte to the buffer.
   *
   * @param buffer the buffer.
   * @param value the value.
   */
  default void writeByte(ByteBuffer buffer, int value) {
    buffer.put((byte) value);
  }

  /**
   * Write 2 bytes to the buffer.
   *
   * @param buffer the buffer.
   * @param value the value.
   */
  default void writeChar(ByteBuffer buffer, char value) {
    buffer.putChar(value);
  }

  /**
   * Write 2 bytes to the buffer.
   *
   * @param buffer the buffer.
   * @param value the value.
   */
  default void writeChar(final ByteBuffer buffer, final int value) {
    buffer.putChar((char) value);
  }

  /**
   * Write 4 bytes to the buffer.
   *
   * @param buffer the buffer.
   * @param value the value.
   */
  default void writeFloat(ByteBuffer buffer, float value) {
    buffer.putFloat(value);
  }

  /**
   * Write 4 bytes to the buffer.
   *
   * @param buffer the buffer.
   * @param value the value.
   */
  default void writeInt(ByteBuffer buffer, int value) {
    buffer.putInt(value);
  }

  /**
   * Write 8 bytes to the buffer.
   *
   * @param buffer the buffer.
   * @param value the value.
   */
  default void writeLong(ByteBuffer buffer, long value) {
    buffer.putLong(value);
  }

  /**
   * Writes 2 bytes to the buffer.
   *
   * @param buffer the buffer.
   * @param value the value for writing.
   */
  default void writeShort(ByteBuffer buffer, int value) {
    buffer.putShort((short) value);
  }

  /**
   * Writes the string to the buffer.
   *
   * @param buffer the buffer.
   * @param string the string for writing.
   */
  default void writeString(ByteBuffer buffer, String string) {
    try {

      writeInt(buffer, string.length());

      for (int i = 0, length = string.length(); i < length; i++) {
        buffer.putChar(string.charAt(i));
      }

    } catch (BufferOverflowException ex) {
      LoggerManager
          .getLogger(WritablePacket.class)
          .error(
              "Cannot write a string to buffer because the string is too long." + " String length: " + string.length()
                  + ", buffer: " + buffer);
      throw ex;
    }
  }

  /**
   * Write a data buffer to packet buffer.
   *
   * @param buffer thr packet buffer.
   * @param data the data buffer.
   */
  default void writeBuffer(ByteBuffer buffer, ByteBuffer data) {
    buffer.put(data.array(), data.position(), data.limit());
  }
}
