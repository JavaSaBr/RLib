package javasabr.rlib.common.compiler;

import org.jspecify.annotations.NullMarked;

/**
 * The interface to implement byte code container.
 *
 * @author JavaSaBr
 */
@NullMarked
public interface ByteCode {

  /**
   * Get the byte code.
   *
   * @return the byte code.
   */
  byte[] getByteCode();
}
