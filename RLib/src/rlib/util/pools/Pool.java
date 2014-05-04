package rlib.util.pools;

/**
 * Интерфейс для реализации объектоного пула. Используется при подходе к
 * переиспользованию объектов для облегчения нагрузки на GC. Создается с помощью
 * {@link PoolFactory}
 * 
 * @see PoolFactory
 * @author Ronn
 */
public interface Pool<E> {

	/**
	 * @return пустой ли пул.
	 */
	public boolean isEmpty();

	/**
	 * Положить объект в пул.
	 * 
	 * @param object объект, который хотим положить.
	 */
	public void put(E object);

	/**
	 * Удаление из пула объекта.
	 * 
	 * @param object удаляемый объект.
	 */
	public void remove(E object);

	/**
	 * Взять из пула объект.
	 * 
	 * @return объект из пула.
	 */
	public E take();
}
