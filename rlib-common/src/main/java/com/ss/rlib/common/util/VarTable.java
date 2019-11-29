package com.ss.rlib.common.util;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import static java.lang.Float.parseFloat;
import com.ss.rlib.common.geom.Quaternion4f;
import com.ss.rlib.common.geom.Vector3f;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.ObjectDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The utility class to contain properties of different types.
 *
 * @author JavaSaBr
 */
public class VarTable {

    /**
     * The table with values.
     */
    @NotNull
    private final ObjectDictionary<String, Object> values;

    /**
     * @since 8.1.0
     */
    public VarTable() {
        this.values = DictionaryFactory.newObjectDictionary();
    }

    /**
     * Clear this table.
     */
    public void clear() {
        values.clear();
    }

    /**
     * Get a value by the key.
     *
     * @param key the key.
     * @param <T> the value's type.
     * @return the value.
     * @throws IllegalArgumentException if the value is null.
     */
    public <T> @NotNull T get(@NotNull String key) {

        T result = ClassUtils.unsafeCast(values.get(key));

        if (result != null) {
            return result;
        }

        throw new IllegalArgumentException("Not found value for the key: " + key);
    }

    /**
     * Get a value by a key.
     *
     * @param <T>  the value's type.
     * @param key  the key.
     * @param type the type.
     * @return the value.
     * @throws IllegalArgumentException if the value is null or has wrong type.
     */
    public <T> @NotNull T get(@NotNull String key, @NotNull Class<T> type) {

        var object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found value for the key: " + key);
        } else if (type.isInstance(object)) {
            return type.cast(object);
        }

        throw new IllegalArgumentException(
            "Value: " + object + " has another type than requested: " + type + " by the key: " + key);
    }

    /**
     * Get a value by a key.
     *
     * @param <T>  the value's type.
     * @param <E>  the default value's type.
     * @param key  the key.
     * @param type the type.
     * @param def  the default value.
     * @return the value or default if the value is null or has wrong type.
     */
    public <T, E extends T> @NotNull T get(@NotNull String key, @NotNull Class<T> type, @NotNull E def) {

        var object = values.get(key);

        if (object == null) {
            return def;
        } else if (type.isInstance(object)) {
            return type.cast(object);
        }

        return def;
    }

    /**
     * Get a value by a key.
     *
     * @param <T>  the value's type.
     * @param <E>  the default value's type.
     * @param key  the key.
     * @param type the type.
     * @param def  the default value.
     * @return the value or default if the value not found.
     * @throws IllegalArgumentException if the value has wrong type.
     */
    public <T, E extends T> @Nullable T getNullable(@NotNull String key, @NotNull Class<T> type, @Nullable E def) {

        var object = values.get(key);

        if (object == null) {
            return def;
        } else if (type.isInstance(object)) {
            return type.cast(object);
        }

        throw new IllegalArgumentException(
            "Value: " + object + " has another type than requested: " + type + " by the key: " + key);
    }

    /**
     * Get a value by a key.
     *
     * @param <T>  the value's type.
     * @param key the key.
     * @param def the default value.
     * @return the value or default if the value is null.
     */
    public <T> @NotNull T get(@NotNull String key, @NotNull T def) {

        var object = values.get(key);

        if (object == null) {
            return def;
        }

        var type = def.getClass();

        if (type.isInstance(object)) {
            return notNull(ClassUtils.unsafeCast(object));
        }

        throw new IllegalArgumentException(
            "Value: " + object + " has another type than requested: " + type + " by the key: " + key);
    }

    /**
     * Get a value by a key.
     *
     * @param <T>  the value's type.
     * @param key the key.
     * @param def the default value.
     * @return the value or default if the value is not found.
     */
    public <T> @Nullable T getNullable(@NotNull String key, @Nullable T def) {

        var object = values.get(key);

        if (object == null) {
            return def;
        }

        if (def != null) {

            var type = def.getClass();

            if (type.isInstance(object)) {
                return ClassUtils.unsafeCast(object);
            }

            return def;

        } else {
            return ClassUtils.unsafeCast(object);
        }
    }

    /**
     * Get an array of a key.
     *
     * @param <T>  the array element's type.
     * @param key  the key.
     * @param type the array's type.
     * @param def  the default array.
     * @return the array.
     */
    @SafeVarargs
    public final <T> @NotNull T[] getArray(@NotNull String key, @NotNull Class<T[]> type, @NotNull T... def) {

        var object = values.get(key);

        if (object == null) {
            return def;
        } else if (type.isInstance(object)) {
            return ClassUtils.unsafeNNCast(object);
        }

        throw new IllegalArgumentException(
            "Value: " + object + " has another type than requested: " + type + " by the key: " + key);
    }

    /**
     * Get a boolean value by a key.
     *
     * @param key the key.
     * @return the value.
     */
    public boolean getBoolean(@NotNull String key) {

        var object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found value for the key: " + key);
        } else if (object instanceof Boolean) {
            return (Boolean) object;
        } else if (object instanceof String) {
            return Boolean.parseBoolean(object.toString());
        }

        throw new IllegalArgumentException(
            "Value: " + object + " cannot be converted to boolean by the key: " + key);
    }

    /**
     * Get a boolean value by a key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the value.
     */
    public boolean getBoolean(@NotNull String key, boolean def) {

        var object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Boolean) {
            return (Boolean) object;
        } else if (object instanceof String) {
            return Boolean.parseBoolean(object.toString());
        }

        throw new IllegalArgumentException(
            "Value: " + object + " cannot be converted to boolean by the key: " + key);
    }

    /**
     * Get a boolean array by a key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the boolean array.
     */
    public @NotNull boolean[] getBooleanArray(@NotNull String key, @NotNull String regex) {

        var object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found value for the key: " + key);
        } else if (object instanceof boolean[]) {
            return (boolean[]) object;
        } else if (object instanceof String) {
            return parseBooleanArray(regex, object);
        }

        throw new IllegalArgumentException(
            "Value: " + object + " cannot be converted to boolean array by the key: " + key);
    }

    private boolean[] parseBooleanArray(@NotNull String regex, @NotNull Object object) {

        var strings = object.toString().split(regex);
        var booleans = new boolean[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            booleans[i] = Boolean.parseBoolean(strings[i].trim());
        }

        return booleans;
    }

    /**
     * Get a boolean array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the boolean array.
     */
    public @NotNull boolean[] getBooleanArray(@NotNull final String key, @NotNull final String regex,
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
     * Get a byte value by the key.
     *
     * @param key the key.
     * @return the byte.
     */
    public byte getByte(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof Byte) {
            return (Byte) object;
        } else if (object instanceof String) {
            return Byte.parseByte(object.toString());
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    /**
     * Get a byte value by the key.
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
     * Get a byte array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the byte array.
     */
    public @NotNull byte[] getByteArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof byte[]) {
            return (byte[]) object;
        } else if (object instanceof String) {
            return parseByteArray(regex, object);
        }

        throw new IllegalArgumentException("Not found " + key);
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
     * Get a byte array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default byte array.
     * @return the byte array.
     */
    public @NotNull byte[] getByteArray(@NotNull final String key, @NotNull final String regex,
                                        @NotNull final byte... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof byte[]) {
            return (byte[]) object;
        } else if (object instanceof String) {
            return parseByteArray(regex, object);
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    /**
     * Get a double value by the key.
     *
     * @param key the key.
     * @return the value.
     */
    public double getDouble(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof Double) {
            return (Double) object;
        } else if (object instanceof String) {
            return Double.parseDouble(object.toString());
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    /**
     * Get a double value by the key.
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
     * Get a double array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the double array.
     */
    public @NotNull double[] getDoubleArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof double[]) {
            return (double[]) object;
        } else if (object instanceof String) {
            return parseDoubleArray(regex, object);
        }

        throw new IllegalArgumentException("Not found " + key);
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
     * Get a double array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the double array.
     */
    public @NotNull double[] getDoubleArray(@NotNull final String key, @NotNull final String regex,
                                            @NotNull final double... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof double[]) {
            return (double[]) object;
        } else if (object instanceof String) {
            return parseDoubleArray(regex, object);
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    /**
     * Get an enum value by the key.
     *
     * @param <T>  the type parameter
     * @param key  the key.
     * @param type the type of enum.
     * @return the value.
     */
    public @NotNull <T extends Enum<T>> T getEnum(@NotNull final String key, @NotNull final Class<T> type) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (type.isInstance(object)) {
            return type.cast(object);
        } else if (object instanceof String) {
            return Enum.valueOf(type, object.toString());
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    /**
     * Get an enum value by the key.
     *
     * @param <T>  the type parameter
     * @param key  the key.
     * @param type the type of enum.
     * @param def  the default value.
     * @return the value.
     */
    public <T extends Enum<T>> @NotNull T getEnum(@NotNull final String key, @NotNull final Class<T> type,
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
     * Get an enum array by the key.
     *
     * @param <T>   the type parameter
     * @param key   the key.
     * @param type  the type of enum.
     * @param regex the regex to split if a value is string.
     * @return the enum array.
     */
    public <T extends Enum<T>> @NotNull T[] getEnumArray(@NotNull final String key, @NotNull final Class<T> type,
                                                         @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof Enum[]) {
            return notNull(ClassUtils.unsafeCast(object));
        } else if (object instanceof String) {
            return parseEnumArray(type, regex, object);
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    private <T extends Enum<T>> @NotNull T[] parseEnumArray(@NotNull final Class<T> type, @NotNull final String regex,
                                                            @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final T[] result = notNull(ClassUtils.unsafeCast(ArrayUtils.create(type, strings.length)));

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Enum.valueOf(type, strings[i]);
        }

        return result;
    }

    /**
     * Get an enum array by the key.
     *
     * @param <T>   the type parameter
     * @param key   the key.
     * @param type  the type of enum.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the enum array.
     */

    public <T extends Enum<T>> @NotNull T[] getEnumArray(@NotNull final String key, @NotNull final Class<T> type,
                                                         @NotNull final String regex, @NotNull final T... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Enum[]) {
            return notNull(ClassUtils.unsafeCast(object));
        } else if (object instanceof String) {
            return parseEnumArray(type, regex, object);
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    /**
     * Get a float value by the key.
     *
     * @param key the key.
     * @return the float value.
     */
    public float getFloat(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof Float) {
            return (Float) object;
        } else if (object instanceof String) {
            return Float.parseFloat(object.toString());
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    /**
     * Get a float value by the key.
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
     * Get a float array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the float array.
     */
    public @NotNull float[] getFloatArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException();
        } else if (object instanceof float[]) {
            return (float[]) object;
        } else if (object instanceof String) {
            return parseFloatArray(regex, object);
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    private @NotNull float[] parseFloatArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final float[] result = new float[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Float.parseFloat(strings[i]);
        }

        return result;
    }

    /**
     * Get a float array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the float array.
     */
    public @NotNull float[] getFloatArray(
        @NotNull String key, 
        @NotNull String regex,
        @NotNull float... def
    ) {

        var object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof float[]) {
            return (float[]) object;
        } else if (object instanceof String) {
            return parseFloatArray(regex, object);
        }

        throw new IllegalArgumentException("Not found " + key);
    }
    

    /**
     * Get an integer value by the key.
     *
     * @param key the key.
     * @return the integer value.
     * @throws IllegalArgumentException if the value isn't exist.
     * @throws IllegalStateException if the value can't be presented as int.
     * @since 8.1.0
     */
    public int getInt(@NotNull String key) {

        var object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof String) {
            return Integer.parseInt(object.toString());
        }

        throw new IllegalStateException("Value: " + object + " can't be presented as int, key: " + key);
    }

    /**
     * Get an integer value by the key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the integer value or the default value if the value isn't exist.
     * @throws IllegalStateException if the value can't be presented as int.
     * @since 8.1.0
     */
    public int getInt(@NotNull String key, int def) {

        var object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof String) {
            return Integer.parseInt(object.toString());
        }

        throw new IllegalStateException("Value: " + object + " can't be presented as int, key: " + key);
    }

    /**
     * Get an int array value by the key.
     *
     * @param key   the key.
     * @return the int array value.
     * @throws IllegalStateException if the value can't be presented as int array.
     * @throws IllegalArgumentException if the value isn't exist.
     * @since 9.2.1
     */
    public @NotNull int[] getIntArray(@NotNull String key) {

        var object = values.get(key);

        if(object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof int[]) {
            return (int[]) object;
        } else if (object instanceof Integer[]) {
            return ArrayUtils.toIntArray((Integer[]) object);
        }

        throw new IllegalStateException("Value: " + object + " can't be presented as int array, key: " + key);
    }

    /**
     * Get an int array value by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the int array value.
     * @throws IllegalArgumentException if the value isn't exist.
     * @throws IllegalStateException if the value can't be presented as int array.
     * @since 8.1.0
     */
    public @NotNull int[] getIntArray(@NotNull String key, @NotNull String regex) {

        var object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof int[]) {
            return (int[]) object;
        } else if (object instanceof String) {
            return ArrayUtils.toIntArray((String) object, regex);
        }

        throw new IllegalStateException("Value: " + object + " can't be presented as int array, key: " + key);
    }

    /**
     * Get an int array value by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default value.
     * @return the int array value or the default value if the value isn't exist.
     * @throws IllegalStateException if the value can't be presented as int array.
     * @since 8.1.0
     */
    public @NotNull int[] getIntArray(@NotNull String key, @NotNull String regex, @NotNull int... def) {

        var object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof int[]) {
            return (int[]) object;
        } else if (object instanceof String) {
            return ArrayUtils.toIntArray((String) object, regex);
        }

        throw new IllegalStateException("Value: " + object + " can't be presented as int array, key: " + key);
    }

    /**
     * Get a long value by the key.
     *
     * @param key the key.
     * @return the long value.
     */
    public long getLong(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof Long) {
            return (Long) object;
        } else if (object instanceof String) {
            return Long.parseLong(object.toString());
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    /**
     * Get a long value by the key.
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
     * Get a long array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the long array.
     */
    public @NotNull long[] getLongArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof long[]) {
            return (long[]) object;
        } else if (object instanceof String) {
            return parseLongArray(regex, object);
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    private @NotNull long[] parseLongArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final long[] result = new long[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Long.parseLong(strings[i]);
        }

        return result;
    }

    /**
     * Get a long array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the long array.
     */
    public @NotNull long[] getLongArray(@NotNull final String key, @NotNull final String regex,
                                        @NotNull final long... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof long[]) {
            return (long[]) object;
        } else if (object instanceof String) {
            return parseLongArray(regex, object);
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    /**
     * Get a rotation by the key.
     *
     * @param key the key.
     * @return the rotation.
     */
    public @NotNull Quaternion4f getRotation(@NotNull final String key) {

        var object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof Quaternion4f) {
            return (Quaternion4f) object;
        } else if (object instanceof String) {
            return parseRotation((String) object);
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    private @NotNull Quaternion4f parseRotation(@NotNull String object) {

        var values = object.split(",");

        var rotation = new Quaternion4f();
        rotation.setXYZW(
            parseFloat(values[0]),
            parseFloat(values[1]),
            parseFloat(values[2]),
            parseFloat(values[3])
        );

        return rotation;
    }

    /**
     * Get a rotation by the key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the rotation.
     */
    public @NotNull Quaternion4f getRotation(@NotNull final String key, @NotNull final Quaternion4f def) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof Quaternion4f) {
            return (Quaternion4f) object;
        } else if (object instanceof String) {
            return parseRotation((String) object);
        }

        return def;
    }

    /**
     * Get a short value by the key.
     *
     * @param key the key.
     * @return the short value.
     */
    public short getShort(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof Short) {
            return (Short) object;
        } else if (object instanceof String) {
            return Short.parseShort(object.toString());
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    /**
     * Get a short value by the key.
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
     * Get a short array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the short array.
     */
    public @NotNull short[] getShortArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof short[]) {
            return (short[]) object;
        } else if (object instanceof String) {
            return parseShortArray(regex, object);
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    private @NotNull short[] parseShortArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final short[] result = new short[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Short.parseShort(strings[i]);
        }

        return result;
    }

    /**
     * Get a short array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the short array.
     */
    public @NotNull short[] getShortArray(@NotNull final String key, @NotNull final String regex,
                                          @NotNull final short... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof short[]) {
            return (short[]) object;
        } else if (object instanceof String) {
            return parseShortArray(regex, object);
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    /**
     * Get a string by the key.
     *
     * @param key the key.
     * @return the string value.
     * @throws IllegalArgumentException if the value isn't exist.
     */
    public @NotNull String getString(@NotNull String key) {

        var object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof String) {
            return object.toString();
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    /**
     * Get a string by the key.
     *
     * @param key the key.
     * @param def the default string.
     * @return the string.
     */
    public @NotNull String getString(@NotNull final String key, @NotNull final String def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof String) {
            return object.toString();
        }

        return def;
    }

    /**
     * Get a string by the key.
     *
     * @param key the key.
     * @param def the default string.
     * @return the string.
     */
    public @Nullable String getNullableString(@NotNull final String key, @Nullable final String def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof String) {
            return object.toString();
        }

        return def;
    }

    /**
     * Get a string array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the string array.
     */
    public @NotNull String[] getStringArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof String[]) {
            return (String[]) object;
        } else if (object instanceof String) {
            return object.toString().split(regex);
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    /**
     * Get a string array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the string array.
     */
    public @NotNull String[] getStringArray(@NotNull final String key, @NotNull final String regex,
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
     * Get the values table.
     *
     * @return the values table.
     */
    public @NotNull ObjectDictionary<String, Object> getValues() {
        return values;
    }

    /**
     * Get a vector by the key.
     *
     * @param key the key.
     * @return the vector.
     */
    public @NotNull Vector3f getVector(@NotNull String key) {

        Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("Not found " + key);
        } else if (object instanceof Vector3f) {
            return (Vector3f) object;
        } else if (object instanceof String) {
            return parseVector((String) object);
        }

        throw new IllegalArgumentException("Not found " + key);
    }

    private @NotNull Vector3f parseVector(@NotNull String object) {
        String[] values = object.split(",");
        return new Vector3f(parseFloat(values[0]), parseFloat(values[1]), parseFloat(values[2]));
    }

    /**
     * Get a vector by the key.
     *
     * @param key the key.
     * @param def the default vector.
     * @return the vector.
     */
    public @NotNull Vector3f getVector(@NotNull final String key, @NotNull final Vector3f def) {

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
     * Put the value by the key to this vars table.
     *
     * @param key   the key.
     * @param value the value.
     * @since 8.1.0
     */
    public void put(@NotNull String key, @NotNull Object value) {
        values.put(key, notNull(value));
    }

    /**
     * Clear a value by the key.
     *
     * @param key the key.
     */
    public void clear(@NotNull String key) {
        values.remove(key);
    }

    /**
     * Copy all values from the another vars table.
     *
     * @param vars the another vars table.
     * @return this vars table.
     * @throws IllegalArgumentException is the vars table is the same as this vars table.
     * @since 8.1.0
     */
    public @NotNull VarTable put(@NotNull VarTable vars) {

        if (vars == this) {
            throw new IllegalArgumentException("Can't set itself.");
        }

        values.put(vars.getValues());
        return this;
    }

    /**
     * Return true if a value by the key is presented in this vars table.
     *
     * @param key the key.
     * @return true if a value by the key is presented in this vars table.
     */
    public boolean has(@NotNull String key) {
        return values.containsKey(key);
    }

    @Override
    public String toString() {
        return "VarTable: " + ("values = " + values);
    }
}
