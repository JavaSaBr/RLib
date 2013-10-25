package rlib.util.pools;

/**
 * Интерфейс для реализации объектоного пула.
 * 
 * @author Ronn
 */
public interface FoldablePool<E extends Foldable> {

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
	 * Взять из пула объект.
	 * 
	 * @return объект из пула.
	 */
	public E take();

	/**
	 * Удаление из пула объекта.
	 * 
	 * @param object удаляемый объект.
	 */
	public void remove(E object);
}
