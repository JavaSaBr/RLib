package javasabr.rlib.reusable;

/**
 * A reusable object that is bound to a pool and can be released back to it.
 *
 * @since 10.0.0
 */
public interface BoundReusable extends Reusable {

  /**
   * Releases this object back to its pool.
   *
   * @since 10.0.0
   */
  void release();
}
