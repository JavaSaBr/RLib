package rlib.geom.bounding;

import rlib.geom.Vector;

/**
 * Фабрика форм.
 * 
 * @author Ronn
 */
public final class Boundings {

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

	private Boundings() {
		throw new IllegalArgumentException();
	}
}
