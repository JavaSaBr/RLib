package rlib.util;

import static java.lang.Float.parseFloat;
import static java.util.Objects.requireNonNull;
import static rlib.util.ClassUtils.unsafeCast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import rlib.geom.Quaternion4f;
import rlib.geom.Vector3f;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.ObjectDictionary;

/**
 * THe utility class to contain different properties.
 *
 * @author JavaSaBr
 */
public class VarTable {

    /**
     * @return the new instance.
     */
    @NotNull
    public static VarTable newInstance() {
        return new VarTable();
    }

    /**
     * @param node the xml node.
     * @return the new table with attributes of the node.
     */
    @NotNull
    public static VarTable newInstance(@Nullable final Node node) {
        return newInstance().parse(node);
    }

    @NotNull
    public static VarTable newInstance(@Nullable final Node node, @NotNull final String childName,
                                       @NotNull final String nameType, @NotNull final String nameValue) {
        return newInstance().parse(node, childName, nameType, nameValue);
    }

    /**
     * The table with values.
     */
    @NotNull
    private final ObjectDictionary<String, Object> values;

    private VarTable() {
        this.values = DictionaryFactory.newObjectDictionary();
    }

    /**
     * Clear this table.
     */
    public void clear() {
        values.clear();
    }

    /**
     * Get the value by a key.
     *
     * @param key the key.
     */
    @NotNull
    public <T> T get(@NotNull final String key) {
        final T result = unsafeCast(values.get(key));
        if (result != null) return result;
        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get the value by a key.
     *
     * @param key  the key.
     * @param type the type.
     * @return the value.
     */
    @NotNull
    public <T> T get(@NotNull final String key, @NotNull final Class<T> type) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException();
        } else if (type.isInstance(object)) {
            return type.cast(object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get the value by a key.
     *
     * @param key  the key.
     * @param type the type.
     * @param def  the default value.
     * @return the value.
     */
    @NotNull
    public <T, E extends T> T get(@NotNull final String key, @NotNull final Class<T> type, @NotNull final E def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (type.isInstance(object)) {
            return type.cast(object);
        }

        return def;
    }

    /**
     * Get the value by a key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the value.
     */
    @NotNull
    public <T> T get(@NotNull final String key, @NotNull final T def) {

        final Object object = values.get(key);
        if (object == null) return def;

        final Class<?> type = def.getClass();

        if (type.isInstance(object)) {
            return requireNonNull(unsafeCast(object));
        }

        return def;
    }

    /**
     * Get an array by a key.
     *
     * @param key  the key.
     * @param type the type.
     * @return the array.
     */
    @NotNull
    public <T> T[] getArray(@NotNull final String key, @NotNull final Class<T[]> type) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (type.isInstance(object)) {
            return requireNonNull(unsafeCast(object));
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get an array of a key.
     *
     * @param key  the key.
     * @param type the type.
     * @param def  the default array.
     * @return the array.
     */
    @NotNull
    @SafeVarargs
    public final <T> T[] getArray(@NotNull final String key, @NotNull final Class<T[]> type, @NotNull final T... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (type.isInstance(object)) {
            return requireNonNull(unsafeCast(object));
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a boolean value by a key.
     *
     * @param key the key.
     * @return the value.
     */
    public boolean getBoolean(@NotNull final String key) {

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
     * Get a boolean value by a key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the value.
     */
    public boolean getBoolean(@NotNull final String key, final boolean def) {

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
     * Get a boolean array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the boolean array.
     */
    @NotNull
    public boolean[] getBooleanArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof boolean[]) {
            return (boolean[]) object;
        } else if (object instanceof String) {
            return parseBooleanArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private boolean[] parseBooleanArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final boolean[] result = new boolean[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Boolean.parseBoolean(strings[i]);
        }

        return result;
    }

    /**
     * Get a boolean array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the boolean array.
     */
    @NotNull
    public boolean[] getBooleanArray(@NotNull final String key, @NotNull final String regex,
                                     @NotNull final boolean... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof boolean[]) {
            return (boolean[]) object;
        } else if (object instanceof String) {
            return parseBooleanArray(regex, object);
        }

        return def;
    }

    /**
     * Get a byte value by a key.
     *
     * @param key the key.
     * @return the byte.
     */
    public byte getByte(@NotNull final String key) {

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
     * Get a byte value by a key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the byte.
     */
    public byte getByte(@NotNull final String key, final byte def) {

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
     * Get a byte array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the byte array.
     */
    @NotNull
    public byte[] getByteArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof byte[]) {
            return (byte[]) object;
        } else if (object instanceof String) {
            return parseByteArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private byte[] parseByteArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final byte[] result = new byte[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Byte.parseByte(strings[i]);
        }

        return result;
    }

    /**
     * Get a byte array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default byte array.
     * @return the byte array.
     */
    public byte[] getByteArray(@NotNull final String key, @NotNull final String regex, @NotNull final byte... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof byte[]) {
            return (byte[]) object;
        } else if (object instanceof String) {
            return parseByteArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a double value by a key.
     *
     * @param key the key.
     * @return the value.
     */
    public double getDouble(@NotNull final String key) {

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
     * Get a double value by a key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the value.
     */
    public double getDouble(@NotNull final String key, final double def) {

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
     * Get a double array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the double array.
     */
    @NotNull
    public double[] getDoubleArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof double[]) {
            return (double[]) object;
        } else if (object instanceof String) {
            return parseDoubleArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private double[] parseDoubleArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final double[] result = new double[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Double.parseDouble(strings[i]);
        }

        return result;
    }

    /**
     * Get a double array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the double array.
     */
    @NotNull
    public double[] getDoubleArray(@NotNull final String key, @NotNull final String regex,
                                   @NotNull final double... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof double[]) {
            return (double[]) object;
        } else if (object instanceof String) {
            return parseDoubleArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get an enum value by a key.
     *
     * @param key  the key.
     * @param type the type of enum.
     * @return the value.
     */
    @NotNull
    public <T extends Enum<T>> T getEnum(@NotNull final String key, @NotNull final Class<T> type) {

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
     * Get an enum value by a key.
     *
     * @param key  the key.
     * @param type the type of enum.
     * @param def  the default value.
     * @return the value.
     */
    @NotNull
    public <T extends Enum<T>> T getEnum(@NotNull final String key, @NotNull final Class<T> type,
                                         @NotNull final T def) {

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
     * Get an enum array by a key.
     *
     * @param key   the key.
     * @param type  the type of enum.
     * @param regex the regex to split if a value is string.
     * @return the enum array.
     */
    @NotNull
    public <T extends Enum<T>> T[] getEnumArray(@NotNull final String key, @NotNull final Class<T> type,
                                                @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Enum[]) {
            return requireNonNull(unsafeCast(object));
        } else if (object instanceof String) {
            return parseEnumArray(type, regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private <T extends Enum<T>> T[] parseEnumArray(@NotNull final Class<T> type, @NotNull final String regex,
                                                   @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final T[] result = requireNonNull(unsafeCast(ArrayUtils.create(type, strings.length)));

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Enum.valueOf(type, strings[i]);
        }

        return result;
    }

    /**
     * Get an enum array by a key.
     *
     * @param key   the key.
     * @param type  the type of enum.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the enum array.
     */
    @NotNull
    public <T extends Enum<T>> T[] getEnumArray(@NotNull final String key, @NotNull final Class<T> type,
                                                @NotNull final String regex, @NotNull final T... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Enum[]) {
            return requireNonNull(unsafeCast(object));
        } else if (object instanceof String) {
            return parseEnumArray(type, regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a float value by a key.
     *
     * @param key the key.
     * @return the float value.
     */
    public float getFloat(@NotNull final String key) {

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
     * Get a float value by a key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the float value.
     */
    public float getFloat(@NotNull final String key, final float def) {

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
     * Get a float array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the float array.
     */
    @NotNull
    public float[] getFloatArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException();
        } else if (object instanceof float[]) {
            return (float[]) object;
        } else if (object instanceof String) {
            return parseFloatArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private float[] parseFloatArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final float[] result = new float[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Float.parseFloat(strings[i]);
        }

        return result;
    }

    /**
     * Get a float array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the float array.
     */
    @NotNull
    public float[] getFloatArray(@NotNull final String key, @NotNull final String regex, @NotNull final float... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof float[]) {
            return (float[]) object;
        } else if (object instanceof String) {
            return parseFloatArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get an integer value by a key.
     *
     * @param key the key.
     * @return the integer value.
     */
    public int getInteger(@NotNull final String key) {

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
     * Get an integer value by a key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the integer value.
     */
    public int getInteger(@NotNull final String key, final int def) {

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
     * Get an integer value by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the integer value.
     */
    @NotNull
    public int[] getIntegerArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof int[]) {
            return (int[]) object;
        } else if (object instanceof String) {
            return parseIntegerArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private int[] parseIntegerArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final int[] result = new int[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Integer.parseInt(strings[i]);
        }

        return result;
    }

    /**
     * Get an integer value by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default value.
     * @return the integer value.
     */
    @NotNull
    public int[] getIntegerArray(@NotNull final String key, @NotNull final String regex, @NotNull final int... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof int[]) {
            return (int[]) object;
        } else if (object instanceof String) {
            return parseIntegerArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a long value by a key.
     *
     * @param key the key.
     * @return the long value.
     */
    public long getLong(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Long) {
            return (Long) object;
        } else if (object instanceof String) {
            return Long.parseLong(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a long value by a key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the long value.
     */
    public long getLong(@NotNull final String key, final long def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Long) {
            return (Long) object;
        } else if (object instanceof String) {
            return Long.parseLong(object.toString());
        }

        return def;
    }

    /**
     * Get a long array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the long array.
     */
    @NotNull
    public long[] getLongArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof long[]) {
            return (long[]) object;
        } else if (object instanceof String) {
            return parseLongArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private long[] parseLongArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final long[] result = new long[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Long.parseLong(strings[i]);
        }

        return result;
    }

    /**
     * Get a long array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the long array.
     */
    @NotNull
    public long[] getLongArray(@NotNull final String key, @NotNull final String regex, @NotNull final long... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof long[]) {
            return (long[]) object;
        } else if (object instanceof String) {
            return parseLongArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a rotation by a key.
     *
     * @param key the key.
     * @return the rotation.
     */
    @NotNull
    public Quaternion4f getRotation(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Quaternion4f) {
            return (Quaternion4f) object;
        } else if (object instanceof String) {
            return parseRotation((String) object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    @NotNull
    private Quaternion4f parseRotation(@NotNull final String object) {

        final String[] values = object.split(",");

        final Quaternion4f rotation = Quaternion4f.newInstance();
        rotation.setXYZW(parseFloat(values[0]), parseFloat(values[1]),
                parseFloat(values[2]), parseFloat(values[3]));

        return rotation;
    }

    /**
     * Get a rotation by a key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the rotation.
     */
    @NotNull
    public Quaternion4f getRotation(@NotNull final String key, @NotNull final Quaternion4f def) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Quaternion4f) {
            return (Quaternion4f) object;
        } else if (object instanceof String) {
            return parseRotation((String) object);
        }

        return def;
    }

    /**
     * Get a short value by a key.
     *
     * @param key the key.
     * @return the short value.
     */
    public short getShort(@NotNull final String key) {

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
     * Get a short value by a key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the short value.
     */
    public short getShort(@NotNull final String key, final short def) {

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
     * Get a short array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the short array.
     */
    @NotNull
    public short[] getShortArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof short[]) {
            return (short[]) object;
        } else if (object instanceof String) {
            return parseShortArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private short[] parseShortArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final short[] result = new short[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Short.parseShort(strings[i]);
        }

        return result;
    }

    /**
     * Get a short array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the short array.
     */
    @NotNull
    public short[] getShortArray(@NotNull final String key, @NotNull final String regex, @NotNull final short... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof short[]) {
            return (short[]) object;
        } else if (object instanceof String) {
            return parseShortArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a string by a key.
     *
     * @param key the key.
     * @return the string.
     */
    @NotNull
    public String getString(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof String) {
            return object.toString();
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a string by a key.
     *
     * @param key the key.
     * @param def the default string.
     * @return the string.
     */
    @NotNull
    public String getString(@NotNull final String key, @NotNull final String def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof String) {
            return object.toString();
        }

        return def;
    }

    /**
     * Get a string array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the string array.
     */
    @NotNull
    public String[] getStringArray(@NotNull final String key, @NotNull final String regex) {

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
     * Get a string array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the string array.
     */
    @NotNull
    public String[] getStringArray(@NotNull final String key, @NotNull final String regex,
                                   @NotNull final String... def) {

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
     * @return the table with values.
     */
    @NotNull
    public ObjectDictionary<String, Object> getValues() {
        return values;
    }

    /**
     * Get a vector by a key.
     *
     * @param key the key.
     * @return the vector.
     */
    @NotNull
    public Vector3f getVector(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Vector3f) {
            return (Vector3f) object;
        } else if (object instanceof String) {
            return parseVector((String) object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    @NotNull
    private Vector3f parseVector(@NotNull final String object) {

        final String[] values = object.split(",");

        final Vector3f vector = Vector3f.newInstance();
        vector.set(parseFloat(values[0]), parseFloat(values[1]), parseFloat(values[2]));

        return vector;
    }

    /**
     * Get a vector by a key.
     *
     * @param key the key.
     * @param def the default vector.
     * @return the vector.
     */
    @NotNull
    public Vector3f getVector(@NotNull final String key, @NotNull final Vector3f def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Vector3f) {
            return (Vector3f) object;
        } else if (object instanceof String) {
            return parseVector((String) object);
        }

        return def;
    }

    /**
     * Clear and fill this table by attributes from a xml node.
     *
     * @param node the xml node.
     */
    public VarTable parse(@Nullable final Node node) {
        values.clear();

        if (node == null) return this;

        final NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) return this;

        for (int i = 0, length = attributes.getLength(); i < length; i++) {
            final Node item = attributes.item(i);
            set(item.getNodeName(), item.getNodeValue());
        }

        return this;
    }

    /**
     * Clear and fill this table using children of a xml node:
     * <pre>
     * 	< element >
     * 		< child name="name" value="value" />
     * 		< child name="name" value="value" />
     * 		< child name="name" value="value" />
     * 		< child name="name" value="value" />
     * 	< /element >
     *
     * vars.parse(node, "child", "name", "value")
     * </pre>
     *
     * @param node      the xml node.
     * @param childName the name of a child element.
     * @param nameName  the name of name attribute.
     * @param nameValue the name of value attribute.
     */
    public VarTable parse(@Nullable final Node node, @NotNull final String childName, @NotNull final String nameName,
                          @NotNull final String nameValue) {
        values.clear();

        if (node == null) {
            return this;
        }

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {

            if (child.getNodeType() != Node.ELEMENT_NODE || !childName.equals(child.getNodeName())) {
                continue;
            }

            final NamedNodeMap attributes = child.getAttributes();

            final Node nameNode = attributes.getNamedItem(nameName);
            final Node valueNode = attributes.getNamedItem(nameValue);

            if (nameNode == null || valueNode == null) {
                continue;
            }

            set(nameNode.getNodeValue(), valueNode.getNodeValue());
        }

        return this;
    }

    /**
     * Set a value by a key.
     *
     * @param key   the key.
     * @param value the value.
     */
    public void set(@NotNull final String key, @NotNull final Object value) {
        values.put(key, requireNonNull(value));
    }

    /**
     * Clear a value by a key.
     *
     * @param key the key.
     */
    public void clear(@NotNull final String key) {
        values.remove(key);
    }

    /**
     * Copy all values from a other table.
     *
     * @param vars the other table.
     */
    @NotNull
    public VarTable set(@NotNull final VarTable vars) {
        values.put(vars.getValues());
        return this;
    }

    @Override
    public String toString() {
        return "VarTable: " + ("values = " + values);
    }
}
