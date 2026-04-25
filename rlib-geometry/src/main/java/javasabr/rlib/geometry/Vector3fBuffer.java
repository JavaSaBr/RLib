package javasabr.rlib.geometry;

/**
 * A buffer for reusing Vector3f instances to reduce object allocation.
 *
 * @since 10.0.0
 */
public interface Vector3fBuffer {

  /**
   * A no-reuse buffer that always creates new Vector3f instances.
   */
  Vector3fBuffer NO_REUSE = new Vector3fBuffer() {

    @Override
    public Vector3f next() {
      return new Vector3f();
    }

    @Override
    public Vector3f next(Vector3f source) {
      return new Vector3f(source);
    }

    @Override
    public Vector3f next(float x, float y, float z) {
      return new Vector3f(x, y, z);
    }
  };

  /**
   * Returns the next available vector from the buffer.
   *
   * @return a vector instance
   * @since 10.0.0
   */
  Vector3f next();

  /**
   * Returns the next available vector with copied values from the source.
   *
   * @param source the source vector to copy from
   * @return a vector instance with copied values
   * @since 10.0.0
   */
  default Vector3f next(Vector3f source) {
    return next().set(source);
  }

  /**
   * Returns the next available vector with the specified component values.
   *
   * @param x the x component
   * @param y the y component
   * @param z the z component
   * @return a vector instance with the specified values
   * @since 10.0.0
   */
  default Vector3f next(float x, float y, float z) {
    return next().set(x, y, z);
  }
}
