package rlib.geoengine;

import java.io.File;

import rlib.util.array.Array;


/**
 * Интерфейс для реализации карты квадратов геодаты.
 *
 * @author Ronn
 */
public interface GeoMap
{
	/**
	 * Добавление нового квадрата в движек.
	 *
	 * @param quard квадрат геодаты.
	 */
	public void addQuard(GeoQuard quard);
	
	/**
	 * Добавление нового квадрата в движек.
	 * 
	 * @param x индекс по X.
	 * @param y ирдекс по Y.
	 * @param height высота.
	 */
	public void addQuard(int x, int y, float height);

	/**
	 * Экспорт гео-карты в указанный файл.
	 *
	 * @param file файл для экспорта.
	 */
	public void exportTo(File file);

	/**
	 * @return список всех квадратов.
	 */
	public Array<GeoQuard> getAllQuards(Array<GeoQuard> container);

	/**
	 * Получение квадрата гео по указанным координатам.
	 *
	 * @param x координата по X.
	 * @param y координата по Y.
	 * @param z координата по Z.
	 * @return гео квадрат в этой точке.
	 */
	public GeoQuard getGeoQuard(float x, float y, float z);

	/**
	 * Получение высоты по указанным координатам.
	 *
	 * @param x координата по X.
	 * @param y координата по Y.
	 * @param z координата по Z.
	 * @return высота в этой точке.
	 */
	public float getHeight(float x, float y, float z);

	/**
	 * Импорт гео-карты из указанного файла.
	 *
	 * @param file файл для импорта.
	 */
	public GeoMap importTo(File file);

	/**
	 * @return кол-во квадратов в карте.
	 */
	public int size();
}
