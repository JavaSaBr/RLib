package rlib.util.array;

import java.util.Comparator;


/**
 * Быстрый динамический массив.
 * Использовать только в неконкурентных местах.
 *
 * @author Ronn
 */
public class FastArray<E> extends AbstractArray<E>
{
	/**
	 * Быстрый итератор массива.
	 *
	 * @author Ronn
	 */
	private final class FastIterator implements ArrayIterator<E>
	{
		/** текущая позиция в массиве */
		private int ordinal;

		public FastIterator()
		{
			super();

			ordinal = 0;
		}

		@Override
		public void fastRemove()
		{
			FastArray.this.fastRemove(--ordinal);
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
		public E next()
		{
			return array[ordinal++];
		}

		@Override
		public void remove()
		{
			FastArray.this.fastRemove(--ordinal);
		}

		@Override
		public void slowRemove()
		{
			FastArray.this.slowRemove(--ordinal);
		}
	}

	private static final long serialVersionUID = -8477384427415127978L;

	/** массив элементов */
	protected E[] array;

	/** кол-во элементов в колекции */
	protected int size;

	/**
	 * @param type тип элементов в массиве.
	 */
	public FastArray(Class<E> type)
	{
		super(type);
	}

	/**
	 * @param type тип элементов в массиве.
	 * @param size размер массива.
	 */
	public FastArray(Class<E> type, int size)
	{
		super(type, size);
	}

	@Override
	public FastArray<E> add(E element)
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
	public final FastArray<E> addAll(Array<? extends E> elements)
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
		E[] array = elements.array();

		// добавляем новые элементы
		for(int i = 0, length = elements.size(); i < length; i++)
			add(array[i]);

		return this;
	}

	@Override
	public final Array<E> addAll(E[] elements)
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
	public final E[] array()
	{
		return array;
	}

	@Override
	public final FastArray<E> clear()
	{
		// очищаем массив
		Arrays.clear(array);

		// обнуляем счетчик
		size = 0;

		return this;
	}

	@Override
	public final boolean contains(Object object)
	{
		// получаем массив
		E[] array = array();

		// ищем аналогичный объект
		for(int i = 0, length = size; i < length; i++)
			if(array[i].equals(object))
				return true;

		return false;
	}

	@Override
	public final E fastRemove(int index)
	{
		// получаем массив
		E[] array = array();

		// если массив пуст или в нем нет такого элемента, выходим
		if(index < 0 || size < 1 || index >= size)
			return null;

		// уменьшаем счетчик
		size -= 1;

		// получаем удаляемый элемент
		E old = array[index];

		// на место удаляемого ставим последний
		array[index] = array[size];
		// зануляем последний
        array[size] = null;

        // возвращаем удаленный объект
		return old;
	}

	@Override
	public final E first()
	{
		// если массив пуст
		if(size < 1)
			// возвращаем пустой элемент
			return null;

		// возвращаем первый элемент
		return array[0];
	}

	@Override
	public final E get(int index)
	{
		return array[index];
	}

	@Override
	public final int indexOf(Object object)
	{
		if(object == null)
			return -1;

		// получаем массив
		E[] array = array();

		// перебираем все элементы в массиве
		for(int i = 0, length = size; i < length; i++)
		{
			// получаем элемент
			E element = array[i];

			// если они эквивалентны
			if(element.equals(object))
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
	public final ArrayIterator<E> iterator()
	{
		return new FastIterator();
	}

	@Override
	public final E last()
	{
		// если массив пуст
		if(size < 1)
			// возвращаем пустой элемент
			return null;

		// возвращаем последний элемент
		return array[size - 1];
	}

	@Override
	public final int lastIndexOf(Object object)
	{
		if(object == null)
			return -1;

		// получаем массив
		E[] array = array();

		// последний индекс
		int last = -1;

		// перебираем все элементы в массиве
		for(int i = 0, length = size; i < length; i++)
		{
			// получаем элемент
			E element = array[i];

			// если он эквивалентен
			if(element.equals(object))
				// запоминаем его индекс
				last = i;
		}

		return last;
	}

	@Override
	public final E poll()
	{
		return slowRemove(0);
	}

	@Override
	public final E pop()
	{
		return fastRemove(size - 1);
	}

	@Override
	public final boolean removeAll(Array<?> target)
	{
		// если целевой массив пуст, выходим
		if(target.isEmpty())
			return true;

		// получаем массив удаляемых объектов
		Object[] array = target.array();

		// удаляем объекты
		for(int i = 0, length = target.size(); i < length; i++)
			fastRemove(array[i]);

		return true;
	}

	@Override
	public final boolean retainAll(Array<?> target)
	{
		// получаем массив
		E[] array = array();

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
	public final E search(E required, Search<E> search)
	{
		// получаем массив
		E[] array = array();

		// перебираем все элементы массива
		for(int i = 0, length = size; i < length; i++)
		{
			// получаем элемент
			E element = array[i];

			// если он подходит
			if(search.compare(required, element))
				// возвращаем его
				return element;
		}

		return null;
	}

	@Override
	public final void set(int index, E element)
	{
		// если индекс не подходит или элемента нету, выходим
		if(index < 0 || index >= size || element == null)
			return;

		// получаем массив
		E[] array = array();

		// если на нужном месте есть элемент, уменьшаем счетчик
		if(array[index] != null)
			size -= 1;

		// вносим новый элемент
		array[index] = element;

		// увеличиваем счетчик
		size += 1;
	}

	@Override
	protected final void setArray(E[] array)
	{
		this.array = array;
	}

	@Override
	protected final void setSize(int size)
	{
		this.size = size;
	}

	@Override
	public final int size()
	{
		return size;
	}

	@Override
	public final E slowRemove(int index)
	{
		// если индекс не подходит или массив пуст, выходим
		if(index < 0 || size < 1)
			return null;

		// получаем массив
		E[] array = array();

		// определяем размер сдвига
		int numMoved = size - index - 1;

		// получаем удаляемый объект
		E old = array[index];

		// если нужно сдвигать массив
		if(numMoved > 0)
			// сдвигаем
			System.arraycopy(array, index + 1, array, index, numMoved);

		// уменьшаем счетчик
		size -= 1;

		// зануляем последний элемент
		array[size] = null;

		return old;
	}

	@Override
	public final FastArray<E> sort(Comparator<E> comparator)
	{
		Arrays.sort(array, comparator);

		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <T> T[] toArray(T[] container)
	{
		// получаем массив
		E[] array = array();

		// если в указанный массив можно все вместить
		if(container.length >= size)
		{
			// перебираем элементы массива и переносим в новый
			for(int i = 0, j = 0, length = array.length, newLength = container.length; i < length && j < newLength; i++)
			{
				// если ячейка пуста, пропускаем
				if(array[i] == null)
					continue;

				// переносим
				container[j++] = (T) array[i];
			}

			return container;
		}

		return (T[]) array;
	}

	@Override
	public final FastArray<E> trimToSize()
	{
		if(size == array.length)
			return this;

		// формируем новый массив под текущий размер
		array = Arrays.copyOfRange(array, 0, size);

		return this;
	}

	@Override
	public void apply(FuncElement<? super E> func)
	{
		E[] array = array();

		for(int i = 0, length = size; i < length; i++)
			func.apply(array[i]);
	}
}