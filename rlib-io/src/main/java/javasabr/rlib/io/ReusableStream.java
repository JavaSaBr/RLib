package javasabr.rlib.io;

import java.io.IOException;

/**
 * A reusable stream that can be reset and reinitialized with new data.
 *
 * @since 10.0.0
 */
public interface ReusableStream {

  /**
   * Resets this stream to the beginning.
   *
   * @throws IOException if an I/O error occurs
   * @since 10.0.0
   */
  void reset() throws IOException;

  /**
   * Initializes this stream to use the specified buffer.
   *
   * @param buffer the buffer data
   * @param offset the starting offset in the buffer
   * @param length the number of bytes to use
   * @since 10.0.0
   */
  void initFor(byte[] buffer, int offset, int length);
}
