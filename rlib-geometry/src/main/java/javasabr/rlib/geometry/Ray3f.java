package javasabr.rlib.geometry;

/**
 * Represents a 3D ray with a start point and direction.
 *
 * @since 10.0.0
 */
public record Ray3f(Vector3f start, Vector3f direction) {

  /**
   * Constructs a ray at the origin with zero direction.
   *
   * @since 10.0.0
   */
  public Ray3f() {
    this(new Vector3f(), new Vector3f());
  }

  public void direction(Vector3f direction) {
    this.direction.set(direction);
  }

  public void start(Vector3f start) {
    this.start.set(start);
  }

  @Override
  public String toString() {
    return "Ray3f{" + "start=" + start + ", direction=" + direction + '}';
  }
}
