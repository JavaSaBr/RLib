package rlib.geoengine;

/**
 * Модель квадрата гео.
 *
 * @author Ronn
 */
public final class GeoQuard
{
	/** индекс квадрата по X */
	private int x;
	/** индекс квадрата по Y */
	private int y;

	/** высота квадрата */
	private float height;

	public GeoQuard(int x, int y, float height)
	{
		this.x = x;
		this.y = y;
		this.height = height;
	}

	/**
	 * @return высота квадрата.
	 */
	public final float getHeight()
	{
		return height;
	}
	
	/**
	 * @return индекс квадрата по X.
	 */
	public final int getX()
	{
		return x;
	}

	/**
	 * @return индекс квадрата по Y.
	 */
	public final int getY()
	{
		return y;
	}

	/**
	 * @param height высота квадрата.
	 */
	public final void setHeight(float height)
	{
		this.height = height;
	}

	@Override
	public String toString()
	{
		return "GeoQuard [x = " + x + ", y = " + y + ", height = " + height + "]";
	}
}
