package com.ss.rlib.common.util;

import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.dictionary.ConcurrentObjectDictionary;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.DictionaryUtils;
import com.ss.rlib.common.util.dictionary.ObjectDictionary;
import com.ss.rlib.common.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * The class with utility methods.
 *
 * @author JavaSaBr
 */
public final class ObjectUtils {

    private static final ConcurrentObjectDictionary<Class<?>, Array<Field>> FIELDS_CACHE =
            DictionaryFactory.newConcurrentAtomicObjectDictionary();

    /**
     * @see Objects#requireNonNull(Object, String)
     */
    public static <T> @NotNull T notNull(@Nullable T obj, @NotNull String message) {
        return Objects.requireNonNull(obj, message);
    }

    /**
     * @see Objects#requireNonNull(Object)
     */
    public static <T> @NotNull T notNull(@Nullable T obj) {
        return Objects.requireNonNull(obj);
    }

    /**
     * Returns the another object if the first object is null.
     *
     * @param obj     the object.
     * @param another the another object.
     * @return the another object if the first object is null.
     */
    public static <T> @NotNull T ifNull(@Nullable T obj, @NotNull T another) {
        return obj == null ? another : obj;
    }

    /**
     * Returns a new object if the first object is null.
     *
     * @param obj     the object.
     * @param factory the factory.
     * @return a new object if the first object is null.
     */
    public static <T> @NotNull T ifNull(@Nullable T obj, @NotNull Supplier<T> factory) {
        return obj == null ? factory.get() : obj;
    }

    /**
     * Gets hash of the boolean value.
     *
     * @param value the boolean value.
     * @return the hash.
     */
    public static int hash(boolean value) {
        return value ? 1231 : 1237;
    }

    /**
     * Gets hash of the long value.
     *
     * @param value the long value.
     * @return the hash.
     */
    public static int hash(long value) {
        return (int) (value ^ value >>> 32);
    }

    /**
     * Gets hash of the object.
     *
     * @param object the object.
     * @return the hash.
     */
    public static int hash(@Nullable Object object) {
        return object == null ? 0 : object.hashCode();
    }

    /**
     * Update all fields of an original object to a target object.
     *
     * @param <O>      the type parameter
     * @param <N>      the type parameter
     * @param original the original object.
     * @param target   the target object.
     * @param cache    the flag of using cache.
     */
    public static <O, N extends O> void reload(@NotNull O original, @NotNull N target, boolean cache) {

        final Class<?> type = original.getClass();

        boolean needPutToCache = false;
        Array<Field> array = null;


        if (cache) {
            array = DictionaryUtils.getInReadLock(FIELDS_CACHE, type, ObjectDictionary::get);
        }

        if (array == null) {
            needPutToCache = cache;

            array = ArrayFactory.newArray(Field.class);

            for (Class<?> cs = type; cs != null; cs = cs.getSuperclass()) {
                array.addAll(cs.getDeclaredFields());
            }

            array.forEach(filtered -> {
                final String fieldName = filtered.toString();
                return !(fieldName.contains("final") || fieldName.contains("static"));
            }, toUpdate -> toUpdate.setAccessible(true));
        }


        array.forEach(field -> {
            try {
                field.set(original, field.get(target));
            } catch (final IllegalArgumentException | IllegalAccessException e) {
                Utils.print(ObjectUtils.class, e);
            }
        });

        if (needPutToCache) {
            DictionaryUtils.runInWriteLock(FIELDS_CACHE, type, array, ObjectDictionary::put);
        }
    }

    /**
     * Call the method {@link Reusable#release()} if the object is instanceof {@link Reusable}.
     *
     * @param object the object.
     */
    public static void release(@Nullable Object object) {
        if (object instanceof Reusable) {
            ((Reusable) object).release();
        }
    }
}
