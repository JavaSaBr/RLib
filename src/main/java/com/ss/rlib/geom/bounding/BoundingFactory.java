package com.ss.rlib.geom.bounding;

import org.jetbrains.annotations.NotNull;

import com.ss.rlib.geom.Vector3f;
import com.ss.rlib.geom.bounding.impl.AbstractBounding;
import com.ss.rlib.geom.bounding.impl.AxisAlignedBoundingBox;
import com.ss.rlib.geom.bounding.impl.BoundingSphere;

/**
 * The factory of bounding implementations.
 *
 * @author JavaSaBr
 */
public final class BoundingFactory {

    /**
     * New bounding box bounding.
     *
     * @param center the center
     * @param offset the offset
     * @param sizeX  the size x
     * @param sizeY  the size y
     * @param sizeZ  the size z
     * @return the bounding
     */
    public static Bounding newBoundingBox(@NotNull final Vector3f center, @NotNull final Vector3f offset,
                                          final float sizeX, final float sizeY, final float sizeZ) {
        return new AxisAlignedBoundingBox(center, offset, sizeX, sizeY, sizeZ);
    }

    /**
     * New bounding empty bounding.
     *
     * @return the bounding
     */
    public static Bounding newBoundingEmpty() {
        return new AbstractBounding(null, null) {
        };
    }

    /**
     * New bounding sphere bounding.
     *
     * @param center the center
     * @param offset the offset
     * @param radius the radius
     * @return the bounding
     */
    public static Bounding newBoundingSphere(@NotNull final Vector3f center, @NotNull final Vector3f offset,
                                             final float radius) {
        return new BoundingSphere(center, offset, radius);
    }

    private BoundingFactory() {
        throw new IllegalArgumentException();
    }
}
