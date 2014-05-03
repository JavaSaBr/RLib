package rlib.geom;

import rlib.util.pools.Foldable;

/**
 * Модель матрицы 3x3.
 * 
 * @author Ronn
 */
public final class Matrix3f implements Foldable {

	public static final Matrix3f ZERO = new Matrix3f(0, 0, 0, 0, 0, 0, 0, 0, 0);

	public static final Matrix3f IDENTITY = new Matrix3f();

	public static Matrix3f newInstance() {
		return new Matrix3f();
	}

	public static Matrix3f newInstance(final float val_0_0, final float val_0_1, final float val_0_2, final float val_1_0, final float val_1_1, final float val_1_2, final float val_2_0,
			final float val_2_1, final float val_2_2) {
		return new Matrix3f(val_0_0, val_0_1, val_0_2, val_1_0, val_1_1, val_1_2, val_2_0, val_2_1, val_2_2);
	}

	/** значения матрицы */
	protected float val_0_0, val_0_1, val_0_2;
	protected float val_1_0, val_1_1, val_1_2;
	protected float val_2_0, val_2_1, val_2_2;

	public Matrix3f() {
		val_0_1 = val_0_2 = val_1_0 = val_1_2 = val_2_0 = val_2_1 = 0;
		val_0_0 = val_1_1 = val_2_2 = 1;
	}

	private Matrix3f(final float val_0_0, final float val_0_1, final float val_0_2, final float val_1_0, final float val_1_1, final float val_1_2, final float val_2_0, final float val_2_1,
			final float val_2_2) {
		this.val_0_0 = val_0_0;
		this.val_0_1 = val_0_1;
		this.val_0_2 = val_0_2;
		this.val_1_0 = val_1_0;
		this.val_1_1 = val_1_1;
		this.val_1_2 = val_1_2;
		this.val_2_0 = val_2_0;
		this.val_2_1 = val_2_1;
		this.val_2_2 = val_2_2;
	}

	/**
	 * Приведение всех значений матрицы в абсолютные.
	 */
	public void absoluteLocal() {
		val_0_0 = Math.abs(val_0_0);
		val_0_1 = Math.abs(val_0_1);
		val_0_2 = Math.abs(val_0_2);
		val_1_0 = Math.abs(val_1_0);
		val_1_1 = Math.abs(val_1_1);
		val_1_2 = Math.abs(val_1_2);
		val_2_0 = Math.abs(val_2_0);
		val_2_1 = Math.abs(val_2_1);
		val_2_2 = Math.abs(val_2_2);
	}

	/**
	 * Приминение разворота матрицы на вектор.
	 * 
	 * @param vector изначальный вектор.
	 * @param result контейнер результата.
	 * @return результат приминения матрицы.
	 */
	public Vector mult(final Vector vector, final Vector result) {

		final float x = vector.x;
		final float y = vector.y;
		final float z = vector.z;

		result.x = val_0_0 * x + val_0_1 * y + val_0_2 * z;
		result.y = val_1_0 * x + val_1_1 * y + val_1_2 * z;
		result.z = val_2_0 * x + val_2_1 * y + val_2_2 * z;

		return result;
	}

	public void set(final float val_0_0, final float val_0_1, final float val_0_2, final float val_1_0, final float val_1_1, final float val_1_2, final float val_2_0, final float val_2_1,
			final float val_2_2) {
		this.val_0_0 = val_0_0;
		this.val_0_1 = val_0_1;
		this.val_0_2 = val_0_2;
		this.val_1_0 = val_1_0;
		this.val_1_1 = val_1_1;
		this.val_1_2 = val_1_2;
		this.val_2_0 = val_2_0;
		this.val_2_1 = val_2_1;
		this.val_2_2 = val_2_2;
	}

	/**
	 * Формирование матрицы в соответствии с указанным разворотом.
	 * 
	 * @param rotation целевой разворот.
	 * @return соответствующая матрица.
	 */
	public Matrix3f set(final Rotation rotation) {
		return rotation.toRotationMatrix(this);
	}
}
