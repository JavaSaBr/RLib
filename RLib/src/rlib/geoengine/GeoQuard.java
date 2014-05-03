package rlib.geoengine;

/**
 * Модель квадрата гео.
 *
 * @author Ronn
 */
public final class GeoQuard {

	/** индекс квадрата по X */
	private final int x;
	/** индекс квадрата по Y */
	private final int y;

	/** высота квадрата */
	private float height;

	public GeoQuard(final int x, final int y, final float height) {
		this.x = x;
		this.y = y;
		this.height = height;
	}

	/**
	 * @return высота квадрата.
	 */
	public final float getHeight() {
		return height;
	}

	/**
	 * @return индекс квадрата по X.
	 */
	public final int getX() {
		return x;
	}

	/**
	 * @return индекс квадрата по Y.
	 */
	public final int getY() {
		return y;
	}

	/**
	 * @param height высота квадрата.
	 */
	public final void setHeight(final float height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "GeoQuard [x = " + x + ", y = " + y + ", height = " + height + "]";
	}
}
