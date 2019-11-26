package com.ss.rlib.common.geom;

import static java.lang.Float.floatToIntBits;
import static java.lang.Float.isFinite;
import com.ss.rlib.common.util.ExtMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of vector with 3 float values.
 *
 * @author JavaSaBr
 */
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

    public final static Vector3f POSITIVE_INFINITY = new Vector3f(Float.POSITIVE_INFINITY,
            Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);

    public final static Vector3f NEGATIVE_INFINITY = new Vector3f(Float.NEGATIVE_INFINITY,
            Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

    /**
     * Return true if the vector is not null and valid.
     *
     * @param vector the vector.
     * @return true if the vector is not null and valid.
     */
    public static boolean isValid(@Nullable Vector3f vector) {
        return vector != null &&
            isFinite(vector.getX()) &&
            isFinite(vector.getY()) &&
            isFinite(vector.getZ());
    }

    /**
     * Get a subtraction result between the two vectors.
     *
     * @param first  the first vector.
     * @param second the second vector.
     * @return the subtraction result.
     */
    public static @NotNull Vector3f substract(@NotNull Vector3f first, @NotNull Vector3f second) {
        return first.clone().subtractLocal(second);
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
     * Add the values to this vector.
     *
     * @param addX x axis value.
     * @param addY y axis value.
     * @param addZ z axis value.
     * @return this vector.
     */
    public @NotNull Vector3f addLocal(float addX, float addY, float addZ) {
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
     * Calculate a cross vector between this vector and the coordinates.
     *
     * @param otherX the other x
     * @param otherY the other y
     * @param otherZ the other z
     * @param result the result vector.
     * @return the result vector.
     */
    public @NotNull Vector3f cross(
            float otherX,
            float otherY,
            float otherZ,
            @NotNull Vector3f result
    ) {

        var resX = y * otherZ - z * otherY;
        var resY = z * otherX - x * otherZ;
        var resZ = x * otherY - y * otherX;

        result.set(resX, resY, resZ);

        return result;
    }

    /**
     * Calculate a cross vector between this vector and the vector.
     *
     * @param vector the vector.
     * @return the result vector.
     */
    public @NotNull Vector3f cross(@NotNull Vector3f vector) {
        return cross(vector, new Vector3f());
    }

    /**
     * Calculate a cross vector between this vector and the vector.
     *
     * @param vector the vector.
     * @param result the result vector to store result.
     * @return the result vector.
     */
    public @NotNull Vector3f cross(@NotNull Vector3f vector, @NotNull Vector3f result) {
        return cross(vector.x, vector.y, vector.z, result);
    }

    /**
     * Calculate a cross vector between this vector and the coordinates and store the result to this vector.
     *
     * @param otherX the other x.
     * @param otherY the other y.
     * @param otherZ the other z.
     * @return this changed vector.
     */
    public @NotNull Vector3f crossLocal(float otherX, float otherY, float otherZ) {

        var tempx = y * otherZ - z * otherY;
        var tempy = z * otherX - x * otherZ;

        z = x * otherY - y * otherX;
        x = tempx;
        y = tempy;

        return this;
    }

    /**
     * Calculate a cross vector between this vector and the coordinates and store the result to this vector.
     *
     * @param vector the vector.
     * @return this changed vector.
     */
    public @NotNull Vector3f crossLocal(@NotNull Vector3f vector) {
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

        var dx = x - targetX;
        var dy = y - targetY;
        var dz = z - targetZ;

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
    public @NotNull Vector3f setX(float x) {
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
    public @NotNull Vector3f setY(float y) {
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
    public @NotNull Vector3f setZ(float z) {
        this.z = z;
        return this;
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
     * Multiply this vector by the scalar values.
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
     * Create a new vector as negative version of this vector.
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

        var length = x * x + y * y + z * z;

        if (length != 1F && length != 0F) {
            length = 1.0F / ExtMath.sqrt(length);
            return new Vector3f(x * length, y * length, z * length);
        }

        return new Vector3f(x, y, z);
    }

    /**
     * Normalize this vector.
     *
     * @return this vector.
     */
    public @NotNull Vector3f normalizeLocal() {

        var length = x * x + y * y + z * z;

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
     * Subtract the vector from this vector and store the result to the result vector.
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
     * Subtract the components from this vector.
     *
     * @param subX the sub x.
     * @param subY the sub y.
     * @param subZ the sub z.
     * @return this changed vector.
     */
    public @NotNull Vector3f subtractLocal(float subX, float subY, float subZ) {
        x -= subX;
        y -= subY;
        z -= subZ;
        return this;
    }

    /**
     * Subtract the vector from this vector.
     *
     * @param vector the vector.
     * @return this changed vector.
     */
    public @NotNull Vector3f subtractLocal(@NotNull Vector3f vector) {
        return subtractLocal(vector.x, vector.y, vector.z);
    }
    
    /**
     * Return vector's length (magnitude).
     * 
     * @return the vector's length.
     */
    public float length() {
        return ExtMath.sqrt(x * x + y * y + z * z);
    }
    
    /**
     * Return vector's squared length (magnitude).
     *
     * @return the vector's squared length.
     */
    public float sqrLength() {
        return x * x + y * y + z * z;
    }
    
    /**
     * Divide this vector by the components.
     *
     * @param x the divider x.
     * @param y the divider y.
     * @param z the divider z.
     * @return this changed vector.
     */
    public @NotNull Vector3f divideLocal(float x, float y, float z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }
    
    /**
     * Divide this vector by the vector.
     *
     * @param vector the divider vector.
     * @return this changed vector.
     */
    public @NotNull Vector3f divideLocal(@NotNull Vector3f vector) {
        return divideLocal(vector.x, vector.y, vector.z);
    }
    
    /**
     * Divide this vector by the scalar.
     *
     * @param scalar the divider scalar.
     * @return this changed vector.
     */
    public @NotNull Vector3f divideLocal(float scalar) {
        return divideLocal(scalar, scalar, scalar);
    }

    /**
     * Move this vector to a new point by specified direction.
     *
     * @param direction move direction.
     * @param distance  move distance.
     * @return this changed vector.
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
    protected @NotNull Vector3f clone() {
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
     * @param vector  the vector.
     * @param epsilon the epsilon.
     * @return true if these vectors are equal with the epsilon.
     */
    public boolean equals(@NotNull Vector3f vector, float epsilon) {
        return Math.abs(x - vector.getX()) < epsilon &&
                Math.abs(y - vector.getY()) < epsilon &&
                Math.abs(z - vector.getZ()) < epsilon;
    }

    @Override
    public String toString() {
        return "Vector3f(" + x + ", " + y + ", " + z + ')';
    }
}
