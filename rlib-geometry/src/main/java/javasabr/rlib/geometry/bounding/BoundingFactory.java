package javasabr.rlib.geometry.bounding;

import javasabr.rlib.geometry.Vector3f;
import javasabr.rlib.geometry.bounding.impl.AbstractBounding;
import javasabr.rlib.geometry.bounding.impl.AxisAlignedBoundingBox;
import javasabr.rlib.geometry.bounding.impl.BoundingSphere;
import org.jspecify.annotations.NullMarked;

/**
 * Factory for creating {@link Bounding} instances.
 *
 * @since 10.0.0
 */
@NullMarked
public final class BoundingFactory {

  /**
   * Creates a new axis-aligned bounding box.
   *
   * @param center the center of the bounding box
   * @param offset the offset from the center
   * @param sizeX the half-size along the X axis
   * @param sizeY the half-size along the Y axis
   * @param sizeZ the half-size along the Z axis
   * @return a new bounding box
   * @since 10.0.0
   */
  public static Bounding newBoundingBox(
      Vector3f center,
      Vector3f offset,
      float sizeX,
      float sizeY,
      float sizeZ) {
    return new AxisAlignedBoundingBox(center, offset, sizeX, sizeY, sizeZ);
  }

  /**
   * Creates an empty bounding volume.
   *
   * @return an empty bounding
   * @since 10.0.0
   */
  public static Bounding newBoundingEmpty() {
    return new AbstractBounding(new Vector3f(), new Vector3f()) {};
  }

  /**
   * Creates a new bounding sphere.
   *
   * @param center the center of the sphere
   * @param offset the offset from the center
   * @param radius the radius of the sphere
   * @return a new bounding sphere
   * @since 10.0.0
   */
  public static Bounding newBoundingSphere(Vector3f center, Vector3f offset, float radius) {
    return new BoundingSphere(center, offset, radius);
  }

  private BoundingFactory() {
    throw new IllegalArgumentException();
  }
}
