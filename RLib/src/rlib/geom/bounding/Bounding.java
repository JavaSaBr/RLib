package rlib.geom.bounding;

import rlib.geom.Ray;
import rlib.geom.Rotation;
import rlib.geom.Vector;
import rlib.geom.VectorBuffer;

/**
 * Интерфейс для реализации формы из точек.
 * 
 * @author Ronn
 */
public interface Bounding {

	/**
	 * Входит ли в форму указанная точка.
	 * 
	 * @param x координата точки.
	 * @param y координата точки.
	 * @param z координата точки.
	 * @param buffer векторный буффер.
	 * @return входит ли точка.
	 */
	public boolean contains(float x, float y, float z, VectorBuffer buffer);

	/**
	 * Входит ли в форму указанная точка.
	 * 
	 * @param point определяемая точка.
	 * @param buffer векторный буффер.
	 * @return входит ли.
	 */
	public boolean contains(Vector point, VectorBuffer buffer);

	/**
	 * Определение дистанции от центра формы до указанной точки.
	 * 
	 * @param point целевая точка.
	 * @return дистанция до точки.
	 */
	public float distanceTo(Vector point);

	/**
	 * @return тип формы.
	 */
	public BoundingType getBoundingType();

	/**
	 * @return центр формы.
	 */
	public Vector getCenter();

	/**
	 * @return смещение вектора.
	 */
	public Vector getOffset();

	/**
	 * @param buffer буффер векторов.
	 * @return итоговый вектор центра формы.
	 */
	public Vector getResultCenter(VectorBuffer buffer);

	/**
	 * Проверка пересечения с формой.
	 * 
	 * @param bounding проверяемая форма.
	 * @return пересекаются ли формы.
	 */
	public boolean intersects(Bounding bounding, VectorBuffer buffer);

	/**
	 * Пересекает ли указанный луч эту форму.
	 * 
	 * @param ray целевой луч.
	 * @param buffer буфер векторов.
	 * @return перескает ли.
	 */
	public boolean intersects(Ray ray, VectorBuffer buffer);

	/**
	 * Пересекает ли указанный луч эту форму.
	 * 
	 * @param start точка начало луча.
	 * @param direction направление луча.
	 * @param buffer буфер векторов.
	 * @return пересекает ли.
	 */
	public boolean intersects(Vector start, Vector direction, VectorBuffer buffer);

	/**
	 * @param center новый центр.
	 */
	public void setCenter(Vector center);

	/**
	 * Обновление формы с учетом вращения.
	 * 
	 * @param rotation раворот формы.
	 * @param buffer векторный буффер.
	 */
	public void update(Rotation rotation, VectorBuffer buffer);
}
