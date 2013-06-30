package rlib.geom.bounding;

import rlib.geom.Matrix3f;
import rlib.geom.Rotation;
import rlib.geom.Vector;
import rlib.geom.VectorBuffer;

/**
 * Модель формы коробки.
 * 
 * @author Ronn
 */
public final class AxisAlignedBoundingBox extends AbstractBounding
{
	/** матрица для промежуточных вычислений */
	private Matrix3f matrix;
	
	/** вектор, описывающий размер формы */
	private Vector size;

	/** размер формы по x */
	protected float sizeX;
	/** размер формы по y */
	protected float sizeY;
	/** размер формы по z */
	protected float sizeZ;
	
	protected AxisAlignedBoundingBox(Vector center, Vector offset, float sizeX, float sizeY, float sizeZ)
	{
		super(center, offset);
		
		this.matrix = Matrix3f.newInstance();
		this.size = Vector.newInstance(sizeX, sizeY, sizeZ);

		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
	}

	@Override
	public boolean contains(float x, float y, float z, VectorBuffer buffer)
	{
		// получаем итоговый центр формы
		Vector center = getResultCenter(buffer);
		
		// определяем вхождение
		return Math.abs(center.getX() - x) < sizeX && Math.abs(center.getY() - y) < sizeY && Math.abs(center.getZ() - z) < sizeZ;
	}

	@Override
	public BoundingType getBoundingType()
	{
		return BoundingType.AXIS_ALIGNED_BOX;
	}

	@Override
	public Vector getResultCenter(VectorBuffer buffer)
	{
		// получаем контейнер результата
		Vector vector = buffer.getNextVector();
		
		// вносим текущий центр
		vector.set(center);
		
		if(offset == Vector.ZERO)
			return vector;
		
		// добавляем отсут от центра
		return vector.addLocal(offset);
	}
	
	@Override
	public boolean intersects(Bounding bounding, VectorBuffer buffer)
	{
		switch(bounding.getBoundingType())
		{
			case EMPTY: return false;
			case AXIS_ALIGNED_BOX:
			{
				AxisAlignedBoundingBox box = (AxisAlignedBoundingBox) bounding;
				
				// получаем итоговый центр целевой формы
				Vector target = box.getResultCenter(buffer);
				// получаем итоговый центр нашей формы
				Vector center = getResultCenter(buffer);
				
				float sizeX = getSizeX();
				float sizeY = getSizeY();
				float sizeZ = getSizeZ();
				
				// проверяем по оси X
				if(center.getX() + sizeX < target.getX() - box.getSizeX() || center.getX() - sizeX > target.getX() + box.getSizeX())
					return false;
				// проверяем по оси Y
				else if(center.getY() + sizeY < target.getY() - box.getSizeY() || center.getY() - sizeY > target.getY() + box.getSizeY())
					return false;
				// проверяем по оси Z
				else if(center.getZ() + sizeZ < target.getZ() - box.getSizeZ() || center.getZ() - sizeZ > target.getZ() + box.getSizeZ())
					return false;
				
				// если все проверки прошли, значит есть пересечение
				return true;
			}
			case SPHERE:
			{
				BoundingSphere sphere = (BoundingSphere) bounding;
				
				// получаем итоговый центр сферы
				Vector target = sphere.getResultCenter(buffer);
				
				// получаем итоговый центр формы
				Vector center = getResultCenter(buffer);
				
				// получаем радиус сферы
				float radius = sphere.getRadius();
				
				// проверяем по оси X
				if(Math.abs(center.getX() - target.getX()) > radius + getSizeX())
					return false;
				
				// проверяем по оси Y
				if(Math.abs(center.getY() - target.getY()) > radius + getSizeY())
					return false;
				
				// проверяем по оси Z
				if(Math.abs(center.getZ() - target.getZ()) > radius + getSizeZ())
					return false;
				
				return true;
			}
			default:
				log.warning(new IllegalArgumentException("incorrect bounding type " + bounding.getBoundingType()));
		}
		
		return false;
	}

	@Override
	public boolean intersects(Vector start, Vector direction, VectorBuffer buffer)
	{
		float rhs;

		// получаем разницу между вектором начала луча и вектором центра формы
        Vector diff = start.subtract(getResultCenter(buffer), buffer.getNextVector());
        
        // получаем буферные вектора для вычислений
        Vector fWdU = buffer.getNextVector();
        Vector fAWdU = buffer.getNextVector();
        Vector fDdU = buffer.getNextVector();;
        Vector fADdU = buffer.getNextVector();
        Vector fAWxDdU = buffer.getNextVector();

        fWdU.setX(direction.dot(Vector.UNIT_X));
        fAWdU.setX(Math.abs(fWdU.getX()));
        fDdU.setX(diff.dot(Vector.UNIT_X));
        fADdU.setX(Math.abs(fDdU.getX()));
        
        float sizeX = getSizeX();
		float sizeY = getSizeY();
		float sizeZ = getSizeZ();
		
        if(fADdU.getX() > sizeX && fDdU.getX() * fWdU.getX() >= 0.0F) 
            return false;

        fWdU.setY(direction.dot(Vector.UNIT_Y));
        fAWdU.setY(Math.abs(fWdU.getY()));
        fDdU.setY(diff.dot(Vector.UNIT_Y));
        fADdU.setY(Math.abs(fDdU.getY()));
        
        if(fADdU.getY() > sizeY && fDdU.getY() * fWdU.getY() >= 0.0F) 
            return false;

        fWdU.setZ(direction.dot(Vector.UNIT_Z));
        fAWdU.setZ(Math.abs(fWdU.getZ()));
        fDdU.setZ(diff.dot(Vector.UNIT_Z));
        fADdU.setZ(Math.abs(fDdU.getZ()));
        
        if(fADdU.getZ() > sizeZ && fDdU.getZ() * fWdU.getZ() >= 0.0F) 
            return false;

        Vector wCrossD = direction.cross(diff, buffer.getNextVector());

        fAWxDdU.setX(Math.abs(wCrossD.dot(Vector.UNIT_X)));
        
        rhs = sizeY * fAWdU.getZ() + sizeZ * fAWdU.getY();
        
        if(fAWxDdU.getX() > rhs)
            return false;

        fAWxDdU.setY(Math.abs(wCrossD.dot(Vector.UNIT_Y)));
        
        rhs = sizeX * fAWdU.getZ() + sizeZ * fAWdU.getZ();
        
        if(fAWxDdU.getY() > rhs) 
            return false;

        fAWxDdU.setZ(Math.abs(wCrossD.dot(Vector.UNIT_Z)));
        
        rhs = sizeX * fAWdU.getY() + sizeY * fAWdU.getX();
        
        return !(fAWxDdU.getZ() > rhs);
	}

	@Override
	public void update(Rotation rotation, VectorBuffer buffer)
	{
		// конвектируем разворот в матрицу
		matrix.set(rotation);
		
		// приводим матрицу в абсолютные значения
		matrix.absoluteLocal();

		// получаем буферный вектор
		Vector vector = buffer.getNextVector();
		
		// заносим размеры формы в вектор
		vector.set(size);
		
		// применяем разворот матрицы
		matrix.mult(vector, vector);
		
		// сохраняем результат
		sizeX = Math.abs(vector.getX());
		sizeY = Math.abs(vector.getY());
		sizeZ = Math.abs(vector.getZ());
	}

	/**
	 * @return размер формы по X.
	 */
	public final float getSizeX()
	{
		return sizeX;
	}

	/**
	 * @return размер формы по Y.
	 */
	public final float getSizeY()
	{
		return sizeY;
	}

	/**
	 * @return размер формы по Z.
	 */
	public final float getSizeZ()
	{
		return sizeZ;
	}

	@Override
	public String toString()
	{
		return "BoundingBox size = " + size + ", sizeX = " + sizeX + ", sizeY = " + sizeY + ", sizeZ = " + sizeZ + ", center = " + center + ", offset = " + offset;
	}
}
