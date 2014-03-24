package rlib.geom;

import rlib.util.ExtMath;

/**
 * Реализация игровой позиции и вектора.
 * 
 * @author Ronn
 */
public final class Vector implements GamePoint {

	/** нулнвой вектор */
	public final static Vector ZERO = new Vector(0, 0, 0);
	/** вектор в бесконечность */
	public final static Vector NAN = new Vector(Float.NaN, Float.NaN, Float.NaN);

	/** вектор в сторону оси X */
	public final static Vector UNIT_X = new Vector(1, 0, 0);
	/** вектор в сторону оси Y */
	public final static Vector UNIT_Y = new Vector(0, 1, 0);
	/** вектор в сторону оси Z */
	public final static Vector UNIT_Z = new Vector(0, 0, 1);

	/** вектор в сторону XYZ */
	public final static Vector UNIT_XYZ = new Vector(1, 1, 1);

	public final static Vector POSITIVE_INFINITY = new Vector(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
	public final static Vector NEGATIVE_INFINITY = new Vector(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

	/**
	 * Проверка на валидность вектора.
	 * 
	 * @param vector проверяемый вектор.
	 * @return валиден ли вектор.
	 */
	public static boolean isValidVector(Vector vector) {

		if(vector == null) {
			return false;
		}

		if(Float.isNaN(vector.getX()) || Float.isNaN(vector.getY()) || Float.isNaN(vector.getZ())) {
			return false;
		}

		if(Float.isInfinite(vector.getX()) || Float.isInfinite(vector.getY()) || Float.isInfinite(vector.getZ())) {
			return false;
		}

		return true;
	}

	public static Vector newInstance() {
		return new Vector();
	}

	public static Vector newInstance(float x, float y, float z) {
		return new Vector(x, y, z);
	}

	public static Vector newInstance(float[] vals) {
		return new Vector(vals[0], vals[1], vals[2]);
	}

	/** координаты */
	protected float x;
	protected float y;

	protected float z;

	private Vector() {
		super();
	}

	private Vector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Локальное добавление координат к вектору.
	 */
	public Vector addLocal(float addX, float addY, float addZ) {
		x += addX;
		y += addY;
		z += addZ;
		return this;
	}

	/**
	 * Локальное добавление указанного вектора.
	 */
	public Vector addLocal(Vector vec) {
		return addLocal(vec.x, vec.y, vec.z);
	}

	/**
	 * Вычисление векторного произведения текущего вектора на указанный.
	 * 
	 * @param otherX модификатор x.
	 * @param otherY модификатор y.
	 * @param otherZ модификатор z.
	 * @param result вектор для хранения результата.
	 * @return результирующий веткор.
	 */
	public Vector cross(float otherX, float otherY, float otherZ, Vector result) {

		float resX = ((y * otherZ) - (z * otherY));
		float resY = ((z * otherX) - (x * otherZ));
		float resZ = ((x * otherY) - (y * otherX));

		result.setXYZ(resX, resY, resZ);
		return result;
	}

	/**
	 * Вычисление векторного произведения текущего вектора на указанный.
	 * 
	 * @param vector вектор, на который будет произведено произведение.
	 * @return результирующий вектор.
	 */
	public Vector cross(Vector vector) {
		return cross(vector, newInstance());
	}

	/**
	 * Вычисление векторного произведения текущего вектора на указанный.
	 * 
	 * @param vector вектор, на который будет произведено произведение.
	 * @param result вектор для хранения результата.
	 * @return результирующий веткор.
	 */
	public Vector cross(Vector vector, Vector result) {
		return cross(vector.x, vector.y, vector.z, result);
	}

	/**
	 * Вычислние векторного произведение этого вектора.
	 * 
	 * @param otherX координата вектора, с которым нужно произвести
	 * произведение.
	 * @param otherY координата вектора, с которым нужно произвести
	 * произведение.
	 * @param otherZ координата вектора, с которым нужно произвести
	 * произведение.
	 * @return этот же вектор.
	 */
	public Vector crossLocal(float otherX, float otherY, float otherZ) {

		float tempx = (y * otherZ) - (z * otherY);
		float tempy = (z * otherX) - (x * otherZ);

		z = (x * otherY) - (y * otherX);
		x = tempx;
		y = tempy;

		return this;
	}

	/**
	 * Вычислние векторного произведение этого вектора.
	 * 
	 * @param vector вектор, с которым нужно произвести произведение.
	 * @return этот же вектор.
	 */
	public Vector crossLocal(Vector vector) {
		return crossLocal(vector.x, vector.y, vector.z);
	}

	/**
	 * Рассчет расстояния до указанного вектора.
	 * 
	 * @param vector целевой вектор.
	 * @return расстояние до вектора.
	 */
	public float distance(Vector vector) {
		return ExtMath.sqrt(distanceSquared(vector));
	}

	/**
	 * Рассчет квадрата расстояния до указанного вектора.
	 * 
	 * @param targetX координата.
	 * @param targetY координата.
	 * @param targetZ координата.
	 * @return квадрат расстояния до вектора.
	 */
	public float distanceSquared(float targetX, float targetY, float targetZ) {

		float dx = x - targetX;
		float dy = y - targetY;
		float dz = z - targetZ;

		return dx * dx + dy * dy + dz * dz;
	}

	/**
	 * Рассчет квадрата расстояния до указанного вектора.
	 * 
	 * @param vector целевой вектор.
	 * @return квадрат расстояния до вектора.
	 */
	public float distanceSquared(Vector vector) {
		return distanceSquared(vector.x, vector.y, vector.z);
	}

	/**
	 * Вычисление скалярного произведение векторов.
	 * 
	 * @param vector вектор, на который умножаем.
	 * @return результат произведения.
	 */
	public float dot(Vector vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}

	@Override
	public int getHeading() {
		return 0;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getZ() {
		return z;
	}

	/**
	 * @return является ли вектор нулевым.
	 */
	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}

	/**
	 * Локальное умножение вектора.
	 */
	public Vector multLocal(float scalar) {
		return multLocal(scalar, scalar, scalar);
	}

	/**
	 * Локальное умножение вектора.
	 */
	public Vector multLocal(float x, float y, float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}

	/**
	 * Локальное умножение вектора.
	 */
	public Vector multLocal(Vector vector) {
		return multLocal(vector.getX(), vector.getY(), vector.getZ());
	}

	/**
	 * Получение противоположного вектора.
	 */
	public Vector negate() {
		return newInstance(-getX(), -getY(), -getZ());
	}

	/**
	 * Изменение вектора на противоположное.
	 */
	public Vector negateLocal() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	/**
	 * Конвектирование вектора в еденичный.
	 */
	public Vector normalize() {

		float length = x * x + y * y + z * z;

		if(length != 1F && length != 0F) {
			length = 1.0F / ExtMath.sqrt(length);
			return new Vector(x * length, y * length, z * length);
		}

		return new Vector(x, y, z);
	}

	/**
	 * Конвектирование вектора в еденичный.
	 */
	public Vector normalizeLocal() {

		float length = x * x + y * y + z * z;

		if(length != 1f && length != 0f) {
			length = 1.0f / ExtMath.sqrt(length);
			x *= length;
			y *= length;
			z *= length;
		}

		return this;
	}

	public Vector set(Vector vector) {
		return setXYZ(vector.getX(), vector.getY(), vector.getZ());
	}

	@Override
	public GamePoint setHeading(int heading) {
		return this;
	}

	@Override
	public Vector setX(float x) {
		this.x = x;
		return this;
	}

	@Override
	public Vector setXYZ(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	@Override
	public GamePoint setXYZH(float x, float y, float z, int heading) {
		return this;
	}

	@Override
	public Vector setY(float y) {
		this.y = y;
		return this;
	}

	@Override
	public Vector setZ(float z) {
		this.z = z;
		return this;
	}

	/**
	 * Получение вектора разности между текущим и указанным.
	 * 
	 * @param vector вектор, который нужно вычесть.
	 * @param result результат вычитания.
	 * @return разность между текущим и указанным вектором.
	 */
	public Vector subtract(Vector vector, Vector result) {
		result.x = x - vector.x;
		result.y = y - vector.y;
		result.z = z - vector.z;
		return result;
	}

	/**
	 * Локальное вычитание вектора.
	 * 
	 * @param subX вычитаемый X.
	 * @param subY вычитаемый Y.
	 * @param subZ вычитаемый Z.
	 * @return итоговый вектор.
	 */
	public Vector subtractLocal(float subX, float subY, float subZ) {
		x -= subX;
		y -= subY;
		z -= subZ;
		return this;
	}

	/**
	 * Локальное вычитание вектора.
	 * 
	 * @param vector вычитаемый вектор.
	 * @return итоговый вектор.
	 */
	public Vector subtractLocal(Vector vector) {
		return subtractLocal(vector.x, vector.y, vector.z);
	}

	@Override
	public String toString() {
		return "Vector [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
}
