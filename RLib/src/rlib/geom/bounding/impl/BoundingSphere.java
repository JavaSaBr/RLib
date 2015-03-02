package rlib.geom.bounding.impl;

import rlib.geom.Vector;
import rlib.geom.VectorBuffer;
import rlib.geom.bounding.Bounding;
import rlib.geom.bounding.BoundingType;

/**
 * Реализация формы сферы.
 * 
 * @author Ronn
 */
public final class BoundingSphere extends AbstractBounding {

	/** радиус сферы */
	protected float radius;
	/** квадрат радиуса сферы */
	protected float squareRadius;

	public BoundingSphere(final Vector center, final Vector offset, final float radius) {
		super(center, offset);

		this.radius = radius;
		this.squareRadius = radius * radius;
	}

	@Override
	public boolean contains(final float x, final float y, final float z, final VectorBuffer buffer) {
		final Vector center = getResultCenter(buffer);
		return center.distanceSquared(x, y, z) < squareRadius;
	}

	@Override
	public BoundingType getBoundingType() {
		return BoundingType.SPHERE;
	}

	/**
	 * @return радиус сферы.
	 */
	public float getRadius() {
		return radius;
	}

	@Override
	public Vector getResultCenter(final VectorBuffer buffer) {

		final Vector vector = buffer.getNextVector();
		vector.set(center);

		if(offset == Vector.ZERO) {
			return vector;
		}

		return vector.addLocal(offset);
	}

	@Override
	public boolean intersects(final Bounding bounding, final VectorBuffer buffer) {
		switch(bounding.getBoundingType()) {
			case EMPTY: {
				return false;
			}
			case SPHERE: {

				final BoundingSphere sphere = (BoundingSphere) bounding;

				final Vector diff = getResultCenter(buffer);
				diff.subtractLocal(sphere.getResultCenter(buffer));

				final float rsum = getRadius() + sphere.getRadius();

				return diff.dot(diff) <= rsum * rsum;
			}
			case AXIS_ALIGNED_BOX: {

				final AxisAlignedBoundingBox box = (AxisAlignedBoundingBox) bounding;

				final Vector center = getResultCenter(buffer);
				final Vector target = box.getResultCenter(buffer);

				if(!(Math.abs(target.getX() - center.getX()) < getRadius() + box.getSizeX())) {
					return false;
				}

				if(!(Math.abs(target.getY() - center.getY()) < getRadius() + box.getSizeY())) {
					return false;
				}

				if(!(Math.abs(target.getZ() - center.getZ()) < getRadius() + box.getSizeZ())) {
					return false;
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean intersects(final Vector start, final Vector direction, final VectorBuffer buffer) {

		final Vector diff = buffer.getNextVector();
		diff.set(start).subtractLocal(getResultCenter(buffer));

		final float a = start.dot(diff) - squareRadius;

		if(a <= 0.0) {
			return true;
		}

		final float b = direction.dot(diff);

		if(b >= 0.0) {
			return false;
		}

		return b * b >= a;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [radius=" + radius + ", squareRadius=" + squareRadius + "]";
	}
}
