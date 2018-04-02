package com.ss.rlib.geom;

import static java.lang.Float.floatToIntBits;
import static java.lang.Float.isInfinite;
import static java.lang.Float.isNaN;
import com.ss.rlib.util.ExtMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of vector with 3 float values.
 *
 * @author JavaSaBr
 */
public final class Vector3f {

    /**
     * The constant ZERO.
     */
    @NotNull
    public final static Vector3f ZERO = new Vector3f(0, 0, 0);

    /**
     * The constant NAN.
     */
    @NotNull
    public final static Vector3f NAN = new Vector3f(Float.NaN, Float.NaN, Float.NaN);

    /**
     * The constant UNIT_X.
     */
    @NotNull
    public final static Vector3f UNIT_X = new Vector3f(1, 0, 0);

    /**
     * The constant UNIT_Y.
     */
    @NotNull
    public final static Vector3f UNIT_Y = new Vector3f(0, 1, 0);

    /**
     * The constant UNIT_Z.
     */
    @NotNull
    public final static Vector3f UNIT_Z = new Vector3f(0, 0, 1);

    /**
     * The constant UNIT_XYZ.
     */
    @NotNull
    public final static Vector3f UNIT_XYZ = new Vector3f(1, 1, 1);

    /**
     * The constant POSITIVE_INFINITY.
     */
    @NotNull
    public final static Vector3f POSITIVE_INFINITY = new Vector3f(Float.POSITIVE_INFINITY,
            Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);

    /**
     * The constant NEGATIVE_INFINITY.
     */
    @NotNull
    public final static Vector3f NEGATIVE_INFINITY = new Vector3f(Float.NEGATIVE_INFINITY,
            Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);


    /**
     * Create a new instance of the vector.
     *
     * @return the new instance.
     */
    public @NotNull static Vector3f newInstance() {
        return new Vector3f();
    }

    /**
     * Return true if the vector is valid.
     *
     * @param vector the vector.
     * @return true if the vector is valid.
     */
    public static boolean isValidVector(@Nullable final Vector3f vector) {

        if (vector == null) {
            return false;
        } else if (isNaN(vector.getX()) || isNaN(vector.getY()) || isNaN(vector.getZ())) {
            return false;
        } else if (isInfinite(vector.getX()) || isInfinite(vector.getY()) || isInfinite(vector.getZ())) {
            return false;
        }

        return true;
    }


    /**
     * Create a new instance of the vector.
     *
     * @param x the x value.
     * @param y the y value.
     * @param z the z value.
     * @return the new instance.
     */
    public static @NotNull Vector3f newInstance(final float x, final float y, final float z) {
        return new Vector3f(x, y, z);
    }

    /**
     * Create a new instance of the vector.
     *
     * @param values the array with values.
     * @return the new instance.
     */
    public static @NotNull Vector3f newInstance(@NotNull final float[] values) {
        return new Vector3f(values[0], values[1], values[2]);
    }

    /**
     * The X component.
     */
    protected float x;

    /**
     * The Y component.
     */
    protected float y;

    /**
     * The Z component.
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
     * Add the values to the current vector.
     *
     * @param addX the add x.
     * @param addY the add y.
     * @param addZ the add z.
     * @return the current vector.
     */
    public @NotNull Vector3f addLocal(final float addX, final float addY, final float addZ) {
        x += addX;
        y += addY;
        z += addZ;
        return this;
    }

    /**
     * Add the vector to the current vector.
     *
     * @param vector the vector.
     * @return the current vector.
     */
    public @NotNull Vector3f addLocal(@NotNull final Vector3f vector) {
        return addLocal(vector.x, vector.y, vector.z);
    }

    /**
     * Calculate a cross vector between the current vector and the coords.
     *
     * @param otherX the other x
     * @param otherY the other y
     * @param otherZ the other z
     * @param result the result vector.
     * @return the result vector.
     */
    public @NotNull Vector3f cross(
            final float otherX,
            final float otherY,
            final float otherZ,
            @NotNull final Vector3f result
    ) {

        final float resX = y * otherZ - z * otherY;
        final float resY = z * otherX - x * otherZ;
        final float resZ = x * otherY - y * otherX;

        result.set(resX, resY, resZ);

        return result;
    }

    /**
     * Calculate a cross vector between the current vector and the vector.
     *
     * @param vector the vector.
     * @return the result vector.
     */
    public @NotNull Vector3f cross(@NotNull final Vector3f vector) {
        return cross(vector, newInstance());
    }

    /**
     * Calculate a cross vector between the current vector and the vector.
     *
     * @param vector the vector.
     * @param result the result vector to store result.
     * @return the result vector.
     */
    public @NotNull Vector3f cross(@NotNull final Vector3f vector, @NotNull final Vector3f result) {
        return cross(vector.x, vector.y, vector.z, result);
    }

    /**
     * Calculate a cross vector between the current vector and the target coordinates.
     *
     * @param otherX the other x.
     * @param otherY the other y.
     * @param otherZ the other z.
     * @return this changed vector.
     */
    public @NotNull Vector3f crossLocal(final float otherX, final float otherY, final float otherZ) {

        final float tempx = y * otherZ - z * otherY;
        final float tempy = z * otherX - x * otherZ;

        z = x * otherY - y * otherX;
        x = tempx;
        y = tempy;

        return this;
    }

    /**
     * Calculate a cross vector between the current vector and the target coordinates.
     *
     * @param vector the vector.
     * @return this changed vector.
     */
    public @NotNull Vector3f crossLocal(@NotNull final Vector3f vector) {
        return crossLocal(vector.x, vector.y, vector.z);
    }

    /**
     * Calculate distance to the vector.
     *
     * @param vector the vector.
     * @return the distance.
     */
    public float distance(@NotNull final Vector3f vector) {
        return ExtMath.sqrt(distanceSquared(vector));
    }

    /**
     * Calculate squared distance to the coords.
     *
     * @param targetX the target x.
     * @param targetY the target y.
     * @param targetZ the target z.
     * @return the squared distance.
     */
    public float distanceSquared(final float targetX, final float targetY, final float targetZ) {

        final float dx = x - targetX;
        final float dy = y - targetY;
        final float dz = z - targetZ;

        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Calculate squared distance to the vector.
     *
     * @param vector the vector.
     * @return the squared distance.
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
     * Get the X component.
     *
     * @return the X component.
     */
    public float getX() {
        return x;
    }

    /**
     * Set the X component.
     *
     * @param x the X component.
     * @return this vector.
     */
    public @NotNull Vector3f setX(final float x) {
        this.x = x;
        return this;
    }

    /**
     * Get the Y component.
     *
     * @return the Y component.
     */
    public float getY() {
        return y;
    }

    /**
     * Set the Y component,
     *
     * @param y the Y component.
     * @return this vector.
     */
    public @NotNull Vector3f setY(final float y) {
        this.y = y;
        return this;
    }

    /**
     * Get the Z component.
     *
     * @return the Z component.
     */
    public float getZ() {
        return z;
    }

    /**
     * Set the Z component,
     *
     * @param z the Z component.
     * @return this vector.
     */
    public @NotNull Vector3f setZ(final float z) {
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
     * Return true if all components are zero.
     *
     * @return true if all components are zero.
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
    public @NotNull Vector3f multLocal(final float x, final float y, final float z) {
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
    public @NotNull Vector3f multLocal(@NotNull final Vector3f vector) {
        return multLocal(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Negate vector 3 f.
     *
     * @return the vector 3 f
     */
    public @NotNull Vector3f negate() {
        return newInstance(-getX(), -getY(), -getZ());
    }

    /**
     * Negate local vector 3 f.
     *
     * @return the vector 3 f
     */
    public @NotNull Vector3f negateLocal() {
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
    public @NotNull Vector3f normalize() {

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
    public @NotNull Vector3f normalizeLocal() {

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
    public @NotNull Vector3f set(@NotNull final Vector3f vector) {
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
    public @NotNull Vector3f set(final float x, final float y, final float z) {
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
    public @NotNull Vector3f subtract(@NotNull final Vector3f vector, @NotNull final Vector3f result) {
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
    public @NotNull Vector3f subtractLocal(final float subX, final float subY, final float subZ) {
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
    public @NotNull Vector3f subtractLocal(@NotNull final Vector3f vector) {
        return subtractLocal(vector.x, vector.y, vector.z);
    }
    
    /**
     * Return vector length (magnitude).
     * 
     * @return length
     */
    public float length() {
        return ExtMath.sqrt(x * x + y * y + z * z);
    }
    
    /**
     * Divide local vector 3 f.
     *
     * @param x the divider x
     * @param y the divider y
     * @param z the divider z
     * @return the vector 3 f
     */
    public @NotNull Vector3f divideLocal(final float x, final float y, final float z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }
    
    /**
     * Divide local vector 3 f.
     *
     * @param vector the divider vector
     * @return the vector 3 f
     */
    public @NotNull Vector3f divideLocal(@NotNull final Vector3f vector) {
        return divideLocal(vector.x, vector.y, vector.z);
    }
    
    /**
     * Divide local vector 3 f.
     *
     * @param scalar the divider scalar
     * @return the vector 3 f
     */
    public @NotNull Vector3f divideLocal(final float scalar) {
        return divideLocal(scalar, scalar, scalar);
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
