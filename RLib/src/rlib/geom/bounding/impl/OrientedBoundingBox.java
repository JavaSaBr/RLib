package rlib.geom.bounding.impl;

import rlib.geom.Matrix3f;
import rlib.geom.Rotation;
import rlib.geom.Vector;
import rlib.geom.VectorBuffer;
import rlib.geom.bounding.Bounding;
import rlib.geom.bounding.BoundingType;

/**
 * Модель формы коробки.
 *
 * @author Ronn
 */
public final class OrientedBoundingBox extends AbstractBounding {

    /**
     * Матрица для промежуточных вычислений
     */
    private final Matrix3f matrix;

    /**
     * Вектор, описывающий размер формы
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

    protected OrientedBoundingBox(final Vector center, final Vector offset, final float sizeX, final float sizeY, final float sizeZ) {
        super(center, offset);

        this.matrix = Matrix3f.newInstance();
        this.size = Vector.newInstance(sizeX, sizeY, sizeZ);

        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    @Override
    public boolean contains(final float x, final float y, final float z, final VectorBuffer buffer) {
        final Vector center = getResultCenter(buffer);
        return Math.abs(center.getX() - x) < sizeX && Math.abs(center.getY() - y) < sizeY && Math.abs(center.getZ() - z) < sizeZ;
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

        return vector.addLocal(offset);
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

                final OrientedBoundingBox box = (OrientedBoundingBox) bounding;

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

                if (Math.abs(center.getX() - target.getX()) > radius + getSizeX()) {
                    return false;
                }

                if (Math.abs(center.getY() - target.getY()) > radius + getSizeY()) {
                    return false;
                }

                if (Math.abs(center.getZ() - target.getZ()) > radius + getSizeZ()) {
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

        float rhs;

        final Vector diff = start.subtract(getResultCenter(buffer), buffer.getNextVector());

        final Vector fWdU = buffer.getNextVector();
        final Vector fAWdU = buffer.getNextVector();
        final Vector fDdU = buffer.getNextVector();

        final Vector fADdU = buffer.getNextVector();
        final Vector fAWxDdU = buffer.getNextVector();

        fWdU.setX(direction.dot(Vector.UNIT_X));
        fAWdU.setX(Math.abs(fWdU.getX()));
        fDdU.setX(diff.dot(Vector.UNIT_X));
        fADdU.setX(Math.abs(fDdU.getX()));

        final float sizeX = getSizeX();
        final float sizeY = getSizeY();
        final float sizeZ = getSizeZ();

        if (fADdU.getX() > sizeX && fDdU.getX() * fWdU.getX() >= 0.0F) {
            return false;
        }

        fWdU.setY(direction.dot(Vector.UNIT_Y));
        fAWdU.setY(Math.abs(fWdU.getY()));
        fDdU.setY(diff.dot(Vector.UNIT_Y));
        fADdU.setY(Math.abs(fDdU.getY()));

        if (fADdU.getY() > sizeY && fDdU.getY() * fWdU.getY() >= 0.0F) {
            return false;
        }

        fWdU.setZ(direction.dot(Vector.UNIT_Z));
        fAWdU.setZ(Math.abs(fWdU.getZ()));
        fDdU.setZ(diff.dot(Vector.UNIT_Z));
        fADdU.setZ(Math.abs(fDdU.getZ()));

        if (fADdU.getZ() > sizeZ && fDdU.getZ() * fWdU.getZ() >= 0.0F) {
            return false;
        }

        final Vector wCrossD = direction.cross(diff, buffer.getNextVector());

        fAWxDdU.setX(Math.abs(wCrossD.dot(Vector.UNIT_X)));

        rhs = sizeY * fAWdU.getZ() + sizeZ * fAWdU.getY();

        if (fAWxDdU.getX() > rhs) {
            return false;
        }

        fAWxDdU.setY(Math.abs(wCrossD.dot(Vector.UNIT_Y)));

        rhs = sizeX * fAWdU.getZ() + sizeZ * fAWdU.getZ();

        if (fAWxDdU.getY() > rhs) {
            return false;
        }

        fAWxDdU.setZ(Math.abs(wCrossD.dot(Vector.UNIT_Z)));

        rhs = sizeX * fAWdU.getY() + sizeY * fAWdU.getX();

        return !(fAWxDdU.getZ() > rhs);
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

        sizeX = Math.abs(vector.getX());
        sizeY = Math.abs(vector.getY());
        sizeZ = Math.abs(vector.getZ());
    }
}
