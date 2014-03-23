package rlib.util;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import rlib.geom.Vector;
import rlib.util.table.Table;
import rlib.util.table.TableFactory;

/**
 * Таблица параметров.
 * 
 * @author Ronn
 * @created 29.02.2012
 */
public class VarTable {

	/**
	 * @return новый экземпляр таблицы.
	 */
	public static VarTable newInstance() {
		return new VarTable();
	}

	/**
	 * @param node хмл узел с атрибутами.
	 * @return новая таблица с атрибутами узла.
	 */
	public static VarTable newInstance(final Node node) {
		return newInstance().parse(node);
	}

	public static VarTable newInstance(final Node node, final String childName, final String nameType, final String nameValue) {
		return newInstance().parse(node, childName, nameType, nameValue);
	}

	/** таблица параметров */
	private final Table<String, Object> values;

	public VarTable() {
		this.values = TableFactory.newObjectTable();
	}

	/**
	 * Очистка таблицы.
	 */
	public void clear() {
		values.clear();
	}

	/**
	 * @param key название параметра.
	 * @return значение параметра.
	 */
	public Object get(final String key) {
		return values.get(key);
	}

	/**
	 * Получить значение параметра по его названию.
	 * 
	 * @param key название параметра.
	 * @param type тип параметра.
	 * @param def значение по умолчанию.
	 * @return текущее значение.
	 */
	public <T extends Object, E extends T> T get(final String key, final Class<T> type, final E def) {

		final Object object = values.get(key);

		if(object == null) {
			return def;
		}

		if(type.isInstance(object)) {
			return type.cast(object);
		}

		return def;
	}

	/**
	 * Получить значение ключа типа указанного по умолчанию значения.
	 * 
	 * @param key название параметра.
	 * @param def значение по умолчанию.
	 * @return найденное значение.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final String key, final T def) {

		final Object object = values.get(key);

		if(object == null) {
			return def;
		}

		final Class<? extends Object> type = def.getClass();

		if(type.isInstance(object)) {
			return (T) object;
		}

		return def;
	}

	/**
	 * Получить значение по ключу.
	 * 
	 * @param key название параметра.
	 * @return найденное начение.
	 */
	public boolean getBoolean(final String key) {

		final Object object = values.get(key);

		if(object == null) {
			throw new IllegalArgumentException("not found " + key);
		}

		if(object instanceof Boolean) {
			return (boolean) object;
		}

		if(object instanceof String) {
			return Boolean.parseBoolean(object.toString());
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * Получить значение по ключу.
	 * 
	 * @param key название параметра.
	 * @param def значение по умолчанию.
	 * @return найденное значение.
	 */
	public boolean getBoolean(final String key, final boolean def) {

		final Object object = values.get(key);

		if(object == null) {
			return def;
		}

		if(object instanceof Boolean) {
			return (boolean) object;
		}

		if(object instanceof String) {
			return Boolean.parseBoolean(object.toString());
		}

		return def;
	}

	/**
	 * Получить массив значений по ключу.
	 * 
	 * @param key название параметра.
	 * @param regex строка разбиения, если значение строка.
	 * @return найденный массив.
	 */
	public boolean[] getBooleanArray(final String key, final String regex) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof boolean[])
			return (boolean[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final boolean[] result = new boolean[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Boolean.parseBoolean(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * Получение массив значений по ключу.
	 * 
	 * @param key название параметра.
	 * @param regex строка разбиения, если значение строка.
	 * @param def значение по умолчанию.
	 * @return найденное значение
	 */
	public boolean[] getBooleanArray(final String key, final String regex, final boolean... def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof boolean[])
			return (boolean[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final boolean[] result = new boolean[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Boolean.parseBoolean(strs[i]);

			return result;
		}

		return def;
	}

	/**
	 * @return найденное значение.
	 */
	public byte getByte(final String key) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof Byte)
			return (byte) object;

		if(object instanceof String)
			return Byte.parseByte(object.toString());

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public byte getByte(final String key, final byte def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof Byte)
			return (byte) object;

		if(object instanceof String)
			return Byte.parseByte(object.toString());

		return def;
	}

	/**
	 * @return найденное значение.
	 */
	public byte[] getByteArray(final String key, final String regex) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof byte[])
			return (byte[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final byte[] result = new byte[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Byte.parseByte(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public byte[] getByteArray(final String key, final String regex, final byte... def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof byte[])
			return (byte[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final byte[] result = new byte[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Byte.parseByte(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public double getDouble(final String key) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof Double)
			return (double) object;

		if(object instanceof String)
			return Double.parseDouble(object.toString());

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public double getDouble(final String key, final double def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof Double)
			return (double) object;

		if(object instanceof String)
			return Double.parseDouble(object.toString());

		return def;
	}

	/**
	 * @return найденное значение.
	 */
	public double[] getDoubleArray(final String key, final String regex) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof double[])
			return (double[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final double[] result = new double[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Double.parseDouble(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public double[] getDoubleArray(final String key, final String regex, final double... def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof double[])
			return (double[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final double[] result = new double[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Double.parseDouble(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public <T extends Enum<T>> T getEnum(final String key, final Class<T> type) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(type.isInstance(object))
			return type.cast(object);

		if(object instanceof String)
			return Enum.valueOf(type, object.toString());

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public <T extends Enum<T>> T getEnum(final String key, final Class<T> type, final T def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(type.isInstance(object))
			return type.cast(object);

		if(object instanceof String)
			return Enum.valueOf(type, object.toString());

		return def;
	}

	/**
	 * @return найденное значение.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> T[] getEnumArray(final String key, final Class<T> type, final String regex) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof Enum[])
			return (T[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final T[] result = (T[]) java.lang.reflect.Array.newInstance(type, strs.length);

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Enum.valueOf(type, strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> T[] getEnumArray(final String key, final Class<T> type, final String regex, final T... def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof Enum[])
			return (T[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final T[] result = (T[]) java.lang.reflect.Array.newInstance(type, strs.length);

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Enum.valueOf(type, strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public float getFloat(final String key) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof Float)
			return (float) object;

		if(object instanceof String)
			return Float.parseFloat(object.toString());

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public float getFloat(final String key, final float def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof Float)
			return (float) object;

		if(object instanceof String)
			return Float.parseFloat(object.toString());

		return def;
	}

	/**
	 * @return найденное значение.
	 */
	public float[] getFloatArray(final String key, final String regex) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException();

		if(object instanceof float[])
			return (float[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final float[] result = new float[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Float.parseFloat(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public float[] getFloatArray(final String key, final String regex, final float... def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof float[])
			return (float[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final float[] result = new float[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Float.parseFloat(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public <T extends Object> T getGeneric(final String key, final Class<T> type) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException();

		if(type.isInstance(object))
			return type.cast(object);

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] getGenericArray(final String key, final Class<T[]> type) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(type.isInstance(object))
			return (T[]) object;

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] getGenericArray(final String key, final Class<T[]> type, final T... def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(type.isInstance(object))
			return (T[]) object;

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public int getInteger(final String key) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof Integer)
			return (int) object;

		if(object instanceof String)
			return Integer.parseInt(object.toString());

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public int getInteger(final String key, final int def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof Integer)
			return (int) object;

		if(object instanceof String)
			return Integer.parseInt(object.toString());

		return def;
	}

	/**
	 * @return найденное значение.
	 */
	public int[] getIntegerArray(final String key, final String regex) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof int[])
			return (int[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final int[] result = new int[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Integer.parseInt(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public int[] getIntegerArray(final String key, final String regex, final int... def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof int[])
			return (int[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final int[] result = new int[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Integer.parseInt(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public long getLong(final String key) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof Long)
			return (long) object;

		if(object instanceof String)
			return Long.parseLong(object.toString());

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public long getLong(final String key, final long def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof Long)
			return (long) object;

		if(object instanceof String)
			return Long.parseLong(object.toString());

		return def;
	}

	/**
	 * @return найденное значение.
	 */
	public long[] getLongArray(final String key, final String regex) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof long[])
			return (long[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final long[] result = new long[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Long.parseLong(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public long[] getLongArray(final String key, final String regex, final long... def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof long[])
			return (long[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final long[] result = new long[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Long.parseLong(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public short getShort(final String key) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof Short)
			return (short) object;

		if(object instanceof String)
			return Short.parseShort(object.toString());

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public short getShort(final String key, final short def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof Short)
			return (short) object;

		if(object instanceof String)
			return Short.parseShort(object.toString());

		return def;
	}

	/**
	 * @return найденное значение.
	 */
	public short[] getShortArray(final String key, final String regex) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof short[])
			return (short[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final short[] result = new short[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Short.parseShort(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public short[] getShortArray(final String key, final String regex, final short... def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof short[])
			return (short[]) object;

		if(object instanceof String) {

			final String[] strs = object.toString().split(regex);

			final short[] result = new short[strs.length];

			for(int i = 0, length = strs.length; i < length; i++)
				result[i] = Short.parseShort(strs[i]);

			return result;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public String getString(final String key) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof String)
			return (String) object;

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public String getString(final String key, final String def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof String)
			return (String) object;

		return def;
	}

	/**
	 * @return найденное значение.
	 */
	public String[] getStringArray(final String key, final String regex) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof String[])
			return (String[]) object;

		if(object instanceof String)
			return object.toString().split(regex);

		throw new IllegalArgumentException("not found " + key);
	}

	/**
	 * @return найденное значение.
	 */
	public String[] getStringArray(final String key, final String regex, final String... def) {
		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof String[])
			return (String[]) object;

		if(object instanceof String)
			return (String[]) object;

		throw new IllegalArgumentException("no found " + key);
	}

	/**
	 * @return все пропаршенные параметры.
	 */
	public Table<String, Object> getValues() {
		return values;
	}

	/**
	 * @return вектор по ключу.
	 */
	public Vector getVector(final String key) {

		final Object object = values.get(key);

		if(object == null)
			throw new IllegalArgumentException("not found " + key);

		if(object instanceof Vector)
			return (Vector) object;

		if(object instanceof String) {

			final String[] vals = object.toString().split(",");

			final Vector vector = Vector.newInstance();

			vector.setXYZ(Float.parseFloat(vals[0]), Float.parseFloat(vals[1]), Float.parseFloat(vals[2]));

			return vector;
		}

		throw new IllegalArgumentException("not found " + key);
	}

	public Vector getVector(final String key, final Vector def) {

		final Object object = values.get(key);

		if(object == null)
			return def;

		if(object instanceof Vector)
			return (Vector) object;

		if(object instanceof String) {

			final String[] vals = object.toString().split(",");

			final Vector vector = Vector.newInstance();

			vector.setXYZ(Float.parseFloat(vals[0]), Float.parseFloat(vals[1]), Float.parseFloat(vals[2]));

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
	public VarTable parse(final Node node) {

		values.clear();

		if(node == null)
			return this;

		final NamedNodeMap attrs = node.getAttributes();

		if(attrs == null)
			return this;

		for(int i = 0, length = attrs.getLength(); i < length; i++) {

			final Node item = attrs.item(i);

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
	public VarTable parse(final Node node, final String childName, final String nameType, final String nameValue) {

		values.clear();

		if(node == null)
			return this;

		for(Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {

			if(!childName.equals(child.getNodeName()))
				continue;

			final NamedNodeMap attrs = child.getAttributes();

			final Node name = attrs.getNamedItem(nameType);
			final Node val = attrs.getNamedItem(nameValue);

			if(name == null || val == null)
				continue;

			set(name.getNodeValue(), val.getNodeValue());
		}

		return this;
	}

	/**
	 * @return найденное значение.
	 */
	public void set(final String key, final Object value) {
		values.put(key, value);
	}

	/**
	 * @return найденное значение.
	 */
	public VarTable set(final VarTable set) {
		values.put(set.getValues());
		return this;
	}

	@Override
	public String toString() {
		return "VarTable: " + (values != null ? "values = " + values : "");
	}
}
