package rlib.geom;

import rlib.util.ExtMath;

/**
 * Модель описание направления объекта в 3D пространстве.
 * 
 * @author Ronn
 */
public class Rotation
{
	public static Rotation newInstance()
	{
		return new Rotation();
	}
	
	public static Rotation newInstance(float x, float y, float z, float w)
	{
		return new Rotation(x, y, z, w);
	}
	
	public static Rotation newInstance(float[] vals)
	{
		return new Rotation(vals[0], vals[1], vals[2], vals[3]);
	}
	
	private float x;
	private float y;
	private float z;
	private float w;
	
	private Rotation()
	{
		w = 1;
	}
	
	private Rotation(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
	 * <code>dot</code> calculates and returns the dot product of this
	 * quaternion with that of the parameter quaternion.
	 *
	 * @param q
	 *            the quaternion to calculate the dot product of.
	 * @return the dot product of this and the parameter quaternion.
	 */
	public float dot(Rotation q)
	{
		return w * q.w + x * q.x + y * q.y + z * q.z;
	}
	
	/**
	 * Получение вектора нужного типа.
	 * 
	 * @param type
	 *            тип вектора.
	 * @param store
	 *            контейнер.
	 * @return вычисленный вектор.
	 */
	public Vector getRotationColumn(VectorType type, Vector store)
	{
		if(store == null)
			store = Vector.newInstance();
		
		float norm = norm();
		
		if(norm != 1.0f)
			norm = ExtMath.invSqrt(norm);
		
		float xx = x * x * norm;
		float xy = x * y * norm;
		float xz = x * z * norm;
		float xw = x * w * norm;
		float yy = y * y * norm;
		float yz = y * z * norm;
		float yw = y * w * norm;
		float zz = z * z * norm;
		float zw = z * w * norm;
		
		switch(type)
		{
			case LEFT:
			{
				store.setX(1 - 2 * (yy + zz));
				store.setY(2 * (xy + zw));
				store.setZ(2 * (xz - yw));
				
				break;
			}
			case UP:
			{
				store.setX(2 * (xy - zw));
				store.setY(1 - 2 * (xx + zz));
				store.setZ(2 * (yz + xw));
				
				break;
			}
			case DIRECTION:
			{
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
	public final float getW()
	{
		return w;
	}
	
	/**
	 * @return степень разворота по оси х.
	 */
	public final float getX()
	{
		return x;
	}
	
	/**
	 * @return степень разворота по оси y.
	 */
	public final float getY()
	{
		return y;
	}
	
	/**
	 * @return степень разворота по оси z.
	 */
	public final float getZ()
	{
		return z;
	}
	
	/**
	 * <code>norm</code> returns the norm of this quaternion. This is the dot product of this quaternion with itself.
	 * 
	 * @return the norm of the quaternion.
	 */
	public float norm()
	{
		return w * w + x * x + y * y + z * z;
	}
	
	public Rotation set(Rotation rotation)
	{
		this.x = rotation.x;
		this.y = rotation.y;
		this.z = rotation.z;
		this.w = rotation.w;
		
		return this;
	}
	
	/**
	 * @param степень
	 *            разворота по оси w.
	 */
	public final void setW(float w)
	{
		this.w = w;
	}
	
	/**
	 * @param степень
	 *            разворота по оси х.
	 */
	public final void setX(float x)
	{
		this.x = x;
	}
	
	public void setXYZW(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
	 * @param степень
	 *            разворота по оси y.
	 */
	public final void setY(float y)
	{
		this.y = y;
	}
	
	/**
	 * @param степень
	 *            разворота по оси z.
	 */
	public final void setZ(float z)
	{
		this.z = z;
	}
	
	/**
	 * Sets the values of this quaternion to the slerp from itself to q2 by
	 * changeAmnt
	 *
	 * @param q2
	 *            Final interpolation value
	 * @param changeAmnt
	 *            The amount diffrence
	 */
	public void slerp(Rotation q2, float changeAmnt)
	{
		if(this.x == q2.x && this.y == q2.y && this.z == q2.z && this.w == q2.w)
		{
			return;
		}
		
		float result = (this.x * q2.x) + (this.y * q2.y) + (this.z * q2.z) + (this.w * q2.w);
		
		if(result < 0.0f)
		{
			// Negate the second quaternion and the result of the dot product
			q2.x = -q2.x;
			q2.y = -q2.y;
			q2.z = -q2.z;
			q2.w = -q2.w;
			result = -result;
		}
		
		// Set the first and second scale for the interpolation
		float scale0 = 1 - changeAmnt;
		float scale1 = changeAmnt;
		
		// Check if the angle between the 2 quaternions was big enough to
		// warrant such calculations
		if((1 - result) > 0.1f)
		{
			// Get the angle between the 2 quaternions, and then store the sin()
			// of that angle
			float theta = ExtMath.acos(result);
			float invSinTheta = 1f / ExtMath.sin(theta);
			
			// Calculate the scale for q1 and q2, according to the angle and
			// it's sine value
			scale0 = ExtMath.sin((1 - changeAmnt) * theta) * invSinTheta;
			scale1 = ExtMath.sin((changeAmnt * theta)) * invSinTheta;
		}
		
		// Calculate the x, y, z and w values for the quaternion by using a
		// special
		// form of linear interpolation for quaternions.
		this.x = (scale0 * this.x) + (scale1 * q2.x);
		this.y = (scale0 * this.y) + (scale1 * q2.y);
		this.z = (scale0 * this.z) + (scale1 * q2.z);
		this.w = (scale0 * this.w) + (scale1 * q2.w);
	}
	
	/**
	 * <code>slerp</code> sets this quaternion's value as an interpolation
	 * between two other quaternions.
	 *
	 * @param q1
	 *            the first quaternion.
	 * @param q2
	 *            the second quaternion.
	 * @param t
	 *            the amount to interpolate between the two quaternions.
	 */
	public Rotation slerp(Rotation q1, Rotation q2, float t)
	{
		// Create a local quaternion to store the interpolated quaternion
		if(q1.x == q2.x && q1.y == q2.y && q1.z == q2.z && q1.w == q2.w)
		{
			this.set(q1);
			return this;
		}
		
		float result = (q1.x * q2.x) + (q1.y * q2.y) + (q1.z * q2.z) + (q1.w * q2.w);
		
		if(result < 0.0f)
		{
			// Negate the second quaternion and the result of the dot product
			q2.x = -q2.x;
			q2.y = -q2.y;
			q2.z = -q2.z;
			q2.w = -q2.w;
			result = -result;
		}
		
		// Set the first and second scale for the interpolation
		float scale0 = 1 - t;
		float scale1 = t;
		
		// Check if the angle between the 2 quaternions was big enough to
		// warrant such calculations
		if((1 - result) > 0.1f)
		{// Get the angle between the 2 quaternions,
			// and then store the sin() of that angle
			float theta = ExtMath.acos(result);
			float invSinTheta = 1f / ExtMath.sin(theta);
			
			// Calculate the scale for q1 and q2, according to the angle and
			// it's sine value
			scale0 = ExtMath.sin((1 - t) * theta) * invSinTheta;
			scale1 = ExtMath.sin((t * theta)) * invSinTheta;
		}
		
		// Calculate the x, y, z and w values for the quaternion by using a
		// special
		// form of linear interpolation for quaternions.
		this.x = (scale0 * q1.x) + (scale1 * q2.x);
		this.y = (scale0 * q1.y) + (scale1 * q2.y);
		this.z = (scale0 * q1.z) + (scale1 * q2.z);
		this.w = (scale0 * q1.w) + (scale1 * q2.w);
		
		// Return the interpolated quaternion
		return this;
	}
	
	/**
	 * Высчитать интерполяцию между 2мя разворотами.
	 * 
	 * @param start начальный разворот.
	 * @param end конечный разворот.
	 * @param done какой % выполненности разворота.
	 * @param forceLinear принудительное использование линейной интерполяции.
	 * @return итоговый разворот.
	 */
	public final Rotation slerp(Rotation start, Rotation end, float done, boolean forceLinear)
	{
		// если развороты эквивалентные
		if(start.equals(end))
		{
			set(start);
			return this;
		}
		
		// рассчитываем косинус угла
		float result = start.dot(end);
		
		// если он отрицательный
		if(result < 0.0f)
		{
			// переворачиваем разворот
			end.negateLocal();
			
			// переворачиваем результат
			result = -result;
		}
		
		// определяем маштаб
		float startScale = 1 - done;
		float endScale = done;
		
		// если есть смысл использовать сфеерическую интерполяцию
		if(!forceLinear && (1 - result) > 0.1f)
		{
			float theta = ExtMath.acos(result);
			float invSinTheta = 1f / ExtMath.sin(theta);

			// обновляем маштабы
			startScale = ExtMath.sin((1 - done) * theta) * invSinTheta;
			endScale = ExtMath.sin((done * theta)) * invSinTheta;
		}
		
		// применяем интерполяцию
		this.x = (startScale * start.getX()) + (endScale * end.getX());
		this.y = (startScale * start.getY()) + (endScale * end.getY());
		this.z = (startScale * start.getZ()) + (endScale * end.getZ());
		this.w = (startScale * start.getW()) + (endScale * end.getW());
		
		return this;
	}
	
	/**
	 * <code>toAngleAxis</code> sets a given angle and axis to that represented by the current quaternion. The values are stored as follows: The axis is provided as a parameter and built by the method, the angle is returned as a float.
	 * 
	 * @param axisStore
	 *            the object we'll store the computed axis in.
	 * @return the angle of rotation in radians.
	 */
	public final float toAngleAxis(Vector axisStore)
	{
		float sqrLength = x * x + y * y + z * z;
		float angle;
		if(sqrLength == 0.0f)
		{
			angle = 0.0f;
			if(axisStore != null)
			{
				axisStore.setX(1.0F);
				axisStore.setY(0.0F);
				axisStore.setZ(0.0F);
			}
		}
		else
		{
			angle = (2.0f * ExtMath.acos(w));
			if(axisStore != null)
			{
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
	public final Matrix3f toRotationMatrix(Matrix3f result)
	{		
		// получаем нормаль этого квантерниона
		float norm = norm();
		
		// we explicitly test norm against one here, saving a division
		// at the cost of a test and branch. Is it worth it?
		float s = (norm == 1f) ? 2f : (norm > 0f) ? 2f / norm : 0;
		
		float x = getX();
		float y = getY();
		float z = getZ();
		float w = getW();
		
		// производим промежуточные рассчеты
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
		
		// рассчитываем итоговые значения матрицы
		result.set(1 - (yy + zz), xy - zw, xz + yw, xy + zw, 1 - (xx + zz), yz - xw, xz - yw, yz + xw, 1 - (xx + yy));
		
		// возвращаем результат
		return result;
	}
	
	/**
	 * Приминение разворота на вектор.
	 *
	 * @param vector вектор, который надо развернуть.
	 * @return полученный вектор.
	 */
	public final Vector multLocal(Vector vector)
	{
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
    public final Rotation fromAngles(float[] angles) 
    {
    	return fromAngles(angles[0],  angles[1],  angles[2]);
    }
	
	 /**
     * Расчет разворота по углам в 3х осях.
     * 
     * @param angleX угол по оси X.
     * @param yAngle угол по оси Y.
     * @param zAngle угол по оси Z.
     */
    public final Rotation fromAngles(float angleX, float yAngle, float zAngle) 
    {
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
    public final Rotation normalizeLocal() 
    {
        float norm = ExtMath.invSqrt(norm());
        
        x *= norm;
        y *= norm;
        z *= norm;
        w *= norm;
        
        return this;
    }

	@Override
	public final int hashCode()
	{
		final int prime = 31;
		int result = 1;
		
		result = prime * result + Float.floatToIntBits(w);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		
		return result;
	}

	@Override
	public final boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(getClass() != obj.getClass())
			return false;
		
		Rotation other = (Rotation) obj;
		
		if(Float.floatToIntBits(w) != Float.floatToIntBits(other.w))
			return false;
		if(Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if(Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if(Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		
		return true;
	}
	
	/**
	 * @return перевернуть значения.
	 */
	public final Rotation negateLocal()
	{
		x = -x;
		y = -y;
		z = -z;
		w = -w;
		
		return this;
	}

	@Override
	public String toString()
	{
		return "Rotation x = " + x + ", y = " + y + ", z = " + z + ", w = " + w;
	}
}
