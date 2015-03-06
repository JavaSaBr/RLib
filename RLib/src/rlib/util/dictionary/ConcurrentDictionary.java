package rlib.util.dictionary;

/**
 * Интерфейс для реализации механизмов работы в многопоточной среде со словарем.
 * 
 * @author Ronn
 */
public interface ConcurrentDictionary<K, V> extends Dictionary<K, V> {

	/**
	 * Блокировка изменение таблицы на время чтения его.
	 */
	public void readLock();

	/**
	 * Разблокировка изменения таблицы.
	 */
	public void readUnlock();

	/**
	 * Блокировка чтений для изменения таблицы.
	 */
	public void writeLock();

	/**
	 * Разблокировка чтения таблицы.
	 */
	public void writeUnlock();
}
