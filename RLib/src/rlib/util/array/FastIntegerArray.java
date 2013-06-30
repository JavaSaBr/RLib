package rlib.util.array;

/**
 * Быстрый динамический массив примитивных int.
 *
 * @author Ronn
 */
public class FastIntegerArray implements IntegerArray
{
	/**
	 * Быстрый итератор массива.
	 *
	 * @author Ronn
	 */
	private final class FastIterator implements ArrayIterator<Integer>
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
			FastIntegerArray.this.fastRemove(--ordinal);
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
		public Integer next()
		{
			return array[ordinal++];
		}

		@Override
		public void remove()
		{
			FastIntegerArray.this.fastRemove(--ordinal);
		}

		@Override
		public void slowRemove()
		{
			FastIntegerArray.this.slowRemove(--ordinal);
		}
	}

	/** массив элементов */
	protected int[] array;

	/** кол-во элементов в колекции */
	protected int size;

	/**
	 * @param type тип элементов в массиве.
	 */
	public FastIntegerArray()
	{
		this(10);
	}

	public FastIntegerArray(int size)
	{
		this.array = new int[size];
		this.size = 0;
	}

	@Override
	public FastIntegerArray add(int element)
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
	public final FastIntegerArray addAll(int[] elements)
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
	public final FastIntegerArray addAll(IntegerArray elements)
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
	public final int[] array()
	{
		return array;
	}

	@Override
	public final FastIntegerArray clear()
	{
		// обнуляем счетчик
		size = 0;

		return this;
	}

	@Override
	public final boolean contains(int element)
	{
		// получаем массив
		int[] array = array();
		
		// ищем аналогичный объект
		for(int i = 0, length = size; i < length; i++)
			if(array[i] == element)
				return true;

		return false;
	}
	
	@Override
	public final boolean containsAll(int[] array)
	{
		// если какого-нибудь элемента нету, значит не содержит
		for(int i = 0, length = array.length; i < length; i++)
			if(!contains(array[i]))
				return false;

		return true;
	}

	@Override
	public final boolean containsAll(IntegerArray array)
	{
		// получаем массив элементов
		int[] elements = array.array();

		// если какого-нибудь элемента нету, значит не содержит
		for(int i = 0, length = array.size(); i < length; i++)
			if(!contains(elements[i]))
				return false;

		return true;
	}

	@Override
	public boolean fastRemove(int element)
	{
		int index = indexOf(element);
		
		if(index > -1)
			fastRemoveByIndex(index);
		
		return index > -1;
	}

	@Override
	public final boolean fastRemoveByIndex(int index)
	{
		// если массив пуст или в нем нет такого элемента, выходим
		if(index < 0 || size < 1 || index >= size)
			return false;
		
		// получаем массив
		int[] array = array();
		
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
	public final int first()
	{
		// если массив пуст
		if(size < 1)
			// возвращаем пустой элемент
			return 0;

		// возвращаем первый элемент
		return array[0];
	}

	@Override
	public final int get(int index)
	{
		return array[index];
	}

	@Override
	public final int indexOf(int element)
	{
		// получаем массив
		int[] array = array();
		
		// перебираем все элементы в массиве
		for(int i = 0, length = size; i < length; i++)
		{
			// получаем элемент
			int val = array[i];

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
	public final ArrayIterator<Integer> iterator()
	{
		return new FastIterator();
	}

	@Override
	public final int last()
	{
		// если массив пуст
		if(size < 1)
			// возвращаем пустой элемент
			return 0;

		// возвращаем последний элемент
		return array[size - 1];
	}

	@Override
	public final int lastIndexOf(int element)
	{
		// получаем массив
		int[] array = array();
		
		// последний индекс
		int last = -1;

		// перебираем все элементы в массиве
		for(int i = 0, length = size; i < length; i++)
		{
			// получаем элемент
			int val = array[i];

			// если он эквивалентен
			if(element == val)
				// запоминаем его индекс
				last = i;
		}

		return last;
	}

	@Override
	public final int poll()
	{
		int val = first();
		
		slowRemoveByIndex(0);
		
		return val;
	}

	@Override
	public final int pop()
	{
		int last = last();
		
		fastRemoveByIndex(size - 1);
		
		return last;
	}

	@Override
	public void readLock(){}

	@Override
	public void readUnlock(){}
	
	@Override
	public final boolean removeAll(IntegerArray target)
	{
		// если целевой массив пуст, выходим
		if(target.isEmpty())
			return true;

		// получаем массив удаляемых объектов
		int[] array = target.array();

		// удаляем объекты
		for(int i = 0, length = target.size(); i < length; i++)
			fastRemove(array[i]);

		return true;
	}

	@Override
	public final boolean retainAll(IntegerArray target)
	{
		// получаем массив
		int[] array = array();
		
		// перебираем элементы в массиве
		for(int i = 0, length = size; i < length; i++)
			// если такой элемент есть в целевом массиве
			if(!target.contains(array[i]))
			{
				// удаляем его из массива
				fastRemoveByIndex(i--);
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
	public boolean slowRemove(int element)
	{
		int index = indexOf(element);
		
		if(index > -1)
			slowRemoveByIndex(index);
		
		return index > -1;
	}
	
	@Override
	public final boolean slowRemoveByIndex(int index)
	{
		// если индекс не подходит или массив пуст, выходим
		if(index < 0 || size < 1)
			return false;
		
		// получаем массив
		int[] array = array();
		
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
	public final FastIntegerArray sort()
	{
		Arrays.sort(array, 0, size);

		return this;
	}

	@Override
	public final int[] toArray(int[] container)
	{		
		// получаем массив
		int[] array = array();
		
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
	public final FastIntegerArray trimToSize()
	{		
		// получаем массив
		int[] array = array();
		
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