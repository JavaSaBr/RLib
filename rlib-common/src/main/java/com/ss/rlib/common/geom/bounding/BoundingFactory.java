package com.ss.rlib.common.geom.bounding;

import com.ss.rlib.common.geom.Vector3f;
import com.ss.rlib.common.geom.bounding.impl.AbstractBounding;
import com.ss.rlib.common.geom.bounding.impl.AxisAlignedBoundingBox;
import com.ss.rlib.common.geom.bounding.impl.BoundingSphere;
import com.ss.rlib.common.geom.Vector3f;
import com.ss.rlib.common.geom.bounding.impl.AbstractBounding;
import com.ss.rlib.common.geom.bounding.impl.AxisAlignedBoundingBox;
import com.ss.rlib.common.geom.bounding.impl.BoundingSphere;
import org.jetbrains.annotations.NotNull;

/**
 * The bounding's factory.
 *
 * @author JavaSaBr
 */
public final class BoundingFactory {

    /**
     * Creates a new bounding box.
     *
     * @param center the center.
     * @param offset the offset.
     * @param sizeX  the size x.
     * @param sizeY  the size y.
     * @param sizeZ  the size z.
     * @return the new bounding box.
     */
    public static Bounding newBoundingBox(
            @NotNull Vector3f center,
            @NotNull Vector3f offset,
            float sizeX,
            float sizeY,
            float sizeZ
    ) {
        return new AxisAlignedBoundingBox(center, offset, sizeX, sizeY, sizeZ);
    }

    /**
     * Creates a new empty bounding.
     *
     * @return the new empty bounding.
     */
    public static Bounding newBoundingEmpty() {
        return new AbstractBounding(new Vector3f(), new Vector3f()) {};
    }

    /**
     * Creates a new bounding sphere.
     *
     * @param center the center.
     * @param offset the offset.
     * @param radius the radius.
     * @return the bounding sphere.
     */
    public static Bounding newBoundingSphere(
            @NotNull Vector3f center,
            @NotNull Vector3f offset,
            float radius
    ) {
        return new BoundingSphere(center, offset, radius);
    }

    private BoundingFactory() {
        throw new IllegalArgumentException();
    }
}
