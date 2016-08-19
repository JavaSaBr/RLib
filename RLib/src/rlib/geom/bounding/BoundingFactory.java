package rlib.geom.bounding;

import rlib.geom.Vector;
import rlib.geom.bounding.impl.AbstractBounding;
import rlib.geom.bounding.impl.AxisAlignedBoundingBox;
import rlib.geom.bounding.impl.BoundingSphere;

/**
 * Фабрика форм.
 *
 * @author JavaSaBr
 */
public final class BoundingFactory {

    public static Bounding newBoundingBox(final Vector center, final Vector offset, final float sizeX, final float sizeY, final float sizeZ) {
        return new AxisAlignedBoundingBox(center, offset, sizeX, sizeY, sizeZ);
    }

    public static Bounding newBoundingEmpty() {
        return new AbstractBounding(null, null) {
        };
    }

    public static Bounding newBoundingSphere(final Vector center, final Vector offset, final int radius) {
        return new BoundingSphere(center, offset, radius);
    }

    private BoundingFactory() {
        throw new IllegalArgumentException();
    }
}
