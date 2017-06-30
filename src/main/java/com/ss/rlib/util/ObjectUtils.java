package com.ss.rlib.util;

import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import com.ss.rlib.util.dictionary.ConcurrentObjectDictionary;
import com.ss.rlib.util.dictionary.DictionaryFactory;
import com.ss.rlib.util.dictionary.DictionaryUtils;
import com.ss.rlib.util.dictionary.ObjectDictionary;
import com.ss.rlib.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Objects;

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
    @NotNull
    public static <T> T notNull(@Nullable final T obj, @NotNull final String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
        return obj;
    }

    /**
     * @see Objects#requireNonNull(Object)
     */
    @NotNull
    public static <T> T notNull(@Nullable final T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument is null.");
        }
        return obj;
    }

    /**
     * Get a hash for a boolean value.
     *
     * @param value the boolean value.
     * @return the hash.
     */
    public static int hash(final boolean value) {
        return value ? 1231 : 1237;
    }

    /**
     * Get a hash for a long value.
     *
     * @param value the long value.
     * @return the hash.
     */
    public static int hash(final long value) {
        return (int) (value ^ value >>> 32);
    }

    /**
     * Get a hash for an object.
     *
     * @param object the object.
     * @return the hash.
     */
    public static int hash(@Nullable final Object object) {
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
    public static <O, N extends O> void reload(@NotNull final O original, @NotNull final N target, final boolean cache) {

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
     * Call the method {@link Reusable#release()} if the object instanceof {@link Reusable}.
     *
     * @param object the object.
     */
    public static void release(@Nullable final Object object) {
        if (object instanceof Reusable) ((Reusable) object).release();
    }
}
