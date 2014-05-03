package rlib.geom.bounding.impl;

import rlib.geom.Ray;
import rlib.geom.Rotation;
import rlib.geom.Vector;
import rlib.geom.VectorBuffer;
import rlib.geom.bounding.Bounding;
import rlib.geom.bounding.BoundingType;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * Базовая реализация формы из точек.
 * 
 * @author Ronn
 */
public abstract class AbstractBounding implements Bounding {

	protected static final Logger LOGGER = LoggerManager.getLogger(Bounding.class);

	/** центр формы */
	protected Vector center;
	/** смещение от центра */
	protected Vector offset;

	protected AbstractBounding(final Vector center, final Vector offset) {
		this.center = center;
		this.offset = offset;
	}

	@Override
	public boolean contains(final float x, final float y, final float z, final VectorBuffer buffer) {
		return false;
	}

	@Override
	public boolean contains(final Vector point, final VectorBuffer buffer) {
		return contains(point.getX(), point.getY(), point.getZ(), buffer);
	}

	@Override
	public final float distanceTo(final Vector point) {
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
	public Vector getResultCenter(final VectorBuffer buffer) {
		return null;
	}

	@Override
	public boolean intersects(final Bounding bounding, final VectorBuffer buffer) {
		return false;
	}

	@Override
	public final boolean intersects(final Ray ray, final VectorBuffer buffer) {
		return intersects(ray.getStart(), ray.getDirection(), buffer);
	}

	@Override
	public boolean intersects(final Vector start, final Vector direction, final VectorBuffer buffer) {
		return false;
	}

	@Override
	public void setCenter(final Vector center) {
		this.center = center;
	}

	@Override
	public void update(final Rotation rotation, final VectorBuffer buffer) {
	}
}
