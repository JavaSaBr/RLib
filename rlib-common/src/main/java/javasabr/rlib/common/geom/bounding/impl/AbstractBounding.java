package javasabr.rlib.common.geom.bounding.impl;

import javasabr.rlib.common.geom.Quaternion4f;
import javasabr.rlib.common.geom.Ray3f;
import javasabr.rlib.common.geom.Vector3f;
import javasabr.rlib.common.geom.Vector3fBuffer;
import javasabr.rlib.common.geom.bounding.Bounding;
import javasabr.rlib.common.geom.bounding.BoundingType;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import org.jspecify.annotations.NullMarked;

/**
 * The base implementation of a bounding.
 *
 * @author JavaSaBr
 */
@NullMarked
public abstract class AbstractBounding implements Bounding {

  protected static final Logger LOGGER = LoggerManager.getLogger(Bounding.class);

  /**
   * The center.
   */
  protected Vector3f center;

  /**
   * The offset.
   */
  protected Vector3f offset;

  protected AbstractBounding(Vector3f center, Vector3f offset) {
    this.center = center;
    this.offset = offset;
  }

  @Override
  public boolean contains(float x, float y, float z) {
    return false;
  }

  @Override
  public boolean contains(Vector3f point) {
    return contains(point.getX(), point.getY(), point.getZ());
  }

  @Override
  public final float distanceTo(Vector3f point) {
    return center.distance(point);
  }

  @Override
  public BoundingType getBoundingType() {
    return BoundingType.EMPTY;
  }

  @Override
  public final Vector3f getCenter() {
    return center;
  }

  @Override
  public void setCenter(Vector3f center) {
    this.center = center;
  }

  @Override
  public Vector3f getOffset() {
    return offset;
  }

  @Override
  public Vector3f getResultCenter(Vector3fBuffer buffer) {
    return getCenter();
  }

  @Override
  public float getResultCenterX() {
    return getCenter().getX();
  }

  @Override
  public float getResultCenterY() {
    return getCenter().getY();
  }

  @Override
  public float getResultCenterZ() {
    return getCenter().getZ();
  }

  @Override
  public boolean intersects(Bounding bounding, Vector3fBuffer buffer) {
    return false;
  }

  @Override
  public final boolean intersects(Ray3f ray, Vector3fBuffer buffer) {
    return intersects(ray.getStart(), ray.getDirection(), buffer);
  }

  @Override
  public boolean intersects(Vector3f start, Vector3f direction, Vector3fBuffer buffer) {
    return false;
  }

  @Override
  public void update(Quaternion4f rotation, Vector3fBuffer buffer) {}
}
