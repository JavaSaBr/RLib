package com.ss.rlib.geom.bounding.impl;

import com.ss.rlib.geom.Ray3f;
import com.ss.rlib.geom.Vector3f;
import org.jetbrains.annotations.NotNull;

import com.ss.rlib.geom.Quaternion4f;
import com.ss.rlib.geom.Vector3fBuffer;
import com.ss.rlib.geom.bounding.Bounding;
import com.ss.rlib.geom.bounding.BoundingType;
import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerManager;

/**
 * The base implementation of a bounding.
 *
 * @author JavaSaBr
 */
public abstract class AbstractBounding implements Bounding {

    /**
     * The constant LOGGER.
     */
    protected static final Logger LOGGER = LoggerManager.getLogger(Bounding.class);

    /**
     * The center.
     */
    protected Vector3f center;

    /**
     * The offset.
     */
    protected Vector3f offset;

    /**
     * Instantiates a new Abstract bounding.
     *
     * @param center the center
     * @param offset the offset
     */
    protected AbstractBounding(@NotNull final Vector3f center, @NotNull final Vector3f offset) {
        this.center = center;
        this.offset = offset;
    }

    @Override
    public boolean contains(final float x, final float y, final float z, @NotNull final Vector3fBuffer buffer) {
        return false;
    }

    @Override
    public boolean contains(@NotNull final Vector3f point, @NotNull final Vector3fBuffer buffer) {
        return contains(point.getX(), point.getY(), point.getZ(), buffer);
    }

    @Override
    public final float distanceTo(@NotNull final Vector3f point) {
        return center.distance(point);
    }

    @NotNull
    @Override
    public BoundingType getBoundingType() {
        return BoundingType.EMPTY;
    }

    @NotNull
    @Override
    public final Vector3f getCenter() {
        return center;
    }

    @Override
    public void setCenter(@NotNull final Vector3f center) {
        this.center = center;
    }

    @NotNull
    @Override
    public Vector3f getOffset() {
        return offset;
    }

    @NotNull
    @Override
    public Vector3f getResultCenter(@NotNull final Vector3fBuffer buffer) {
        return getCenter();
    }

    @Override
    public boolean intersects(@NotNull final Bounding bounding, @NotNull final Vector3fBuffer buffer) {
        return false;
    }

    @Override
    public final boolean intersects(@NotNull final Ray3f ray, @NotNull final Vector3fBuffer buffer) {
        return intersects(ray.getStart(), ray.getDirection(), buffer);
    }

    @Override
    public boolean intersects(@NotNull final Vector3f start, @NotNull final Vector3f direction, @NotNull final Vector3fBuffer buffer) {
        return false;
    }

    @Override
    public void update(@NotNull final Quaternion4f rotation, @NotNull final Vector3fBuffer buffer) {
    }
}
