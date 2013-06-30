package rlib.gamemodel;

/**
 * Интерфейс для реализации игрового объекта.
 * 
 * @author Ronn
 */
public interface GameObject extends Comparable<GameObject>
{
	/**
	 * @return направление разворота объекта.
	 */
	public int getHeading();
	
	/**
	 * @return уникальный ид объекта.
	 */
	public int getObjectId();
	
	/**
	 * @return х координата объекта.
	 */
	public float getX();
	
	/**
	 * @return у координата объекта.
	 */
	public float getY();
	
	/**
	 * @return z координата объекта.
	 */
	public float getZ();
	
	/**
	 * @return видимый ли объект.
	 */
	public boolean isVisible();
	
	/**
	 * Спавн объекта в указанные координаты с указанным разворотом.
	 * 
	 * @param x координата.
	 * @param y координата.
	 * @param z координата.
	 * @param heading разворот.
	 */
	public void spawnMe(float x, float y, float z, int heading);
}
