package rlib.geom.bounding.impl;

import rlib.geom.Ray;
import rlib.geom.Rotation;
import rlib.geom.Vector;
import rlib.geom.VectorBuffer;
import rlib.geom.bounding.Bounding;
import rlib.geom.bounding.BoundingType;
import rlib.logging.Logger;
import rlib.logging.Loggers;

/**
 * Базовая реализация формы из точек.
 * 
 * @author Ronn
 */
public abstract class AbstractBounding implements Bounding {

	protected static final Logger LOGGER = Loggers.getLogger(Bounding.class);

	/** центр формы */
	protected Vector center;
	/** смещение от центра */
	protected Vector offset;

	protected AbstractBounding(Vector center, Vector offset) {
		this.center = center;
		this.offset = offset;
	}

	@Override
	public boolean contains(float x, float y, float z, VectorBuffer buffer) {
		return false;
	}

	@Override
	public boolean contains(Vector point, VectorBuffer buffer) {
		return contains(point.getX(), point.getY(), point.getZ(), buffer);
	}

	@Override
	public final float distanceTo(Vector point) {
		return center.distance(point);
	}

	@Override
	public BoundingType getBoundingType() {
		return BoundingType.EMPTY;
	}

	@Override
	public final Vector getCenter() {
		return center;
	}

	@Override
	public Vector getOffset() {
		return offset;
	}

	@Override
	public Vector getResultCenter(VectorBuffer buffer) {
		return null;
	}

	@Override
	public boolean intersects(Bounding bounding, VectorBuffer buffer) {
		return false;
	}

	@Override
	public final boolean intersects(Ray ray, VectorBuffer buffer) {
		return intersects(ray.getStart(), ray.getDirection(), buffer);
	}

	@Override
	public boolean intersects(Vector start, Vector direction, VectorBuffer buffer) {
		return false;
	}

	@Override
	public void setCenter(Vector center) {
		this.center = center;
	}

	@Override
	public void update(Rotation rotation, VectorBuffer buffer) {
	}
}
