package javasabr.rlib.geometry;

import static java.lang.Float.floatToIntBits;
import static java.lang.Float.isFinite;

import javasabr.rlib.common.util.ExtMath;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@Setter
@Getter
@AllArgsConstructor
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public final class Vector3f implements Cloneable {

  public final static Vector3f ZERO = new Vector3f(0, 0, 0);
  public final static Vector3f NAN = new Vector3f(Float.NaN, Float.NaN, Float.NaN);

  public final static Vector3f UNIT_X = new Vector3f(1, 0, 0);
  public final static Vector3f UNIT_Y = new Vector3f(0, 1, 0);
  public final static Vector3f UNIT_Z = new Vector3f(0, 0, 1);
  public final static Vector3f UNIT_XYZ = new Vector3f(1, 1, 1);

  public final static Vector3f UNIT_X_NEGATIVE = new Vector3f(-1, 0, 0);
  public final static Vector3f UNIT_Y_NEGATIVE = new Vector3f(0, -1, 0);
  public final static Vector3f UNIT_Z_NEGATIVE = new Vector3f(0, 0, -1);
  public final static Vector3f UNIT_XYZ_NEGATIVE = new Vector3f(-1, -1, -1);

  public final static Vector3f POSITIVE_INFINITY = new Vector3f(
      Float.POSITIVE_INFINITY,
      Float.POSITIVE_INFINITY,
      Float.POSITIVE_INFINITY);

  public final static Vector3f NEGATIVE_INFINITY = new Vector3f(
      Float.NEGATIVE_INFINITY,
      Float.NEGATIVE_INFINITY,
      Float.NEGATIVE_INFINITY);

  /**
   * Return true if the vector is not null and valid.
   */
  public static boolean isValid(@Nullable Vector3f vector) {
    return vector != null && isFinite(vector.x()) && isFinite(vector.y()) && isFinite(vector.z());
  }

  /**
   * Create a subtraction result between the two vectors.
   */
  public static Vector3f substract(Vector3f first, Vector3f second) {
    return first
        .clone()
        .subtractLocal(second);
  }

  float x, y, z;

  public Vector3f() {
    super();
  }

  public Vector3f(float value) {
    this.x = value;
    this.y = value;
    this.z = value;
  }

  public Vector3f(float[] components) {
    this(components[0], components[1], components[2]);
  }

  public Vector3f(Vector3f another) {
    this(another.x(), another.y(), another.z());
  }

  public Vector3f addLocal(float addX, float addY, float addZ) {
    x += addX;
    y += addY;
    z += addZ;
    return this;
  }

  public Vector3f addLocal(Vector3f vector) {
    return addLocal(vector.x, vector.y, vector.z);
  }

  public Vector3f cross(float otherX, float otherY, float otherZ, Vector3f result) {

    var resX = y * otherZ - z * otherY;
    var resY = z * otherX - x * otherZ;
    var resZ = x * otherY - y * otherX;

    result.set(resX, resY, resZ);

    return result;
  }

  public Vector3f cross(Vector3f vector) {
    return cross(vector, new Vector3f());
  }

  public Vector3f cross(Vector3f vector, Vector3f result) {
    return cross(vector.x, vector.y, vector.z, result);
  }

  public Vector3f crossLocal(float otherX, float otherY, float otherZ) {

    var tempx = y * otherZ - z * otherY;
    var tempy = z * otherX - x * otherZ;

    z = x * otherY - y * otherX;
    x = tempx;
    y = tempy;

    return this;
  }

  public Vector3f crossLocal(Vector3f vector) {
    return crossLocal(vector.x, vector.y, vector.z);
  }

  public float distance(Vector3f vector) {
    return ExtMath.sqrt(distanceSquared(vector));
  }

  public float distanceSquared(float targetX, float targetY, float targetZ) {

    var dx = x - targetX;
    var dy = y - targetY;
    var dz = z - targetZ;

    return dx * dx + dy * dy + dz * dz;
  }

  public float distanceSquared(Vector3f vector) {
    return distanceSquared(vector.x, vector.y, vector.z);
  }

  public float dot(Vector3f vector) {
    return x * vector.x + y * vector.y + z * vector.z;
  }

  @Override
  public int hashCode() {
    var prime = 31;
    var result = 1;
    result = prime * result + Float.floatToIntBits(x);
    result = prime * result + Float.floatToIntBits(y);
    result = prime * result + Float.floatToIntBits(z);
    return result;
  }

  /**
   * Return true if all components are zero.
   */
  public boolean isZero() {
    return this == ZERO || ExtMath.isZero(x) && ExtMath.isZero(y) && ExtMath.isZero(z);
  }

  public Vector3f multLocal(float scalar) {
    return multLocal(scalar, scalar, scalar);
  }

  public Vector3f multLocal(float x, float y, float z) {
    this.x *= x;
    this.y *= y;
    this.z *= z;
    return this;
  }

  public Vector3f multLocal(Vector3f vector) {
    return multLocal(vector.x(), vector.y(), vector.z());
  }

  public Vector3f negate() {
    return new Vector3f(-x(), -y(), -z());
  }

  public Vector3f negateLocal() {
    x = -x;
    y = -y;
    z = -z;
    return this;
  }

  public Vector3f normalize() {

    var length = x * x + y * y + z * z;

    if (length != 1F && length != 0F) {
      length = 1.0F / ExtMath.sqrt(length);
      return new Vector3f(x * length, y * length, z * length);
    }

    return new Vector3f(x, y, z);
  }

  public Vector3f normalizeLocal() {

    var length = x * x + y * y + z * z;

    if (length != 1f && length != 0f) {
      length = 1.0f / ExtMath.sqrt(length);
      x *= length;
      y *= length;
      z *= length;
    }

    return this;
  }

  public Vector3f set(Vector3f vector) {
    return set(vector.x, vector.y, vector.z);
  }

  public Vector3f set(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
    return this;
  }

  public Vector3f subtract(Vector3f vector, Vector3f result) {
    result.x = x - vector.x;
    result.y = y - vector.y;
    result.z = z - vector.z;
    return result;
  }

  public Vector3f subtractLocal(float subX, float subY, float subZ) {
    x -= subX;
    y -= subY;
    z -= subZ;
    return this;
  }

  public Vector3f subtractLocal(Vector3f vector) {
    return subtractLocal(vector.x, vector.y, vector.z);
  }

  public float length() {
    return ExtMath.sqrt(x * x + y * y + z * z);
  }

  public float sqrLength() {
    return x * x + y * y + z * z;
  }

  public Vector3f divideLocal(float x, float y, float z) {
    this.x /= x;
    this.y /= y;
    this.z /= z;
    return this;
  }

  public Vector3f divideLocal(Vector3f vector) {
    return divideLocal(vector.x, vector.y, vector.z);
  }

  public Vector3f divideLocal(float scalar) {
    return divideLocal(scalar, scalar, scalar);
  }

  /**
   * Move this vector to a new point by specified direction.
   */
  public Vector3f moveToDirection(Vector3f direction, float distance) {
    return addLocal(direction.x() * distance, direction.y() * distance, direction.z() * distance);
  }

  /**
   * Moves this vector towards the destination by the given distance.
   * If the distance is greater than or equal to the actual distance to the destination,
   * this vector is set to the destination.
   *
   * @param destination the destination vector
   * @param distance the maximum distance to move
   * @return this vector with the new position
   * @since 10.0.0
   */
  public Vector3f moveToPoint(Vector3f destination, float distance) {

    Vector3f direction = new Vector3f(destination).subtractLocal(this);

    double length = Math.sqrt(
            direction.x() * direction.x() +
            direction.y() * direction.y() +
            direction.z() * direction.z());

    if (length <= distance || length < ExtMath.EPSILON) {
      set(destination);
      return this;
    }

    // normalize vector by exists length:
    // avoid new vector length calculation via normalizeLocal
    direction.divideLocal((float) length);

    return moveToDirection(direction, distance);
  }

  /**
   * Linear time-based interpolation stored to this vector.
   */
  public Vector3f lerp(Vector3f min, Vector3f max, float time) {

    time = ExtMath.clamp(time);

    this.x = min.x + (max.x - min.x) * time;
    this.y = min.y + (max.y - min.y) * time;
    this.z = min.z + (max.z - min.z) * time;

    return this;
  }

  @Override
  protected Vector3f clone() {
    try {
      return (Vector3f) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean equals(@Nullable Object obj) {

    if (this == obj) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (getClass() != obj.getClass()) {
      return false;
    }

    var other = (Vector3f) obj;

    if (floatToIntBits(x) != floatToIntBits(other.x)) {
      return false;
    } else if (floatToIntBits(y) != floatToIntBits(other.y)) {
      return false;
    } else if (floatToIntBits(z) != floatToIntBits(other.z)) {
      return false;
    }

    return true;
  }

  /**
   * Return true if these vectors are equal with the epsilon.
   *
   * @param vector the vector.
   * @param epsilon the epsilon.
   * @return true if these vectors are equal with the epsilon.
   */
  public boolean equals(Vector3f vector, float epsilon) {
    return
        Math.abs(x - vector.x()) < epsilon &&
        Math.abs(y - vector.y()) < epsilon &&
        Math.abs(z - vector.z()) < epsilon;
  }

  @Override
  public String toString() {
    return "Vector3f(" + x + ", " + y + ", " + z + ')';
  }
}
