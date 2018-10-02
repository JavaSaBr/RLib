package com.ss.rlib.common.geom;

import org.jetbrains.annotations.NotNull;

import com.ss.rlib.common.util.ExtMath;

/**
 * Geometry 3D plane.<br>
 * Follow to the formula: <pre>Ax + By + Cz + D = 0</pre>
 * 
 * @author zcxv
 * @date 25.09.2018
 * @project rlib
 */
public class Plane {
    /** Plane normal */
    private final Vector3f normal;
    
    /** D component, inverted by sign. */ 
    private float d;
    
    /** Construct plane by 3 points.
     * @param a first point
     * @param b second point
     * @param c third point */
    public Plane(@NotNull Vector3f a, @NotNull Vector3f b, @NotNull Vector3f c) {
        final Vector3f ba = new Vector3f(b).subtractLocal(a);
        final Vector3f ca = new Vector3f(c).subtractLocal(a);
        normal = ba.cross(ca).normalizeLocal();
        this.d = a.dot(normal);
    }
    
    /** Construct plane by exists normal and plane point.
     * @param planePoint plane point
     * @param normal plane normal */
    public Plane(@NotNull Vector3f planePoint, @NotNull Vector3f normal) {
        this.normal = new Vector3f(normal);
        this.d = planePoint.dot(normal);
    }
    
    /** Return a plane normal.
     * @return normal */
    public @NotNull Vector3f getNormal() {
        return normal;
    }
    
    /** Return a D component inverted by sign.
     * @return d component */
    public float getD() {
        return d;
    }
    
    /** Multiply plane by scalar.
     * @param scalar scalar
     * @return this plane */
    public @NotNull Plane multLocal(float scalar) {
        normal.multLocal(scalar);
        d *= scalar;
        return this;
    }
    
    /** Divide plane by scalar.
     * @param scalar scalar
     * @return this plane */
    public @NotNull Plane divideLocal(float scalar) {
        normal.divideLocal(scalar);
        d /= scalar;
        return this;
    }
    
    /** Add values to plane.
     * @param x x axis value
     * @param y y axis value
     * @param z z axis value
     * @param d d component
     * @return this plane */
    public @NotNull Plane addLocal(float x, float y, float z, float d) {
        normal.addLocal(x, y, z);
        this.d += d;
        return this;
    }
    
    /** Subtract values to plane.
     * @param x x axis value
     * @param y y axis value
     * @param z z axis value
     * @param d d component
     * @return this plane */
    public @NotNull Plane subtractLocal(float x, float y, float z, float d) {
        normal.subtractLocal(x, y, z);
        this.d -= d;
        return this;
    }
    
    /** Multiply plane by other plane.
     * @param plane other plane
     * @return this plane */
    public @NotNull Plane multLocal(@NotNull Plane plane) {
        normal.multLocal(plane.normal);
        d *= plane.d;
        return this;
    }
    
    /** Divide plane by other plane.
     * @param plane other plane
     * @return this plane */
    public @NotNull Plane divideLocal(@NotNull Plane plane) {
        normal.divideLocal(plane.normal);
        d /= plane.d;
        return this;
    }
    
    /** Add plane by other plane.
     * @param plane other plane
     * @return this plane */
    public @NotNull Plane addLocal(@NotNull Plane plane) {
        normal.addLocal(plane.normal);
        d += plane.d;
        return this;
    }
    
    /** Subtract plane by other plane.
     * @param plane other plane
     * @return this plane */
    public @NotNull Plane subtractLocal(@NotNull Plane plane) {
        normal.subtractLocal(plane.normal);
        d -= plane.d;
        return this;
    }
    
    /** Multiply plane by vector.<br>
     * Its operation is equals to multiply plane normal vector with vector.
     * @param vector vector
     * @return this plane */
    public @NotNull Plane multLocal(@NotNull Vector3f vector) {
        normal.multLocal(vector);
        return this;
    }
    
    /** Divide plane by vector.<br>
     * Its operation is equals to divide plane normal vector with vector.
     * @param vector vector
     * @return this plane */
    public @NotNull Plane divideLocal(@NotNull Vector3f vector) {
        normal.divideLocal(vector);
        return this;
    }
    
    /** Add plane by vector.<br>
     * Its operation is equals to: plane normal plus vector.
     * @param vector vector
     * @return this plane */
    public @NotNull Plane addLocal(@NotNull Vector3f vector) {
        normal.addLocal(vector);
        return this;
    }
    
    /** Subtract plane by vector.<br>
     * Its operation is equals to: plane normal minus vector.
     * @param vector vector
     * @return this plane */
    public @NotNull Plane subtractLocal(@NotNull Vector3f vector) {
        normal.subtractLocal(vector);
        return this;
    }
    
    /** Dot product plane with vector.
     * @param point vector
     * @return dot product */
    public float dot(@NotNull Vector3f point) {
        return normal.dot(point) - d;
    }
    
    /** Dot product plane with plane.
     * @param plane plane
     * @return dot product */
    public float dot(@NotNull Plane plane) {
        return normal.dot(plane.normal) - d * plane.d;
    }
    
    /** Distance between point and plane.
     * @param point point
     * @param plane point plane point
     * @return distance */
    public float distance(@NotNull Vector3f point, @NotNull Vector3f planePoint) {
        return new Vector3f(point).subtractLocal(planePoint).dot(normal);
    }
    
    /** Distance between point and plane.
     * @param point point
     * @return distance */
    public float distance(@NotNull Vector3f point) {
        return -d + point.dot(normal);
    }
    
    /** Angle between planes.
     * @param plane plane
     * @return angle in radians */
    public float angle(@NotNull Plane plane) {
        return ExtMath.cos(
                normal.dot(plane.normal) / 
                ExtMath.sqrt(normal.sqrLength() * plane.normal.sqrLength())
        );
    }
    
    /** Check planes to parallel.
     * @param plane plane
     * @param epsilon epsilon
     * @return true if planes parallels */
    public boolean isParallel(@NotNull Plane plane, float epsilon) {
        //check plane normals to collinearity
        final float fA = plane.normal.getX() / normal.getX();
        final float fB = plane.normal.getY() / normal.getY();
        final float fC = plane.normal.getZ() / normal.getZ();
        return Math.abs(fA - fB) < epsilon && Math.abs(fA - fC) < epsilon;
    }
    
    /** Check planes to perpendicular.
     * @param plane plane
     * @param epsilon epsilon
     * @return true if planes perpendicular */
    public boolean isPerpendicular(@NotNull Plane plane, float epsilon) {
        return Math.abs(normal.dot(plane.normal)) < epsilon;
    }
    
    /** Return point side relative plane.<br>
     * <b>Front side:</b><br>
     * <pre>
     *         ___
     *        |   | 
     *  *     |   | Plane
     * Point  |___|
     * </pre><br>
     * <b>Behind side:</b>
     * <pre>
     *  Plane
     *   ___
     *  |   | 
     *  |   |   *
     *  |___| Point
     * </pre><br>
     * <b>Inside side:</b>
     * <pre>
     *    _______ 
     *   |       |
     *   | *     |
     *   | Point |
     *   |_______|
     *     Plane
     * </pre>
     * @param point point
     * @param epsilon epsilon
     * @return side */
    public @NotNull PlaneSide side(@NotNull Vector3f point, float epsilon) {
        final float dot = dot(point);
        if(dot > epsilon) {
            return PlaneSide.Front;
        }
        
        if(dot < -epsilon) {
            return PlaneSide.Behind;
        }
        
        return PlaneSide.OnPlane;
    }
    
    /** Ray plane intersection. Return point where ray intersect plane.<br>
     * <i>This method doesnt check plane-vector collinearity!</i>
     * @param a start point
     * @param b end point
     * @return intersection point */
    public @NotNull Vector3f rayIntersection(@NotNull Vector3f a, @NotNull Vector3f b) {
        final Vector3f direction = new Vector3f(b).subtractLocal(a);
        final float denominator = direction.dot(normal);
        final float distance = (d - a.dot(normal)) / denominator;
        direction.multLocal(distance);
        return new Vector3f(a).addLocal(direction);
    }
    
    /** Ray plane intersection. Return point where ray intersect plane.<br>
     * <i>This method doesnt check plane-vector collinearity!</i> 
     * @param a start point 
     * @param b end point 
     * @return intersection point */
    public @NotNull Vector3f rayIntersection(@NotNull Vector3f a, @NotNull Vector3f b, @NotNull Vector3f planePoint) {
        final Vector3f direction = new Vector3f(b).subtractLocal(a);
        final float denominator = direction.dot(normal);
        final float distance = new Vector3f(planePoint).subtractLocal(a).dot(normal) / denominator;
        direction.multLocal(distance);
        return new Vector3f(a).addLocal(direction);
    }
    
    /** Ray plane intersection. Return point where ray intersect plane.<br>
     * <i>This method doesnt check plane-vector collinearity!</i> 
     * @param ray ray
     * @return intersection point */
    public @NotNull Vector3f rayIntersection(@NotNull Ray3f ray) {
        final float denominator = ray.getDirection().dot(normal);
        final float distance = (d - ray.getStart().dot(normal)) / denominator;
        return new Vector3f(ray.getStart()).addLocal(new Vector3f(ray.getDirection()).multLocal(distance));
    }
}
