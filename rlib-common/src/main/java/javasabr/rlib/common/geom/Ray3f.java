package javasabr.rlib.common.geom;

import javasabr.rlib.common.util.pools.Reusable;
import org.jspecify.annotations.NullMarked;

/**
 * The implementation of a ray.
 *
 * @author JavaSaBr
 */
@NullMarked
public class Ray3f implements Reusable {

  /**
   * The start point of this ray.
   */
  private final Vector3f start;

  /**
   * The direction of this ray.
   */
  private final Vector3f direction;

  /**
   * Construct new ray in start point and specified direction.
   *
   * @param origin start point
   * @param direction direction
   */
  public Ray3f(Vector3f origin, Vector3f direction) {
    this.start = origin;
    this.direction = direction;
  }

  /**
   * Construct empty ray in zero point and zero direction.
   */
  public Ray3f() {
    this(new Vector3f(), new Vector3f());
  }

  /**
   * Get the direction.
   *
   * @return the direction.
   */
  public final Vector3f getDirection() {
    return direction;
  }

  /**
   * Set the direction.
   *
   * @param direction the direction.
   */
  public final void setDirection(Vector3f direction) {
    this.direction.set(direction);
  }

  /**
   * Get the start point.
   *
   * @return the start point.
   */
  public final Vector3f getStart() {
    return start;
  }

  /**
   * Set the start point.
   *
   * @param start the start point.
   */
  public final void setStart(Vector3f start) {
    this.start.set(start);
  }

  @Override
  public String toString() {
    return "Ray3f{" + "start=" + start + ", direction=" + direction + '}';
  }
}
