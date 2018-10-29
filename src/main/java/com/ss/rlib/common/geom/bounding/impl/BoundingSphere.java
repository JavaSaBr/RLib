package com.ss.rlib.common.geom.bounding.impl;

import static java.lang.Math.abs;

import com.ss.rlib.common.geom.util.GeometryUtils;
import org.jetbrains.annotations.NotNull;

import com.ss.rlib.common.geom.Vector3f;
import com.ss.rlib.common.geom.Vector3fBuffer;
import com.ss.rlib.common.geom.bounding.Bounding;
import com.ss.rlib.common.geom.bounding.BoundingType;

/**
 * The implementation of sphere bounding.
 *
 * @author JavaSaBr
 */
public class BoundingSphere extends AbstractBounding {

    /**
     * The sphere radius.
     */
    protected float radius;

    /**
     * The square radius.
     */
    protected float squareRadius;

    public BoundingSphere(@NotNull Vector3f center, @NotNull Vector3f offset, float radius) {
        super(center, offset);

        this.radius = radius;
        this.squareRadius = radius * radius;
    }

    @Override
    public boolean contains(float x, float y, float z) {

        float startX = getResultCenterX();
        float centerY = getResultCenterY();
        float centerZ = getResultCenterZ();

        return GeometryUtils.getSquareDistance(startX, centerY, centerZ, x, y, z) < squareRadius;
    }

    /** {@inheritDoc} */
    @Override
    public float getResultCenterZ() {
        return center.getZ() + offset.getZ();
    }

    /** {@inheritDoc} */
    @Override
    public float getResultCenterY() {
        return center.getY() + offset.getY();
    }

    /** {@inheritDoc} */
    @Override
    public float getResultCenterX() {
        return center.getX() + offset.getX();
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull BoundingType getBoundingType() {
        return BoundingType.SPHERE;
    }

    /**
     * Get the sphere's radius.
     *
     * @return the sphere's radius.
     */
    public float getRadius() {
        return radius;
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull Vector3f getResultCenter(@NotNull Vector3fBuffer buffer) {

        var vector = buffer.take(center);

        if (offset.isZero()) {
            return vector;
        }

        return vector.addLocal(offset);
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(@NotNull Bounding bounding, @NotNull Vector3fBuffer buffer) {
        switch (bounding.getBoundingType()) {
            case EMPTY: {
                return false;
            }
            case SPHERE: {

                BoundingSphere sphere = (BoundingSphere) bounding;

                Vector3f diff = getResultCenter(buffer)
                        .subtractLocal(sphere.getResultCenter(buffer));

                float rsum = getRadius() + sphere.getRadius();

                return diff.dot(diff) <= rsum * rsum;
            }
            case AXIS_ALIGNED_BOX: {

                AxisAlignedBoundingBox box = (AxisAlignedBoundingBox) bounding;

                Vector3f center = getResultCenter(buffer);
                Vector3f target = box.getResultCenter(buffer);

                return abs(target.getX() - center.getX()) < getRadius() + box.getSizeX() &&
                        abs(target.getY() - center.getY()) < getRadius() + box.getSizeY() &&
                        abs(target.getZ() - center.getZ()) < getRadius() + box.getSizeZ();
            }
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(@NotNull Vector3f start, @NotNull Vector3f direction, @NotNull Vector3fBuffer buffer) {

        var diff = buffer.take(start)
                .subtractLocal(getResultCenter(buffer));

        float a = start.dot(diff) - squareRadius;

        if (a <= 0.0) {
            buffer.put(diff);
            return true;
        }

        float b = direction.dot(diff);

        buffer.put(diff);
        return b < 0.0 && b * b >= a;

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [radius=" + radius + ", squareRadius=" + squareRadius + "]";
    }
}
