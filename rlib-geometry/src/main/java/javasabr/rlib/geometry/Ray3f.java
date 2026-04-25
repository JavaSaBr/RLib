package javasabr.rlib.geometry;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * Represents a 3D ray with a start point and direction.
 *
 * @since 10.0.0
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Ray3f {

  Vector3f start, direction;

  /**
   * Constructs a ray at the origin with zero direction.
   *
   * @since 10.0.0
   */
  public Ray3f() {
    this(new Vector3f(), new Vector3f());
  }

  public final void direction(Vector3f direction) {
    this.direction.set(direction);
  }

  public final void start(Vector3f start) {
    this.start.set(start);
  }

  @Override
  public String toString() {
    return "Ray3f{" + "start=" + start + ", direction=" + direction + '}';
  }
}
