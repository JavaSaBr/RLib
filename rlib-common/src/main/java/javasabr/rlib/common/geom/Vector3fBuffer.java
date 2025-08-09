package javasabr.rlib.common.geom;

import org.jspecify.annotations.NullMarked;

/**
 * The interface to implement a buffer of vectors.
 *
 * @author JavaSaBr
 */
@NullMarked
public interface Vector3fBuffer {

  Vector3fBuffer NO_REUSE = new Vector3fBuffer() {

    @Override
    public Vector3f nextVector() {
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
   * Take a next free vector.
   *
   * @return the next vector.
   */
  Vector3f nextVector();

  /**
   * Take a next free vector with copied values from the source vector.
   *
   * @param source the source vector.
   * @return the next free vector with copied values.
   */
  default Vector3f next(Vector3f source) {
    return nextVector().set(source);
  }

  /**
   * Take a next free vector with copied values.
   *
   * @param x the X component.
   * @param y the Y component.
   * @param z the Z component.
   * @return the next free vector with copied values.
   */
  default Vector3f next(float x, float y, float z) {
    return nextVector().set(x, y, z);
  }
}
