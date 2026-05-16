package javasabr.rlib.geometry;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javasabr.rlib.common.util.ExtMath;
import javasabr.rlib.geometry.util.AngleUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

/**
 * A quaternion for representing 3D rotations with float components.
 *
 * @since 10.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Quaternion4f {

  /**
   * The identity quaternion representing no rotation.
   */
  public static final Quaternion4f IDENTITY = new Quaternion4f(0, 0, 0, 1);

  private static final ThreadLocal<Quaternion4f> ROTATION_LOCAL = ThreadLocal.withInitial(Quaternion4f::new);

  /**
   * Returns a thread-local quaternion instance.
   *
   * @return a thread-local quaternion
   * @since 10.0.0
   */
  public static Quaternion4f threadLocal() {
    return ROTATION_LOCAL.get();
  }

  float x, y, z, w;

  public Quaternion4f() {
    w = 1;
  }

  public Quaternion4f(float[] vals) {
    this(vals[0], vals[1], vals[2], vals[3]);
  }

  public Quaternion4f(float angleX, float angleY, float angleZ) {
    fromAngles(angleX, angleY, angleZ);
  }

  public Quaternion4f addLocal(Quaternion4f rotation) {
    this.x += rotation.x;
    this.y += rotation.y;
    this.z += rotation.z;
    this.w += rotation.w;
    return this;
  }

  public float dot(Quaternion4f rotation) {
    return w * rotation.w + x * rotation.x + y * rotation.y + z * rotation.z;
  }

  public final Quaternion4f fromAngles(float angleX, float yAngle, float zAngle) {

    float angle = zAngle * 0.5f;

    float sinZ = ExtMath.sin(angle);
    float cosZ = ExtMath.cos(angle);

    angle = yAngle * 0.5f;

    float sinY = ExtMath.sin(angle);
    float cosY = ExtMath.cos(angle);

    angle = angleX * 0.5f;

    float sinX = ExtMath.sin(angle);
    float cosX = ExtMath.cos(angle);

    float cosYXcosZ = cosY * cosZ;
    float sinYXsinZ = sinY * sinZ;
    float cosYXsinZ = cosY * sinZ;
    float sinYXcosZ = sinY * cosZ;

    w = cosYXcosZ * cosX - sinYXsinZ * sinX;
    x = cosYXcosZ * sinX + sinYXsinZ * cosX;
    y = sinYXcosZ * cosX + cosYXsinZ * sinX;
    z = cosYXsinZ * cosX - sinYXcosZ * sinX;

    return normalizeLocal();
  }

  public final Quaternion4f fromAngles(float[] angles) {
    return fromAngles(angles[0], angles[1], angles[2]);
  }

  public Quaternion4f fromAxes(Vector3f axisX, Vector3f axisY, Vector3f axisZ) {
    return fromRotationMatrix(
        axisX.x(),
        axisY.x(),
        axisZ.x(),
        axisX.y(),
        axisY.y(),
        axisZ.y(),
        axisX.z(),
        axisY.z(),
        axisZ.z());
  }

  public Quaternion4f fromRotationMatrix(
      float val_0_0,
      float val_0_1,
      float val_0_2,
      float val_1_0,
      float val_1_1,
      float val_1_2,
      float val_2_0,
      float val_2_1,
      float val_2_2) {

    float t = val_0_0 + val_1_1 + val_2_2;

    // we protect the division by s by ensuring that s>=1
    if (t >= 0) { // |w| >= .5
      float s = ExtMath.sqrt(t + 1); // |s|>=1 ...
      w = 0.5f * s;
      s = 0.5f / s; // so this division isn't bad
      x = (val_2_1 - val_1_2) * s;
      y = (val_0_2 - val_2_0) * s;
      z = (val_1_0 - val_0_1) * s;
    } else if (val_0_0 > val_1_1 && val_0_0 > val_2_2) {
      float s = ExtMath.sqrt(1.0f + val_0_0 - val_1_1 - val_2_2); // |s|>=1
      x = s * 0.5f; // |x| >= .5
      s = 0.5f / s;
      y = (val_1_0 + val_0_1) * s;
      z = (val_0_2 + val_2_0) * s;
      w = (val_2_1 - val_1_2) * s;
    } else if (val_1_1 > val_2_2) {
      float s = ExtMath.sqrt(1.0f + val_1_1 - val_0_0 - val_2_2); // |s|>=1
      y = s * 0.5f; // |y| >= .5
      s = 0.5f / s;
      x = (val_1_0 + val_0_1) * s;
      z = (val_2_1 + val_1_2) * s;
      w = (val_0_2 - val_2_0) * s;
    } else {
      float s = ExtMath.sqrt(1.0f + val_2_2 - val_0_0 - val_1_1); // |s|>=1
      z = s * 0.5f; // |z| >= .5
      s = 0.5f / s;
      x = (val_0_2 + val_2_0) * s;
      y = (val_2_1 + val_1_2) * s;
      w = (val_1_0 - val_0_1) * s;
    }

    return this;
  }

  public Vector3f getDirection(DirectionType type) {
    return getDirection(type, null);
  }

  public Vector3f getDirection(DirectionType type, @Nullable Vector3f store) {

    if (store == null) {
      store = new Vector3f();
    }

    float norm = norm();

    if (norm != 1.0f) {
      norm = ExtMath.invSqrt(norm);
    }

    float xx = x * x * norm;
    float xy = x * y * norm;
    float xz = x * z * norm;
    float xw = x * w * norm;
    float yy = y * y * norm;
    float yz = y * z * norm;
    float yw = y * w * norm;
    float zz = z * z * norm;
    float zw = z * w * norm;

    switch (type) {
      case RIGHT: {
        store.x(1 - 2 * (yy + zz));
        store.y(2 * (xy + zw));
        store.z(2 * (xz - yw));
        break;
      }
      case LEFT: {
        store.x(1 - 2 * (yy + zz));
        store.y(2 * (xy + zw));
        store.z(2 * (xz - yw));
        store.negateLocal();
        break;
      }
      case UP: {
        store.x(2 * (xy - zw));
        store.y(1 - 2 * (xx + zz));
        store.z(2 * (yz + xw));
        break;
      }
      case DOWN: {
        store.x(2 * (xy - zw));
        store.y(1 - 2 * (xx + zz));
        store.z(2 * (yz + xw));
        store.negateLocal();
        break;
      }
      case FRONT: {
        store.x(2 * (xz + yw));
        store.y(2 * (yz - xw));
        store.z(1 - 2 * (xx + yy));
        break;
      }
      case BEHIND: {
        store.x(2 * (xz + yw));
        store.y(2 * (yz - xw));
        store.z(1 - 2 * (xx + yy));
        store.negateLocal();
        break;
      }
    }

    return store;
  }

  @Override
  public final boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (getClass() != obj.getClass()) {
      return false;
    }
    Quaternion4f other = (Quaternion4f) obj;
    if (Float.floatToIntBits(w) != Float.floatToIntBits(other.w)) {
      return false;
    } else if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) {
      return false;
    } else if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
      return false;
    } else if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) {
      return false;
    }
    return true;
  }

  @Override
  public final int hashCode() {

    final int prime = 31;
    int result = 1;

    result = prime * result + Float.floatToIntBits(w);
    result = prime * result + Float.floatToIntBits(x);
    result = prime * result + Float.floatToIntBits(y);
    result = prime * result + Float.floatToIntBits(z);

    return result;
  }

  public void lookAt(Vector3f direction, Vector3f up) {
    lookAt(direction, up, Vector3fBuffer.NO_REUSE);
  }

  public void lookAt(Vector3f direction, Vector3f up, Vector3fBuffer buffer) {

    var axisZ = buffer
        .next(direction)
        .normalizeLocal();

    var axisX = buffer
        .next(up)
        .crossLocal(direction)
        .normalizeLocal();

    var axisY = buffer
        .next(direction)
        .crossLocal(axisX)
        .normalizeLocal();

    fromAxes(axisX, axisY, axisZ);
    normalizeLocal();
  }

  public final Vector3f multLocal(Vector3f vector) {

    float vectorX = vector.x();
    float vectorY = vector.y();
    float vectorZ = vector.z();

    float x = x();
    float y = y();
    float z = z();
    float w = w();

    vector.x(w * w * vectorX + 2 * y * w * vectorZ - 2 * z * w * vectorY + x * x * vectorX + 2 * y * x * vectorY
        + 2 * z * x * vectorZ - z * z * vectorX - y * y * vectorX);
    vector.y(2 * x * y * vectorX + y * y * vectorY + 2 * z * y * vectorZ + 2 * w * z * vectorX - z * z * vectorY
        + w * w * vectorY - 2 * x * w * vectorZ - x * x * vectorY);
    vector.z(2 * x * z * vectorX + 2 * y * z * vectorY + z * z * vectorZ - 2 * w * y * vectorX - y * y * vectorZ
        + 2 * w * x * vectorY - x * x * vectorZ + w * w * vectorZ);

    return vector;
  }

  public final Quaternion4f negateLocal() {
    x = -x;
    y = -y;
    z = -z;
    w = -w;
    return this;
  }

  public float norm() {
    return w * w + x * x + y * y + z * z;
  }

  public final Quaternion4f normalizeLocal() {

    var norm = ExtMath.invSqrt(norm());

    x *= norm;
    y *= norm;
    z *= norm;
    w *= norm;

    return this;
  }

  public void random() {
    random(ThreadLocalRandom.current());
  }

  public void random(Random random) {

    float x = AngleUtils.degreeToRadians(random.nextInt(0, 360));
    float y = AngleUtils.degreeToRadians(random.nextInt(0, 360));
    float z = AngleUtils.degreeToRadians(random.nextInt(0, 360));

    fromAngles(x, y, z);
  }

  public Quaternion4f set(Quaternion4f rotation) {
    this.x = rotation.x;
    this.y = rotation.y;
    this.z = rotation.z;
    this.w = rotation.w;
    return this;
  }

  public void set(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }


  public void slerp(Quaternion4f end, float percent) {

    if (equals(end)) {
      return;
    }

    float result = x * end.x + y * end.y + z * end.z + w * end.w;

    if (result < 0.0f) {
      end.x = -end.x;
      end.y = -end.y;
      end.z = -end.z;
      end.w = -end.w;
      result = -result;
    }

    float scale0 = 1 - percent;
    float scale1 = percent;

    if (1 - result > 0.1f) {
      float theta = ExtMath.acos(result);
      float invSinTheta = 1f / ExtMath.sin(theta);
      scale0 = ExtMath.sin((1 - percent) * theta) * invSinTheta;
      scale1 = ExtMath.sin(percent * theta) * invSinTheta;
    }

    x = scale0 * x + scale1 * end.x;
    y = scale0 * y + scale1 * end.y;
    z = scale0 * z + scale1 * end.z;
    w = scale0 * w + scale1 * end.w;
  }

  public Quaternion4f slerp(Quaternion4f start, Quaternion4f end, float percent) {
    return slerp(start, end, percent, false);
  }

  public final Quaternion4f slerp(Quaternion4f start, Quaternion4f end, float percent, boolean forceLinear) {

    if (start.equals(end)) {
      set(start);
      return this;
    }

    float result = start.dot(end);

    if (result < 0.0f) {
      end.negateLocal();
      result = -result;
    }

    float startScale = 1 - percent;
    float endScale = percent;

    if (!forceLinear && 1 - result > 0.1f) {
      float theta = ExtMath.acos(result);
      float invSinTheta = 1f / ExtMath.sin(theta);
      startScale = ExtMath.sin((1 - percent) * theta) * invSinTheta;
      endScale = ExtMath.sin(percent * theta) * invSinTheta;
    }

    this.x = startScale * start.x() + endScale * end.x();
    this.y = startScale * start.y() + endScale * end.y();
    this.z = startScale * start.z() + endScale * end.z();
    this.w = startScale * start.w() + endScale * end.w();

    return this;
  }

  public void nlerp(Quaternion4f targetRotation, float percent) {

    float dot = dot(targetRotation);
    float blendI = 1.0F - percent;

    if (dot < 0.0F) {
      x = blendI * x - percent * targetRotation.x;
      y = blendI * y - percent * targetRotation.y;
      z = blendI * z - percent * targetRotation.z;
      w = blendI * w - percent * targetRotation.w;
    } else {
      x = blendI * x + percent * targetRotation.x;
      y = blendI * y + percent * targetRotation.y;
      z = blendI * z + percent * targetRotation.z;
      w = blendI * w + percent * targetRotation.w;
    }

    normalizeLocal();
  }

  public Quaternion4f subtractLocal(Quaternion4f rotation) {
    this.x -= rotation.x;
    this.y -= rotation.y;
    this.z -= rotation.z;
    this.w -= rotation.w;
    return this;
  }

  public final float toAngleAxis(@Nullable Vector3f axisStore) {

    float sqrLength = x * x + y * y + z * z;
    float angle;

    if (sqrLength == 0.0f) {
      angle = 0.0f;
      if (axisStore != null) {
        axisStore.x(1.0F);
        axisStore.y(0.0F);
        axisStore.z(0.0F);
      }
    } else {
      angle = 2.0f * ExtMath.acos(w);
      if (axisStore != null) {
        float invLength = 1.0f / ExtMath.sqrt(sqrLength);
        axisStore.x(x * invLength);
        axisStore.y(y * invLength);
        axisStore.z(z * invLength);
      }
    }

    return angle;
  }

  /**
   * <code>toAngles</code> returns this quaternion converted to Euler rotation angles (yaw,roll,pitch). Note that
   * the result is not always 100% accurate due to the implications of euler angles.
   *
   * @param angles the float[] in which the angles should be stored, or null if you want a new float[] to be created
   * @return the float[] in which the angles are stored.
   * @see <a
   * href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm">http://www
   * .euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm</a>
   */
  public float[] toAngles(float @Nullable [] angles) {

    if (angles == null) {
      angles = new float[3];
    } else if (angles.length != 3) {
      throw new IllegalArgumentException("Angles array must have three elements");
    }

    float sqw = w * w;
    float sqx = x * x;
    float sqy = y * y;
    float sqz = z * z;
    float unit = sqx + sqy + sqz + sqw; // if normalized is one, otherwise

    // is correction factor
    float test = x * y + z * w;
    if (test > 0.499 * unit) { // singularity at north pole
      angles[1] = 2 * ExtMath.atan2(x, w);
      angles[2] = ExtMath.HALF_PI;
      angles[0] = 0;
    } else if (test < -0.499 * unit) { // singularity at south pole
      angles[1] = -2 * ExtMath.atan2(x, w);
      angles[2] = -ExtMath.HALF_PI;
      angles[0] = 0;
    } else {
      angles[1] = ExtMath.atan2(2 * y * w - 2 * x * z, sqx - sqy - sqz + sqw); // roll
      // or
      // heading
      angles[2] = ExtMath.asin(2 * test / unit); // pitch or attitude
      angles[0] = ExtMath.atan2(2 * x * w - 2 * y * z, -sqx + sqy - sqz + sqw); // yaw
      // or
      // bank
    }
    return angles;
  }

  public final Matrix3f toRotationMatrix(Matrix3f result) {

    float norm = norm();
    float s = norm == 1f ? 2f : norm > 0f ? 2f / norm : 0;

    float x = x();
    float y = y();
    float z = z();
    float w = w();

    float xs = x * s;
    float ys = y * s;
    float zs = z * s;
    float xx = x * xs;
    float xy = x * ys;
    float xz = x * zs;
    float xw = w * xs;
    float yy = y * ys;
    float yz = y * zs;
    float yw = w * ys;
    float zz = z * zs;
    float zw = w * zs;

    result.set(
        1 - (yy + zz),
        xy - zw,
        xz + yw,
        xy + zw,
        1 - (xx + zz),
        yz - xw,
        xz - yw,
        yz + xw,
        1 - (xx + yy));

    return result;
  }

  public Vector3f rotate(Vector3f vector) {

    float px = vector.x;
    float py = vector.y;
    float pz = vector.z;

    float norm = norm();
    float s = norm == 1f ? 2f : norm > 0f ? 2f / norm : 0;

    float x = x() * s;
    float y = y() * s;
    float z = z() * s;
    float xx = x() * x;
    float xy = x() * y;
    float xz = x() * z;
    float xw = w() * x;
    float yy = y() * y;
    float yz = y() * z;
    float yw = w() * y;
    float zz = z() * z;
    float zw = w() * z;

    vector.x((1f - (yy + zz)) * px + (xy - zw) * py + (xz + yw) * pz);
    vector.y((xy + zw) * px + (1f - (xx + zz)) * py + (yz - xw) * pz);
    vector.z((xz - yw) * px + (yz - xw) * py + (1f - (xx + yy)) * pz);

    return vector;
  }

  @Override
  public String toString() {
    return "Quaternion4f{" + "x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + '}';
  }
}
