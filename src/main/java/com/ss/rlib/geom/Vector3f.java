package com.ss.rlib.geom;

import static java.lang.Float.floatToIntBits;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ss.rlib.util.ExtMath;

/**
 * The type Vector 3 f.
 */
public final class Vector3f {

    /**
     * The constant ZERO.
     */
    public final static Vector3f ZERO = new Vector3f(0, 0, 0);

    /**
     * The constant NAN.
     */
    public final static Vector3f NAN = new Vector3f(Float.NaN, Float.NaN, Float.NaN);

    /**
     * The constant UNIT_X.
     */
    public final static Vector3f UNIT_X = new Vector3f(1, 0, 0);
    /**
     * The constant UNIT_Y.
     */
    public final static Vector3f UNIT_Y = new Vector3f(0, 1, 0);

    /**
     * The constant UNIT_Z.
     */
    public final static Vector3f UNIT_Z = new Vector3f(0, 0, 1);

    /**
     * The constant UNIT_XYZ.
     */
    public final static Vector3f UNIT_XYZ = new Vector3f(1, 1, 1);

    /**
     * The constant POSITIVE_INFINITY.
     */
    public final static Vector3f POSITIVE_INFINITY = new Vector3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
    /**
     * The constant NEGATIVE_INFINITY.
     */
    public final static Vector3f NEGATIVE_INFINITY = new Vector3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

    /**
     * Is valid vector boolean.
     *
     * @param vector the vector
     * @return the boolean
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

    /**
     * New instance vector 3 f.
     *
     * @return the vector 3 f
     */
    @NotNull
    public static Vector3f newInstance() {
        return new Vector3f();
    }

    /**
     * New instance vector 3 f.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the vector 3 f
     */
    @NotNull
    public static Vector3f newInstance(final float x, final float y, final float z) {
        return new Vector3f(x, y, z);
    }

    /**
     * New instance vector 3 f.
     *
     * @param vals the vals
     * @return the vector 3 f
     */
    @NotNull
    public static Vector3f newInstance(@NotNull final float[] vals) {
        return new Vector3f(vals[0], vals[1], vals[2]);
    }

    /**
     * The X.
     */
    protected float x;
    /**
     * The Y.
     */
    protected float y;
    /**
     * The Z.
     */
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
     * Add local vector 3 f.
     *
     * @param addX the add x
     * @param addY the add y
     * @param addZ the add z
     * @return the vector 3 f
     */
    @NotNull
    public Vector3f addLocal(final float addX, final float addY, final float addZ) {
        x += addX;
        y += addY;
        z += addZ;
        return this;
    }

    /**
     * Add local vector 3 f.
     *
     * @param vec the vec
     * @return the vector 3 f
     */
    @NotNull
    public Vector3f addLocal(@NotNull final Vector3f vec) {
        return addLocal(vec.x, vec.y, vec.z);
    }

    /**
     * Cross vector 3 f.
     *
     * @param otherX the other x
     * @param otherY the other y
     * @param otherZ the other z
     * @param result the result
     * @return the vector 3 f
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
     * Cross vector 3 f.
     *
     * @param vector the vector
     * @return the vector 3 f
     */
    @NotNull
    public Vector3f cross(@NotNull final Vector3f vector) {
        return cross(vector, newInstance());
    }

    /**
     * Cross vector 3 f.
     *
     * @param vector the vector
     * @param result the result
     * @return the vector 3 f
     */
    @NotNull
    public Vector3f cross(@NotNull final Vector3f vector, @NotNull final Vector3f result) {
        return cross(vector.x, vector.y, vector.z, result);
    }

    /**
     * Cross local vector 3 f.
     *
     * @param otherX the other x
     * @param otherY the other y
     * @param otherZ the other z
     * @return the vector 3 f
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
     * Cross local vector 3 f.
     *
     * @param vector the vector
     * @return the vector 3 f
     */
    @NotNull
    public Vector3f crossLocal(@NotNull final Vector3f vector) {
        return crossLocal(vector.x, vector.y, vector.z);
    }

    /**
     * Distance float.
     *
     * @param vector the vector
     * @return the float
     */
    public float distance(@NotNull final Vector3f vector) {
        return ExtMath.sqrt(distanceSquared(vector));
    }

    /**
     * Distance squared float.
     *
     * @param targetX the target x
     * @param targetY the target y
     * @param targetZ the target z
     * @return the float
     */
    public float distanceSquared(final float targetX, final float targetY, final float targetZ) {

        final float dx = x - targetX;
        final float dy = y - targetY;
        final float dz = z - targetZ;

        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Distance squared float.
     *
     * @param vector the vector
     * @return the float
     */
    public float distanceSquared(@NotNull final Vector3f vector) {
        return distanceSquared(vector.x, vector.y, vector.z);
    }

    /**
     * Dot float.
     *
     * @param vector the vector
     * @return the float
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

    /**
     * Gets x.
     *
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * Sets x.
     *
     * @param x the x
     * @return the x
     */
    @NotNull
    public Vector3f setX(final float x) {
        this.x = x;
        return this;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * Sets y.
     *
     * @param y the y
     * @return the y
     */
    @NotNull
    public Vector3f setY(final float y) {
        this.y = y;
        return this;
    }

    /**
     * Gets z.
     *
     * @return the z
     */
    public float getZ() {
        return z;
    }

    /**
     * Sets z.
     *
     * @param z the z
     * @return the z
     */
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
     * Is zero boolean.
     *
     * @return the boolean
     */
    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    /**
     * Mult local vector 3 f.
     *
     * @param scalar the scalar
     * @return the vector 3 f
     */
    public Vector3f multLocal(final float scalar) {
        return multLocal(scalar, scalar, scalar);
    }

    /**
     * Mult local vector 3 f.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the vector 3 f
     */
    @NotNull
    public Vector3f multLocal(final float x, final float y, final float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    /**
     * Mult local vector 3 f.
     *
     * @param vector the vector
     * @return the vector 3 f
     */
    @NotNull
    public Vector3f multLocal(@NotNull final Vector3f vector) {
        return multLocal(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Negate vector 3 f.
     *
     * @return the vector 3 f
     */
    @NotNull
    public Vector3f negate() {
        return newInstance(-getX(), -getY(), -getZ());
    }

    /**
     * Negate local vector 3 f.
     *
     * @return the vector 3 f
     */
    @NotNull
    public Vector3f negateLocal() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    /**
     * Normalize vector 3 f.
     *
     * @return the vector 3 f
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
     * Normalize local vector 3 f.
     *
     * @return the vector 3 f
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

    /**
     * Set vector 3 f.
     *
     * @param vector the vector
     * @return the vector 3 f
     */
    @NotNull
    public Vector3f set(@NotNull final Vector3f vector) {
        return set(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Set vector 3 f.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the vector 3 f
     */
    @NotNull
    public Vector3f set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Subtract vector 3 f.
     *
     * @param vector the vector
     * @param result the result
     * @return the vector 3 f
     */
    @NotNull
    public Vector3f subtract(@NotNull final Vector3f vector, @NotNull final Vector3f result) {
        result.x = x - vector.x;
        result.y = y - vector.y;
        result.z = z - vector.z;
        return result;
    }

    /**
     * Subtract local vector 3 f.
     *
     * @param subX the sub x
     * @param subY the sub y
     * @param subZ the sub z
     * @return the vector 3 f
     */
    @NotNull
    public Vector3f subtractLocal(final float subX, final float subY, final float subZ) {
        x -= subX;
        y -= subY;
        z -= subZ;
        return this;
    }

    /**
     * Subtract local vector 3 f.
     *
     * @param vector the vector
     * @return the vector 3 f
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
