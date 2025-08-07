package javasabr.rlib.common.util.random;

import org.jspecify.annotations.NullMarked;

/**
 * The factory of randoms implementations.
 *
 * @author JavaSaBr
 */
@NullMarked
public final class RandomFactory {

  /**
   * New fast random random.
   *
   * @return the random
   */
  public static Random newFastRandom() {
    return new FastRandom();
  }

  private RandomFactory() {
    throw new RuntimeException();
  }
}
