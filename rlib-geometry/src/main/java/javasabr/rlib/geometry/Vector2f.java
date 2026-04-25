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
 * @author zcxv
 */
@Setter
@Getter
@AllArgsConstructor
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Vector2f implements Cloneable {

  public final static Vector2f ZERO = new Vector2f(0, 0);
  public final static Vector2f NAN = new Vector2f(Float.NaN, Float.NaN);

  public final static Vector2f UNIT_X = new Vector2f(1, 0);
  public final static Vector2f UNIT_Y = new Vector2f(0, 1);
  public final static Vector2f UNIT_XYZ = new Vector2f(1, 1);

  public final static Vector2f UNIT_X_NEGATIVE = new Vector2f(-1, 0);
  public final static Vector2f UNIT_Y_NEGATIVE = new Vector2f(0, -1);
  public final static Vector2f UNIT_XYZ_NEGATIVE = new Vector2f(-1, -1);

  public final static Vector2f POSITIVE_INFINITY = new Vector2f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);

  public final static Vector2f NEGATIVE_INFINITY = new Vector2f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

  /**
   * Return true if the vector is not null and valid.
   */
  public static boolean isValid(@Nullable Vector2f vector) {
    return vector != null && isFinite(vector.x()) && isFinite(vector.y());
  }

  float x, y;

  public Vector2f(float value) {
    this.x = value;
    this.y = value;
  }

  public Vector2f(float[] components) {
    this(components[0], components[1]);
  }

  public Vector2f(Vector2f another) {
    this(another.x(), another.y());
  }

  public Vector2f addLocal(float addX, float addY) {
    x += addX;
    y += addY;
    return this;
  }

  public Vector2f addLocal(Vector2f vector) {
    return addLocal(vector.x, vector.y);
  }

  public float distance(Vector2f vector) {
    return ExtMath.sqrt(distanceSquared(vector));
  }

  public float distanceSquared(float targetX, float targetY) {

    var dx = x - targetX;
    var dy = y - targetY;

    return dx * dx + dy * dy;
  }

  public float distanceSquared(Vector2f vector) {
    return distanceSquared(vector.x, vector.y);
  }


  public float dot(Vector2f vector) {
    return x * vector.x + y * vector.y;
  }

  @Override
  public int hashCode() {
    var prime = 31;
    var result = 1;
    result = prime * result + Float.floatToIntBits(x);
    result = prime * result + Float.floatToIntBits(y);
    return result;
  }

  /**
   * Checks if all components are zero.
   *
   * @return true if all components are zero
   * @since 10.0.0
   */
  public boolean isZero() {
    return ExtMath.isZero(x) && ExtMath.isZero(y);
  }

  public Vector2f multLocal(float scalar) {
    return multLocal(scalar, scalar);
  }

  public Vector2f multLocal(float x, float y) {
    this.x *= x;
    this.y *= y;
    return this;
  }

  public Vector2f multLocal(Vector2f vector) {
    return multLocal(vector.x(), vector.y());
  }

  public Vector2f negate() {
    return new Vector2f(-x(), -y());
  }

  public Vector2f negateLocal() {
    x = -x;
    y = -y;
    return this;
  }

  public Vector2f normalize() {

    float length = x * x + y * y;

    if (length != 1F && length != 0F) {
      length = 1.0F / ExtMath.sqrt(length);
      return new Vector2f(x * length, y * length);
    }

    return new Vector2f(x, y);
  }

  public Vector2f normalizeLocal() {

    float length = x * x + y * y;

    if (length != 1f && length != 0f) {
      length = 1.0f / ExtMath.sqrt(length);
      x *= length;
      y *= length;
    }

    return this;
  }

  public Vector2f set(Vector2f vector) {
    return set(vector.x, vector.y);
  }

  public Vector2f set(float x, float y) {
    this.x = x;
    this.y = y;
    return this;
  }

  public Vector2f subtract(Vector2f vector, Vector2f result) {
    result.x = x - vector.x;
    result.y = y - vector.y;
    return result;
  }

  public Vector2f subtractLocal(float subX, float subY) {
    x -= subX;
    y -= subY;
    return this;
  }

  public Vector2f subtractLocal(Vector2f vector) {
    return subtractLocal(vector.x, vector.y);
  }

  public float length() {
    return ExtMath.sqrt(x * x + y * y);
  }

  public float sqrLength() {
    return x * x + y * y;
  }

  public Vector2f divideLocal(float x, float y) {
    this.x /= x;
    this.y /= y;
    return this;
  }

  public Vector2f divideLocal(Vector2f vector) {
    return divideLocal(vector.x, vector.y);
  }

  public Vector2f divideLocal(float scalar) {
    return divideLocal(scalar, scalar);
  }

  /**
   * Linear time-based interpolation stored to this vector.
   */
  public Vector2f lerp(Vector2f min, Vector2f max, float time) {

    time = ExtMath.clamp(time);

    this.x = min.x + (max.x - min.x) * time;
    this.y = min.y + (max.y - min.y) * time;

    return this;
  }

  @Override
  protected Vector2f clone() {
    try {
      return (Vector2f) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean equals(Vector3f vector, float epsilon) {
    return Math.abs(x - vector.x()) < epsilon && Math.abs(y - vector.y()) < epsilon;
  }

  public boolean equals(Vector2f vector, float epsilon) {
    return Math.abs(x - vector.x()) < epsilon && Math.abs(y - vector.y()) < epsilon;
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

    var other = (Vector2f) obj;

    if (floatToIntBits(x) != floatToIntBits(other.x)) {
      return false;
    } else if (floatToIntBits(y) != floatToIntBits(other.y)) {
      return false;
    }

    return true;
  }

  @Override
  public String toString() {
    return "Vector2f(" + x + ", " + y + ')';
  }
}
