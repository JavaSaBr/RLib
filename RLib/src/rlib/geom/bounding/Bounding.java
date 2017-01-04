package rlib.geom.bounding;

import org.jetbrains.annotations.NotNull;

import rlib.geom.Quaternion4f;
import rlib.geom.Ray3f;
import rlib.geom.Vector3f;
import rlib.geom.Vector3fBuffer;

/**
 * The interface to implement a bounding of objects.
 *
 * @author JavaSaBr
 */
public interface Bounding {

    /**
     * Check this bounding that it contains a point.
     *
     * @param x      the x coordinate.
     * @param y      the y coordinate.
     * @param z      the z coordinate.
     * @param buffer the vector buffer.
     * @return true if this bounding contains the point.
     */
    boolean contains(float x, float y, float z, @NotNull Vector3fBuffer buffer);

    /**
     * Check this bounding that it contains a point
     *
     * @param point  the point
     * @param buffer the vector buffer.
     * @return true if this bounding contains the point.
     */
    boolean contains(@NotNull Vector3f point, @NotNull Vector3fBuffer buffer);

    /**
     * Get a distance from a center of a bounding to a point.
     *
     * @param point the point.
     * @return the distance.
     */
    float distanceTo(@NotNull Vector3f point);

    /**
     * Get a type of a bounding.
     *
     * @return the type.
     */
    @NotNull
    BoundingType getBoundingType();

    /**
     * Get a center of a bounding.
     *
     * @return the center.
     */
    @NotNull
    Vector3f getCenter();

    /**
     * Change a center of a bounding.
     *
     * @param center the new center.
     */
    void setCenter(@NotNull Vector3f center);

    /**
     * Get an offset of a bounding.
     *
     * @return the offset.
     */
    @NotNull
    Vector3f getOffset();

    /**
     * Get a result center of a bounding.
     *
     * @param buffer the vector buffer.
     * @return the result center.
     */
    @NotNull
    Vector3f getResultCenter(@NotNull Vector3fBuffer buffer);

    /**
     * Check this bounding that it intersects with an other bounding.
     *
     * @param bounding the other bounding.
     * @param buffer   the vector buffer.
     * @return true if this bounding interests with other bounding.
     */
    boolean intersects(@NotNull Bounding bounding, @NotNull Vector3fBuffer buffer);

    /**
     * Check this bounding that it intersects with a ray.
     *
     * @param ray    the ray.
     * @param buffer the vector buffer.
     * @return true if this bounding interests with the ray.
     */
    boolean intersects(@NotNull Ray3f ray, @NotNull Vector3fBuffer buffer);

    /**
     * Check this bounding that it intersects with a ray.
     *
     * @param start     the start point of the ray.
     * @param direction the direction of the ray.
     * @param buffer    the vector buffer.
     * @return true if this bounding interests with the ray.
     */
    boolean intersects(@NotNull Vector3f start, @NotNull Vector3f direction, @NotNull Vector3fBuffer buffer);

    /**
     * Update a rotation of a bounding.
     *
     * @param rotation the rotation.
     * @param buffer   the vector buffer.
     */
    void update(@NotNull Quaternion4f rotation, @NotNull Vector3fBuffer buffer);
}
