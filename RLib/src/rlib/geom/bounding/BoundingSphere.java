package rlib.geom.bounding;

import rlib.geom.Vector;
import rlib.geom.VectorBuffer;

/**
 * Модель формы сферы.
 * 
 * @author Ronn
 */
public final class BoundingSphere extends AbstractBounding
{
	/** радиус сферы */
	protected float radius;
	/** квадрат радиуса сферы */
	protected float squareRadius;
	
	protected BoundingSphere(Vector center, Vector offset, float radius)
	{
		super(center, offset);
		
		this.radius = radius;
		this.squareRadius = radius * radius;
	}

	@Override
	public boolean contains(float x, float y, float z, VectorBuffer buffer)
	{
		// получаем итоговый центр
		Vector center = getResultCenter(buffer);
		
		// смотрим входит ли в радиус
		return center.distanceSquared(x, y, z) < squareRadius;
	}

	@Override
	public BoundingType getBoundingType()
	{
		return BoundingType.SPHERE;
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
			case EMPTY:	return false;
			case SPHERE:
			{
				BoundingSphere sphere = (BoundingSphere) bounding;
				
				// получаем текущий центр
				Vector diff = getResultCenter(buffer);
				
				// получаем расстояние между центрами
				diff.subtractLocal(sphere.getResultCenter(buffer));
				
				// получаем сумму радиусов
				float rsum = getRadius() + sphere.getRadius();
				
				// смотрим входят ли
				return (diff.dot(diff) <= rsum * rsum);
			}
			case AXIS_ALIGNED_BOX:
			{
				AxisAlignedBoundingBox box = (AxisAlignedBoundingBox) bounding;
				
				// получаем центр сферы
				Vector center = getResultCenter(buffer);
				
				// получаем центр  бокса
				Vector target = box.getResultCenter(buffer);
				
				if(!(Math.abs(target.getX() - center.getX()) < getRadius() + box.getSizeX()))
					return false;
				
				if(!(Math.abs(target.getY() - center.getY()) < getRadius() + box.getSizeY()))
					return false;
				
				if(!(Math.abs(target.getZ() - center.getZ()) < getRadius() + box.getSizeZ()))
					return false;
				
				return true;
			}
		}
		
		return false;
	}


	@Override
	public boolean intersects(Vector start, Vector direction, VectorBuffer buffer)
	{
		// получаем свободный вектор
		Vector diff = buffer.getNextVector();
		
		// рассчитываем разницу между вектором старта луча и центром сферы
		diff.set(start).subtractLocal(getResultCenter(buffer));
		 
		// вычисляем разницу между скалярным произведением вектора и квадратом радиуса сферы
		float a = start.dot(diff) - squareRadius;
		
		// если она меньше либа равна нулю
		if(a <= 0.0)
			// возвращаем что пересекается
			return true;

		// разницу между скалярным произведением вектора и квадратом радиуса сферы
		float b = direction.dot(diff);
		
		// если положительное
		if(b >= 0.0)
			// то не пересекаются
			return false;
		
		return b * b >= a;
	}

	/**
	 * @return радиус сферы.
	 */
	public float getRadius()
	{
		return radius;
	}

	@Override
	public String toString()
	{
		return "BoundingSphere [radius=" + radius + ", squareRadius=" + squareRadius + "]";
	}
}
