package javasabr.rlib.geometry.bounding.impl;

import javasabr.rlib.geometry.Quaternion4f;
import javasabr.rlib.geometry.Ray3f;
import javasabr.rlib.geometry.Vector3f;
import javasabr.rlib.geometry.Vector3fBuffer;
import javasabr.rlib.geometry.bounding.Bounding;
import javasabr.rlib.geometry.bounding.BoundingType;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * @author JavaSaBr
 */
@Getter
@CustomLog
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractBounding implements Bounding {
  
  Vector3f center, offset;

  protected AbstractBounding(Vector3f center, Vector3f offset) {
    this.center = new Vector3f(center);
    this.offset = new Vector3f(offset);
  }

  @Override
  public boolean contains(float x, float y, float z) {
    return false;
  }

  @Override
  public boolean contains(Vector3f point) {
    return contains(point.x(), point.y(), point.z());
  }

  @Override
  public final float distanceTo(Vector3f point) {
    return center.distance(point);
  }

  @Override
  public BoundingType boundingType() {
    return BoundingType.EMPTY;
  }

  @Override
  public void center(Vector3f center) {
    this.center.set(center);
  }
  @Override
  public Vector3f calculateResultCenter(Vector3fBuffer buffer) {
    return center();
  }

  @Override
  public float calculateResultCenterX() {
    return center().x();
  }

  @Override
  public float calculateResultCenterY() {
    return center().y();
  }

  @Override
  public float calculateResultCenterZ() {
    return center().z();
  }

  @Override
  public boolean intersects(Bounding bounding, Vector3fBuffer buffer) {
    return false;
  }

  @Override
  public final boolean intersects(Ray3f ray, Vector3fBuffer buffer) {
    return intersects(ray.start(), ray.direction(), buffer);
  }

  @Override
  public boolean intersects(Vector3f start, Vector3f direction, Vector3fBuffer buffer) {
    return false;
  }

  @Override
  public void update(Quaternion4f rotation, Vector3fBuffer buffer) {}
}
