package com.ss.rlib.common.geom;

import com.ss.rlib.common.geom.util.AngleUtils;
import com.ss.rlib.common.util.ExtMath;
import com.ss.rlib.common.util.random.Random;
import com.ss.rlib.common.util.random.RandomFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of rotation in 3D world based on Quaternion.
 *
 * @author JavaSaBr
 */
public class Quaternion4f {

    public static final Quaternion4f IDENTITY = new Quaternion4f(0, 0, 0, 1);
    
    private static final ThreadLocal<Random> RANDOM_LOCAL =
            ThreadLocal.withInitial(RandomFactory::newFastRandom);

    private static final ThreadLocal<Quaternion4f> ROTATION_LOCAL =
            ThreadLocal.withInitial(Quaternion4f::new);

    /**
     * Get the thread local instance.
     *
     * @return the thread local instance.
     */
    public static Quaternion4f get() {
        return ROTATION_LOCAL.get();
    }

    /**
     * The X component.
     */
    private float x;

    /**
     * The Y component.
     */
    private float y;

    /**
     * The Z component.
     */
    private float z;

    /**
     * The W component.
     */
    private float w;

    public Quaternion4f() {
        w = 1;
    }

    public Quaternion4f(float[] vals) {
        this(vals[0], vals[1], vals[2], vals[3]);
    }

    public Quaternion4f(float angleX, float angleY, float angleZ) {
        fromAngles(angleX, angleY, angleZ);
    }

    public Quaternion4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Add local quaternion 4 f.
     *
     * @param rotation the rotation
     * @return the quaternion 4 f
     */
    @NotNull
    public Quaternion4f addLocal(@NotNull final Quaternion4f rotation) {
        this.x += rotation.x;
        this.y += rotation.y;
        this.z += rotation.z;
        this.w += rotation.w;
        return this;
    }

    /**
     * Рассчет косинуса угла между текущим и указанным разворотом.
     *
     * @param rotation сверяемый разворот.
     * @return косинус угла между 2мя разворотами.
     */
    public float dot(@NotNull final Quaternion4f rotation) {
        return w * rotation.w + x * rotation.x + y * rotation.y + z * rotation.z;
    }

    @Override
    public final boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        final Quaternion4f other = (Quaternion4f) obj;

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

    /**
     * Расчет разворота по углам в 3х осях.
     *
     * @param angleX угол по оси X.
     * @param yAngle угол по оси Y.
     * @param zAngle угол по оси Z.
     * @return the quaternion 4 f
     */

    public final @NotNull Quaternion4f fromAngles(float angleX, float yAngle, float zAngle) {

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

    /**
     * Calculate a rotation from angles.
     *
     * @param angles the angles.
     * @return the quaternion 4 f
     */
    @NotNull
    public final Quaternion4f fromAngles(final float[] angles) {
        return fromAngles(angles[0], angles[1], angles[2]);
    }

    /**
     * <code>fromAxes</code> creates a <code>Quaternion</code> that represents the coordinate system defined by three
     * axes. These axes are assumed to be orthogonal and no error checking is applied. Thus, the user must insure that
     * the three axes being provided indeed represents a proper right handed coordinate system.
     *
     * @param axisX vector representing the x-axis of the coordinate system.
     * @param axisY vector representing the y-axis of the coordinate system.
     * @param axisZ vector representing the z-axis of the coordinate system.
     * @return the quaternion 4 f
     */
    @NotNull
    public Quaternion4f fromAxes(@NotNull final Vector3f axisX, @NotNull final Vector3f axisY, @NotNull final Vector3f axisZ) {
        return fromRotationMatrix(axisX.getX(), axisY.getX(), axisZ.getX(), axisX.getY(), axisY.getY(),
                axisZ.getY(), axisX.getZ(), axisY.getZ(), axisZ.getZ());
    }

    /**
     * From rotation matrix quaternion 4 f.
     *
     * @param val_0_0 the val 0 0
     * @param val_0_1 the val 0 1
     * @param val_0_2 the val 0 2
     * @param val_1_0 the val 1 0
     * @param val_1_1 the val 1 1
     * @param val_1_2 the val 1 2
     * @param val_2_0 the val 2 0
     * @param val_2_1 the val 2 1
     * @param val_2_2 the val 2 2
     * @return the quaternion 4 f
     */
    @NotNull
    public Quaternion4f fromRotationMatrix(final float val_0_0, final float val_0_1, final float val_0_2,
                                           final float val_1_0, final float val_1_1, final float val_1_2,
                                           final float val_2_0, final float val_2_1, final float val_2_2) {

        final float t = val_0_0 + val_1_1 + val_2_2;

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

    /**
     * Calculate a vector for the direction type.
     *
     * @param type the direction type.
     * @return the calculated vector.
     */
    public @NotNull Vector3f getDirection(@NotNull DirectionType type) {
        return getDirection(type, null);
    }

    /**
     * Calculate a vector for the direction type.
     *
     * @param type  the direction type.
     * @param store the vector to store result.
     * @return the calculated vector.
     */
    public @NotNull Vector3f getDirection(@NotNull DirectionType type, @Nullable Vector3f store) {

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
                store.setX(1 - 2 * (yy + zz));
                store.setY(2 * (xy + zw));
                store.setZ(2 * (xz - yw));
                break;
            }
            case LEFT: {
                store.setX(1 - 2 * (yy + zz));
                store.setY(2 * (xy + zw));
                store.setZ(2 * (xz - yw));
                store.negateLocal();
                break;
            }
            case UP: {
                store.setX(2 * (xy - zw));
                store.setY(1 - 2 * (xx + zz));
                store.setZ(2 * (yz + xw));
                break;
            }
            case DOWN: {
                store.setX(2 * (xy - zw));
                store.setY(1 - 2 * (xx + zz));
                store.setZ(2 * (yz + xw));
                store.negateLocal();
                break;
            }
            case FRONT: {
                store.setX(2 * (xz + yw));
                store.setY(2 * (yz - xw));
                store.setZ(1 - 2 * (xx + yy));
                break;
            }
            case BEHIND: {
                store.setX(2 * (xz + yw));
                store.setY(2 * (yz - xw));
                store.setZ(1 - 2 * (xx + yy));
                store.negateLocal();
                break;
            }
        }

        return store;
    }

    /**
     * Gets w.
     *
     * @return степень разворота по оси w.
     */
    public final float getW() {
        return w;
    }

    /**
     * Sets w.
     *
     * @param w степень разворота по оси w.
     */
    public final void setW(final float w) {
        this.w = w;
    }

    /**
     * Gets x.
     *
     * @return степень разворота по оси х.
     */
    public final float getX() {
        return x;
    }

    /**
     * Sets x.
     *
     * @param x степень разворота по оси х.
     */
    public final void setX(final float x) {
        this.x = x;
    }

    /**
     * Gets y.
     *
     * @return степень разворота по оси y.
     */
    public final float getY() {
        return y;
    }

    /**
     * Sets y.
     *
     * @param y степень разворота по оси y.
     */
    public final void setY(final float y) {
        this.y = y;
    }

    /**
     * Gets z.
     *
     * @return степень разворота по оси z.
     */
    public final float getZ() {
        return z;
    }

    /**
     * Sets z.
     *
     * @param z степень разворота по оси z.
     */
    public final void setZ(final float z) {
        this.z = z;
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

    /**
     * Calculate the value of the quaternion, which will look in the specified direction.
     *
     * @param direction the direction.
     * @param up        the vector of orientation where is top.
     */
    public void lookAt(@NotNull Vector3f direction, @NotNull Vector3f up) {
        lookAt(direction, up, Vector3fBuffer.NO_REUSE);
    }

    /**
     * Calculate the value of the quaternion, which will look in the specified direction.
     *
     * @param direction the direction.
     * @param up        the vector of orientation where is top.
     * @param buffer    the vector's buffer.
     */
    public void lookAt(@NotNull Vector3f direction, @NotNull Vector3f up, @NotNull Vector3fBuffer buffer) {

        var axisZ = buffer.next(direction)
                .normalizeLocal();

        var axisX = buffer.next(up)
                .crossLocal(direction)
                .normalizeLocal();

        var axisY = buffer.next(direction)
                .crossLocal(axisX)
                .normalizeLocal();

        fromAxes(axisX, axisY, axisZ);

        normalizeLocal();
    }

    /**
     * Приминение разворота на вектор.
     *
     * @param vector вектор, который надо развернуть.
     * @return полученный вектор.
     */
    @NotNull
    public final Vector3f multLocal(@NotNull final Vector3f vector) {

        final float vectorX = vector.getX();
        final float vectorY = vector.getY();
        final float vectorZ = vector.getZ();

        final float x = getX();
        final float y = getY();
        final float z = getZ();
        final float w = getW();

        vector.setX(w * w * vectorX + 2 * y * w * vectorZ - 2 * z * w * vectorY + x * x * vectorX + 2 * y * x * vectorY + 2 * z * x * vectorZ - z * z * vectorX - y * y * vectorX);
        vector.setY(2 * x * y * vectorX + y * y * vectorY + 2 * z * y * vectorZ + 2 * w * z * vectorX - z * z * vectorY + w * w * vectorY - 2 * x * w * vectorZ - x * x * vectorY);
        vector.setZ(2 * x * z * vectorX + 2 * y * z * vectorY + z * z * vectorZ - 2 * w * y * vectorX - y * y * vectorZ + 2 * w * x * vectorY - x * x * vectorZ + w * w * vectorZ);

        return vector;
    }

    /**
     * Negate local quaternion 4 f.
     *
     * @return перевернуть значения.
     */
    public final Quaternion4f negateLocal() {
        x = -x;
        y = -y;
        z = -z;
        w = -w;
        return this;
    }

    /**
     * Norm float.
     *
     * @return норма этого разворота.
     */
    public float norm() {
        return w * w + x * x + y * y + z * z;
    }

    /**
     * Normalizing of the current quaternion
     *
     * @return this quaternion.
     */
    public final @NotNull Quaternion4f normalizeLocal() {

        var norm = ExtMath.invSqrt(norm());

        x *= norm;
        y *= norm;
        z *= norm;
        w *= norm;

        return this;
    }

    /**
     * Создание случайного разворота.
     */
    public void random() {
        random(RANDOM_LOCAL.get());
    }

    /**
     * Создание случайного разворота.
     *
     * @param random the random
     */
    public void random(@NotNull final Random random) {

        final float x = AngleUtils.degreeToRadians(random.nextInt(0, 360));
        final float y = AngleUtils.degreeToRadians(random.nextInt(0, 360));
        final float z = AngleUtils.degreeToRadians(random.nextInt(0, 360));

        fromAngles(x, y, z);
    }

    /**
     * Set quaternion 4 f.
     *
     * @param rotation the rotation
     * @return the quaternion 4 f
     */
    @NotNull
    public Quaternion4f set(@NotNull final Quaternion4f rotation) {
        this.x = rotation.x;
        this.y = rotation.y;
        this.z = rotation.z;
        this.w = rotation.w;
        return this;
    }

    /**
     * Sets xyzw.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @param w the w
     */
    public void setXYZW(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Рассчитывает промежуточный разворот между текуим и указанным в зависимости от указанного %.
     *
     * @param end     конечный разворот.
     * @param percent % разворота от текущего к конечному.
     */
    public void slerp(@NotNull final Quaternion4f end, final float percent) {
        if (equals(end)) return;

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

            final float theta = ExtMath.acos(result);
            final float invSinTheta = 1f / ExtMath.sin(theta);

            scale0 = ExtMath.sin((1 - percent) * theta) * invSinTheta;
            scale1 = ExtMath.sin(percent * theta) * invSinTheta;
        }

        x = scale0 * x + scale1 * end.x;
        y = scale0 * y + scale1 * end.y;
        z = scale0 * z + scale1 * end.z;
        w = scale0 * w + scale1 * end.w;
    }

    /**
     * Рассчитывает промежуточный разворот от указанного стартового, до указанного конечного в зависимости от указанного
     * %.
     *
     * @param start   стартовый разворот.
     * @param end     конечный разворот.
     * @param percent % разворота от стартового до конечного.
     * @return the quaternion 4 f
     */
    @NotNull
    public Quaternion4f slerp(@NotNull final Quaternion4f start, @NotNull final Quaternion4f end, final float percent) {
        return slerp(start, end, percent, false);
    }

    /**
     * Рассчитывает промежуточный разворот от указанного стартового, до указанного конечного в зависимости от указанного
     * %.
     *
     * @param start       стартовый разворот.
     * @param end         конечный разворот.
     * @param percent     % разворота от стартового до конечного.
     * @param forceLinear принудительное использование линейной интерполяции.
     * @return the quaternion 4 f
     */
    @NotNull
    public final Quaternion4f slerp(@NotNull final Quaternion4f start, @NotNull final Quaternion4f end,
                                    final float percent, final boolean forceLinear) {

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

            final float theta = ExtMath.acos(result);
            final float invSinTheta = 1f / ExtMath.sin(theta);

            startScale = ExtMath.sin((1 - percent) * theta) * invSinTheta;
            endScale = ExtMath.sin(percent * theta) * invSinTheta;
        }

        this.x = startScale * start.getX() + endScale * end.getX();
        this.y = startScale * start.getY() + endScale * end.getY();
        this.z = startScale * start.getZ() + endScale * end.getZ();
        this.w = startScale * start.getW() + endScale * end.getW();

        return this;
    }

    /**
     * Рассчитывает промежуточный разворот от указанного стартового, до указанного конечного в зависимости от указанного
     * %.
     *
     * @param targetRotation конечный разворот.
     * @param percent        % разворота от стартового до конечного.
     */
    public void nlerp(@NotNull final Quaternion4f targetRotation, float percent) {

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

    /**
     * Subtract local quaternion 4 f.
     *
     * @param rotation the rotation
     * @return the quaternion 4 f
     */
    public Quaternion4f subtractLocal(@NotNull final Quaternion4f rotation) {
        this.x -= rotation.x;
        this.y -= rotation.y;
        this.z -= rotation.z;
        this.w -= rotation.w;
        return this;
    }

    /**
     * To angle axis float.
     *
     * @param axisStore the axis store
     * @return the float
     */
    public final float toAngleAxis(@Nullable final Vector3f axisStore) {

        final float sqrLength = x * x + y * y + z * z;
        float angle;

        if (sqrLength == 0.0f) {

            angle = 0.0f;

            if (axisStore != null) {
                axisStore.setX(1.0F);
                axisStore.setY(0.0F);
                axisStore.setZ(0.0F);
            }

        } else {

            angle = 2.0f * ExtMath.acos(w);

            if (axisStore != null) {

                final float invLength = 1.0f / ExtMath.sqrt(sqrLength);

                axisStore.setX(x * invLength);
                axisStore.setY(y * invLength);
                axisStore.setZ(z * invLength);
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
     * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm">http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm</a>
     */
    @NotNull
    public float[] toAngles(@Nullable float[] angles) {

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

    /**
     * Конвектирование квантерниона в матрицу 3х3
     *
     * @param result матрица, в которую занести нужно результат.
     * @return результат в виде матрицы.
     */
    @NotNull
    public final Matrix3f toRotationMatrix(@NotNull final Matrix3f result) {

        final float norm = norm();

        final float s = norm == 1f ? 2f : norm > 0f ? 2f / norm : 0;

        final float x = getX();
        final float y = getY();
        final float z = getZ();
        final float w = getW();

        final float xs = x * s;
        final float ys = y * s;
        final float zs = z * s;
        final float xx = x * xs;
        final float xy = x * ys;
        final float xz = x * zs;
        final float xw = w * xs;
        final float yy = y * ys;
        final float yz = y * zs;
        final float yw = w * ys;
        final float zz = z * zs;
        final float zw = w * zs;

        result.set(
                1 - (yy + zz), xy - zw, xz + yw,
                xy + zw, 1 - (xx + zz), yz - xw,
                xz - yw, yz + xw, 1 - (xx + yy)
        );

        return result;
    }

    /**
     * Rotate the vector by this quaternion.
     *
     * @param vector the vector to rotate.
     * @return the same vector which was rotated.
     */
    public @NotNull Vector3f rotate(@NotNull Vector3f vector) {

        float px = vector.x;
        float py = vector.y;
        float pz = vector.z;
        
        float norm = norm();
        float s = norm == 1f ? 2f : norm > 0f ? 2f / norm : 0;
        
        float x = getX() * s;
        float y = getY() * s;
        float z = getZ() * s;
        float xx = getX() * x;
        float xy = getX() * y;
        float xz = getX() * z;
        float xw = getW() * x;
        float yy = getY() * y;
        float yz = getY() * z;
        float yw = getW() * y;
        float zz = getZ() * z;
        float zw = getW() * z;
        
        vector.setX((1f - (yy + zz)) * px + (xy - zw) * py + (xz + yw) * pz);
        vector.setY((xy + zw) * px + (1f - (xx + zz)) * py + (yz - xw) * pz);
        vector.setZ((xz - yw) * px + (yz - xw) * py + (1f - (xx + yy)) * pz);

        return vector;
    }

    @Override
    public String toString() {
        return "Quaternion4f{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }
}
