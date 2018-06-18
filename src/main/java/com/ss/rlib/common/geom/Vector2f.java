package com.ss.rlib.common.geom;

import static java.lang.Float.floatToIntBits;

import com.ss.rlib.common.util.ExtMath;

/**
 * Implementation of float vector in 2D space (two coordinates)
 * 
 * @author zcxv
 */
public class Vector2f {
    
    private float x;
    private float y;
    
    public Vector2f() {
    }
    
    public Vector2f(float v) {
        this.x = v;
        this.y = v;
    }
    
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Add coordinates to this vector.
     * 
     * @param addX x axis value
     * @param addY y axis value
     * @return this vector
     */
    public Vector2f addLocal(float addX, float addY) {
        x += addX;
        y += addY;
        return this;
    }
    
    /**
     * Add other vector to this vector.
     * 
     * @param vector other vector
     * @return this vector
     */
    public Vector2f addLocal(Vector2f vector) {
        return addLocal(vector.x, vector.y);
    }
    
    /**
     * Calculate distance to the vector.
     *
     * @param vector the vector.
     * @return the distance.
     */
    public float distance(Vector2f vector) {
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

        final float dx = x - targetX;
        final float dy = y - targetY;

        return dx * dx + dy * dy;
    }

    /**
     * Calculate squared distance to the vector.
     *
     * @param vector the vector.
     * @return the squared distance.
     */
    public float distanceSquared(Vector2f vector) {
        return distanceSquared(vector.x, vector.y);
    }

    /**
     * Dot float.
     *
     * @param vector the vector
     * @return dot product
     */
    public float dot(Vector2f vector) {
        return x * vector.x + y * vector.y;
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

        final Vector2f other = (Vector2f) obj;

        if (floatToIntBits(x) != floatToIntBits(other.x)) {
            return false;
        } else if (floatToIntBits(y) != floatToIntBits(other.y)) {
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
    public Vector2f setX(float x) {
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
    public Vector2f setY(float y) {
        this.y = y;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        return x == 0 && y == 0;
    }

    /**
     * Multiply local vector.
     *
     * @param scalar the scalar
     * @return this vector
     */
    public Vector2f multLocal(float scalar) {
        return multLocal(scalar, scalar);
    }

    /**
     * Multiply local vector.
     *
     * @param x the x
     * @param y the y
     * @return this vector
     */
    public Vector2f multLocal(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    /**
     * Multiply local vector.
     *
     * @param vector the vector
     * @return this vector
     */
    public Vector2f multLocal(final Vector2f vector) {
        return multLocal(vector.getX(), vector.getY());
    }

    /**
     * Negate vector.
     *
     * @return result vector
     */
    public Vector2f negate() {
        return new Vector2f(-getX(), -getY());
    }
    
    /**
     * Negate local vector.
     *
     * @return this vector
     */
    public Vector2f negateLocal() {
        x = -x;
        y = -y;
        return this;
    }

    /**
     * Normalize vector.
     *
     * @return result vector
     */
    public Vector2f normalize() {

        float length = x * x + y * y;

        if (length != 1F && length != 0F) {
            length = 1.0F / ExtMath.sqrt(length);
            return new Vector2f(x * length, y * length);
        }

        return new Vector2f(x, y);
    }

    /**
     * Normalize local vector.
     *
     * @return this vector
     */
    public Vector2f normalizeLocal() {

        float length = x * x + y * y;

        if (length != 1f && length != 0f) {
            length = 1.0f / ExtMath.sqrt(length);
            x *= length;
            y *= length;
        }

        return this;
    }

    /**
     * Set vector.
     *
     * @param vector the vector
     * @return this vector
     */
    public Vector2f set(Vector2f vector) {
        return set(vector.getX(), vector.getY());
    }

    /**
     * Set vector.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return this vector
     */
    public Vector2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Subtract vector.
     *
     * @param vector the vector
     * @param result the result
     * @return result vector
     */
    public Vector2f subtract(Vector2f vector, Vector2f result) {
        result.x = x - vector.x;
        result.y = y - vector.y;
        return result;
    }

    /**
     * Subtract local vector.
     *
     * @param subX the sub x
     * @param subY the sub y
     * @param subZ the sub z
     * @return this vector
     */
    public Vector2f subtractLocal(float subX, float subY) {
        x -= subX;
        y -= subY;
        return this;
    }

    /**
     * Subtract local vector.
     *
     * @param vector the vector
     * @return this vector
     */
    public Vector2f subtractLocal(Vector2f vector) {
        return subtractLocal(vector.x, vector.y);
    }
    
    /**
     * Return vector length (magnitude).
     * 
     * @return length
     */
    public float length() {
        return ExtMath.sqrt(x * x + y * y);
    }
    
    /**
     * Divide local vector.
     *
     * @param x the divider x
     * @param y the divider y
     * @return this vector
     */
    public Vector2f divideLocal(float x, float y) {
        this.x /= x;
        this.y /= y;
        return this;
    }
    
    /**
     * Divide local vector.
     *
     * @param vector the divider vector
     * @return this vector
     */
    public Vector2f divideLocal(Vector2f vector) {
        return divideLocal(vector.x, vector.y);
    }
    
    /**
     * Divide local vector.
     *
     * @param scalar the divider scalar
     * @return this vector
     */
    public Vector2f divideLocal(float scalar) {
        return divideLocal(scalar, scalar);
    }

    @Override
    public String toString() {
        return "Vector2f{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
    
}
