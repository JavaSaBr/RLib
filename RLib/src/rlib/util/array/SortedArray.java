package rlib.util.array;

/**
 * Динамический массив для сортируемых объектов.
 * При вставке объекта, он устанавливается по сортеровочному индексу.
 * Если вставляемый объект null, он вставляется последним.
 * 
 * @author Ronn
 * @created 28.02.2012
 */
public class SortedArray<E extends Comparable<E>> extends FastArray<E>
{
	private static final long serialVersionUID = 1L;

	/**
	 * @param type тип элементов в массиве.
	 */
	public SortedArray(Class<E> type)
	{
		super(type);
	}
	
	/**
	 * @param type тип элементов в массиве.
	 * @param size размер массива.
	 */
	public SortedArray(Class<E> type, int size)
	{
		super(type, size);
	}

	@Override
	public SortedArray<E> add(E element)
	{
		// если массив заполнился
		if(size == array.length)
			// расширяем на треть
			array = Arrays.copyOf(array, array.length * 3 / 2 + 1);
		
		// получаем локально массив элементов
		E[] array = array();
		
		// перебираем элементы
		for(int i = 0, length = array.length; i < length; i++)
		{
			// получаем элемент
			E old = array[i];
			
			// если элемента нет
			if(old == null)
			{
				// вносим добавляемый сюда
				array[i] = element;
				// увеличиваем размер
				size++;
				// выходим
				return this;
			}
			
			// если добавляемый меньше элемента в этом месте
			if(element.compareTo(old) < 0)
			{
				// увеличиваем размер
				size++;
				
				// рассчитываем спещение
				int numMoved = size - i - 1;
				
				// сдвигаем элементы в массивве
				System.arraycopy(array, i, array, i + 1, numMoved);
				
				// добавляем элемент
				array[i] = element;
				
				return this;
			}
		}
		
		return this;
	}
}