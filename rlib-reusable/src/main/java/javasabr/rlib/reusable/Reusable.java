package javasabr.rlib.reusable;

/**
 * Represents a reusable object that can be cleaned up and reused.
 *
 * @since 10.0.0
 */
public interface Reusable extends AutoCloseable {

  /**
   * Cleans up this object for reuse.
   *
   * @since 10.0.0
   */
  default void cleanup() {}

  @Override
  default void close() {
    cleanup();
  }
}
