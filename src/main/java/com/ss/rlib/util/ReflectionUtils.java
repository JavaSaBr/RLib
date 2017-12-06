package com.ss.rlib.util;

import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * The class with utility reflection methods.
 *
 * @author JavaSaBr
 */
public final class ReflectionUtils {

    /**
     * Get all fields of the class.
     *
     * @param container  the field container.
     * @param cs         the class.
     * @param last       the last class.
     * @param declared   the flag of getting private fields.
     * @param exceptions exception fields.
     */
    public static void addAllFields(@NotNull final Array<Field> container, @NotNull final Class<?> cs,
                                    @NotNull final Class<?> last, final boolean declared,
                                    @Nullable final String... exceptions) {

        Class<?> next = cs;

        while (next != null && next != last) {

            final Field[] fields = declared ? next.getDeclaredFields() : next.getFields();
            next = next.getSuperclass();
            if (fields.length < 1) continue;

            if (exceptions == null || exceptions.length < 1) {
                container.addAll(fields);
            } else {
                ArrayUtils.forEach(fields, toCheck -> !ArrayUtils.contains(exceptions, toCheck.getName()), container::add);
            }
        }
    }

    /**
     * Get all fields of the class.
     *
     * @param cs         the class.
     * @param last       the last class.
     * @param declared   the flag of getting private fields.
     * @param exceptions exception fields.
     * @return the all fields
     */
    public static @NotNull Array<Field> getAllFields(@NotNull final Class<?> cs, @NotNull final Class<?> last,
                                                     final boolean declared, @Nullable final String... exceptions) {
        final Array<Field> container = ArrayFactory.newArray(Field.class);
        addAllFields(container, cs, last, declared, exceptions);
        return container;
    }

    /**
     * Get a field.
     *
     * @param object    the object.
     * @param fieldName the field name.
     * @return the field.
     */

    public static @NotNull Field getField(@NotNull final Object object, @NotNull final String fieldName) {
        try {
            return object.getClass().getDeclaredField(fieldName);
        } catch (final NoSuchFieldException | SecurityException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a field using full access.
     *
     * @param object    the object.
     * @param fieldName the field name.
     * @return the field.
     */
    public static @NotNull Field getUnsafeField(@NotNull final Object object, @NotNull final String fieldName) {
        try {
            final Field field = getField(object, fieldName);
            field.setAccessible(true);
            return field;
        } catch (final SecurityException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a field value.
     *
     * @param <T>       the type parameter
     * @param object    the object.
     * @param fieldName the field name.
     * @return the value.
     */
    public static <T> @Nullable T getFiledValue(@NotNull final Object object, @NotNull final String fieldName) {
        try {
            final Field field = getField(object, fieldName);
            return ClassUtils.unsafeCast(field.get(object));
        } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a field value using full access.
     *
     * @param <T>       the type parameter
     * @param object    the object.
     * @param fieldName the field name.
     * @return the value.
     */
    public static <T> @Nullable T getUnsafeFieldValue(@NotNull final Object object, @NotNull final String fieldName) {
        try {
            final Field field = getUnsafeField(object, fieldName);
            return ClassUtils.unsafeCast(field.get(object));
        } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a field value.
     *
     * @param <T>    the type parameter
     * @param object the object.
     * @param field  the field.
     * @return the value.
     */
    public static <T> @Nullable T getFieldValue(@NotNull final Object object, @NotNull final Field field) {
        try {
            return ClassUtils.unsafeCast(field.get(object));
        } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set a field value.
     *
     * @param object    the object.
     * @param fieldName the field name.
     * @param value     the value.
     */
    public static void setFieldValue(@NotNull final Object object, @NotNull final String fieldName,
                                     @NotNull final Object value) {
        try {
            final Field field = getField(object, fieldName);
            field.set(object, value);
        } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set a field value using full access.
     *
     * @param object    the object.
     * @param fieldName the field name.
     * @param value     the value.
     */
    public static void setUnsafeFieldValue(@NotNull final Object object, @NotNull final String fieldName,
                                           @NotNull final Object value) {
        try {
            final Field field = getUnsafeField(object, fieldName);
            field.set(object, value);
        } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Set a field value.
     *
     * @param object the object.
     * @param field  the field.
     * @param value  the value.
     */
    public static void setFieldValue(@NotNull final Object object, @NotNull final Field field,
                                     @NotNull final Object value) {
        try {
            field.set(object, value);
        } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a static field.
     *
     * @param type      the class.
     * @param fieldName the field name.
     * @return the static field.
     */
    public static @NotNull Field getStaticField(@NotNull final Class<?> type, @NotNull final String fieldName) {
        try {
            return type.getDeclaredField(fieldName);
        } catch (final NoSuchFieldException | SecurityException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a static field using full access.
     *
     * @param type      the class.
     * @param fieldName the field name.
     * @return the static field.
     */
    public static @NotNull Field getUnsafeStaticField(@NotNull final Class<?> type, @NotNull final String fieldName) {
        try {
            final Field field = getStaticField(type, fieldName);
            field.setAccessible(true);
            return field;
        } catch (final SecurityException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a static field value.
     *
     * @param <T>       the type parameter
     * @param type      the class.
     * @param fieldName the field name.
     * @return the value.
     */
    public static <T> @Nullable T getStaticFieldValue(@NotNull final Class<?> type, @NotNull final String fieldName) {
        try {
            final Field field = getStaticField(type, fieldName);
            return ClassUtils.unsafeCast(field.get(null));
        } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a static field value using full access.
     *
     * @param <T>       the type parameter
     * @param type      the class.
     * @param fieldName the field name.
     * @return the value.
     */
    public static <T> @Nullable T getUnsafeStaticFieldValue(@NotNull final Class<?> type,
                                                            @NotNull final String fieldName) {
        try {
            final Field field = getUnsafeStaticField(type, fieldName);
            return ClassUtils.unsafeCast(field.get(null));
        } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a static field value.
     *
     * @param <T>   the type parameter
     * @param field the field.
     * @return the value.
     */
    public static <T> @Nullable T getStaticFieldValue(@NotNull final Field field) {
        try {
            return ClassUtils.unsafeCast(field.get(null));
        } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set a static field value.
     *
     * @param type      the class.
     * @param fieldName the field name.
     * @param value     the new value.
     */
    public static void setStaticFieldValue(@NotNull final Class<?> type, @NotNull final String fieldName,
                                           @NotNull final Object value) {
        try {
            final Field field = getStaticField(type, fieldName);
            field.set(null, value);
        } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set a static field value.
     *
     * @param type      the class.
     * @param fieldName thr field name.
     * @param value     the new value.
     */
    public static void setUnsafeStaticFieldValue(@NotNull final Class<?> type, @NotNull final String fieldName,
                                                 @NotNull final Object value) {
        try {
            final Field field = getUnsafeStaticField(type, fieldName);
            field.set(null, value);
        } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Change a static field value.
     *
     * @param field the field.
     * @param value the new value.
     */
    public static void setStaticFieldValue(@NotNull final Field field, @NotNull final Object value) {
        try {
            field.set(null, value);
        } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private ReflectionUtils() {
        throw new IllegalArgumentException();
    }
}
