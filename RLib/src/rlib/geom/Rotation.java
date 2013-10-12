package rlib.geom;

import rlib.util.ExtMath;
import rlib.util.random.Random;
import rlib.util.random.Randoms;

/**
 * Модель описание направления объекта в 3D пространстве, реализовано в виде
 * кватерниона.
 * 
 * @author Ronn
 */
public class Rotation {

	private static final ThreadLocal<Rotation> ROTATION_LOCAL = new ThreadLocal<Rotation>() {

		@Override
		protected Rotation initialValue() {
			return newInstance();
		};
	};

	private static final ThreadLocal<Random> RANDOM_LOCAL = new ThreadLocal<Random>() {

		@Override
		protected Random initialValue() {
			return Randoms.newFastRandom();
		};
	};

	/**
	 * @return локалый для потока экземпляр разворота.
	 */
	public static final Rotation get() {
		return ROTATION_LOCAL.get();
	}

	public static Rotation newInstance() {
		return new Rotation();
	}

	public static Rotation newInstance(float x, float y, float z, float w) {
		return new Rotation(x, y, z, w);
	}

	public static Rotation newInstance(float[] vals) {
		return new Rotation(vals[0], vals[1], vals[2], vals[3]);
	}

	public static Rotation newInstance(float angleX, float angleY, float angleZ) {
		return new Rotation().fromAngles(angleX, angleY, angleZ);
	}

	private float x;
	private float y;
	private float z;
	private float w;

	private Rotation() {
		w = 1;
	}

	private Rotation(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Рассчет косинуса угла между текущим и указанным разворотом.
	 * 
	 * @param rotation сверяемый разворот.
	 * @return косинус угла между 2мя разворотами.
	 */
	public float dot(Rotation rotation) {
		return w * rotation.w + x * rotation.x + y * rotation.y + z * rotation.z;
	}

	/**
	 * Получение вектора направления нужного типа.
	 * 
	 * @param type тип направления.
	 * @param store контейнер.
	 * @return вычисленный вектор.
	 */
	public Vector getVectorDirection(DirectionType type, Vector store) {

		if(store == null) {
			store = Vector.newInstance();
		}

		float norm = norm();

		if(norm != 1.0f) {
			norm = ExtMath.invSqrt(norm);
		}

		float xx = x * x * norm;
		float xy = x * y * norm;
		float xz = x * z * norm;
		float xw = x * w * norm;
		float yy = y * y * norm;
		float yz = y * z * norm;
		float yw = y * w * norm;
		float zz = z * z * norm;
		float zw = z * w * norm;

		switch(type) {
			case LEFT: {
				store.setX(1 - 2 * (yy + zz));
				store.setY(2 * (xy + zw));
				store.setZ(2 * (xz - yw));
				break;
			}
			case UP: {
				store.setX(2 * (xy - zw));
				store.setY(1 - 2 * (xx + zz));
				store.setZ(2 * (yz + xw));
				break;
			}
			case DIRECTION: {
				store.setX(2 * (xz + yw));
				store.setY(2 * (yz - xw));
				store.setZ(1 - 2 * (xx + yy));
			}
		}

		return store;
	}

	/**
	 * @return степень разворота по оси w.
	 */
	public final float getW() {
		return w;
	}

	/**
	 * @return степень разворота по оси х.
	 */
	public final float getX() {
		return x;
	}

	/**
	 * @return степень разворота по оси y.
	 */
	public final float getY() {
		return y;
	}

	/**
	 * @return степень разворота по оси z.
	 */
	public final float getZ() {
		return z;
	}

	/**
	 * @return норма этого разворота.
	 */
	public float norm() {
		return w * w + x * x + y * y + z * z;
	}

	public Rotation set(Rotation rotation) {
		this.x = rotation.x;
		this.y = rotation.y;
		this.z = rotation.z;
		this.w = rotation.w;
		return this;
	}

	/**
	 * @param степень разворота по оси w.
	 */
	public final void setW(float w) {
		this.w = w;
	}

	/**
	 * @param степень разворота по оси х.
	 */
	public final void setX(float x) {
		this.x = x;
	}

	public void setXYZW(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * @param степень разворота по оси y.
	 */
	public final void setY(float y) {
		this.y = y;
	}

	/**
	 * @param степень разворота по оси z.
	 */
	public final void setZ(float z) {
		this.z = z;
	}

	/**
	 * Рассчитывает промежуточный разворот между текуим и указанным в
	 * зависимости от указанного %.
	 * 
	 * @param end конечный разворот.
	 * @param percent % разворота от текущего к конечному.
	 */
	public void slerp(Rotation end, float percent) {

		if(x == end.x && y == end.y && z == end.z && w == end.w) {
			return;
		}

		float result = (x * end.x) + (y * end.y) + (z * end.z) + (w * end.w);

		if(result < 0.0f) {
			end.x = -end.x;
			end.y = -end.y;
			end.z = -end.z;
			end.w = -end.w;
			result = -result;
		}

		float scale0 = 1 - percent;
		float scale1 = percent;

		if((1 - result) > 0.1f) {

			float theta = ExtMath.acos(result);
			float invSinTheta = 1f / ExtMath.sin(theta);

			scale0 = ExtMath.sin((1 - percent) * theta) * invSinTheta;
			scale1 = ExtMath.sin((percent * theta)) * invSinTheta;
		}

		x = (scale0 * x) + (scale1 * end.x);
		y = (scale0 * y) + (scale1 * end.y);
		z = (scale0 * z) + (scale1 * end.z);
		w = (scale0 * w) + (scale1 * end.w);
	}

	/**
	 * Рассчитывает промежуточный разворот от указанного стартового, до
	 * указанного конечного в зависимости от указанного %.
	 * 
	 * @param start стартовый разворот.
	 * @param end конечный разворот.
	 * @param percent % разворота от стартового до конечного.
	 */
	public Rotation slerp(Rotation start, Rotation end, float percent) {
		return slerp(start, end, percent, false);
	}

	/**
	 * Рассчитывает промежуточный разворот от указанного стартового, до
	 * указанного конечного в зависимости от указанного %.
	 * 
	 * @param start стартовый разворот.
	 * @param end конечный разворот.
	 * @param percent % разворота от стартового до конечного.
	 * @param forceLinear принудительное использование линейной интерполяции.
	 */
	public final Rotation slerp(Rotation start, Rotation end, float percent, boolean forceLinear) {

		if(start.equals(end)) {
			set(start);
			return this;
		}

		float result = start.dot(end);

		if(result < 0.0f) {
			end.negateLocal();
			result = -result;
		}

		float startScale = 1 - percent;
		float endScale = percent;

		if(!forceLinear && (1 - result) > 0.1f) {

			float theta = ExtMath.acos(result);
			float invSinTheta = 1f / ExtMath.sin(theta);

			startScale = ExtMath.sin((1 - percent) * theta) * invSinTheta;
			endScale = ExtMath.sin((percent * theta)) * invSinTheta;
		}

		this.x = (startScale * start.getX()) + (endScale * end.getX());
		this.y = (startScale * start.getY()) + (endScale * end.getY());
		this.z = (startScale * start.getZ()) + (endScale * end.getZ());
		this.w = (startScale * start.getW()) + (endScale * end.getW());

		return this;
	}

	/**
	 * <code>toAngleAxis</code> sets a given angle and axis to that represented
	 * by the current quaternion. The values are stored as follows: The axis is
	 * provided as a parameter and built by the method, the angle is returned as
	 * a float.
	 * 
	 * @param axisStore the object we'll store the computed axis in.
	 * @return the angle of rotation in radians.
	 */
	public final float toAngleAxis(Vector axisStore) {

		float sqrLength = x * x + y * y + z * z;
		float angle;

		if(sqrLength == 0.0f) {

			angle = 0.0f;

			if(axisStore != null) {
				axisStore.setX(1.0F);
				axisStore.setY(0.0F);
				axisStore.setZ(0.0F);
			}

		} else {

			angle = (2.0f * ExtMath.acos(w));

			if(axisStore != null) {

				float invLength = (1.0f / ExtMath.sqrt(sqrLength));

				axisStore.setX(x * invLength);
				axisStore.setY(y * invLength);
				axisStore.setZ(z * invLength);
			}
		}

		return angle;
	}

	/**
	 * Конвектирование квантерниона в матрицу 3х3
	 * 
	 * @param result матрица, в которую занести нужно результат.
	 * @return результат в виде матрицы.
	 */
	public final Matrix3f toRotationMatrix(Matrix3f result) {

		float norm = norm();

		float s = (norm == 1f) ? 2f : (norm > 0f) ? 2f / norm : 0;

		float x = getX();
		float y = getY();
		float z = getZ();
		float w = getW();

		float xs = x * s;
		float ys = y * s;
		float zs = z * s;
		float xx = x * xs;
		float xy = x * ys;
		float xz = x * zs;
		float xw = w * xs;
		float yy = y * ys;
		float yz = y * zs;
		float yw = w * ys;
		float zz = z * zs;
		float zw = w * zs;

		result.set(1 - (yy + zz), xy - zw, xz + yw, xy + zw, 1 - (xx + zz), yz - xw, xz - yw, yz + xw, 1 - (xx + yy));
		return result;
	}

	/**
	 * Приминение разворота на вектор.
	 * 
	 * @param vector вектор, который надо развернуть.
	 * @return полученный вектор.
	 */
	public final Vector multLocal(Vector vector) {

		float vectorX = vector.getX();
		float vectorY = vector.getY();
		float vectorZ = vector.getZ();

		float x = getX();
		float y = getY();
		float z = getZ();
		float w = getW();

		vector.setX(w * w * vectorX + 2 * y * w * vectorZ - 2 * z * w * vectorY + x * x * vectorX + 2 * y * x * vectorY + 2 * z * x * vectorZ - z * z * vectorX - y * y * vectorX);
		vector.setY(2 * x * y * vectorX + y * y * vectorY + 2 * z * y * vectorZ + 2 * w * z * vectorX - z * z * vectorY + w * w * vectorY - 2 * x * w * vectorZ - x * x * vectorY);
		vector.setZ(2 * x * z * vectorX + 2 * y * z * vectorY + z * z * vectorZ - 2 * w * y * vectorX - y * y * vectorZ + 2 * w * x * vectorY - x * x * vectorZ + w * w * vectorZ);

		return vector;
	}

	/**
	 * Расчет разворота по углам в 3х осях.
	 * 
	 * @param angles угол наклона по осям.
	 */
	public final Rotation fromAngles(float[] angles) {
		return fromAngles(angles[0], angles[1], angles[2]);
	}

	/**
	 * Расчет разворота по углам в 3х осях.
	 * 
	 * @param angleX угол по оси X.
	 * @param yAngle угол по оси Y.
	 * @param zAngle угол по оси Z.
	 */
	public final Rotation fromAngles(float angleX, float yAngle, float zAngle) {

		float angle = zAngle * 0.5f;

		float sinZ = ExtMath.sin(angle);
		float cosZ = ExtMath.cos(angle);

		angle = yAngle * 0.5f;

		float sinY = ExtMath.sin(angle);
		float cosY = ExtMath.cos(angle);

		angle = angleX * 0.5f;

		float sinX = ExtMath.sin(angle);
		float cosX = ExtMath.cos(angle);

		float cosYXcosZ = cosY * cosZ;
		float sinYXsinZ = sinY * sinZ;
		float cosYXsinZ = cosY * sinZ;
		float sinYXcosZ = sinY * cosZ;

		w = (cosYXcosZ * cosX - sinYXsinZ * sinX);
		x = (cosYXcosZ * sinX + sinYXsinZ * cosX);
		y = (sinYXcosZ * cosX + cosYXsinZ * sinX);
		z = (cosYXsinZ * cosX - sinYXcosZ * sinX);

		normalizeLocal();

		return this;
	}

	/**
	 * Нормализация текущего разворота.
	 */
	public final Rotation normalizeLocal() {

		float norm = ExtMath.invSqrt(norm());

		x *= norm;
		y *= norm;
		z *= norm;
		w *= norm;

		return this;
	}

	@Override
	public final int hashCode() {

		final int prime = 31;
		int result = 1;

		result = prime * result + Float.floatToIntBits(w);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);

		return result;
	}

	@Override
	public final boolean equals(Object obj) {

		if(this == obj) {
			return true;
		}

		if(obj == null)
			return false;

		if(getClass() != obj.getClass()) {
			return false;
		}

		Rotation other = (Rotation) obj;

		if(Float.floatToIntBits(w) != Float.floatToIntBits(other.w)) {
			return false;
		}
		if(Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) {
			return false;
		}
		if(Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
			return false;
		}
		if(Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) {
			return false;
		}

		return true;
	}

	/**
	 * @return перевернуть значения.
	 */
	public final Rotation negateLocal() {
		x = -x;
		y = -y;
		z = -z;
		w = -w;
		return this;
	}

	/**
	 * Рассчитать разворот, который будет смотреть в указанном напралвении.
	 * 
	 * @param direction направление, куда нужно смотреть.
	 * @param up вектор для ориаентации где верх а где низ.
	 * @param buffer буффер векторов для рассчета.
	 */
	public void lookAt(Vector direction, Vector up, VectorBuffer buffer) {

		Vector first = buffer.getNextVector();
		first.set(direction).normalizeLocal();

		Vector second = buffer.getNextVector();
		second.set(up).crossLocal(direction).normalizeLocal();

		Vector thrid = buffer.getNextVector();
		thrid.set(direction).crossLocal(second).normalizeLocal();

		fromAxes(second, thrid, first);
		normalizeLocal();
	}

	/**
	 * 
	 * <code>fromAxes</code> creates a <code>Quaternion</code> that represents
	 * the coordinate system defined by three axes. These axes are assumed to be
	 * orthogonal and no error checking is applied. Thus, the user must insure
	 * that the three axes being provided indeed represents a proper right
	 * handed coordinate system.
	 * 
	 * @param axisX vector representing the x-axis of the coordinate system.
	 * @param axisY vector representing the y-axis of the coordinate system.
	 * @param axisZ vector representing the z-axis of the coordinate system.
	 */
	public Rotation fromAxes(Vector axisX, Vector axisY, Vector axisZ) {
		return fromRotationMatrix(axisX.getX(), axisY.getX(), axisZ.getX(), axisX.getY(), axisY.getY(), axisZ.getY(), axisX.getZ(), axisY.getZ(), axisZ.getZ());
	}

	public Rotation fromRotationMatrix(float val_0_0, float val_0_1, float val_0_2, float val_1_0, float val_1_1, float val_1_2, float val_2_0, float val_2_1, float val_2_2) {
		// Use the Graphics Gems code, from
		// ftp://ftp.cis.upenn.edu/pub/graphics/shoemake/quatut.ps.Z
		// *NOT* the "Matrix and Quaternions FAQ", which has errors!

		// the trace is the sum of the diagonal elements; see
		// http://mathworld.wolfram.com/MatrixTrace.html
		float t = val_0_0 + val_1_1 + val_2_2;

		// we protect the division by s by ensuring that s>=1
		if(t >= 0) { // |w| >= .5
			float s = ExtMath.sqrt(t + 1); // |s|>=1 ...
			w = 0.5f * s;
			s = 0.5f / s; // so this division isn't bad
			x = (val_2_1 - val_1_2) * s;
			y = (val_0_2 - val_2_0) * s;
			z = (val_1_0 - val_0_1) * s;
		} else if((val_0_0 > val_1_1) && (val_0_0 > val_2_2)) {
			float s = ExtMath.sqrt(1.0f + val_0_0 - val_1_1 - val_2_2); // |s|>=1
			x = s * 0.5f; // |x| >= .5
			s = 0.5f / s;
			y = (val_1_0 + val_0_1) * s;
			z = (val_0_2 + val_2_0) * s;
			w = (val_2_1 - val_1_2) * s;
		} else if(val_1_1 > val_2_2) {
			float s = ExtMath.sqrt(1.0f + val_1_1 - val_0_0 - val_2_2); // |s|>=1
			y = s * 0.5f; // |y| >= .5
			s = 0.5f / s;
			x = (val_1_0 + val_0_1) * s;
			z = (val_2_1 + val_1_2) * s;
			w = (val_0_2 - val_2_0) * s;
		} else {
			float s = ExtMath.sqrt(1.0f + val_2_2 - val_0_0 - val_1_1); // |s|>=1
			z = s * 0.5f; // |z| >= .5
			s = 0.5f / s;
			x = (val_0_2 + val_2_0) * s;
			y = (val_2_1 + val_1_2) * s;
			w = (val_1_0 - val_0_1) * s;
		}

		return this;
	}

	/**
	 * Создание случайного разворота.
	 */
	public void random() {
		Random random = RANDOM_LOCAL.get();
		setXYZW(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat());
	}

	@Override
	public String toString() {
		return "Rotation x = " + x + ", y = " + y + ", z = " + z + ", w = " + w;
	}
}
