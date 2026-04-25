package javasabr.rlib.geometry;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * @author JavaSaBr
 */
@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public final class Matrix3f {

  public static final Matrix3f ZERO = new Matrix3f(
      0, 0, 0,
      0, 0, 0,
      0, 0, 0);

  public static final Matrix3f IDENTITY = new Matrix3f();

  /**
   * Values.
   */
  float val_0_0, val_0_1, val_0_2;
  float val_1_0, val_1_1, val_1_2;
  float val_2_0, val_2_1, val_2_2;

  public Matrix3f() {
    val_0_1 = val_0_2 = val_1_0 = val_1_2 = val_2_0 = val_2_1 = 0;
    val_0_0 = val_1_1 = val_2_2 = 1;
  }

  public Matrix3f(Matrix3f matrix) {
    this(
        matrix.val_0_0,
        matrix.val_0_1,
        matrix.val_0_2,
        matrix.val_1_0,
        matrix.val_1_1,
        matrix.val_1_2,
        matrix.val_2_0,
        matrix.val_2_1,
        matrix.val_2_2);
  }

  public Matrix3f(
      float val_0_0,
      float val_0_1,
      float val_0_2,
      float val_1_0,
      float val_1_1,
      float val_1_2,
      float val_2_0,
      float val_2_1,
      float val_2_2) {
    this.val_0_0 = val_0_0;
    this.val_0_1 = val_0_1;
    this.val_0_2 = val_0_2;
    this.val_1_0 = val_1_0;
    this.val_1_1 = val_1_1;
    this.val_1_2 = val_1_2;
    this.val_2_0 = val_2_0;
    this.val_2_1 = val_2_1;
    this.val_2_2 = val_2_2;
  }

  public void absoluteLocal() {
    val_0_0 = Math.abs(val_0_0);
    val_0_1 = Math.abs(val_0_1);
    val_0_2 = Math.abs(val_0_2);
    val_1_0 = Math.abs(val_1_0);
    val_1_1 = Math.abs(val_1_1);
    val_1_2 = Math.abs(val_1_2);
    val_2_0 = Math.abs(val_2_0);
    val_2_1 = Math.abs(val_2_1);
    val_2_2 = Math.abs(val_2_2);
  }

  public Vector3f mult(Vector3f vector, Vector3f result) {

    float x = vector.x;
    float y = vector.y;
    float z = vector.z;

    result.x = val_0_0 * x + val_0_1 * y + val_0_2 * z;
    result.y = val_1_0 * x + val_1_1 * y + val_1_2 * z;
    result.z = val_2_0 * x + val_2_1 * y + val_2_2 * z;

    return result;
  }

  public void set(
      float val_0_0,
      float val_0_1,
      float val_0_2,
      float val_1_0,
      float val_1_1,
      float val_1_2,
      float val_2_0,
      float val_2_1,
      float val_2_2) {
    this.val_0_0 = val_0_0;
    this.val_0_1 = val_0_1;
    this.val_0_2 = val_0_2;
    this.val_1_0 = val_1_0;
    this.val_1_1 = val_1_1;
    this.val_1_2 = val_1_2;
    this.val_2_0 = val_2_0;
    this.val_2_1 = val_2_1;
    this.val_2_2 = val_2_2;
  }

  /**
   * Sets this matrix from a quaternion rotation.
   *
   * @param rotation the quaternion to convert to a rotation matrix
   * @return this matrix
   * @since 10.0.0
   */
  public Matrix3f set(Quaternion4f rotation) {
    return rotation.toRotationMatrix(this);
  }

  @Override
  public String toString() {
    return val_0_0 + ", " + val_0_1 + ", " + val_0_2 + "\n" +
        val_1_0 + ", " + val_1_1 + ", " + val_1_2 + "\n" +
        val_2_0 + ", " + val_2_1 + ", " + val_2_2 + "\n";
  }
}
