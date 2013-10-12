package rlib.geom;

/**
 * Интерфейс для реализации позиции с вещесвенным координатами.
 * 
 * @author Ronn
 */
public interface GamePoint {

	/**
	 * @return направление позиции.
	 */
	public int getHeading();

	/**
	 * @return х координата.
	 */
	public float getX();

	/**
	 * @return y координата.
	 */
	public float getY();

	/**
	 * @return z координата.
	 */
	public float getZ();

	/**
	 * @param heading направление позиции.
	 * @return this.
	 */
	public GamePoint setHeading(int heading);

	/**
	 * @param x координата.
	 * @return this.
	 */
	public GamePoint setX(float x);

	/**
	 * @param x координата.
	 * @param y координата.
	 * @param z координата.
	 * @return this.
	 */
	public GamePoint setXYZ(float x, float y, float z);

	/**
	 * @param x координата.
	 * @param y координата.
	 * @param z координата.
	 * @param heading направление разварота.
	 * @return this.
	 */
	public GamePoint setXYZH(float x, float y, float z, int heading);

	/**
	 * @param y координата.
	 * @return this.
	 */
	public GamePoint setY(float y);

	/**
	 * @param z координата.
	 * @return this.
	 */
	public GamePoint setZ(float z);
}
