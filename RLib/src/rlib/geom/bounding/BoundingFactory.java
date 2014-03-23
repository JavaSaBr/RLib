package rlib.geom.bounding;

import rlib.geom.Vector;
import rlib.geom.bounding.impl.AbstractBounding;
import rlib.geom.bounding.impl.AxisAlignedBoundingBox;
import rlib.geom.bounding.impl.BoundingSphere;

/**
 * Фабрика форм.
 * 
 * @author Ronn
 */
public final class BoundingFactory {

	public static Bounding newBoundingBox(Vector center, Vector offset, float sizeX, float sizeY, float sizeZ) {
		return new AxisAlignedBoundingBox(center, offset, sizeX, sizeY, sizeZ);
	}

	public static Bounding newBoundingEmpty() {
		return new AbstractBounding(null, null) {
		};
	}

	public static Bounding newBoundingSphere(Vector center, Vector offset, int radius) {
		return new BoundingSphere(center, offset, radius);
	}

	private BoundingFactory() {
		throw new IllegalArgumentException();
	}
}
