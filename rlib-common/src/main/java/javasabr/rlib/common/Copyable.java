package javasabr.rlib.common;

/**
 * Represents an object that can create a copy of itself.
 *
 * @param <T> the type of the object to be copied
 * @since 10.0.0
 */
public interface Copyable<T> {

  /**
   * Creates a copy of this object.
   *
   * @return a copy of this object
   */
  T copy();
}
