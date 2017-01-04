package rlib.geom.bounding;

import org.jetbrains.annotations.NotNull;

import rlib.geom.Vector3f;
import rlib.geom.bounding.impl.AbstractBounding;
import rlib.geom.bounding.impl.AxisAlignedBoundingBox;
import rlib.geom.bounding.impl.BoundingSphere;

/**
 * The factory of bounding implementations.
 *
 * @author JavaSaBr
 */
public final class BoundingFactory {

    public static Bounding newBoundingBox(@NotNull final Vector3f center, @NotNull final Vector3f offset,
                                          final float sizeX, final float sizeY, final float sizeZ) {
        return new AxisAlignedBoundingBox(center, offset, sizeX, sizeY, sizeZ);
    }

    public static Bounding newBoundingEmpty() {
        return new AbstractBounding(null, null) {
        };
    }

    public static Bounding newBoundingSphere(@NotNull final Vector3f center, @NotNull final Vector3f offset,
                                             final int radius) {
        return new BoundingSphere(center, offset, radius);
    }

    private BoundingFactory() {
        throw new IllegalArgumentException();
    }
}
