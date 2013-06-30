package rlib.util.pools;

import rlib.util.array.Array;
import rlib.util.array.Arrays;

/**
 * Не потокобезопасный пул.
 * 
 * @author Ronn
 */
public class FastFoldablePool<E extends Foldable> implements FoldablePool<E>
{
	/** пул объектов */
	private Array<E> pool;
	
	/**
	 * @param size базовый размер пула.
	 */
	protected FastFoldablePool(int size, Class<?> type)
	{
		this.pool = Arrays.toArray(type, size);
	}

	@Override
	public boolean isEmpty()
	{
		return pool.isEmpty();
	}

	@Override
	public void put(E object)
	{
		// если объекта нет, выходим
		if(object == null)
			return;
		
		// очищаем объект
		object.finalyze();
		
		// добавляем в пул
		pool.add(object);
	}

	@Override
	public E take()
	{
		// вытаскиваем последний объект
		E object = pool.pop();
		
		// если объект нет, возвращаем пустышку
		if(object == null)
			return null;
		
		// реинициализируем объект
		object.reinit();
		
		// возвращаем его
		return object;
	}

	@Override
	public String toString()
	{
		return pool.toString();
	}
}
