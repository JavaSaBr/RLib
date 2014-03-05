package rlib.geom.bounding;

import rlib.geom.Vector;
import rlib.geom.VectorBuffer;

/**
 * Модель формы сферы.
 * 
 * @author Ronn
 */
public final class BoundingSphere extends AbstractBounding {

	/** радиус сферы */
	protected float radius;
	/** квадрат радиуса сферы */
	protected float squareRadius;

	protected BoundingSphere(Vector center, Vector offset, float radius) {
		super(center, offset);

		this.radius = radius;
		this.squareRadius = radius * radius;
	}

	@Override
	public boolean contains(float x, float y, float z, VectorBuffer buffer) {
		Vector center = getResultCenter(buffer);
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
	public Vector getResultCenter(VectorBuffer buffer) {

		Vector vector = buffer.getNextVector();
		vector.set(center);

		if(offset == Vector.ZERO) {
			return vector;
		}

		return vector.addLocal(offset);
	}

	@Override
	public boolean intersects(Bounding bounding, VectorBuffer buffer) {
		switch(bounding.getBoundingType()) {
			case EMPTY: {
				return false;
			}
			case SPHERE: {

				BoundingSphere sphere = (BoundingSphere) bounding;

				Vector diff = getResultCenter(buffer);
				diff.subtractLocal(sphere.getResultCenter(buffer));

				float rsum = getRadius() + sphere.getRadius();

				return (diff.dot(diff) <= rsum * rsum);
			}
			case AXIS_ALIGNED_BOX: {

				AxisAlignedBoundingBox box = (AxisAlignedBoundingBox) bounding;

				Vector center = getResultCenter(buffer);
				Vector target = box.getResultCenter(buffer);

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
	public boolean intersects(Vector start, Vector direction, VectorBuffer buffer) {

		Vector diff = buffer.getNextVector();
		diff.set(start).subtractLocal(getResultCenter(buffer));

		float a = start.dot(diff) - squareRadius;

		if(a <= 0.0) {
			return true;
		}

		float b = direction.dot(diff);

		if(b >= 0.0) {
			return false;
		}

		return b * b >= a;
	}

	@Override
	public String toString() {
		return "BoundingSphere [radius=" + radius + ", squareRadius=" + squareRadius + "]";
	}
}
