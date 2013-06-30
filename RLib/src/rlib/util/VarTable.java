package rlib.util;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import rlib.geom.Vector;
import rlib.util.table.Table;
import rlib.util.table.Tables;


/**
 * Таблица параметров.
 *
 * @author Ronn
 * @created 29.02.2012
 */
public class VarTable
{
	/**
	 * @return новый экземпляр таблицы.
	 */
	public static VarTable newInstance()
	{
		return new VarTable();
	}

	/**
	 * @param node хмл узел с атрибутами.
	 * @return новая таблица с атрибутами узла.
	 */
	public static VarTable newInstance(Node node)
	{
		return newInstance().parse(node);
	}

	public static VarTable newInstance(Node node, String childName, String nameType, String nameValue)
	{
		return newInstance().parse(node, childName, nameType, nameValue);
	}

	/** хранилище значений */
	private Table<String, Object> values;

	public VarTable()
	{
		this.values = Tables.newObjectTable();
	}

	/**
	 * Очистка таблицы.
	 */
	public void clear()
	{
		values.clear();
	}

	/**
	 *
	 * @return значение по ключу.
	 */
	public Object get(String key)
	{
		return values.get(key);
	}

	/**
	 * Получить значению ключа указанного типа.
	 *
	 * @param key ключ значения.
	 * @param type тип значения.
	 * @param def значение по умолчанию.
	 * @return найденное значение.
	 */
	public <T extends Object, E extends T> T get(String key, Class<T> type, E def)
	{
		// извлекаем объект по ключу
		Object object = values.get(key);

		// если его нет, возвращаем дефолтное
		if(object == null)
			return def;

		// если объект нужного типа
		if(type.isInstance(object))
			// возвращаем его в нужном типе.
			return type.cast(object);

		// возвращаем значение по умолчанию.
		return def;
	}
	/**
	 * Получить значение ключа типа указанного по умолчанию значения.
	 *
	 * @param key ключ значения.
	 * @param def значение по умолчанию.
	 * @return найденное значение.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key, T def)
	{
		// получаем объект по ключу
		Object object = values.get(key);

		// еси его нет, возвращаем значение по умолчанию
		if(object == null)
			return def;

		// получаем тип значения по умолчанию
		Class<? extends Object> type = def.getClass();

		// если найденный объект подходит с типом
		if(type.isInstance(object))
			// возвращаем его в нужном типе
			return (T) object;

		// возвращаем значение по умолчанию
		return def;
	}

	/**
	 * Получить значение по ключу.
	 *
	 * @param key ключ значения.
	 * @return найденноез начение.
	 */
	public boolean getBoolean(String key)
	{
		// получаем значение по ключу
		Object object = values.get(key);

		// если его нету, выбрасываем исключение
		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		// если он бул, возвращаем бул
		if(object instanceof Boolean)
			return (boolean) object;

		// если это строка
		if(object instanceof String)
			// парсим в бул и возвращаем
			return Boolean.parseBoolean(object.toString());

		// выбрасываем исключение
		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * Получить значение по ключу.
	 *
	 * @param key ключ значения.
	 * @param def значение по умолчанию.
	 * @return найденное значение.
	 */
	public boolean getBoolean(String key, boolean def)
	{
		// получаем значение по ключу.
		Object object = values.get(key);

		// если его нету
		if(object == null)
			// возвращаем значение по умолчанию.
			return def;

		// если это бул, возвращаем его
		if(object instanceof Boolean)
			return (boolean) object;

		// если это строка
		if(object instanceof String)
			// парсим в бул и возвращаем
			return Boolean.parseBoolean(object.toString());

		// возвращаем значение по умолчанию
		return def;
	}

	/**
	 * Получить массив значений по ключу.
	 *
	 * @param key ключ значения.
	 * @param regex строка разбиения, если значение строка.
	 * @return найденный массив.
	 */
	public boolean[] getBooleanArray(String key, String regex)
	{
		// получаем значение по ключу.
		Object object = values.get(key);

		// если нет значения, кидаем исключение
		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		// если значение уже массив булов, то возвращаем его
		if(object instanceof boolean[])
			return (boolean[]) object;

		// если значение строка
		if(object instanceof String)
		{
			// разбиваем строку
			String[] strs = object.toString().split(regex);
			// создаем новый массив булов
			boolean[] result = new boolean[strs.length];

			// парсим строки в булы
			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Boolean.parseBoolean(strs[i]);

			// возвращаем итоговый массив
			return result;
		}

		// если ничего не подошло, выбрасываем исключение
		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * Получение массив значений по ключу.
	 *
	 * @param key ключ значения.
	 * @param regex строка разбиения, если значение строка.
	 * @param def значение по умолчанию.
	 * @return найденное значение
	 */
	public boolean[] getBooleanArray(String key, String regex, boolean... def)
	{
		// получаем значение по ключу
		Object object = values.get(key);

		// если нет значения, возвращаем по умолчанию
		if(object == null)
			return def;

		// если значение уже массив булов, возвращаем его
		if(object instanceof boolean[])
			return (boolean[]) object;

		// если значение строка
		if(object instanceof String)
		{
			// разбиваем строку
			String[] strs = object.toString().split(regex);
			// создаем массив булов
			boolean[] result = new boolean[strs.length];

			// парсим строки в булы
			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Boolean.parseBoolean(strs[i]);

			// возвращаем результат
			return result;
		}

		// возвращаем значение по умолчанию
		return def;
	}

	/**
	 * @return значение по ключу.
	 */
	public byte getByte(String key)
	{
		Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof Byte)
			return (byte) object;

		if(object instanceof String)
			return Byte.parseByte(object.toString());

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public byte getByte(String key, byte def)
	{
		Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof Byte)
			return (byte) object;

		if(object instanceof String)
			return Byte.parseByte(object.toString());

		return def;
	}

	/**
	 * @return значение по ключу.
	 */
	public byte[] getByteArray(String key, String regex)
	{
		Object object = values.get(key);

		// если нет значения, кидаем исключение
		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof byte[])
			return (byte[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
		{
			String[] strs = object.toString().split(regex);

			byte[] result = new byte[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Byte.parseByte(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public byte[] getByteArray(String key, String regex, byte... def)
	{
		Object object = values.get(key);

		// если нет значения, кидаем дефолтное
		if(object == null)
			return def;

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof byte[])
			return (byte[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
		{
			String[] strs = object.toString().split(regex);

			byte[] result = new byte[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Byte.parseByte(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public double getDouble(String key)
	{
		Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof Double)
			return (double) object;

		if(object instanceof String)
			return Double.parseDouble(object.toString());

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public double getDouble(String key, double def)
	{
		Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof Double)
			return (double) object;

		if(object instanceof String)
			return Double.parseDouble(object.toString());

		return def;
	}

	/**
	 * @return значение по ключу.
	 */
	public double[] getDoubleArray(String key, String regex)
	{
		Object object = values.get(key);

		// если нет значения, кидаем исключение
		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof double[])
			return (double[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
		{
			String[] strs = object.toString().split(regex);

			double[] result = new double[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Double.parseDouble(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public double[] getDoubleArray(String key, String regex, double... def)
	{
		Object object = values.get(key);

		// если нет значения, кидаем дефолтное
		if(object == null)
			return def;

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof double[])
			return (double[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
		{
			String[] strs = object.toString().split(regex);

			double[] result = new double[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Double.parseDouble(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public <T extends Enum<T>> T getEnum(String key, Class<T> type)
	{
		Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(type.isInstance(object))
			return type.cast(object);

		if(object instanceof String)
			return Enum.valueOf(type, object.toString());

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public <T extends Enum<T>> T getEnum(String key, Class<T> type, T def)
	{
		Object object = values.get(key);

		if(object == null)
			return def;

		if(type.isInstance(object))
			return type.cast(object);

		if(object instanceof String)
			return Enum.valueOf(type, object.toString());

		return def;
	}

	/**
	 * @return значение по ключу.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> T[] getEnumArray(String key, Class<T> type, String regex)
	{
		Object object = values.get(key);

		// если нет значения, кидаем исключение
		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof Enum[])
			return (T[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
		{
			String[] strs = object.toString().split(regex);

			T[] result = (T[]) java.lang.reflect.Array.newInstance(type, strs.length);

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Enum.valueOf(type, strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> T[] getEnumArray(String key, Class<T> type, String regex, T... def)
	{
		Object object = values.get(key);

		// если нет значения, кидаем исключение
		if(object == null)
			return def;

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof Enum[])
			return (T[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
		{
			String[] strs = object.toString().split(regex);

			T[] result = (T[]) java.lang.reflect.Array.newInstance(type, strs.length);

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Enum.valueOf(type, strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public float getFloat(String key)
	{
		Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof Float)
			return (float) object;

		if(object instanceof String)
			return Float.parseFloat(object.toString());

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public float getFloat(String key, float def)
	{
		Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof Float)
			return (float) object;

		if(object instanceof String)
			return Float.parseFloat(object.toString());

		return def;
	}

	/**
	 * @return значение по ключу.
	 */
	public float[] getFloatArray(String key, String regex)
	{
		Object object = values.get(key);

		// если нет значения, кидаем исключение
		if(object == null)
			throw new IllegalArgumentException();

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof float[])
			return (float[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
		{
			String[] strs = object.toString().split(regex);

			float[] result = new float[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Float.parseFloat(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public float[] getFloatArray(String key, String regex, float... def)
	{
		Object object = values.get(key);

		// если нет значения, кидаем дефолтное
		if(object == null)
			return def;

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof float[])
			return (float[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
		{
			String[] strs = object.toString().split(regex);

			float[] result = new float[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Float.parseFloat(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public <T extends Object> T getGeneric(String key, Class<T> type)
	{
		Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException();

		if(type.isInstance(object))
			return type.cast(object);

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] getGenericArray(String key, Class<T[]> type)
	{
		Object object = values.get(key);

		// если нет значения, кидаем исключение
		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		// если уже лежит массив байтов, то просто кастим
		if(type.isInstance(object))
			return (T[]) object;

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] getGenericArray(String key, Class<T[]> type, T... def)
	{
		Object object = values.get(key);

		// если нет значения, кидаем исключение
		if(object == null)
			return def;

		// если уже лежит массив байтов, то просто кастим
		if(type.isInstance(object))
			return (T[]) object;

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public int getInteger(String key)
	{
		Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof Integer)
			return (int) object;

		if(object instanceof String)
			return Integer.parseInt(object.toString());

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public int getInteger(String key, int def)
	{
		Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof Integer)
			return (int) object;

		if(object instanceof String)
			return Integer.parseInt(object.toString());

		return def;
	}

	/**
	 * @return значение по ключу.
	 */
	public int[] getIntegerArray(String key, String regex)
	{
		Object object = values.get(key);

		// если нет значения, кидаем исключение
		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof int[])
			return (int[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
		{
			String[] strs = object.toString().split(regex);

			int[] result = new int[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Integer.parseInt(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public int[] getIntegerArray(String key, String regex, int... def)
	{
		Object object = values.get(key);

		// если нет значения, кидаем дефолтное
		if(object == null)
			return def;

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof int[])
			return (int[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
		{
			String[] strs = object.toString().split(regex);

			int[] result = new int[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Integer.parseInt(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public long getLong(String key)
	{
		Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof Long)
			return (long) object;

		if(object instanceof String)
			return Long.parseLong(object.toString());

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public long getLong(String key, long def)
	{
		Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof Long)
			return (long) object;

		if(object instanceof String)
			return Long.parseLong(object.toString());

		return def;
	}

	/**
	 * @return значение по ключу.
	 */
	public long[] getLongArray(String key, String regex)
	{
		Object object = values.get(key);

		// если нет значения, кидаем исключение
		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof long[])
			return (long[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
		{
			String[] strs = object.toString().split(regex);

			long[] result = new long[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Long.parseLong(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public long[] getLongArray(String key, String regex, long... def)
	{
		Object object = values.get(key);

		// если нет значения, кидаем дефолтное
		if(object == null)
			return def;

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof long[])
			return (long[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
		{
			String[] strs = object.toString().split(regex);

			long[] result = new long[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Long.parseLong(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public short getShort(String key)
	{
		Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof Short)
			return (short) object;

		if(object instanceof String)
			return Short.parseShort(object.toString());

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public short getShort(String key, short def)
	{
		Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof Short)
			return (short) object;

		if(object instanceof String)
			return Short.parseShort(object.toString());

		return def;
	}

	/**
	 * @return значение по ключу.
	 */
	public short[] getShortArray(String key, String regex)
	{
		Object object = values.get(key);

		// если нет значения, кидаем исключение
		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof short[])
			return (short[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
		{
			String[] strs = object.toString().split(regex);

			short[] result = new short[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Short.parseShort(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public short[] getShortArray(String key, String regex, short... def)
	{
		Object object = values.get(key);

		// если нет значения, кидаем дефолтное
		if(object == null)
			return def;

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof short[])
			return (short[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
		{
			String[] strs = object.toString().split(regex);

			short[] result = new short[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Short.parseShort(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public String getString(String key)
	{
		Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof String)
			return (String) object;

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public String getString(String key, String def)
	{
		Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof String)
			return (String) object;

		return def;
	}

	/**
	 * @return значение по ключу.
	 */
	public String[] getStringArray(String key, String regex)
	{
		Object object = values.get(key);

		// если нет значения, кидаем исключение
		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof String[])
			return (String[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
			return object.toString().split(regex);

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return значение по ключу.
	 */
	public String[] getStringArray(String key, String regex, String... def)
	{
		Object object = values.get(key);

		// если нет значения, кидаем дефолтное
		if(object == null)
			return def;

		// если уже лежит массив байтов, то просто кастим
		if(object instanceof String[])
			return (String[]) object;

		// если строка, сплитим и парсим
		if(object instanceof String)
			return object.toString().split(regex);

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return все пропаршенные параметры.
	 */
	public Table<String, Object> getValues()
	{
		return values;
	}

	/**
	 * @return вектор по ключу.
	 */
	public Vector getVector(String key)
	{
		// подучаем значение по ключу
		Object object = values.get(key);

		// если нет значения, кидаем исключение
		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		// если это уже вектор, возвращаем его
		if(object instanceof Vector)
			return (Vector) object;

		// если это строка
		if(object instanceof String)
		{
			// разбиваем ее
			String[] vals = object.toString().split(",");

			// создаем вектор
			Vector vector = Vector.newInstance();

			// парсим вектор
			vector.setXYZ(Float.parseFloat(vals[0]), Float.parseFloat(vals[1]), Float.parseFloat(vals[2]));

			// возвращаем вектор
			return vector;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	public Vector getVector(String key, Vector def)
	{
		// подучаем значение по ключу
		Object object = values.get(key);

		// если значения нет, возвращаем дефолт
		if(object == null)
			return def;

		// если это уже вектор, возвращаем его
		if(object instanceof Vector)
			return (Vector) object;

		// если это строка
		if(object instanceof String)
		{
			// разбиваем ее
			String[] vals = object.toString().split(",");

			// создаем вектор
			Vector vector = Vector.newInstance();

			// парсим вектор
			vector.setXYZ(Float.parseFloat(vals[0]), Float.parseFloat(vals[1]), Float.parseFloat(vals[2]));

			// возвращаем вектор
			return vector;
		}

		return def;
	}

	/**
	 * Парс атрибутов хмл узла.
	 *
	 * @param node хмл узел.
	 * @return таблица параметров с атрибутами хмл узла.
	 */
	public VarTable parse(Node node)
	{
		// очищаем таблицу
		values.clear();

		if(node == null)
			return this;

		// получаем атрибуты хмл узла
		NamedNodeMap attrs = node.getAttributes();

		// если их нет, выходим
		if(attrs == null)
			return this;

		// перебираем атрибуты
		for(int i = 0, length = attrs.getLength(); i < length; i++)
		{
			// извлекаем атриубт
			Node item = attrs.item(i);
			// вносим его значение
			set(item.getNodeName(), item.getNodeValue());
		}

		return this;
	}

	/**
	 * Парс хмл узла.
	 *
	 * @param node узел, который парсим.
	 * @param childName название узла, который задает параметр.
	 * @param nameType название атрибута, задающее название параметра.
	 * @param nameValue название атрибута, задающее значение параметра.
	 * @return
	 */
	public VarTable parse(Node node, String childName, String nameType, String nameValue)
	{
		// очищаем таблицу
		values.clear();

		if(node == null)
			return this;

		for(Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
		{
			if(!childName.equals(child.getNodeName()))
				continue;

			NamedNodeMap attrs = child.getAttributes();

			Node name = attrs.getNamedItem(nameType);
			Node val = attrs.getNamedItem(nameValue);

			if(name == null || val == null)
				continue;

			set(name.getNodeValue(), val.getNodeValue());
		}

		return this;
	}

	/**
	 * @return значение по ключу.
	 */
	public void set(String key, Object value)
	{
		values.put(key, value);
	}

	/**
	 * @return значение по ключу.
	 */
	public VarTable set(VarTable set)
	{
		// вносим данные
		values.put(set.getValues());

		return this;
	}

	@Override
	public String toString()
	{
		return "VarTable: " + (values != null ? "values = " + values : "");
	}
}
