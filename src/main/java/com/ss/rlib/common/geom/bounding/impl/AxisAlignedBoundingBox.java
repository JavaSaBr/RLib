package com.ss.rlib.common.geom.bounding.impl;

import static java.lang.Math.*;
import com.ss.rlib.common.geom.Matrix3f;
import com.ss.rlib.common.geom.Quaternion4f;
import com.ss.rlib.common.geom.Vector3f;
import com.ss.rlib.common.geom.Vector3fBuffer;
import com.ss.rlib.common.geom.bounding.Bounding;
import com.ss.rlib.common.geom.bounding.BoundingType;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation AxisAlignedBoundingBox.
 *
 * @author JavaSaBr
 */
public class AxisAlignedBoundingBox extends AbstractBounding {

    /**
     * The matrix.
     */
    @NotNull
    private final Matrix3f matrix;

    /**
     * The vector size.
     */
    @NotNull
    private final Vector3f size;

    /**
     * The size X.
     */
    protected float sizeX;

    /**
     * The size Y.
     */
    protected float sizeY;

    /**
     * The size Z.
     */
    protected float sizeZ;

    /**
     * The offset X.
     */
    protected float offsetX;

    /**
     * The offset Y.
     */
    protected float offsetY;

    /**
     * The offset Z.
     */
    protected float offsetZ;

    public AxisAlignedBoundingBox(
            @NotNull Vector3f center,
            @NotNull Vector3f offset,
            float sizeX,
            float sizeY,
            float sizeZ
    ) {
        super(center, offset);

        this.matrix = new Matrix3f();
        this.size = new Vector3f(sizeX, sizeY, sizeZ);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.offsetX = offset.getX();
        this.offsetY = offset.getY();
        this.offsetZ = offset.getZ();
    }

    @Override
    public boolean contains(float x, float y, float z) {
        return abs(getResultCenterX() - x) < sizeX &&
               abs(getResultCenterY() - y) < sizeY &&
               abs(getResultCenterZ() - z) < sizeZ;
    }

    @Override
    public @NotNull BoundingType getBoundingType() {
        return BoundingType.AXIS_ALIGNED_BOX;
    }

    @Override
    public @NotNull Vector3f getResultCenter(@NotNull Vector3fBuffer buffer) {

        Vector3f vector = buffer.nextVector()
                .set(center);

        if (offset == Vector3f.ZERO) {
            return vector;
        }

        return vector.addLocal(offsetX, offsetY, offsetZ);
    }

    @Override
    public float getResultCenterZ() {
        return center.getZ() + offsetZ;
    }

    @Override
    public float getResultCenterY() {
        return center.getY() + offsetY;
    }

    @Override
    public float getResultCenterX() {
        return center.getX() + offsetX;
    }

    /**
     * Get the size by X coordinates.
     *
     * @return the size.
     */
    protected final float getSizeX() {
        return sizeX;
    }

    /**
     * Get the size by Y coordinates.
     *
     * @return the size.
     */
    protected final float getSizeY() {
        return sizeY;
    }

    /**
     * Get the size by Z coordinates.
     *
     * @return the size.
     */
    protected final float getSizeZ() {
        return sizeZ;
    }

    @Override
    public boolean intersects(@NotNull Bounding bounding, @NotNull Vector3fBuffer buffer) {
        switch (bounding.getBoundingType()) {
            case EMPTY: {
                return false;
            }
            case AXIS_ALIGNED_BOX: {

                final AxisAlignedBoundingBox box = (AxisAlignedBoundingBox) bounding;

                final Vector3f target = box.getResultCenter(buffer);
                final Vector3f center = getResultCenter(buffer);

                final float sizeX = getSizeX();
                final float sizeY = getSizeY();
                final float sizeZ = getSizeZ();

                if (center.getX() + sizeX < target.getX() - box.getSizeX() || center.getX() - sizeX > target.getX() + box.getSizeX()) {
                    return false;
                } else if (center.getY() + sizeY < target.getY() - box.getSizeY() || center.getY() - sizeY > target.getY() + box.getSizeY()) {
                    return false;
                } else if (center.getZ() + sizeZ < target.getZ() - box.getSizeZ() || center.getZ() - sizeZ > target.getZ() + box.getSizeZ()) {
                    return false;
                }

                return true;
            }
            case SPHERE: {

                final BoundingSphere sphere = (BoundingSphere) bounding;

                final Vector3f target = sphere.getResultCenter(buffer);
                final Vector3f center = getResultCenter(buffer);

                final float radius = sphere.getRadius();

                if (abs(center.getX() - target.getX()) > radius + getSizeX()) {
                    return false;
                } else if (abs(center.getY() - target.getY()) > radius + getSizeY()) {
                    return false;
                } else if (abs(center.getZ() - target.getZ()) > radius + getSizeZ()) {
                    return false;
                }

                return true;
            }
            default: {
                LOGGER.warning(new IllegalArgumentException("incorrect bounding type " + bounding.getBoundingType()));
            }
        }

        return false;
    }

    @Override
    public boolean intersects(@NotNull final Vector3f start, @NotNull final Vector3f direction,
                              @NotNull final Vector3fBuffer buffer) {

        final float divX = 1.0F / (Float.compare(direction.getX(), 0) == 0 ? 0.00001F : direction.getX());
        final float divY = 1.0F / (Float.compare(direction.getY(), 0) == 0 ? 0.00001F : direction.getY());
        final float divZ = 1.0F / (Float.compare(direction.getZ(), 0) == 0 ? 0.00001F : direction.getZ());

        final float sizeX = getSizeX() * 0.5F;
        final float sizeY = getSizeY() * 0.5F;
        final float sizeZ = getSizeZ() * 0.5F;

        final Vector3f center = getResultCenter(buffer);

        final float minX = center.getX() - sizeX;
        final float minY = center.getY() - sizeY;
        final float minZ = center.getZ() - sizeZ;

        final float maxX = center.getX() + sizeX;
        final float maxY = center.getY() + sizeY;
        final float maxZ = center.getZ() + sizeZ;

        final float t1 = (minX - start.getX()) * divX;
        final float t2 = (maxX - start.getX()) * divX;
        final float t3 = (minY - start.getY()) * divY;
        final float t4 = (maxY - start.getY()) * divY;
        final float t5 = (minZ - start.getZ()) * divZ;
        final float t6 = (maxZ - start.getZ()) * divZ;

        final float tmin = max(max(min(t1, t2), min(t3, t4)), min(t5, t6));
        final float tmax = min(min(max(t1, t2), max(t3, t4)), max(t5, t6));

        return tmin <= tmax && tmax > 0.f;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " size = " + size + ", sizeX = " + sizeX +
                ", sizeY = " + sizeY + ", sizeZ = " + sizeZ + ", center = " + center + ", offset = " + offset;
    }

    @Override
    public void update(@NotNull final Quaternion4f rotation, @NotNull final Vector3fBuffer buffer) {

        matrix.set(rotation);
        matrix.absoluteLocal();

        final Vector3f vector = buffer.nextVector();
        vector.set(size);

        matrix.mult(vector, vector);

        sizeX = abs(vector.getX());
        sizeY = abs(vector.getY());
        sizeZ = abs(vector.getZ());

        if (offset == Vector3f.ZERO) {
            return;
        }

        vector.set(offset);

        matrix.mult(vector, vector);

        offsetX = vector.getX();
        offsetY = vector.getY();
        offsetZ = vector.getZ();
    }
}
