package com.ss.rlib.common.geom;

import static java.lang.Float.*;
import com.ss.rlib.common.util.ExtMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of vector with 3 float values.
 *
 * @author JavaSaBr
 */
public final class Vector3f {

    public final static Vector3f ZERO = new Vector3f(0, 0, 0);
    public final static Vector3f NAN = new Vector3f(Float.NaN, Float.NaN, Float.NaN);

    public final static Vector3f UNIT_X = new Vector3f(1, 0, 0);
    public final static Vector3f UNIT_Y = new Vector3f(0, 1, 0);
    public final static Vector3f UNIT_Z = new Vector3f(0, 0, 1);
    public final static Vector3f UNIT_XYZ = new Vector3f(1, 1, 1);

    public final static Vector3f POSITIVE_INFINITY = new Vector3f(Float.POSITIVE_INFINITY,
            Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);

    public final static Vector3f NEGATIVE_INFINITY = new Vector3f(Float.NEGATIVE_INFINITY,
            Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

    /**
     * Return true if the vector is valid.
     *
     * @param vector the vector.
     * @return true if the vector is valid.
     */
    public static boolean isValidVector(@Nullable Vector3f vector) {

        if (vector == null) {
            return false;
        } else if (isNaN(vector.getX()) || isNaN(vector.getY()) || isNaN(vector.getZ())) {
            return false;
        } else {
            return !isInfinite(vector.getX()) && !isInfinite(vector.getY()) && !isInfinite(vector.getZ());
        }
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

    public Vector3f() {
        super();
    }

    public Vector3f(float value) {
        this.x = value;
        this.y = value;
        this.z = value;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(@NotNull float[] components) {
        this(components[0], components[1], components[2]);
    }

    public Vector3f(@NotNull Vector3f another) {
        this(another.getX(), another.getY(), another.getZ());
    }

    /**
     * Add the values to the current vector.
     *
     * @param addX x axis value.
     * @param addY y axis value.
     * @param addZ z axis value.
     * @return this vector.
     */
    public @NotNull Vector3f addLocal(final float addX, final float addY, final float addZ) {
        x += addX;
        y += addY;
        z += addZ;
        return this;
    }

    /**
     * Add the vector to this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public @NotNull Vector3f addLocal(@NotNull Vector3f vector) {
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
        return cross(vector, new Vector3f());
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
    public float distance(@NotNull Vector3f vector) {
        return ExtMath.sqrt(distanceSquared(vector));
    }

    /**
     * Calculate squared distance to the coordinates.
     *
     * @param targetX the target x.
     * @param targetY the target y.
     * @param targetZ the target z.
     * @return the squared distance.
     */
    public float distanceSquared(float targetX, float targetY, float targetZ) {

        float dx = x - targetX;
        float dy = y - targetY;
        float dz = z - targetZ;

        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Calculate squared distance to the vector.
     *
     * @param vector the vector.
     * @return the squared distance.
     */
    public float distanceSquared(@NotNull Vector3f vector) {
        return distanceSquared(vector.x, vector.y, vector.z);
    }

    /**
     * Calculate dot to the vector.
     *
     * @param vector the vector.
     * @return the dot product.
     */
    public float dot(@NotNull Vector3f vector) {
        return x * vector.x + y * vector.y + z * vector.z;
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

        Vector3f other = (Vector3f) obj;

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
        return this == ZERO ||
                ExtMath.isZero(x) &&
                ExtMath.isZero(y) &&
                ExtMath.isZero(z);
    }

    /**
     * Multiply this vector by the scalar.
     *
     * @param scalar the scalar.
     * @return this vector.
     */
    public Vector3f multLocal(float scalar) {
        return multLocal(scalar, scalar, scalar);
    }

    /**
     * Multiply this vector by the X, Y and Z scalars.
     *
     * @param x the x scalar.
     * @param y the y scalar.
     * @param z the z scalar.
     * @return this vector.
     */
    public @NotNull Vector3f multLocal(float x, float y, float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    /**
     * Multiply this vector by the vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public @NotNull Vector3f multLocal(@NotNull Vector3f vector) {
        return multLocal(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Create a new vector as negative of this vector.
     *
     * @return the new negative vector.
     */
    public @NotNull Vector3f negate() {
        return new Vector3f(-getX(), -getY(), -getZ());
    }

    /**
     * Invert this vector to get a negative vector.
     *
     * @return this vector.
     */
    public @NotNull Vector3f negateLocal() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    /**
     * Create a normalized vector from this vector.
     *
     * @return the new normalized vector.
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
     * Normalize this vector.
     *
     * @return ths vector.
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
     * Set components from the vector to this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public @NotNull Vector3f set(@NotNull Vector3f vector) {
        return set(vector.x, vector.y, vector.z);
    }

    /**
     * Set the components to this vector.
     *
     * @param x x component.
     * @param y y component.
     * @param z z component.
     * @return this vector.
     */
    public @NotNull Vector3f set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Subtract this vector by the vector and store it to the result vector.
     *
     * @param vector the vector.
     * @param result the result.
     * @return the result vector.
     */
    public @NotNull Vector3f subtract(@NotNull Vector3f vector, @NotNull Vector3f result) {
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

    /**
     * Move this vector to new point by specified direction.
     *
     * @param direction move direction.
     * @param distance  move distance.
     * @return this vector with new position.
     */
    public @NotNull Vector3f moveToDirection(@NotNull Vector3f direction, float distance) {
        return addLocal(
                direction.getX() * distance,
                direction.getY() * distance,
                direction.getZ() * distance
        );
    }

    /**
     * Move this vector to destination vector.
     * If distance argument is greater or equal to real distance between this vector and
     * destination vector then coordinates will be set to equal destination.
     *
     * @param destination destination vector
     * @param distance    move distance
     * @return this vector with new position
     */
    public @NotNull Vector3f moveToPoint(@NotNull Vector3f destination, float distance) {

        Vector3f direction = new Vector3f(destination)
                .subtractLocal(this);

        double length = Math.sqrt(
                direction.getX() * direction.getX() +
                direction.getY() * direction.getY() +
                direction.getZ() * direction.getZ()
        );

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
     *
     * @param min the minimal vector.
     * @param max the maximal vector.
     * @param t the time.
     * @return this vector.
     */
    public Vector3f lerp(@NotNull Vector3f min, @NotNull Vector3f max, float t) {

        t = ExtMath.clamp(t);

        this.x = min.x + (max.x - min.x) * t;
        this.y = min.y + (max.y - min.y) * t;
        this.z = min.z + (max.z - min.z) * t;

        return this;
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
