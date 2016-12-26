package rlib.util;

import static java.lang.Float.parseFloat;
import static rlib.util.ClassUtils.unsafeCast;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import rlib.geom.Rotation;
import rlib.geom.Vector;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.ObjectDictionary;

/**
 * Реализация таблицы разнородных параметров.
 *
 * @author JavaSaBr
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

    /**
     * Таблица параметров.
     */
    private final ObjectDictionary<String, Object> values;

    public VarTable() {
        this.values = DictionaryFactory.newObjectDictionary();
    }

    /**
     * Очистка таблицы.
     */
    public void clear() {
        values.clear();
    }

    /**
     * Получение значение параметра по ключу.
     */
    public <T> T get(final String key) {
        return unsafeCast(values.get(key));
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key  ключ параметра.
     * @param type тип параметра.
     * @return значение параметра.
     */
    public <T> T get(final String key, final Class<T> type) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException();
        } else if (type.isInstance(object)) {
            return type.cast(object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key  ключ параметра.
     * @param type тип параметра.
     * @param def  значение по умолчанию.
     * @return значение параметра.
     */
    public <T, E extends T> T get(final String key, final Class<T> type, final E def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (type.isInstance(object)) {
            return type.cast(object);
        }

        return def;
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @param def значение по умолчанию.
     * @return значение параметра.
     */
    public <T> T get(final String key, final T def) {

        final Object object = values.get(key);
        if (object == null) return def;

        final Class<?> type = def.getClass();
        if (type.isInstance(object)) return unsafeCast(object);

        return def;
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key  ключ параметра.
     * @param type тип значений параметра.
     * @return массив значений параметра.
     */
    public <T> T[] getArray(final String key, final Class<T[]> type) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (type.isInstance(object)) {
            return unsafeCast(object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key  ключ параметра.
     * @param type тип значений параметра.
     * @param def  список значений по умолчанию.
     * @return массив значений параметра.
     */
    public <T> T[] getArray(final String key, final Class<T[]> type, final T... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (type.isInstance(object)) {
            return unsafeCast(object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @return значение параметра.
     */
    public boolean getBoolean(final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Boolean) {
            return (Boolean) object;
        } else if (object instanceof String) {
            return Boolean.parseBoolean(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @param def значение по умолчанию.
     * @return значение параметра.
     */
    public boolean getBoolean(final String key, final boolean def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Boolean) {
            return (Boolean) object;
        } else if (object instanceof String) {
            return Boolean.parseBoolean(object.toString());
        }

        return def;
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @return массив значений параметра.
     */
    public boolean[] getBooleanArray(final String key, final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof boolean[]) {
            return (boolean[]) object;
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final boolean[] result = new boolean[strs.length];

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Boolean.parseBoolean(strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @param def   набор значений параметра по умолчанию.
     * @return массив значений параметра.
     */
    public boolean[] getBooleanArray(final String key, final String regex, final boolean... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof boolean[]) {
            return (boolean[]) object;
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final boolean[] result = new boolean[strs.length];

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Boolean.parseBoolean(strs[i]);
            }

            return result;
        }

        return def;
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @return значение параметра.
     */
    public byte getByte(final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Byte) {
            return (Byte) object;
        } else if (object instanceof String) {
            return Byte.parseByte(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @param def значение по умолчанию.
     * @return значение параметра.
     */
    public byte getByte(final String key, final byte def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Byte) {
            return (Byte) object;
        } else if (object instanceof String) {
            return Byte.parseByte(object.toString());
        }

        return def;
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @return массив значений параметра.
     */
    public byte[] getByteArray(final String key, final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof byte[]) {
            return (byte[]) object;
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final byte[] result = new byte[strs.length];

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Byte.parseByte(strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @param def   набор значений параметра по умолчанию.
     * @return массив значений параметра.
     */
    public byte[] getByteArray(final String key, final String regex, final byte... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof byte[]) {
            return (byte[]) object;
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final byte[] result = new byte[strs.length];

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Byte.parseByte(strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @return значение параметра.
     */
    public double getDouble(final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Double) {
            return (Double) object;
        } else if (object instanceof String) {
            return Double.parseDouble(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @param def значение по умолчанию.
     * @return значение параметра.
     */
    public double getDouble(final String key, final double def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Double) {
            return (Double) object;
        } else if (object instanceof String) {
            return Double.parseDouble(object.toString());
        }

        return def;
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @return массив значений параметра.
     */
    public double[] getDoubleArray(final String key, final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof double[]) {
            return (double[]) object;
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final double[] result = new double[strs.length];

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Double.parseDouble(strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @param def   набор значений параметра по умолчанию.
     * @return массив значений параметра.
     */
    public double[] getDoubleArray(final String key, final String regex, final double... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof double[]) {
            return (double[]) object;
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final double[] result = new double[strs.length];

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Double.parseDouble(strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key  ключ параметра.
     * @param type тип параметра.
     * @return значение параметра.
     */
    public <T extends Enum<T>> T getEnum(final String key, final Class<T> type) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (type.isInstance(object)) {
            return type.cast(object);
        } else if (object instanceof String) {
            return Enum.valueOf(type, object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key  ключ параметра.
     * @param type тип параметра.
     * @param def  значение по умолчанию.
     * @return значение параметра.
     */
    public <T extends Enum<T>> T getEnum(final String key, final Class<T> type, final T def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (type.isInstance(object)) {
            return type.cast(object);
        } else if (object instanceof String) {
            return Enum.valueOf(type, object.toString());
        }

        return def;
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param type  тип значений параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @return массив значений параметра.
     */
    public <T extends Enum<T>> T[] getEnumArray(final String key, final Class<T> type, final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Enum[]) {
            return unsafeCast(object);
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final T[] result = unsafeCast(java.lang.reflect.Array.newInstance(type, strs.length));

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Enum.valueOf(type, strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param type  тип значений параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @param def   набор значений параметра по умолчанию.
     * @return массив значений параметра.
     */
    public <T extends Enum<T>> T[] getEnumArray(final String key, final Class<T> type, final String regex, final T... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Enum[]) {
            return unsafeCast(object);
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final T[] result = unsafeCast(java.lang.reflect.Array.newInstance(type, strs.length));

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Enum.valueOf(type, strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @return значение параметра.
     */
    public float getFloat(final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Float) {
            return (Float) object;
        } else if (object instanceof String) {
            return Float.parseFloat(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @param def значение по умолчанию.
     * @return значение параметра.
     */
    public float getFloat(final String key, final float def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Float) {
            return (Float) object;
        } else if (object instanceof String) {
            return Float.parseFloat(object.toString());
        }

        return def;
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @return массив значений параметра.
     */
    public float[] getFloatArray(final String key, final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException();
        } else if (object instanceof float[]) {
            return (float[]) object;
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final float[] result = new float[strs.length];

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Float.parseFloat(strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @param def   набор значений параметра по умолчанию.
     * @return массив значений параметра.
     */
    public float[] getFloatArray(final String key, final String regex, final float... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof float[]) {
            return (float[]) object;
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final float[] result = new float[strs.length];

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Float.parseFloat(strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @return значение параметра.
     */
    public int getInteger(final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof String) {
            return Integer.parseInt(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @param def значение по умолчанию.
     * @return значение параметра.
     */
    public int getInteger(final String key, final int def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof String) {
            return Integer.parseInt(object.toString());
        }

        return def;
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @return массив значений параметра.
     */
    public int[] getIntegerArray(final String key, final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof int[]) {
            return (int[]) object;
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final int[] result = new int[strs.length];

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Integer.parseInt(strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @param def   набор значений параметра по умолчанию.
     * @return массив значений параметра.
     */
    public int[] getIntegerArray(final String key, final String regex, final int... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof int[]) {
            return (int[]) object;
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final int[] result = new int[strs.length];

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Integer.parseInt(strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @return значение параметра.
     */
    public long getLong(final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Long) {
            return (Long) object;
        }

        if (object instanceof String) {
            return Long.parseLong(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @param def значение по умолчанию.
     * @return значение параметра.
     */
    public long getLong(final String key, final long def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Long) {
            return (Long) object;
        }

        if (object instanceof String) {
            return Long.parseLong(object.toString());
        }

        return def;
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @return массив значений параметра.
     */
    public long[] getLongArray(final String key, final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof long[]) {
            return (long[]) object;
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final long[] result = new long[strs.length];

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Long.parseLong(strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @param def   набор значений параметра по умолчанию.
     * @return массив значений параметра.
     */
    public long[] getLongArray(final String key, final String regex, final long... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof long[]) {
            return (long[]) object;
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final long[] result = new long[strs.length];

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Long.parseLong(strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @return значение параметра.
     */
    public Rotation getRotation(final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Rotation) {
            return (Rotation) object;
        }

        if (object instanceof String) {

            final String[] vals = ((String) object).split(",");

            final Rotation rotation = Rotation.newInstance();
            rotation.setXYZW(parseFloat(vals[0]), parseFloat(vals[1]), parseFloat(vals[2]), parseFloat(vals[3]));

            return rotation;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @param def значение по умолчанию.
     * @return значение параметра.
     */
    public Rotation getRotation(final String key, final Rotation def) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Rotation) {
            return (Rotation) object;
        }

        if (object instanceof String) {

            final String[] vals = ((String) object).split(",");

            final Rotation rotation = Rotation.newInstance();
            rotation.setXYZW(parseFloat(vals[0]), parseFloat(vals[1]), parseFloat(vals[2]), parseFloat(vals[3]));

            return rotation;
        }

        return def;
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @return значение параметра.
     */
    public short getShort(final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Short) {
            return (Short) object;
        } else if (object instanceof String) {
            return Short.parseShort(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @param def значение по умолчанию.
     * @return значение параметра.
     */
    public short getShort(final String key, final short def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Short) {
            return (Short) object;
        } else if (object instanceof String) {
            return Short.parseShort(object.toString());
        }

        return def;
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @return массив значений параметра.
     */
    public short[] getShortArray(final String key, final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof short[]) {
            return (short[]) object;
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final short[] result = new short[strs.length];

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Short.parseShort(strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @param def   набор значений параметра по умолчанию.
     * @return массив значений параметра.
     */
    public short[] getShortArray(final String key, final String regex, final short... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof short[]) {
            return (short[]) object;
        }

        if (object instanceof String) {

            final String[] strs = object.toString().split(regex);

            final short[] result = new short[strs.length];

            for (int i = 0, length = strs.length; i < length; i++) {
                result[i] = Short.parseShort(strs[i]);
            }

            return result;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @return значение параметра.
     */
    public String getString(final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof String) {
            return object.toString();
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @param def значение по умолчанию.
     * @return значение параметра.
     */
    public String getString(final String key, final String def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof String) {
            return object.toString();
        }

        return def;
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @return массив значений параметра.
     */
    public String[] getStringArray(final String key, final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof String[]) {
            return (String[]) object;
        } else if (object instanceof String) {
            return object.toString().split(regex);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение массива значений параметра по ключу.
     *
     * @param key   ключ параметра.
     * @param regex регулярное выражение для разбития строки значения параметра на массив нужных
     *              значений.
     * @param def   набор значений параметра по умолчанию.
     * @return массив значений параметра.
     */
    public String[] getStringArray(final String key, final String regex, final String... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof String[]) {
            return (String[]) object;
        } else if (object instanceof String) {
            return ((String) object).split(regex);
        }

        throw new IllegalArgumentException("no found " + key);
    }

    /**
     * @return все пропаршенные параметры.
     */
    public ObjectDictionary<String, Object> getValues() {
        return values;
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @return значение параметра.
     */
    public Vector getVector(final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Vector) {
            return (Vector) object;
        }

        if (object instanceof String) {

            final String[] vals = ((String) object).split(",");

            final Vector vector = Vector.newInstance();
            vector.setXYZ(parseFloat(vals[0]), parseFloat(vals[1]), parseFloat(vals[2]));

            return vector;
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Получение значение параметра по ключу.
     *
     * @param key ключ параметра.
     * @param def значение по умолчанию.
     * @return значение параметра.
     */
    public Vector getVector(final String key, final Vector def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Vector) {
            return (Vector) object;
        }

        if (object instanceof String) {

            final String[] vals = ((String) object).split(",");

            final Vector vector = Vector.newInstance();
            vector.setXYZ(parseFloat(vals[0]), parseFloat(vals[1]), parseFloat(vals[2]));

            return vector;
        }

        return def;
    }

    /**
     * Очистка таблицы и внесение в нее значений атрибутов элемента XML документа.
     *
     * @param node узел из XML документа с атрибутами.
     */
    public VarTable parse(final Node node) {
        values.clear();

        if (node == null) {
            return this;
        }

        final NamedNodeMap attributes = node.getAttributes();

        if (attributes == null) {
            return this;
        }

        for (int i = 0, length = attributes.getLength(); i < length; i++) {
            final Node item = attributes.item(i);
            set(item.getNodeName(), item.getNodeValue());
        }

        return this;
    }

    /**
     * Очистка таблицы и внесенее в нее параметров из дочерних элементов указанного узла с
     * указанными именами атрибутов и узлов. <p>
     * <pre>
     * 	< element >
     * 		< child name="name" valu="value" />
     * 		< child name="name" valu="value" />
     * 		< child name="name" valu="value" />
     * 		< child name="name" valu="value" />
     * 	< /element >
     *
     * vars.parse(node, "child", "name", "value")
     * </pre>
     *
     * @param node      узел, который надо отпарсить.
     * @param childName название элемента, у которого будет браться параметр.
     * @param nameType  название атрибута, задающее название параметра.
     * @param nameValue название атрибута, задающее значение параметра.
     */
    public VarTable parse(final Node node, final String childName, final String nameType, final String nameValue) {
        values.clear();

        if (node == null) {
            return this;
        }

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {

            if (child.getNodeType() != Node.ELEMENT_NODE || !childName.equals(child.getNodeName())) {
                continue;
            }

            final NamedNodeMap attributes = child.getAttributes();

            final Node nameNode = attributes.getNamedItem(nameType);
            final Node valueNode = attributes.getNamedItem(nameValue);

            if (nameNode == null || valueNode == null) {
                continue;
            }

            set(nameNode.getNodeValue(), valueNode.getNodeValue());
        }

        return this;
    }

    /**
     * Вставка значения параметра в таблицу.
     *
     * @param key   название параметра.
     * @param value значение параметра.
     */
    public void set(final String key, final Object value) {
        values.put(key, value);
    }

    /**
     * Копирование параметров из указанной таблицы в эту, текущие параметры не очищаются.
     *
     * @param vars копируемая таблица параметров.
     */
    public VarTable set(final VarTable vars) {
        values.put(vars.getValues());
        return this;
    }

    @Override
    public String toString() {
        return "VarTable: " + ("values = " + values);
    }
}
