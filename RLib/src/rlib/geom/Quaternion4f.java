package rlib.geom;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import rlib.geom.util.AngleUtils;
import rlib.util.ExtMath;
import rlib.util.random.Random;
import rlib.util.random.RandomFactory;

/**
 * The implementation of rotation in 3D world based on Quaternion.
 *
 * @author JavaSaBr
 */
public class Quaternion4f {

    private static final ThreadLocal<Random> RANDOM_LOCAL = ThreadLocal.withInitial(RandomFactory::newFastRandom);
    private static final ThreadLocal<Quaternion4f> ROTATION_LOCAL = ThreadLocal.withInitial(Quaternion4f::newInstance);

    /**
     * @return the thread local instance.
     */
    public static Quaternion4f get() {
        return ROTATION_LOCAL.get();
    }

    public static Quaternion4f newInstance() {
        return new Quaternion4f();
    }

    public static Quaternion4f newInstance(final float angleX, final float angleY, final float angleZ) {
        return newInstance().fromAngles(angleX, angleY, angleZ);
    }

    public static Quaternion4f newInstance(final float x, final float y, final float z, final float w) {
        return new Quaternion4f(x, y, z, w);
    }

    public static Quaternion4f newInstance(final float[] vals) {
        return new Quaternion4f(vals[0], vals[1], vals[2], vals[3]);
    }

    private float x;
    private float y;
    private float z;
    private float w;

    private Quaternion4f() {
        w = 1;
    }

    private Quaternion4f(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

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
     */
    @NotNull
    public final Quaternion4f fromAngles(final float angleX, final float yAngle, final float zAngle) {

        float angle = zAngle * 0.5f;

        final float sinZ = ExtMath.sin(angle);
        final float cosZ = ExtMath.cos(angle);

        angle = yAngle * 0.5f;

        final float sinY = ExtMath.sin(angle);
        final float cosY = ExtMath.cos(angle);

        angle = angleX * 0.5f;

        final float sinX = ExtMath.sin(angle);
        final float cosX = ExtMath.cos(angle);

        final float cosYXcosZ = cosY * cosZ;
        final float sinYXsinZ = sinY * sinZ;
        final float cosYXsinZ = cosY * sinZ;
        final float sinYXcosZ = sinY * cosZ;

        w = cosYXcosZ * cosX - sinYXsinZ * sinX;
        x = cosYXcosZ * sinX + sinYXsinZ * cosX;
        y = sinYXcosZ * cosX + cosYXsinZ * sinX;
        z = cosYXsinZ * cosX - sinYXcosZ * sinX;

        normalizeLocal();
        return this;
    }

    /**
     * Calculate a rotation from angles.
     *
     * @param angles the angles.
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
     */
    @NotNull
    public Quaternion4f fromAxes(@NotNull final Vector3f axisX, @NotNull final Vector3f axisY, @NotNull final Vector3f axisZ) {
        return fromRotationMatrix(axisX.getX(), axisY.getX(), axisZ.getX(), axisX.getY(), axisY.getY(),
                axisZ.getY(), axisX.getZ(), axisY.getZ(), axisZ.getZ());
    }

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
     * Calculate a vector by a direction type.
     *
     * @param type  the direction type.
     * @param store the result container.
     * @return the calculated vector.
     */
    public Vector3f getVector(@NotNull final DirectionType type, @Nullable Vector3f store) {
        if (store == null) store = Vector3f.newInstance();

        float norm = norm();
        if (norm != 1.0f) norm = ExtMath.invSqrt(norm);

        final float xx = x * x * norm;
        final float xy = x * y * norm;
        final float xz = x * z * norm;
        final float xw = x * w * norm;
        final float yy = y * y * norm;
        final float yz = y * z * norm;
        final float yw = y * w * norm;
        final float zz = z * z * norm;
        final float zw = z * w * norm;

        switch (type) {
            case LEFT: {
                store.setX(1 - 2 * (yy + zz));
                store.setY(2 * (xy + zw));
                store.setZ(2 * (xz - yw));
                break;
            }
            case RIGHT: {
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
                store.negate();
                break;
            }
        }

        return store;
    }

    /**
     * @return степень разворота по оси w.
     */
    public final float getW() {
        return w;
    }

    /**
     * @param w степень разворота по оси w.
     */
    public final void setW(final float w) {
        this.w = w;
    }

    /**
     * @return степень разворота по оси х.
     */
    public final float getX() {
        return x;
    }

    /**
     * @param x степень разворота по оси х.
     */
    public final void setX(final float x) {
        this.x = x;
    }

    /**
     * @return степень разворота по оси y.
     */
    public final float getY() {
        return y;
    }

    /**
     * @param y степень разворота по оси y.
     */
    public final void setY(final float y) {
        this.y = y;
    }

    /**
     * @return степень разворота по оси z.
     */
    public final float getZ() {
        return z;
    }

    /**
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
     * Рассчитать разворот, который будет смотреть в указанном напралвении.
     *
     * @param direction направление, куда нужно смотреть.
     * @param up        вектор для ориаентации где верх а где низ.
     * @param buffer    буффер векторов для рассчета.
     */
    public void lookAt(@NotNull final Vector3f direction, @NotNull final Vector3f up, @NotNull final Vector3fBuffer buffer) {

        final Vector3f first = buffer.nextVector();
        first.set(direction).normalizeLocal();

        final Vector3f second = buffer.nextVector();
        second.set(up).crossLocal(direction).normalizeLocal();

        final Vector3f third = buffer.nextVector();
        third.set(direction).crossLocal(second).normalizeLocal();

        fromAxes(second, third, first);
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
     * @return норма этого разворота.
     */
    public float norm() {
        return w * w + x * x + y * y + z * z;
    }

    /**
     * Нормализация текущего разворота.
     */
    @NotNull
    public final Quaternion4f normalizeLocal() {

        final float norm = ExtMath.invSqrt(norm());

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
     */
    public void random(@NotNull final Random random) {

        final float x = AngleUtils.degreeToRadians(random.nextInt(0, 360));
        final float y = AngleUtils.degreeToRadians(random.nextInt(0, 360));
        final float z = AngleUtils.degreeToRadians(random.nextInt(0, 360));

        fromAngles(x, y, z);
    }

    @NotNull
    public Quaternion4f set(@NotNull final Quaternion4f rotation) {
        this.x = rotation.x;
        this.y = rotation.y;
        this.z = rotation.z;
        this.w = rotation.w;
        return this;
    }

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

    public Quaternion4f subtractLocal(@NotNull final Quaternion4f rotation) {
        this.x -= rotation.x;
        this.y -= rotation.y;
        this.z -= rotation.z;
        this.w -= rotation.w;
        return this;
    }

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
     * <code>toAngles</code> returns this quaternion converted to Euler rotation angles (yaw,roll,pitch).<br/> Note that
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

        result.set(1 - (yy + zz), xy - zw, xz + yw, xy + zw, 1 - (xx + zz), yz - xw, xz - yw, yz + xw, 1 - (xx + yy));
        return result;
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