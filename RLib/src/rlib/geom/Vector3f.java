package rlib.geom;

import static java.lang.Float.floatToIntBits;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import rlib.util.ExtMath;

/**
 * Реализация игровой позиции и вектора.
 *
 * @author JavaSaBr
 */
public final class Vector3f {

    /**
     * нулнвой вектор
     */
    public final static Vector3f ZERO = new Vector3f(0, 0, 0);

    /**
     * вектор в бесконечность
     */
    public final static Vector3f NAN = new Vector3f(Float.NaN, Float.NaN, Float.NaN);

    /**
     * вектор в сторону оси X
     */
    public final static Vector3f UNIT_X = new Vector3f(1, 0, 0);
    /**
     * вектор в сторону оси Y
     */
    public final static Vector3f UNIT_Y = new Vector3f(0, 1, 0);

    /**
     * вектор в сторону оси Z
     */
    public final static Vector3f UNIT_Z = new Vector3f(0, 0, 1);

    /**
     * вектор в сторону XYZ
     */
    public final static Vector3f UNIT_XYZ = new Vector3f(1, 1, 1);

    public final static Vector3f POSITIVE_INFINITY = new Vector3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
    public final static Vector3f NEGATIVE_INFINITY = new Vector3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

    /**
     * Проверка на валидность вектора.
     *
     * @param vector проверяемый вектор.
     * @return валиден ли вектор.
     */
    public static boolean isValidVector(@Nullable final Vector3f vector) {

        if (vector == null) {
            return false;
        } else if (Float.isNaN(vector.getX()) || Float.isNaN(vector.getY()) || Float.isNaN(vector.getZ())) {
            return false;
        } else if (Float.isInfinite(vector.getX()) || Float.isInfinite(vector.getY()) || Float.isInfinite(vector.getZ())) {
            return false;
        }

        return true;
    }

    @NotNull
    public static Vector3f newInstance() {
        return new Vector3f();
    }

    @NotNull
    public static Vector3f newInstance(final float x, final float y, final float z) {
        return new Vector3f(x, y, z);
    }

    @NotNull
    public static Vector3f newInstance(@NotNull final float[] vals) {
        return new Vector3f(vals[0], vals[1], vals[2]);
    }

    /**
     * Coords.
     */
    protected float x;
    protected float y;
    protected float z;

    private Vector3f() {
        super();
    }

    private Vector3f(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Локальное добавление координат к вектору.
     */
    @NotNull
    public Vector3f addLocal(final float addX, final float addY, final float addZ) {
        x += addX;
        y += addY;
        z += addZ;
        return this;
    }

    /**
     * Локальное добавление указанного вектора.
     */
    @NotNull
    public Vector3f addLocal(@NotNull final Vector3f vec) {
        return addLocal(vec.x, vec.y, vec.z);
    }

    /**
     * Вычисление векторного произведения текущего вектора на указанный.
     *
     * @param otherX модификатор x.
     * @param otherY модификатор y.
     * @param otherZ модификатор z.
     * @param result вектор для хранения результата.
     * @return результирующий веткор.
     */
    @NotNull
    public Vector3f cross(final float otherX, final float otherY, final float otherZ, @NotNull final Vector3f result) {

        final float resX = y * otherZ - z * otherY;
        final float resY = z * otherX - x * otherZ;
        final float resZ = x * otherY - y * otherX;

        result.set(resX, resY, resZ);

        return result;
    }

    /**
     * Вычисление векторного произведения текущего вектора на указанный.
     *
     * @param vector вектор, на который будет произведено произведение.
     * @return результирующий вектор.
     */
    @NotNull
    public Vector3f cross(@NotNull final Vector3f vector) {
        return cross(vector, newInstance());
    }

    /**
     * Вычисление векторного произведения текущего вектора на указанный.
     *
     * @param vector вектор, на который будет произведено произведение.
     * @param result вектор для хранения результата.
     * @return результирующий веткор.
     */
    @NotNull
    public Vector3f cross(@NotNull final Vector3f vector, @NotNull final Vector3f result) {
        return cross(vector.x, vector.y, vector.z, result);
    }

    /**
     * Вычислние векторного произведение этого вектора.
     *
     * @param otherX координата вектора, с которым нужно произвести произведение.
     * @param otherY координата вектора, с которым нужно произвести произведение.
     * @param otherZ координата вектора, с которым нужно произвести произведение.
     * @return этот же вектор.
     */
    @NotNull
    public Vector3f crossLocal(final float otherX, final float otherY, final float otherZ) {

        final float tempx = y * otherZ - z * otherY;
        final float tempy = z * otherX - x * otherZ;

        z = x * otherY - y * otherX;
        x = tempx;
        y = tempy;

        return this;
    }

    /**
     * Вычислние векторного произведение этого вектора.
     *
     * @param vector вектор, с которым нужно произвести произведение.
     * @return этот же вектор.
     */
    @NotNull
    public Vector3f crossLocal(@NotNull final Vector3f vector) {
        return crossLocal(vector.x, vector.y, vector.z);
    }

    /**
     * Рассчет расстояния до указанного вектора.
     *
     * @param vector целевой вектор.
     * @return расстояние до вектора.
     */
    public float distance(@NotNull final Vector3f vector) {
        return ExtMath.sqrt(distanceSquared(vector));
    }

    /**
     * Рассчет квадрата расстояния до указанного вектора.
     *
     * @param targetX координата.
     * @param targetY координата.
     * @param targetZ координата.
     * @return квадрат расстояния до вектора.
     */
    public float distanceSquared(final float targetX, final float targetY, final float targetZ) {

        final float dx = x - targetX;
        final float dy = y - targetY;
        final float dz = z - targetZ;

        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Рассчет квадрата расстояния до указанного вектора.
     *
     * @param vector целевой вектор.
     * @return квадрат расстояния до вектора.
     */
    public float distanceSquared(@NotNull final Vector3f vector) {
        return distanceSquared(vector.x, vector.y, vector.z);
    }

    /**
     * Вычисление скалярного произведение векторов.
     *
     * @param vector вектор, на который умножаем.
     * @return результат произведения.
     */
    public float dot(@NotNull final Vector3f vector) {
        return x * vector.x + y * vector.y + z * vector.z;
    }

    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        final Vector3f other = (Vector3f) obj;

        if (floatToIntBits(x) != floatToIntBits(other.x)) {
            return false;
        } else if (floatToIntBits(y) != floatToIntBits(other.y)) {
            return false;
        } else if (floatToIntBits(z) != floatToIntBits(other.z)) {
            return false;
        }

        return true;
    }

    public float getX() {
        return x;
    }

    @NotNull
    public Vector3f setX(final float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }

    @NotNull
    public Vector3f setY(final float y) {
        this.y = y;
        return this;
    }

    public float getZ() {
        return z;
    }

    @NotNull
    public Vector3f setZ(final float z) {
        this.z = z;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        result = prime * result + Float.floatToIntBits(z);
        return result;
    }

    /**
     * @return является ли вектор нулевым.
     */
    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    /**
     * Локальное умножение вектора.
     */
    public Vector3f multLocal(final float scalar) {
        return multLocal(scalar, scalar, scalar);
    }

    /**
     * Локальное умножение вектора.
     */
    @NotNull
    public Vector3f multLocal(final float x, final float y, final float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    /**
     * Локальное умножение вектора.
     */
    @NotNull
    public Vector3f multLocal(@NotNull final Vector3f vector) {
        return multLocal(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Получение противоположного вектора.
     */
    @NotNull
    public Vector3f negate() {
        return newInstance(-getX(), -getY(), -getZ());
    }

    /**
     * Изменение вектора на противоположное.
     */
    @NotNull
    public Vector3f negateLocal() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    /**
     * Конвектирование вектора в еденичный.
     */
    @NotNull
    public Vector3f normalize() {

        float length = x * x + y * y + z * z;

        if (length != 1F && length != 0F) {
            length = 1.0F / ExtMath.sqrt(length);
            return new Vector3f(x * length, y * length, z * length);
        }

        return new Vector3f(x, y, z);
    }

    /**
     * Конвектирование вектора в еденичный.
     */
    @NotNull
    public Vector3f normalizeLocal() {

        float length = x * x + y * y + z * z;

        if (length != 1f && length != 0f) {
            length = 1.0f / ExtMath.sqrt(length);
            x *= length;
            y *= length;
            z *= length;
        }

        return this;
    }

    @NotNull
    public Vector3f set(@NotNull final Vector3f vector) {
        return set(vector.getX(), vector.getY(), vector.getZ());
    }

    @NotNull
    public Vector3f set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Получение вектора разности между текущим и указанным.
     *
     * @param vector вектор, который нужно вычесть.
     * @param result результат вычитания.
     * @return разность между текущим и указанным вектором.
     */
    @NotNull
    public Vector3f subtract(@NotNull final Vector3f vector, @NotNull final Vector3f result) {
        result.x = x - vector.x;
        result.y = y - vector.y;
        result.z = z - vector.z;
        return result;
    }

    /**
     * Локальное вычитание вектора.
     *
     * @param subX вычитаемый X.
     * @param subY вычитаемый Y.
     * @param subZ вычитаемый Z.
     * @return итоговый вектор.
     */
    @NotNull
    public Vector3f subtractLocal(final float subX, final float subY, final float subZ) {
        x -= subX;
        y -= subY;
        z -= subZ;
        return this;
    }

    /**
     * Локальное вычитание вектора.
     *
     * @param vector вычитаемый вектор.
     * @return итоговый вектор.
     */
    @NotNull
    public Vector3f subtractLocal(@NotNull final Vector3f vector) {
        return subtractLocal(vector.x, vector.y, vector.z);
    }

    @Override
    public String toString() {
        return "Vector3f{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
