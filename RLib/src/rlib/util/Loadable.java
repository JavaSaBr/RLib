package rlib.util;

/**
 * Интерфейс для реализации загружаемых объектов.
 * 
 * @author Ronn
 */
public interface Loadable {

	/**
	 * Загруть данные.
	 */
	public void load();

	/**
	 * Перезагрузить данные.
	 */
	public void reload();

	/**
	 * Выгрузить даные.
	 */
	public void unload();
}
