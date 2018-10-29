package com.ss.rlib.common.geom.bounding.impl;

import com.ss.rlib.common.geom.Quaternion4f;
import com.ss.rlib.common.geom.Ray3f;
import com.ss.rlib.common.geom.Vector3f;
import com.ss.rlib.common.geom.Vector3fBuffer;
import com.ss.rlib.common.geom.bounding.Bounding;
import com.ss.rlib.common.geom.bounding.BoundingType;
import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;
import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of a bounding.
 *
 * @author JavaSaBr
 */
public abstract class AbstractBounding implements Bounding {

    protected static final Logger LOGGER = LoggerManager.getLogger(Bounding.class);

    /**
     * The center.
     */
    protected Vector3f center;

    /**
     * The offset.
     */
    protected Vector3f offset;

    protected AbstractBounding(@NotNull Vector3f center, @NotNull Vector3f offset) {
        this.center = center;
        this.offset = offset;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(float x, float y, float z) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(@NotNull Vector3f point) {
        return contains(point.getX(), point.getY(), point.getZ());
    }

    /** {@inheritDoc} */
    @Override
    public final float distanceTo(@NotNull Vector3f point) {
        return center.distance(point);
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull BoundingType getBoundingType() {
        return BoundingType.EMPTY;
    }

    /** {@inheritDoc} */
    @Override
    public final@NotNull Vector3f getCenter() {
        return center;
    }

    /** {@inheritDoc} */
    @Override
    public void setCenter(@NotNull Vector3f center) {
        this.center = center;
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull Vector3f getOffset() {
        return offset;
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull Vector3f getResultCenter(@NotNull Vector3fBuffer buffer) {
        return getCenter();
    }

    /** {@inheritDoc} */
    @Override
    public float getResultCenterX() {
        return getCenter().getX();
    }

    /** {@inheritDoc} */
    @Override
    public float getResultCenterY() {
        return getCenter().getY();
    }

    /** {@inheritDoc} */
    @Override
    public float getResultCenterZ() {
        return getCenter().getZ();
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(@NotNull Bounding bounding, @NotNull Vector3fBuffer buffer) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean intersects(@NotNull Ray3f ray, @NotNull Vector3fBuffer buffer) {
        return intersects(ray.getStart(), ray.getDirection(), buffer);
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(@NotNull Vector3f start, @NotNull Vector3f direction, @NotNull Vector3fBuffer buffer) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void update(@NotNull Quaternion4f rotation, @NotNull Vector3fBuffer buffer) {
    }

    // deprecated

    /** {@inheritDoc} */
    @Override
    public boolean contains(float x, float y, float z, @NotNull Vector3fBuffer buffer) {
        return contains(x, y, z);
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(@NotNull Vector3f point, @NotNull Vector3fBuffer buffer) {
        return contains(point.getX(), point.getY(), point.getZ());
    }
}
