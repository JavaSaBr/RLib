package rlib.util.pools;

/**
 * Составной потокобезопасный пул.
 * 
 * @author Ronn
 */
public final class MultiConcurrentFoldablePool<E extends Foldable> implements FoldablePool<E>
{
	/** массив пулов */
	private final ConcurrentFoldablePool<E>[] pools;
	
	/** кол-во пулов */
	private final int limit;
	
	/** индекс следующего */
	private int order;
	
	@SuppressWarnings("unchecked")
	protected MultiConcurrentFoldablePool(int size, Class<?> type)
	{
		this.pools = new ConcurrentFoldablePool[size];
		this.limit = size;
		
		for(int i = 0; i < size; i++)
			pools[i] = new ConcurrentFoldablePool<E>(10, type);
	}
	
	/**
	 * @return номер пула.
	 */
	private final int getNextOrder()
	{
		int next = order + 1;
		
		if(next >= limit)
			next = 0;
		
		setOrder(next);
		
		return next;
	}

	/**
	 * @return номер пула.
	 */
	private final int getOrder()
	{
		int next = order;
		
		if(next >= limit)
			next = 0;
		
		setOrder(next);
		
		return next;
	}
	
	/**
	 * @return массив пулов.
	 */
	private final ConcurrentFoldablePool<E>[] getPools()
	{
		return pools;
	}

	@Override
	public boolean isEmpty()
	{
		// получаем список пулов
		ConcurrentFoldablePool<E>[] pools = getPools();
		
		// если какой-то пул не пуст, возвращаем фалс
		for(int i = 0, length = pools.length; i < length; i++)
			if(!pools[i].isEmpty())
				return false;
		
		return true;
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
		pools[getOrder()].put(object);
	}

	/**
	 * @param order номер пула.
	 */
	private final void setOrder(int order)
	{
		this.order = order;
	}

	@Override
	public E take()
	{
		// вытаскиваем последний объект
		E object = pools[getNextOrder()].take();
		
		// если объект нет, возвращаем пустышку
		if(object == null)
			return null;
		
		// реинициализируем объект
		object.reinit();
		
		// возвращаем его
		return object;
	}
}
