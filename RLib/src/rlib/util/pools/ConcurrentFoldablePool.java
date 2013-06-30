package rlib.util.pools;

import rlib.util.array.Array;
import rlib.util.array.Arrays;

/**
 * Потокобезопасный объектный пул.
 * 
 * @author Ronn
 */
public class ConcurrentFoldablePool<E extends Foldable> implements FoldablePool<E>
{
	/** массив объектов */
	private Array<E> pool;
	
	/**
	 * @param size базовый размер пула.
	 */
	protected ConcurrentFoldablePool(int size, Class<?> type)
	{
		this.pool = Arrays.toConcurrentArray(type, size);
	}

	@Override
	public boolean isEmpty()
	{
		return pool.isEmpty();
	}

	@Override
	public void put(E object)
	{
		// если объекты 
		if(object == null)
			return;
		
		// запускаем метод очистки
		object.finalyze();
		
		// добавляем в пул
		pool.add(object);
	}

	@Override
	public E take()
	{
		// получаем объект их пула
		E object = pool.pop();
		
		// если такого нет, возвращаем пустышку
		if(object == null)
			return null;
		
		// реинициализируем
		object.reinit();
		
		// возвращаем
		return object;
	}
}
