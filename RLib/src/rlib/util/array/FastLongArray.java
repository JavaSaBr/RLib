package rlib.util.array;

/**
 * Быстрый динамический массив примитивных long.
 *
 * @author Ronn
 */
public class FastLongArray implements LongArray
{
	/**
	 * Быстрый итератор массива.
	 *
	 * @author Ronn
	 */
	private final class FastIterator implements ArrayIterator<Long>
	{
		/** текущая позиция в массиве */
		private int ordinal;

		public FastIterator()
		{
			ordinal = 0;
		}

		@Override
		public void fastRemove()
		{
			FastLongArray.this.fastRemove(--ordinal);
		}

		@Override
		public boolean hasNext()
		{
			return ordinal < size;
		}

		@Override
		public int index()
		{
			return ordinal - 1;
		}

		@Override
		public Long next()
		{
			return array[ordinal++];
		}

		@Override
		public void remove()
		{
			FastLongArray.this.fastRemove(--ordinal);
		}

		@Override
		public void slowRemove()
		{
			FastLongArray.this.slowRemove(--ordinal);
		}
	}

	/** массив элементов */
	protected long[] array;

	/** кол-во элементов в колекции */
	protected int size;

	/**
	 * @param type тип элементов в массиве.
	 */
	public FastLongArray()
	{
		this(10);
	}

	public FastLongArray(int size)
	{
		this.array = new long[size];
		this.size = 0;
	}

	@Override
	public FastLongArray add(long element)
	{
		// если массив заполнен
		if(size == array.length)
			// расширяем его
			array = Arrays.copyOf(array, array.length * 3 / 2 + 1);

		// вносим новый элемент
		array[size++] = element;

		return this;
	}

	@Override
	public final FastLongArray addAll(long[] elements)
	{
		if(elements == null || elements.length < 1)
			return this;

		// получаем кол-во недостающих ячеяк в массиве
		int diff = size + elements.length - array.length;

		// если такие есть
		if(diff > 0)
			// расширяем массив
			array = Arrays.copyOf(array, diff);

		// добавляем новые элементы
		for(int i = 0, length = elements.length; i < length; i++)
			add(elements[i]);

		return this;
	}

	@Override
	public final FastLongArray addAll(LongArray elements)
	{
		// если добавляемого массива нет или он пуст, выходим
		if(elements == null || elements.isEmpty())
			return this;

		// получаем кол-во недостающих ячеяк в массиве
		int diff = size + elements.size() - array.length;

		// если такие есть
		if(diff > 0)
			// расширяем массив
			array = Arrays.copyOf(array, diff);

		// получаем добавляемый массив
		array = elements.array();

		// добавляем новые элементы
		for(int i = 0, length = elements.size(); i < length; i++)
			add(array[i]);

		return this;
	}

	@Override
	public final long[] array()
	{
		return array;
	}

	@Override
	public final FastLongArray clear()
	{
		// обнуляем счетчик
		size = 0;

		return this;
	}

	@Override
	public final boolean contains(long element)
	{
		// получаем массив
		long[] array = array();
		
		// ищем аналогичный объект
		for(int i = 0, length = size; i < length; i++)
			if(array[i] == element)
				return true;

		return false;
	}
	
	@Override
	public final boolean containsAll(long[] array)
	{
		// если какого-нибудь элемента нету, значит не содержит
		for(int i = 0, length = array.length; i < length; i++)
			if(!contains(array[i]))
				return false;

		return true;
	}

	@Override
	public final boolean containsAll(LongArray array)
	{
		// получаем массив элементов
		long[] elements = array.array();

		// если какого-нибудь элемента нету, значит не содержит
		for(int i = 0, length = array.size(); i < length; i++)
			if(!contains(elements[i]))
				return false;

		return true;
	}

	@Override
	public final boolean fastRemove(int index)
	{
		// если массив пуст или в нем нет такого элемента, выходим
		if(index < 0 || size < 1 || index >= size)
			return false;
		
		// получаем массив
		long[] array = array();
		
		// уменьшаем счетчик
		size -= 1;

		// на место удаляемого ставим последний
		array[index] = array[size];
		// зануляем последний
        array[size] = 0;

        // возвращаем удаленный объект
		return true;
	}

	@Override
	public boolean fastRemove(long element)
	{
		int index = indexOf(element);
		
		if(index > -1)
			fastRemove(index);
		
		return index > -1;
	}
	
	@Override
	public final long first()
	{
		// если массив пуст
		if(size < 1)
			// возвращаем пустой элемент
			return 0;

		// возвращаем первый элемент
		return array[0];
	}

	@Override
	public final long get(int index)
	{
		return array[index];
	}

	@Override
	public final int indexOf(long element)
	{
		// получаем массив
		long[] array = array();
		
		// перебираем все элементы в массиве
		for(int i = 0, length = size; i < length; i++)
		{
			// получаем элемент
			long val = array[i];

			// если они эквивалентны
			if(element == val)
				// возвращаем его индекс
				return i;
		}

		return -1;
	}

	@Override
	public final boolean isEmpty()
	{
		return size < 1;
	}

	@Override
	public final ArrayIterator<Long> iterator()
	{
		return new FastIterator();
	}

	@Override
	public final long last()
	{
		// если массив пуст
		if(size < 1)
			// возвращаем пустой элемент
			return 0;

		// возвращаем последний элемент
		return array[size - 1];
	}

	@Override
	public final int lastIndexOf(long element)
	{
		// получаем массив
		long[] array = array();
		
		// последний индекс
		int last = -1;

		// перебираем все элементы в массиве
		for(int i = 0, length = size; i < length; i++)
		{
			// получаем элемент
			long val = array[i];

			// если он эквивалентен
			if(element == val)
				// запоминаем его индекс
				last = i;
		}

		return last;
	}

	@Override
	public final long poll()
	{
		long val = first();
		
		slowRemove(0);
		
		return val;
	}

	@Override
	public final long pop()
	{
		long last = last();
		
		fastRemove(size - 1);
		
		return last;
	}

	@Override
	public void readLock(){}

	@Override
	public void readUnlock(){}
	
	@Override
	public final boolean removeAll(LongArray target)
	{
		// если целевой массив пуст, выходим
		if(target.isEmpty())
			return true;

		// получаем массив удаляемых объектов
		long[] array = target.array();

		// удаляем объекты
		for(int i = 0, length = target.size(); i < length; i++)
			fastRemove(array[i]);

		return true;
	}

	@Override
	public final boolean retainAll(LongArray target)
	{
		// получаем массив
		long[] array = array();
		
		// перебираем элементы в массиве
		for(int i = 0, length = size; i < length; i++)
			// если такой элемент есть в целевом массиве
			if(!target.contains(array[i]))
			{
				// удаляем его из массива
				fastRemove(i--);
				length--;
			}

		return true;
	}

	@Override
	public final int size()
	{
		return size;
	}

	@Override
	public final boolean slowRemove(int index)
	{
		// если индекс не подходит или массив пуст, выходим
		if(index < 0 || size < 1)
			return false;
		
		// получаем массив
		long[] array = array();
		
		// определяем размер сдвига
		int numMoved = size - index - 1;

		// если нужно сдвигать массив
		if(numMoved > 0)
			// сдвигаем
			System.arraycopy(array, index + 1, array, index, numMoved);

		// уменьшаем счетчик
		size -= 1;

		// зануляем последний элемент
		array[size] = 0;

		return true;
	}
	
	@Override
	public boolean slowRemove(long element)
	{
		int index = indexOf(element);
		
		if(index > -1)
			slowRemove(index);
		
		return index > -1;
	}

	@Override
	public final FastLongArray sort()
	{
		Arrays.sort(array, 0, size);

		return this;
	}

	@Override
	public final long[] toArray(long[] container)
	{		
		// получаем массив
		long[] array = array();
		
		// если в указанный массив можно все вместить
		if(container.length >= size)
		{
			// перебираем элементы массива и переносим в новый
			for(int i = 0, j = 0, length = array.length, newLength = container.length; i < length && j < newLength; i++)
				// переносим
				container[j++] = array[i];

			return container;
		}

		return array;
	}

	@Override
	public String toString()
	{
		return Arrays.toString(this);
	}

	@Override
	public final FastLongArray trimToSize()
	{		
		// получаем массив
		long[] array = array();
		
		if(size == array.length)
			return this;

		// формируем новый массив под текущий размер
		array = Arrays.copyOfRange(array, 0, size);

		return this;
	}

	@Override
	public void writeLock(){}
	
	@Override
	public void writeUnlock(){}
}