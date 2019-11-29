package com.ss.rlib.common.geom;

import static java.lang.Float.floatToIntBits;
import static java.lang.Float.isFinite;
import com.ss.rlib.common.util.ExtMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of float vector in 2D space (two coordinates)
 * 
 * @author zcxv
 */
public class Vector2f implements Cloneable {

    public final static Vector2f ZERO = new Vector2f(0, 0);
    public final static Vector2f NAN = new Vector2f(Float.NaN, Float.NaN);

    public final static Vector2f UNIT_X = new Vector2f(1, 0);
    public final static Vector2f UNIT_Y = new Vector2f(0, 1);
    public final static Vector2f UNIT_XYZ = new Vector2f(1, 1);
    
    public final static Vector2f UNIT_X_NEGATIVE = new Vector2f(-1, 0);
    public final static Vector2f UNIT_Y_NEGATIVE = new Vector2f(0, -1);
    public final static Vector2f UNIT_XYZ_NEGATIVE = new Vector2f(-1, -1);

    public final static Vector2f POSITIVE_INFINITY =
            new Vector2f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);

    public final static Vector2f NEGATIVE_INFINITY =
            new Vector2f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

    /**
     * Return true if the vector is not null and valid.
     *
     * @param vector the vector.
     * @return true if the vector is not null and valid.
     */
    public static boolean isValid(@Nullable Vector2f vector) {
        return vector != null && isFinite(vector.getX()) && isFinite(vector.getY());
    }

    /**
     * The X component.
     */
    private float x;

    /**
     * The Y component.
     */
    private float y;
    
    public Vector2f() {
    }
    
    public Vector2f(float value) {
        this.x = value;
        this.y = value;
    }
    
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(@NotNull float[] components) {
        this(components[0], components[1]);
    }

    public Vector2f(@NotNull Vector2f another) {
        this(another.getX(), another.getY());
    }

    /**
     * Add coordinates to this vector.
     * 
     * @param addX x axis value.
     * @param addY y axis value.
     * @return this vector
     */
    public @NotNull Vector2f addLocal(float addX, float addY) {
        x += addX;
        y += addY;
        return this;
    }

    /**
     * Add the vector to this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public @NotNull Vector2f addLocal(@NotNull Vector2f vector) {
        return addLocal(vector.x, vector.y);
    }
    
    /**
     * Calculate distance to the vector.
     *
     * @param vector the vector.
     * @return the distance.
     */
    public float distance(@NotNull Vector2f vector) {
        return ExtMath.sqrt(distanceSquared(vector));
    }
    
    /**
     * Calculate squared distance to the coordinates.
     *
     * @param targetX the target x.
     * @param targetY the target y.
     * @return the squared distance.
     */
    public float distanceSquared(float targetX, float targetY) {

        var dx = x - targetX;
        var dy = y - targetY;

        return dx * dx + dy * dy;
    }

    /**
     * Calculate squared distance to the vector.
     *
     * @param vector the vector.
     * @return the squared distance.
     */
    public float distanceSquared(@NotNull Vector2f vector) {
        return distanceSquared(vector.x, vector.y);
    }

    /**
     * Calculate dot to the vector.
     *
     * @param vector the vector.
     * @return the dot product.
     */
    public float dot(@NotNull Vector2f vector) {
        return x * vector.x + y * vector.y;
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
    public @NotNull Vector2f setX(float x) {
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
    public @NotNull Vector2f setY(float y) {
        this.y = y;
        return this;
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
     * Return true if all components are zero.
     *
     * @return true if all components are zero.
     */
    public boolean isZero() {
        return ExtMath.isZero(x) && ExtMath.isZero(y);
    }

    /**
     * Multiply this vector by the scalar.
     *
     * @param scalar the scalar.
     * @return this vector.
     */
    public @NotNull Vector2f multLocal(float scalar) {
        return multLocal(scalar, scalar);
    }

    /**
     * Multiply this vector by the scalar values.
     *
     * @param x the x scalar.
     * @param y the y scalar.
     * @return this vector.
     */
    public @NotNull Vector2f multLocal(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    /**
     * Multiply this vector by the vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public @NotNull Vector2f multLocal(@NotNull Vector2f vector) {
        return multLocal(vector.getX(), vector.getY());
    }

    /**
     * Create a new vector as negative version of this vector.
     *
     * @return the new negative vector.
     */
    public @NotNull Vector2f negate() {
        return new Vector2f(-getX(), -getY());
    }

    /**
     * Invert this vector to get a negative vector.
     *
     * @return this vector.
     */
    public @NotNull Vector2f negateLocal() {
        x = -x;
        y = -y;
        return this;
    }

    /**
     * Create a normalized vector from this vector.
     *
     * @return the new normalized vector.
     */
    public @NotNull Vector2f normalize() {

        float length = x * x + y * y;

        if (length != 1F && length != 0F) {
            length = 1.0F / ExtMath.sqrt(length);
            return new Vector2f(x * length, y * length);
        }

        return new Vector2f(x, y);
    }

    /**
     * Normalize this vector.
     *
     * @return this vector.
     */
    public @NotNull Vector2f normalizeLocal() {

        float length = x * x + y * y;

        if (length != 1f && length != 0f) {
            length = 1.0f / ExtMath.sqrt(length);
            x *= length;
            y *= length;
        }

        return this;
    }

    /**
     * Set components from the vector to this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public @NotNull Vector2f set(@NotNull Vector2f vector) {
        return set(vector.x, vector.y);
    }

    /**
     * Set the components to this vector.
     *
     * @param x x component.
     * @param y y component.
     * @return this vector.
     */
    public @NotNull Vector2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Subtract this vector by the vector and store it to the result vector.
     *
     * @param vector the vector.
     * @param result the result.
     * @return the result vector.
     */
    public @NotNull Vector2f subtract(@NotNull Vector2f vector, @NotNull Vector2f result) {
        result.x = x - vector.x;
        result.y = y - vector.y;
        return result;
    }

    /**
     * Subtract the components from this vector.
     *
     * @param subX the sub x.
     * @param subY the sub y.
     * @return this changed vector.
     */
    public Vector2f subtractLocal(float subX, float subY) {
        x -= subX;
        y -= subY;
        return this;
    }

    /**
     * Subtract the vector from this vector.
     *
     * @param vector the vector.
     * @return this changed vector.
     */
    public Vector2f subtractLocal(Vector2f vector) {
        return subtractLocal(vector.x, vector.y);
    }

    /**
     * Return vector's length (magnitude).
     *
     * @return the vector's length.
     */
    public float length() {
        return ExtMath.sqrt(x * x + y * y);
    }

    /**
     * Return vector's squared length (magnitude).
     *
     * @return the vector's squared length.
     */
    public float sqrLength() {
        return x * x + y * y;
    }

    /**
     * Divide this vector by the components.
     *
     * @param x the divider x.
     * @param y the divider y.
     * @return this changed vector.
     */
    public @NotNull Vector2f divideLocal(float x, float y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    /**
     * Divide this vector by the vector.
     *
     * @param vector the divider vector.
     * @return this changed vector.
     */
    public @NotNull Vector2f divideLocal(Vector2f vector) {
        return divideLocal(vector.x, vector.y);
    }

    /**
     * Divide this vector by the scalar.
     *
     * @param scalar the divider scalar.
     * @return this changed vector.
     */
    public @NotNull Vector2f divideLocal(float scalar) {
        return divideLocal(scalar, scalar);
    }
    
    /**
     * Linear time-based interpolation stored to this vector.
     * 
     * @param min the minimal vector
     * @param max the maximal vector
     * @param t the time
     * @return this vector.
     */
    public @NotNull Vector2f lerp(@NotNull Vector2f min, @NotNull Vector2f max, float t) {

        t = ExtMath.clamp(t);

        this.x = min.x + (max.x - min.x) * t;
        this.y = min.y + (max.y - min.y) * t;

        return this;
    }

    @Override
    protected @NotNull Vector2f clone() {
        try {
            return (Vector2f) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /** 
     * Check vectors to equals with epsilon.
     * 
     * @param vector vector
     * @param epsilon epsilon
     * @return true if vectors equals 
     */
    public boolean equals(@NotNull Vector3f vector, float epsilon) {
        return Math.abs(x - vector.getX()) < epsilon && Math.abs(y - vector.getY()) < epsilon;
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

    /**
     * Return true if these vectors are equal with the epsilon.
     *
     * @param vector  the vector.
     * @param epsilon the epsilon.
     * @return true if these vectors are equal with the epsilon.
     */
    public boolean equals(@NotNull Vector2f vector, float epsilon) {
        return Math.abs(x - vector.getX()) < epsilon &&
                Math.abs(y - vector.getY()) < epsilon;
    }

    @Override
    public String toString() {
        return "Vector2f(" + x + ", " + y + ')';
    }
    
}
