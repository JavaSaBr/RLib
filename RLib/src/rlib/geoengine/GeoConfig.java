package rlib.geoengine;

/**
 * Интерфейс конфига для гео движка.
 *
 * @author Ronn
 */
public interface GeoConfig {

	/**
	 * @return отступ по X.
	 */
	public int getOffsetX();

	/**
	 * @return отступ по Y.
	 */
	public int getOffsetY();

	/**
	 * @return размер в высоту гео квадрата.
	 */
	public int getQuardHeight();

	/**
	 * @return размер гео квадрата.
	 */
	public int getQuardSize();

	/**
	 * @return разделитель квадратов в файле.
	 */
	public int getSplit();
}
