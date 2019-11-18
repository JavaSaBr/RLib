package com.ss.rlib.common.geom;

import static com.ss.rlib.common.util.array.ArrayFactory.toArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Geometry 3D polygon.
 * 
 * @author zcxv
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
     * Construct polygon from the vertices.
     *
     * @param vertices the vertices.
     * @throws IllegalArgumentException throw if vertices is less than 3
     * @see Polygon#Polygon(Vector3f, Vector3f, Vector3f)
     */
    public Polygon(@NotNull Vector3f[] vertices) throws IllegalArgumentException {
        this(vertices, Vector3fBuffer.NO_REUSE);
    }

    /**
     * Construct polygon from the vertices.
     *
     * @param vertices the vertices.
     * @param buffer   the vector's buffer.
     * @throws IllegalArgumentException throw if vertices is less than 3
     * @see Polygon#Polygon(Vector3f, Vector3f, Vector3f, Vector3fBuffer)
     */
    public Polygon(@NotNull Vector3f[] vertices, @NotNull Vector3fBuffer buffer) throws IllegalArgumentException {

        if (vertices.length < 3) {
            throw new IllegalArgumentException("polygon cannot have less than 3 vertices.");
        }

        this.vertices = vertices;

        var normal = buffer.nextVector();

        for (int i = 2; i < vertices.length; i++) {
            var ab = buffer.next(vertices[i - 1]).subtractLocal(vertices[0]);
            var ac = buffer.next(vertices[i]).subtractLocal(vertices[0]);
            normal.addLocal(ab.crossLocal(ac));
        }

        this.plane = new Plane(getPlanePoint(), normal.normalizeLocal());
    }

    /**
     * Construct polygon from the 3 vertices.
     *
     * @param first  the first vertex.
     * @param second the second vertex.
     * @param third  the third vertex.
     * @param buffer the vector's buffer.
     * @see Polygon#Polygon(Vector3f[], Vector3fBuffer)
     */
    public Polygon(
            @NotNull Vector3f first,
            @NotNull Vector3f second,
            @NotNull Vector3f third,
            @NotNull Vector3fBuffer buffer
    ) {
        this(toArray(first, second, third), buffer);
    }

    /**
     * Construct polygon from the 3 vertices.
     *
     * @param first  the first vertex.
     * @param second the second vertex.
     * @param third  the third vertex.
     * @see Polygon#Polygon(Vector3f[])
     */
    public Polygon(@NotNull Vector3f first, @NotNull Vector3f second, @NotNull Vector3f third) {
        this(toArray(first, second, third), Vector3fBuffer.NO_REUSE);
    }
    
    /**
     * Return plane point.<br>
     * It's a first polygon vertex.
     * 
     * @return the plane point.
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
     * @param buffer vector's buffer
     * @return mid-point
     */
    public @NotNull Vector3f getMidPoint(@NotNull Vector3fBuffer buffer) {

        var point = buffer.nextVector();

        for (var vertice : vertices) {
            point.addLocal(vertice);
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
     * @param buffer vector's buffer
     * @return true if polygon vertices is coplanar
     */
    public boolean isCoplanar(@NotNull Vector3fBuffer buffer) {

        for (var vertice : vertices) {
            if (!isOnPlane(vertice, buffer)) {
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
     * @param point  point
     * @param buffer vector's buffer
     * @return true if point on plane
     */
    public boolean isOnPlane(@NotNull Vector3f point, @NotNull Vector3fBuffer buffer) {
        var distance = plane.distance(point);
        return distance > -EPSILON_ON_PLANE && distance < EPSILON_ON_PLANE;
    }

    /**
     * Determines if line AB intersect polygon.<br>
     * If point isn't null and line intersect polygon then point coordinates is set to intersection.
     *
     * @param startLine start line point
     * @param endLine   end line point
     * @param point     [out] point with intersection coordinates, can be null
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

        if ((aDistance < 0 && bDistance < 0) || (aDistance > 0 && bDistance > 0)) {
            return false;
        }

        var intersection = plane.rayIntersection(startLine, endLine, vertices[0], vectorBuffer);

        if (intersection.equals(startLine, 0.1f) || intersection.equals(endLine, 0.1f)) {
            return false;
        }

        if (point != null) {
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
     * @param point  point
     * @param buffer vector's buffer
     * @return true if point inside
     */
    public boolean contains(@NotNull Vector3f point, @NotNull Vector3fBuffer buffer) {

        int low = 0;
        int high = vertices.length;

        do {

            int mid = (low + high) / 2;

            if (isTriangleCCW(vertices[0], vertices[mid], point, buffer)) {
                low = mid;
            } else {
                high = mid;
            }

        } while (low + 1 < high);

        if (low == 0 || high == vertices.length) {
            return false;
        }

        return isTriangleCCW(vertices[low], vertices[high], point, buffer);
    }

    /**
     * Determines if triangle specified by 3 points is defined counterclockwise.
     *
     * @param first  first vertex.
     * @param second second vertex.
     * @param third  third vertex.
     * @param buffer vector's buffer.
     * @return true if defined.
     */
    private boolean isTriangleCCW(
            @NotNull Vector3f first,
            @NotNull Vector3f second,
            @NotNull Vector3f third,
            @NotNull Vector3fBuffer buffer
    ) {

        var ab = buffer.next(first)
                .subtractLocal(second);

        var ac = buffer.next(first)
                .subtractLocal(third);

        ab.crossLocal(ac);

        return plane.getNormal()
                .dot(ab) > EPSILON_CWW;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Polygon{vertices=" + Arrays.toString(vertices) + 
                ", plane=" + plane + 
                ", flags=" + flags + "}";
    }
}
