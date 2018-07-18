package com.ss.rlib.common.geom.bounding.impl;

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
        return Math.abs(getResultCenterX() - x) < sizeX &&
               Math.abs(getResultCenterY() - y) < sizeY &&
               Math.abs(getResultCenterZ() - z) < sizeZ;
    }

    @Override
    public @NotNull BoundingType getBoundingType() {
        return BoundingType.AXIS_ALIGNED_BOX;
    }

    @Override
    public @NotNull Vector3f getResultCenter(@NotNull Vector3fBuffer buffer) {

        Vector3f vector = buffer.nextVector()
                .set(center);

        if (offset.isZero()) {
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
    
    /**
     * Get a copy of the AABB's size vector.
     * 
     * @return AABB's size.
     */
    public @NotNull Vector3f getSize() {
        return new Vector3f(size);
    }

    /**
     * Get AABB's size.
     *
     * @param buffer the vector buffer.
     * @return AABB's size.
     */
    public Vector3f getSize(@NotNull Vector3fBuffer buffer) {
        return buffer.nextVector()
                .set(size);
    }

    @Override
    public boolean intersects(@NotNull Bounding bounding, @NotNull Vector3fBuffer buffer) {
        switch (bounding.getBoundingType()) {
            case EMPTY: {
                return false;
            }
            case AXIS_ALIGNED_BOX: {

                AxisAlignedBoundingBox box = (AxisAlignedBoundingBox) bounding;

                Vector3f target = box.getResultCenter(buffer);
                Vector3f center = getResultCenter(buffer);

                float sizeX = getSizeX();
                float sizeY = getSizeY();
                float sizeZ = getSizeZ();

                if (center.getX() + sizeX < target.getX() - box.getSizeX() ||
                        center.getX() - sizeX > target.getX() + box.getSizeX()) {
                    return false;
                } else if (center.getY() + sizeY < target.getY() - box.getSizeY() ||
                        center.getY() - sizeY > target.getY() + box.getSizeY()) {
                    return false;
                } else if (center.getZ() + sizeZ < target.getZ() - box.getSizeZ() ||
                        center.getZ() - sizeZ > target.getZ() + box.getSizeZ()) {
                    return false;
                }

                return true;
            }
            case SPHERE: {

                BoundingSphere sphere = (BoundingSphere) bounding;

                Vector3f target = sphere.getResultCenter(buffer);
                Vector3f center = getResultCenter(buffer);

                float radius = sphere.getRadius();

                if (Math.abs(center.getX() - target.getX()) > radius + getSizeX()) {
                    return false;
                } else if (Math.abs(center.getY() - target.getY()) > radius + getSizeY()) {
                    return false;
                } else {
                    return !(Math.abs(center.getZ() - target.getZ()) > radius + getSizeZ());
                }

            }
            default: {
                LOGGER.warning(new IllegalArgumentException("incorrect bounding type " + bounding.getBoundingType()));
            }
        }

        return false;
    }

    @Override
    public boolean intersects(
            @NotNull Vector3f start,
            @NotNull Vector3f direction,
            @NotNull Vector3fBuffer buffer
    ) {

        float divX = 1.0F / (Float.compare(direction.getX(), 0) == 0 ? 0.00001F : direction.getX());
        float divY = 1.0F / (Float.compare(direction.getY(), 0) == 0 ? 0.00001F : direction.getY());
        float divZ = 1.0F / (Float.compare(direction.getZ(), 0) == 0 ? 0.00001F : direction.getZ());

        float sizeX = getSizeX() * 0.5F;
        float sizeY = getSizeY() * 0.5F;
        float sizeZ = getSizeZ() * 0.5F;

        Vector3f center = getResultCenter(buffer);

        float minX = center.getX() - sizeX;
        float minY = center.getY() - sizeY;
        float minZ = center.getZ() - sizeZ;

        float maxX = center.getX() + sizeX;
        float maxY = center.getY() + sizeY;
        float maxZ = center.getZ() + sizeZ;

        float t1 = (minX - start.getX()) * divX;
        float t2 = (maxX - start.getX()) * divX;
        float t3 = (minY - start.getY()) * divY;
        float t4 = (maxY - start.getY()) * divY;
        float t5 = (minZ - start.getZ()) * divZ;
        float t6 = (maxZ - start.getZ()) * divZ;

        float tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
        float tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

        return tmin <= tmax && tmax > 0.f;
    }

    @Override
    public void update(@NotNull Quaternion4f rotation, @NotNull Vector3fBuffer buffer) {

        matrix.set(rotation);
        matrix.absoluteLocal();

        Vector3f vector = matrix.mult(size, buffer.nextVector());

        sizeX = Math.abs(vector.getX());
        sizeY = Math.abs(vector.getY());
        sizeZ = Math.abs(vector.getZ());

        if (offset.isZero()) {
            return;
        }

        matrix.mult(offset, vector);

        offsetX = vector.getX();
        offsetY = vector.getY();
        offsetZ = vector.getZ();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " size = " + size + ", sizeX = " + sizeX +
                ", sizeY = " + sizeY + ", sizeZ = " + sizeZ + ", center = " + center + ", offset = " + offset;
    }

}
