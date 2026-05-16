package javasabr.rlib.geometry.bounding.impl;

import javasabr.rlib.geometry.Matrix3f;
import javasabr.rlib.geometry.Quaternion4f;
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
@FieldDefaults(level = AccessLevel.PROTECTED)
public class AxisAlignedBoundingBox extends AbstractBounding {

  final Matrix3f matrix;
  final Vector3f size;

  float sizeX, sizeY, sizeZ;
  float offsetX, offsetY, offsetZ;

  public AxisAlignedBoundingBox(Vector3f center, Vector3f offset, float sizeX, float sizeY, float sizeZ) {
    super(center, offset);
    this.matrix = new Matrix3f();
    this.size = new Vector3f(sizeX, sizeY, sizeZ);
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    this.sizeZ = sizeZ;
    this.offsetX = offset.x();
    this.offsetY = offset.y();
    this.offsetZ = offset.z();
  }

  @Override
  public boolean contains(float x, float y, float z) {
    return Math.abs(calculateResultCenterX() - x) < sizeX && Math.abs(calculateResultCenterY() - y) < sizeY
        && Math.abs(this.calculateResultCenterZ() - z) < sizeZ;
  }

  @Override
  public BoundingType boundingType() {
    return BoundingType.AXIS_ALIGNED_BOX;
  }

  @Override
  public  Vector3f calculateResultCenter(Vector3fBuffer buffer) {

    Vector3f vector = buffer
        .next()
        .set(center);

    if (offset.isZero()) {
      return vector;
    }

    return vector.addLocal(offsetX, offsetY, offsetZ);
  }

  @Override
  public float calculateResultCenterZ() {
    return center.z() + offsetZ;
  }

  @Override
  public float calculateResultCenterY() {
    return center.y() + offsetY;
  }

  @Override
  public float calculateResultCenterX() {
    return center.x() + offsetX;
  }

  @Override
  public boolean intersects(Bounding bounding, Vector3fBuffer buffer) {
    switch (bounding.boundingType()) {
      case EMPTY: {
        return false;
      }
      case AXIS_ALIGNED_BOX: {

        AxisAlignedBoundingBox box = (AxisAlignedBoundingBox) bounding;

        Vector3f target = box.calculateResultCenter(buffer);
        Vector3f center = calculateResultCenter(buffer);

        float sizeX = sizeX();
        float sizeY = sizeY();
        float sizeZ = sizeZ();

        if (center.x() + sizeX < target.x() - box.sizeX()
            || center.x() - sizeX > target.x() + box.sizeX()) {
          return false;
        } else if (center.y() + sizeY < target.y() - box.sizeY()
            || center.y() - sizeY > target.y() + box.sizeY()) {
          return false;
        } else if (center.z() + sizeZ < target.z() - box.sizeZ()
            || center.z() - sizeZ > target.z() + box.sizeZ()) {
          return false;
        }

        return true;
      }
      case SPHERE: {

        BoundingSphere sphere = (BoundingSphere) bounding;

        Vector3f target = sphere.calculateResultCenter(buffer);
        Vector3f center = calculateResultCenter(buffer);

        float radius = sphere.radius();

        if (Math.abs(center.x() - target.x()) > radius + sizeX()) {
          return false;
        } else if (Math.abs(center.y() - target.y()) > radius + sizeY()) {
          return false;
        } else {
          return !(Math.abs(center.z() - target.z()) > radius + sizeZ());
        }

      }
      default: {
        log.warn(new IllegalArgumentException("incorrect bounding type " + bounding.boundingType()));
      }
    }

    return false;
  }

  @Override
  public boolean intersects(Vector3f start, Vector3f direction, Vector3fBuffer buffer) {

    float divX = 1.0F / (Float.compare(direction.x(), 0) == 0 ? 0.00001F : direction.x());
    float divY = 1.0F / (Float.compare(direction.y(), 0) == 0 ? 0.00001F : direction.y());
    float divZ = 1.0F / (Float.compare(direction.z(), 0) == 0 ? 0.00001F : direction.z());

    float sizeX = sizeX() * 0.5F;
    float sizeY = sizeY() * 0.5F;
    float sizeZ = sizeZ() * 0.5F;

    Vector3f center = calculateResultCenter(buffer);

    float minX = center.x() - sizeX;
    float minY = center.y() - sizeY;
    float minZ = center.z() - sizeZ;

    float maxX = center.x() + sizeX;
    float maxY = center.y() + sizeY;
    float maxZ = center.z() + sizeZ;

    float t1 = (minX - start.x()) * divX;
    float t2 = (maxX - start.x()) * divX;
    float t3 = (minY - start.y()) * divY;
    float t4 = (maxY - start.y()) * divY;
    float t5 = (minZ - start.z()) * divZ;
    float t6 = (maxZ - start.z()) * divZ;

    float tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
    float tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

    return tmin <= tmax && tmax > 0.f;
  }

  @Override
  public void update(Quaternion4f rotation, Vector3fBuffer buffer) {

    matrix.set(rotation);
    matrix.absoluteLocal();

    Vector3f vector = matrix.mult(size, buffer.next());

    sizeX = Math.abs(vector.x());
    sizeY = Math.abs(vector.y());
    sizeZ = Math.abs(vector.z());

    if (offset.isZero()) {
      return;
    }

    matrix.mult(offset, vector);

    offsetX = vector.x();
    offsetY = vector.y();
    offsetZ = vector.z();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + " size = " + size + ", sizeX = " + sizeX + ", sizeY = " + sizeY + ", sizeZ = "
        + sizeZ + ", center = " + center + ", offset = " + offset;
  }
}
