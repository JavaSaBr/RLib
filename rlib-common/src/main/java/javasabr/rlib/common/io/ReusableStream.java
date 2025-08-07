package javasabr.rlib.common.io;

import java.io.IOException;
import org.jspecify.annotations.NullMarked;

/**
 * The interface to implement a reusable streams.
 *
 * @author JavaSaBr
 */
@NullMarked
public interface ReusableStream {

  /**
   * Reset this stream.
   *
   * @throws IOException the io exception
   */
  void reset() throws IOException;

  /**
   * Initialize this string using the buffer.
   *
   * @param buffer the buffer data.
   * @param offset the offset.
   * @param length the length.
   */
  void initFor(byte[] buffer, int offset, int length);
}
