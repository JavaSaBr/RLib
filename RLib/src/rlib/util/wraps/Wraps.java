package rlib.util.wraps;

/**
 * Фабрика оберток.
 *
 * @author Ronn
 */
public final class Wraps
{
	/**
	 * Создание новой обертки вокруг byte.
	 *
	 * @param value значение по умолчанию.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newByteWrap(byte value, boolean usePool)
	{
		Wrap wrap = null;

		// если используем из пула
		if(usePool)
			// пробуем получить уже использованную обертку
			wrap = WrapType.BYTE.take();

		// если такой нет
		if(wrap == null)
			// создаем новую
			wrap = new ByteWrap();

		// вносим значение
		wrap.setByte(value);

		// возвращаем
		return wrap;
	}

	/**
	 * Создание новой обертки вокруг char.
	 *
	 * @param value значение по умолчанию.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newCharWrap(char value, boolean usePool)
	{
		Wrap wrap = null;

		// если используем из пула
		if(usePool)
			// пробуем получить уже использованную обертку
			wrap = WrapType.CHAR.take();

		// если такой нет
		if(wrap == null)
			// создаем новую
			wrap = new CharWrap();

		// вносим значение
		wrap.setChar(value);

		// возвращаем
		return wrap;
	}

	/**
	 * Создание новой обертки вокруг double.
	 *
	 * @param value значение по умолчанию.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newDoubleWrap(double value, boolean usePool)
	{
		Wrap wrap = null;

		// если используем из пула
		if(usePool)
			// пробуем получить уже использованную обертку
			wrap = WrapType.DOUBLE.take();

		// если такой нет
		if(wrap == null)
			// создаем новую
			wrap = new DoubleWrap();

		// вносим значение
		wrap.setDouble(value);

		// возвращаем
		return wrap;
	}
	
	/**
	 * Создание новой обертки вокруг float.
	 *
	 * @param value значение по умолчанию.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newFloatWrap(float value, boolean usePool)
	{
		Wrap wrap = null;

		// если используем из пула
		if(usePool)
			// пробуем получить уже использованную обертку
			wrap = WrapType.FLOAT.take();

		// если такой нет
		if(wrap == null)
			// создаем новую
			wrap = new FloatWrap();

		// вносим значение
		wrap.setFloat(value);

		// возвращаем
		return wrap;
	}
	
	/**
	 * Создание новой обертки вокруг int.
	 *
	 * @param value значение по умолчанию.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newIntegerWrap(int value, boolean usePool)
	{
		Wrap wrap = null;

		// если используем из пула
		if(usePool)
			// пробуем получить уже использованную обертку
			wrap = WrapType.INTEGER.take();
		
		// если такой нет
		if(wrap == null)
			// создаем новую
			wrap = new IntegerWrap();

		// вносим значение
		wrap.setInt(value);

		// возвращаем
		return wrap;
	}
	
	/**
	 * Создание новой обертки вокруг long.
	 *
	 * @param value значение по умолчанию.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newLongWrap(long value, boolean usePool)
	{
		Wrap wrap = null;

		// если используем из пула
		if(usePool)
			// пробуем получить уже использованную обертку
			wrap = WrapType.LONG.take();

		// если такой нет
		if(wrap == null)
			// создаем новую
			wrap = new LongWrap();

		// вносим значение
		wrap.setLong(value);

		// возвращаем
		return wrap;
	}
	
	/**
	 * Создание новой обертки вокруг Object.
	 *
	 * @param object ссылка на объект.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newObjectWrap(Object object, boolean usePool)
	{
		Wrap wrap = null;

		// если используем из пула
		if(usePool)
			// пробуем получить уже использованную обертку
			wrap = WrapType.OBJECT.take();

		// если такой нет
		if(wrap == null)
			// создаем новую
			wrap = new ObjectWrap();

		// вносим значение
		wrap.setObject(object);

		// возвращаем
		return wrap;
	}
	
	/**
	 * Создание новой обертки вокруг short.
	 *
	 * @param value значение по умолчанию.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newShortWrap(short value, boolean usePool)
	{
		Wrap wrap = null;

		// если используем из пула
		if(usePool)
			// пробуем получить уже использованную обертку
			wrap = WrapType.SHORT.take();

		// если такой нет
		if(wrap == null)
			// создаем новую
			wrap = new ShortWrap();

		// вносим значение
		wrap.setShort(value);

		// возвращаем
		return wrap;
	}
	
	private Wraps()
	{
		throw new IllegalArgumentException();
	}
}
