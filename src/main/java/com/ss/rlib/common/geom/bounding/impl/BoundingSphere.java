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

    @Override
    public float getResultCenterZ() {
        return center.getZ() + offset.getZ();
    }

    @Override
    public float getResultCenterY() {
        return center.getY() + offset.getY();
    }

    @Override
    public float getResultCenterX() {
        return center.getX() + offset.getX();
    }

    @Override
    public @NotNull BoundingType getBoundingType() {
        return BoundingType.SPHERE;
    }

    /**
     * Gets radius.
     *
     * @return the sphere radius.
     */
    public float getRadius() {
        return radius;
    }

    @Override
    public @NotNull Vector3f getResultCenter(@NotNull Vector3fBuffer buffer) {

        Vector3f vector = buffer.nextVector();
        vector.set(center);

        if (offset == Vector3f.ZERO) {
            return vector;
        }

        return vector.addLocal(offset);
    }

    @Override
    public boolean intersects(@NotNull final Bounding bounding, @NotNull final Vector3fBuffer buffer) {
        switch (bounding.getBoundingType()) {
            case EMPTY: {
                return false;
            }
            case SPHERE: {

                final BoundingSphere sphere = (BoundingSphere) bounding;

                final Vector3f diff = getResultCenter(buffer);
                diff.subtractLocal(sphere.getResultCenter(buffer));

                final float rsum = getRadius() + sphere.getRadius();

                return diff.dot(diff) <= rsum * rsum;
            }
            case AXIS_ALIGNED_BOX: {

                final AxisAlignedBoundingBox box = (AxisAlignedBoundingBox) bounding;

                final Vector3f center = getResultCenter(buffer);
                final Vector3f target = box.getResultCenter(buffer);

                return abs(target.getX() - center.getX()) < getRadius() + box.getSizeX() &&
                        abs(target.getY() - center.getY()) < getRadius() + box.getSizeY() &&
                        abs(target.getZ() - center.getZ()) < getRadius() + box.getSizeZ();
            }
        }

        return false;
    }

    @Override
    public boolean intersects(@NotNull final Vector3f start, @NotNull final Vector3f direction, @NotNull final Vector3fBuffer buffer) {

        final Vector3f diff = buffer.nextVector();
        diff.set(start).subtractLocal(getResultCenter(buffer));

        final float a = start.dot(diff) - squareRadius;
        if (a <= 0.0) return true;

        final float b = direction.dot(diff);
        return b < 0.0 && b * b >= a;

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [radius=" + radius + ", squareRadius=" + squareRadius + "]";
    }
}
