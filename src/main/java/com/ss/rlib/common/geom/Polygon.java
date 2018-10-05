package com.ss.rlib.common.geom;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Geometry 3D polygon.
 * 
 * @author zcxv
 * @date 27.09.2018
 */
public class Polygon {
    private final static float EPSILON_ON_PLANE = 0.1f;
    private final static float EPSILON_CWW = 0.01f;
    
    /** 
     * The polygon vertices. 
     */
    private final @NotNull Vector3f[] vertices;
    
    /** 
     * The polygon plane.
     */
    private final @NotNull Plane plane;
    
    /** 
     * Custom flags.
     */
    private int flags;
    
    /**
     * Construct polygon from vertices.
     * 
     * @param vertices vertices
     * @param vectorBuffer vector's buffer
     * @exception RuntimeException throw if vertices is less than 3
     * @see Polygon#Polygon(Vector3f, Vector3f, Vector3f, Vector3fBuffer)
     */
    public Polygon(@NotNull Vector3f[] vertices, @NotNull Vector3fBuffer vectorBuffer) throws RuntimeException {
        if(vertices.length < 3) {
            throw new RuntimeException("Polygon cannot have less than 3 vertices.");
        }
        
        this.vertices = vertices;
        
        var normal = vectorBuffer.nextVector();
        for(int i = 2; i < vertices.length; i++) {
            var ab = vectorBuffer.next(vertices[i - 1]).subtractLocal(vertices[0]);
            var ac = vectorBuffer.next(vertices[i]).subtractLocal(vertices[0]);
            normal.addLocal(ab.crossLocal(ac));
        }
        plane = new Plane(getPlanePoint(), normal.normalizeLocal());
    }
    
    /**
     * Construct polygon from 3 vertices.
     * 
     * @param a first vertex
     * @param b second vertex
     * @param c third vertex
     * @param vectorBuffer vector's buffer
     * @see Polygon#Polygon(Vector3f[], Vector3fBuffer)
     */
    public Polygon(@NotNull Vector3f a, @NotNull Vector3f b, @NotNull Vector3f c, @NotNull Vector3fBuffer vectorBuffer) {
        this(new Vector3f[] { a, b, c }, vectorBuffer);
    }
    
    /**
     * Construct polygon from vertices.
     * 
     * @param vertices vertices
     * @exception RuntimeException throw if vertices is less than 3
     * @see Polygon#Polygon(Vector3f, Vector3f, Vector3f)
     */
    public Polygon(@NotNull Vector3f[] vertices) throws RuntimeException {
        this(vertices, Vector3fBuffer.NO_REUSE);
    }
    
    /**
     * Construct polygon from 3 vertices.
     * 
     * @param a first vertex
     * @param b second vertex
     * @param c third vertex
     * @see Polygon#Polygon(Vector3f[])
     */
    public Polygon(@NotNull Vector3f a, @NotNull Vector3f b, @NotNull Vector3f c) {
        this(new Vector3f[] { a, b, c }, Vector3fBuffer.NO_REUSE);
    }
    
    /**
     * Return plane point.<br>
     * It's a first polygon vertex.
     * 
     * @return plane point
     */
    public @NotNull Vector3f getPlanePoint() {
        return vertices[0];
    }
    
    /**
     * Return polygon vertices.
     * 
     * @return vertices
     */
    public @NotNull Vector3f[] getVertices() {
        return vertices;
    }
    
    /**
     * Return polygon plane.
     * 
     * @return plane
     */
    public @NotNull Plane getPlane() {
        return plane;
    }
    
    /**
     * Return polygon custom flags.<br>
     * By default it is zero.
     * 
     * @return flags
     */
    public int getFlags() {
        return flags;
    }
    
    /**
     * Set polygon custom flags.
     * 
     * @param flags flags
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }
    
    /**
     * Toggle flag bit.
     * 
     * @param flag flag
     * @return flag status
     */
    public boolean toggleFlag(int flag) {
        flags ^= flag;
        return isFlagSet(flag);
    }
    
    /**
     * Return flag status.
     * 
     * @param flag
     * @return true if flag is set
     */
    public boolean isFlagSet(int flag) {
        return (flags & flag) != 0;
    }
    
    /**
     * Set flag bit.
     * 
     * @param flag
     */
    public void setFlag(int flag) {
        flags |= flag;
    }
    
    /**
     * Unset flag bit.
     * 
     * @param flag flag
     */
    public void unsetFlag(int flag) {
        flags &= ~flag;
    }
    
    /**
     * Return mid-point of this polygon.
     * 
     * @return mid-point
     */
    public @NotNull Vector3f getMidPoint() {
        return getMidPoint(Vector3fBuffer.NO_REUSE);
    }
    
    /**
     * Return mid-point of this polygon.
     * 
     * @param vectorBuffer vector's buffer
     * @return mid-point
     */
    public @NotNull Vector3f getMidPoint(@NotNull Vector3fBuffer vectorBuffer) {
        var point = vectorBuffer.nextVector();
        for(int i = 0; i < vertices.length; i++) {
            point.addLocal(vertices[i]);
        }
        return point.divideLocal(vertices.length);
    }
    
    /**
     * Check polygon vertices to coplanar.
     * 
     * @return true if polygon vertices is coplanar
     */
    public boolean isCoplanar() {
        return isCoplanar(Vector3fBuffer.NO_REUSE);
    }
    
    /**
     * Check polygon vertices to coplanar.
     * 
     * @param vectorBuffer vector's buffer
     * @return true if polygon vertices is coplanar
     */
    public boolean isCoplanar(@NotNull Vector3fBuffer vectorBuffer) {
        for(int i = 0; i < vertices.length; i++) {
            if(!isOnPlane(vertices[i], vectorBuffer)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Determines if point on polygon plane.
     * 
     * @param point point
     * @return true if point on plane
     */
    public boolean isOnPlane(@NotNull Vector3f point) {
        return isOnPlane(point, Vector3fBuffer.NO_REUSE);
    }
    
    /**
     * Determines if point on polygon plane.
     * 
     * @param point point
     * @param vectorBuffer vector's buffer
     * @return true if point on plane
     */
    public boolean isOnPlane(@NotNull Vector3f point, @NotNull Vector3fBuffer vectorBuffer) {
        var distance = plane.distance(point);
        return distance > -EPSILON_ON_PLANE && distance < EPSILON_ON_PLANE;
    }
    
    /**
     * Determines if line AB intersect polygon.<br> 
     * If point isn't null and line intersect polygon then point coordinates is set to intersection.
     * 
     * @param startLine start line point
     * @param endLine end line point
     * @param point [out] point with intersection coordinates, can be null
     * @return true if line AB intersect polygon
     */
    public boolean intersect(@NotNull Vector3f startLine, @NotNull Vector3f endLine, @Nullable Vector3f point) {
        return intersect(startLine, endLine, point, Vector3fBuffer.NO_REUSE);
    }
    
    /**
     * Determines if line AB intersect polygon.<br> 
     * If point isn't null and line intersect polygon then point coordinates is set to intersection.
     * 
     * @param startLine start line point
     * @param endLine end line point
     * @param point [out] point with intersection coordinates, can be null
     * @param vectorBuffer vector's buffer
     * @return true if line AB intersect polygon
     */
    public boolean intersect(
            @NotNull Vector3f startLine, 
            @NotNull Vector3f endLine, 
            @Nullable Vector3f point, 
            @NotNull Vector3fBuffer vectorBuffer
    ) {
        var aDistance = plane.distance(startLine, vertices[0], vectorBuffer);
        var bDistance = plane.distance(endLine, vertices[0], vectorBuffer);
        if((aDistance < 0 && bDistance < 0) || (aDistance > 0 && bDistance > 0)) {
            return false;
        }
        
        var intersection = plane.rayIntersection(startLine, endLine, vertices[0], vectorBuffer);
        if(intersection.equals(startLine, 0.1f) || intersection.equals(endLine, 0.1f)) {
            return false;
        }
        
        if(point != null) {
            point.set(intersection);
        }
        
        return contains(intersection, vectorBuffer);
    }
    
    /**
     * Determines if point inside of polygon.
     * 
     * @param point point
     * @return true if point inside
     */
    public boolean contains(@NotNull Vector3f point) {
        return contains(point, Vector3fBuffer.NO_REUSE);
    }
    
    /**
     * Determines if point inside of polygon.
     * 
     * @param point point
     * @param vectorBuffer vector's buffer
     * @return true if point inside
     */
    public boolean contains(@NotNull Vector3f point, @NotNull Vector3fBuffer vectorBuffer) {
        int low = 0;
        int high = vertices.length;
        
        do {
            int mid = (low + high) / 2;
            if(isTriangleCCW(vertices[0], vertices[mid], point, vectorBuffer)) {
                low = mid;
            } else {
                high = mid;
            }
        } while(low + 1 < high);
        
        if(low == 0 || high == vertices.length) {
            return false;
        }
        
        return isTriangleCCW(vertices[low], vertices[high], point, vectorBuffer);
    }
    
    /** 
     * Determines if triangle specified by 3 points is defined counterclockwise.
     * 
     * @param a first vertex
     * @param b second vertex
     * @param c third vertex
     * @param vectorBuffer vector's buffer
     * @return true if defined
     */
    private boolean isTriangleCCW(@NotNull Vector3f a, @NotNull Vector3f b, @NotNull Vector3f c, @NotNull Vector3fBuffer vectorBuffer) {
        var ab = vectorBuffer.next(a).subtractLocal(b);
        var ac = vectorBuffer.next(a).subtractLocal(c);
        ab.crossLocal(ac);
        
        return plane.getNormal().dot(ab) > EPSILON_CWW;
    }
    
    @Override
    public String toString() {
        return "Polygon{vertices=" + Arrays.toString(vertices) + 
                ", plane=" + plane + 
                ", flags=" + flags + "}";
    }
}
