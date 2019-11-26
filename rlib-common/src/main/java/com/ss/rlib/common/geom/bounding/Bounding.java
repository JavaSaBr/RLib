package com.ss.rlib.common.geom.bounding;

import com.ss.rlib.common.geom.Quaternion4f;
import com.ss.rlib.common.geom.Ray3f;
import com.ss.rlib.common.geom.Vector3f;
import com.ss.rlib.common.geom.Vector3fBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a bounding of objects.
 *
 * @author JavaSaBr
 */
public interface Bounding {


    /**
     * Return true if this bounding contains the point.
     *
     * @param x the X coordinate.
     * @param y the Y coordinate.
     * @param z the X coordinate.
     * @return true if this bounding contains the point.
     */
    boolean contains(float x, float y, float z);

    /**
     * Return true if this bounding contains the point.
     *
     * @param point the point.
     * @return true if this bounding contains the point.
     */
    boolean contains(@NotNull Vector3f point);

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
    @NotNull BoundingType getBoundingType();

    /**
     * Get a center of a bounding.
     *
     * @return the center.
     */
    @NotNull Vector3f getCenter();

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
    @NotNull Vector3f getOffset();

    /**
     * Get a result center of this bounding.
     *
     * @param buffer the vector buffer.
     * @return the result center.
     */
    @NotNull Vector3f getResultCenter(@NotNull Vector3fBuffer buffer);

    /**
     * Get a result X coordinate of the center of this bounding.
     *
     * @return the result X coordinate.
     */
    float getResultCenterX();

    /**
     * Get a result Y coordinate of the center of this bounding.
     *
     * @return the result Y coordinate.
     */
    float getResultCenterY();

    /**
     * Get a result Z coordinate of the center of this bounding.
     *
     * @return the result X coordinate.
     */
    float getResultCenterZ();

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
