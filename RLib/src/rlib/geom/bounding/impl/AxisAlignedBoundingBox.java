package rlib.geom.bounding.impl;

import static java.lang.Math.max;
import static java.lang.Math.min;
import rlib.geom.Matrix3f;
import rlib.geom.Rotation;
import rlib.geom.Vector;
import rlib.geom.VectorBuffer;
import rlib.geom.bounding.Bounding;
import rlib.geom.bounding.BoundingType;

/**
 * Реализация формы коробки.
 * 
 * @author Ronn
 */
public final class AxisAlignedBoundingBox extends AbstractBounding {

	/** матрица для промежуточных вычислений */
	private final Matrix3f matrix;

	/** вектор, описывающий размер формы */
	private final Vector size;

	/** размер формы по x */
	protected float sizeX;
	/** размер формы по y */
	protected float sizeY;
	/** размер формы по z */
	protected float sizeZ;

	/** отступ формы от цента по Х */
	protected float offsetX;
	/** отступ формы от цента по Y */
	protected float offsetY;
	/** отступ формы от цента по Z */
	protected float offsetZ;

	public AxisAlignedBoundingBox(Vector center, Vector offset, float sizeX, float sizeY, float sizeZ) {
		super(center, offset);

		this.matrix = Matrix3f.newInstance();
		this.size = Vector.newInstance(sizeX, sizeY, sizeZ);

		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;

		this.offsetX = offset.getX();
		this.offsetY = offset.getY();
		this.offsetZ = offset.getZ();
	}

	@Override
	public boolean contains(float x, float y, float z, VectorBuffer buffer) {
		Vector center = getResultCenter(buffer);
		return Math.abs(center.getX() - x) < sizeX && Math.abs(center.getY() - y) < sizeY && Math.abs(center.getZ() - z) < sizeZ;
	}

	@Override
	public BoundingType getBoundingType() {
		return BoundingType.AXIS_ALIGNED_BOX;
	}

	@Override
	public Vector getResultCenter(VectorBuffer buffer) {

		Vector vector = buffer.getNextVector();
		vector.set(center);

		if(offset == Vector.ZERO) {
			return vector;
		}

		return vector.addLocal(offsetX, offsetY, offsetZ);
	}

	/**
	 * @return размер формы по X.
	 */
	public final float getSizeX() {
		return sizeX;
	}

	/**
	 * @return размер формы по Y.
	 */
	public final float getSizeY() {
		return sizeY;
	}

	/**
	 * @return размер формы по Z.
	 */
	public final float getSizeZ() {
		return sizeZ;
	}

	@Override
	public boolean intersects(Bounding bounding, VectorBuffer buffer) {
		switch(bounding.getBoundingType()) {
			case EMPTY: {
				return false;
			}
			case AXIS_ALIGNED_BOX: {

				AxisAlignedBoundingBox box = (AxisAlignedBoundingBox) bounding;

				Vector target = box.getResultCenter(buffer);
				Vector center = getResultCenter(buffer);

				float sizeX = getSizeX();
				float sizeY = getSizeY();
				float sizeZ = getSizeZ();

				if(center.getX() + sizeX < target.getX() - box.getSizeX() || center.getX() - sizeX > target.getX() + box.getSizeX()) {
					return false;
				} else if(center.getY() + sizeY < target.getY() - box.getSizeY() || center.getY() - sizeY > target.getY() + box.getSizeY()) {
					return false;
				} else if(center.getZ() + sizeZ < target.getZ() - box.getSizeZ() || center.getZ() - sizeZ > target.getZ() + box.getSizeZ()) {
					return false;
				}

				return true;
			}
			case SPHERE: {

				BoundingSphere sphere = (BoundingSphere) bounding;

				Vector target = sphere.getResultCenter(buffer);
				Vector center = getResultCenter(buffer);

				float radius = sphere.getRadius();

				if(Math.abs(center.getX() - target.getX()) > radius + getSizeX()) {
					return false;
				}

				if(Math.abs(center.getY() - target.getY()) > radius + getSizeY()) {
					return false;
				}

				if(Math.abs(center.getZ() - target.getZ()) > radius + getSizeZ()) {
					return false;
				}

				return true;
			}
			default: {
				LOGGER.warning(new IllegalArgumentException("incorrect bounding type " + bounding.getBoundingType()));
			}
		}

		return false;
	}

	@Override
	public boolean intersects(Vector start, Vector direction, VectorBuffer buffer) {

		float divX = 1.0F / (direction.getX() == 0 ? 0.00001F : direction.getX());
		float divY = 1.0F / (direction.getY() == 0 ? 0.00001F : direction.getY());
		float divZ = 1.0F / (direction.getZ() == 0 ? 0.00001F : direction.getZ());

		float sizeX = getSizeX() * 0.5F;
		float sizeY = getSizeY() * 0.5F;
		float sizeZ = getSizeZ() * 0.5F;

		Vector center = getResultCenter(buffer);

		float minX = center.getX() - sizeX;
		float minY = center.getY() - sizeY;
		float minZ = center.getZ() - sizeZ;

		float maxX = center.getX() + sizeX;
		float maxY = center.getY() + sizeY;
		float maxZ = center.getZ() + sizeZ;

		float t1 = (minX - start.getX()) * divX;
		float t2 = (maxX - start.getX()) * divX;
		float t3 = (minY - start.getY()) * divY;
		float t4 = (maxY - start.getY()) * divY;
		float t5 = (minZ - start.getZ()) * divZ;
		float t6 = (maxZ - start.getZ()) * divZ;

		float tmin = max(max(min(t1, t2), min(t3, t4)), min(t5, t6));
		float tmax = min(min(max(t1, t2), max(t3, t4)), max(t5, t6));

		return (tmin <= tmax) && (tmax > 0.f);
	}

	@Override
	public String toString() {
		return "BoundingBox size = " + size + ", sizeX = " + sizeX + ", sizeY = " + sizeY + ", sizeZ = " + sizeZ + ", center = " + center + ", offset = " + offset;
	}

	@Override
	public void update(Rotation rotation, VectorBuffer buffer) {

		matrix.set(rotation);
		matrix.absoluteLocal();

		Vector vector = buffer.getNextVector();
		vector.set(size);

		matrix.mult(vector, vector);

		sizeX = Math.abs(vector.getX());
		sizeY = Math.abs(vector.getY());
		sizeZ = Math.abs(vector.getZ());

		if(offset == Vector.ZERO) {
			return;
		}

		vector.set(offset);

		matrix.mult(vector, vector);

		offsetX = vector.getX();
		offsetY = vector.getY();
		offsetZ = vector.getZ();
	}
}
