package com.ss.rlib.common.util;

import static com.ss.rlib.common.util.ArrayUtils.contains;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
     * @param startClass the class.
     * @param lastClass  the last class.
     * @param declared   the flag to get private fields as well.
     * @param exceptions exception fields.
     */
    public static void addAllFields(
        @NotNull Array<Field> container,
        @NotNull Class<?> startClass,
        @NotNull Class<?> lastClass,
        boolean declared,
        @NotNull String... exceptions
    ) {

        var next = startClass;

        while (next != null && next != lastClass) {

            var fields = declared ? next.getDeclaredFields() : next.getFields();

            next = next.getSuperclass();

            if (fields.length < 1) {
                continue;
            }

            if (exceptions.length < 1) {
                container.addAll(fields);
            } else {
                ArrayUtils.forEach(fields, toCheck -> !contains(exceptions, toCheck.getName()), container::add);
            }
        }
    }

    /**
     * Get all fields of a class.
     *
     * @param cs         the class.
     * @param exceptions exception fields.
     * @return the all declared fields.
     * @since 9.9.0
     */
    public static @NotNull Array<Field> getAllDeclaredFields(@NotNull Class<?> cs, @NotNull String... exceptions) {
        var container = Array.ofType(Field.class);
        addAllFields(container, cs, Object.class, true, exceptions);
        return container;
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
    public static @NotNull Array<Field> getAllFields(
        @NotNull Class<?> cs,
        @NotNull Class<?> last,
        boolean declared,
        @NotNull String... exceptions
    ) {
        var container = Array.ofType(Field.class);
        addAllFields(container, cs, last, declared, exceptions);
        return container;
    }

    /**
     * Get a field by the name from the type.
     *
     * @param type      the type.
     * @param fieldName the field name.
     * @return the field.
     */
    public static @NotNull Field getField(@NotNull Class<?> type, @NotNull String fieldName) {
        try {
            return type.getDeclaredField(fieldName);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a field by the name from the type of the object.
     *
     * @param object    the object.
     * @param fieldName the field name.
     * @return the field.
     */
    public static @NotNull Field getField(@NotNull Object object, @NotNull String fieldName) {
        return getField(object.getClass(), fieldName);
    }

    /**
     * Get a field by the name with full access from the type.
     *
     * @param type      the type.
     * @param fieldName the field name.
     * @return the field.
     */
    public static @NotNull Field getUnsafeField(@NotNull Class<?> type, @NotNull String fieldName) {
        try {
            Field field = getField(type, fieldName);
            field.setAccessible(true);
            return field;
        } catch (SecurityException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a field by the name with full access.
     *
     * @param object    the object.
     * @param fieldName the field name.
     * @return the field.
     */
    public static @NotNull Field getUnsafeField(@NotNull Object object, @NotNull String fieldName) {
        return getUnsafeField(object.getClass(), fieldName);
    }

    /**
     * Get a field value by the field name.
     *
     * @param <T>       the result value's type.
     * @param object    the object.
     * @param fieldName the field name.
     * @return the value.
     */
    public static <T> @Nullable T getFiledValue(@NotNull Object object, @NotNull String fieldName) {
        try {
            Field field = getField(object, fieldName);
            return ClassUtils.unsafeCast(field.get(object));
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a field value by the field name with full access.
     *
     * @param <T>       the result value's type.
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
     * @param <T>       the result value's type.
     * @param object the object.
     * @param field  the field.
     * @return the value.
     */
    public static <T> @Nullable T getFieldValue(@NotNull Object object, @NotNull Field field) {
        try {
            return ClassUtils.unsafeCast(field.get(object));
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
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
    public static void setFieldValue(@NotNull Object object, @NotNull String fieldName, @NotNull Object value) {
        try {
            Field field = getField(object, fieldName);
            field.set(object, value);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
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
    public static void setUnsafeFieldValue(@NotNull Object object, @NotNull String fieldName, @NotNull Object value) {
        try {
            Field field = getUnsafeField(object, fieldName);
            field.set(object, value);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
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
    public static void setFieldValue(@NotNull Object object, @NotNull Field field, @NotNull Object value) {
        try {
            field.set(object, value);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
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
    public static @NotNull Field getStaticField(@NotNull Class<?> type, @NotNull String fieldName) {
        try {
            return type.getDeclaredField(fieldName);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
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
    public static @NotNull Field getUnsafeStaticField(@NotNull Class<?> type, @NotNull String fieldName) {
        try {
            Field field = getStaticField(type, fieldName);
            field.setAccessible(true);
            return field;
        } catch (SecurityException | IllegalArgumentException e) {
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
    public static <T> @Nullable T getStaticFieldValue(@NotNull Class<?> type, @NotNull String fieldName) {
        try {
            Field field = getStaticField(type, fieldName);
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
    public static <T> @Nullable T getUnsafeStaticFieldValue(@NotNull Class<?> type, @NotNull String fieldName) {
        try {
            Field field = getUnsafeStaticField(type, fieldName);
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
    public static <T> @Nullable T getStaticFieldValue(@NotNull Field field) {
        try {
            return ClassUtils.unsafeCast(field.get(null));
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
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
    public static void setStaticFieldValue(@NotNull Class<?> type, @NotNull String fieldName, @NotNull Object value) {
        try {
            Field field = getStaticField(type, fieldName);
            field.set(null, value);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
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
    public static void setUnsafeStaticFieldValue(@NotNull Class<?> type, @NotNull String fieldName, @NotNull Object value) {
        try {
            Field field = getUnsafeStaticField(type, fieldName);
            field.set(null, value);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Change a static field value.
     *
     * @param field the field.
     * @param value the new value.
     */
    public static void setStaticFieldValue(@NotNull Field field, @NotNull Object value) {
        try {
            field.set(null, value);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a method of the type by the method name and arg types.
     *
     * @param type       the type.
     * @param methodName the method name.
     * @param argTypes   the arg types.
     * @return the found method.
     */
    public static @NotNull Method getMethod(@NotNull Class<?> type, @NotNull String methodName, Class<?>... argTypes) {
        try {
            return type.getDeclaredMethod(methodName, argTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Call a void method of the object by the name.
     *
     * @param object     the object.
     * @param methodName the method name.
     */
    public static void callVoidMethod(@NotNull Object object, @NotNull String methodName) {
        try {
            getMethod(object.getClass(), methodName).invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Call a void method of the object by the name using full access.
     *
     * @param object     the object.
     * @param methodName the method name.
     */
    public static void callUnsafeVoidMethod(@NotNull Object object, @NotNull String methodName) {
        try {
            Method method = getMethod(object.getClass(), methodName);
            method.setAccessible(true);
            method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private ReflectionUtils() {
        throw new IllegalArgumentException();
    }
}
