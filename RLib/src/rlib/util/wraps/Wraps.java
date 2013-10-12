package rlib.util.wraps;

/**
 * Фабрика оберток.
 * 
 * @author Ronn
 */
public final class Wraps {

	/**
	 * Создание новой обертки вокруг byte.
	 * 
	 * @param value значение по умолчанию.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newByteWrap(byte value, boolean usePool) {

		Wrap wrap = null;

		if(usePool) {
			wrap = WrapType.BYTE.take();
		}

		if(wrap == null) {
			wrap = new ByteWrap();
		}

		wrap.setByte(value);
		return wrap;
	}

	/**
	 * Создание новой обертки вокруг char.
	 * 
	 * @param value значение по умолчанию.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newCharWrap(char value, boolean usePool) {

		Wrap wrap = null;

		if(usePool) {
			wrap = WrapType.CHAR.take();
		}

		if(wrap == null) {
			wrap = new CharWrap();
		}

		wrap.setChar(value);
		return wrap;
	}

	/**
	 * Создание новой обертки вокруг double.
	 * 
	 * @param value значение по умолчанию.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newDoubleWrap(double value, boolean usePool) {

		Wrap wrap = null;

		if(usePool) {
			wrap = WrapType.DOUBLE.take();
		}

		if(wrap == null) {
			wrap = new DoubleWrap();
		}

		wrap.setDouble(value);
		return wrap;
	}

	/**
	 * Создание новой обертки вокруг float.
	 * 
	 * @param value значение по умолчанию.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newFloatWrap(float value, boolean usePool) {

		Wrap wrap = null;

		if(usePool) {
			wrap = WrapType.FLOAT.take();
		}

		if(wrap == null) {
			wrap = new FloatWrap();
		}

		wrap.setFloat(value);
		return wrap;
	}

	/**
	 * Создание новой обертки вокруг int.
	 * 
	 * @param value значение по умолчанию.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newIntegerWrap(int value, boolean usePool) {

		Wrap wrap = null;

		if(usePool) {
			wrap = WrapType.INTEGER.take();
		}

		if(wrap == null) {
			wrap = new IntegerWrap();
		}

		wrap.setInt(value);
		return wrap;
	}

	/**
	 * Создание новой обертки вокруг long.
	 * 
	 * @param value значение по умолчанию.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newLongWrap(long value, boolean usePool) {

		Wrap wrap = null;

		if(usePool) {
			wrap = WrapType.LONG.take();
		}

		if(wrap == null) {
			wrap = new LongWrap();
		}

		wrap.setLong(value);
		return wrap;
	}

	/**
	 * Создание новой обертки вокруг Object.
	 * 
	 * @param object ссылка на объект.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newObjectWrap(Object object, boolean usePool) {

		Wrap wrap = null;

		if(usePool) {
			wrap = WrapType.OBJECT.take();
		}

		if(wrap == null) {
			wrap = new ObjectWrap();
		}

		wrap.setObject(object);
		return wrap;
	}

	/**
	 * Создание новой обертки вокруг short.
	 * 
	 * @param value значение по умолчанию.
	 * @param usePool пробовать достать из пула.
	 * @return новая обертка.
	 */
	public static Wrap newShortWrap(short value, boolean usePool) {

		Wrap wrap = null;

		if(usePool) {
			wrap = WrapType.SHORT.take();
		}

		if(wrap == null) {
			wrap = new ShortWrap();
		}

		wrap.setShort(value);
		return wrap;
	}

	private Wraps() {
		throw new IllegalArgumentException();
	}
}
