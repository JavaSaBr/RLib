package rlib.geom.bounding.impl;

import rlib.geom.Matrix3f;
import rlib.geom.Rotation;
import rlib.geom.Vector;
import rlib.geom.VectorBuffer;
import rlib.geom.bounding.Bounding;
import rlib.geom.bounding.BoundingType;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Реализация формы коробки.
 *
 * @author Ronn
 */
public class AxisAlignedBoundingBox extends AbstractBounding {

    /**
     * Матрица для промежуточных вычислений.
     */
    private final Matrix3f matrix;

    /**
     * Вектор, описывающий размер формы.
     */
    private final Vector size;

    /**
     * Размер формы по x.
     */
    protected float sizeX;

    /**
     * Размер формы по y.
     */
    protected float sizeY;

    /**
     * Размер формы по z.
     */
    protected float sizeZ;

    /**
     * Отступ формы от цента по Х
     */
    protected float offsetX;

    /**
     * Отступ формы от цента по Y
     */
    protected float offsetY;

    /**
     * Отступ формы от цента по Z
     */
    protected float offsetZ;

    public AxisAlignedBoundingBox(final Vector center, final Vector offset, final float sizeX, final float sizeY, final float sizeZ) {
        super(center, offset);

        this.matrix = Matrix3f.newInstance();
        this.size = Vector.newInstance(sizeX, sizeY, sizeZ);

        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;

        this.offsetX = offset.getX();
        this.offsetY = offset.getY();
        this.offsetZ = offset.getZ();
    }

    @Override
    public boolean contains(final float x, final float y, final float z, final VectorBuffer buffer) {
        final Vector center = getResultCenter(buffer);
        return abs(center.getX() - x) < sizeX && abs(center.getY() - y) < sizeY && abs(center.getZ() - z) < sizeZ;
    }

    @Override
    public BoundingType getBoundingType() {
        return BoundingType.AXIS_ALIGNED_BOX;
    }

    @Override
    public Vector getResultCenter(final VectorBuffer buffer) {

        final Vector vector = buffer.getNextVector();
        vector.set(center);

        if (offset == Vector.ZERO) {
            return vector;
        }

        return vector.addLocal(offsetX, offsetY, offsetZ);
    }

    /**
     * @return размер формы по X.
     */
    public final float getSizeX() {
        return sizeX;
    }

    /**
     * @return размер формы по Y.
     */
    public final float getSizeY() {
        return sizeY;
    }

    /**
     * @return размер формы по Z.
     */
    public final float getSizeZ() {
        return sizeZ;
    }

    @Override
    public boolean intersects(final Bounding bounding, final VectorBuffer buffer) {
        switch (bounding.getBoundingType()) {
            case EMPTY: {
                return false;
            }
            case AXIS_ALIGNED_BOX: {

                final AxisAlignedBoundingBox box = (AxisAlignedBoundingBox) bounding;

                final Vector target = box.getResultCenter(buffer);
                final Vector center = getResultCenter(buffer);

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

                final Vector target = sphere.getResultCenter(buffer);
                final Vector center = getResultCenter(buffer);

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
    public boolean intersects(final Vector start, final Vector direction, final VectorBuffer buffer) {

        final float divX = 1.0F / (Float.compare(direction.getX(), 0) == 0 ? 0.00001F : direction.getX());
        final float divY = 1.0F / (Float.compare(direction.getY(), 0) == 0 ? 0.00001F : direction.getY());
        final float divZ = 1.0F / (Float.compare(direction.getZ(), 0) == 0 ? 0.00001F : direction.getZ());

        final float sizeX = getSizeX() * 0.5F;
        final float sizeY = getSizeY() * 0.5F;
        final float sizeZ = getSizeZ() * 0.5F;

        final Vector center = getResultCenter(buffer);

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
        return getClass().getSimpleName() + " size = " + size + ", sizeX = " + sizeX + ", sizeY = " + sizeY + ", sizeZ = " + sizeZ + ", center = " + center + ", offset = " + offset;
    }

    @Override
    public void update(final Rotation rotation, final VectorBuffer buffer) {

        matrix.set(rotation);
        matrix.absoluteLocal();

        final Vector vector = buffer.getNextVector();
        vector.set(size);

        matrix.mult(vector, vector);

        sizeX = abs(vector.getX());
        sizeY = abs(vector.getY());
        sizeZ = abs(vector.getZ());

        if (offset == Vector.ZERO) {
            return;
        }

        vector.set(offset);

        matrix.mult(vector, vector);

        offsetX = vector.getX();
        offsetY = vector.getY();
        offsetZ = vector.getZ();
    }
}
