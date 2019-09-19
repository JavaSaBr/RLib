package com.ss.rlib.common.util;

import com.ss.rlib.common.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;

/**
 * The class with utility methods.
 *
 * @author JavaSaBr
 */
public final class ObjectUtils {

    /**
     * @param <T>     the object's type.
     * @param obj     the object to check on not null.
     * @param message the message for exception if the object is null.
     * @return the passed object if it is not null.
     * @see Objects#requireNonNull(Object, String)
     */
    public static <T> @NotNull T notNull(@Nullable T obj, @NotNull String message) {
        return Objects.requireNonNull(obj, message);
    }

    /**
     * @param <T> the object's type.
     * @param obj the object to check on not null.
     * @return the passed object if it is not null.
     * @see Objects#requireNonNull(Object)
     */
    public static <T> @NotNull T notNull(@Nullable T obj) {
        return Objects.requireNonNull(obj);
    }

    /**
     * Check the object to be not null. If the object is null this method throws an exception from the supplier.
     *
     * @param <T>      the object's type.
     * @param obj      the checked object.
     * @param supplier the exception factory.
     * @return the object.
     * @since 9.0.2
     */
    public static <T> @NotNull T notNull(@Nullable T obj, @NotNull Supplier<? extends RuntimeException> supplier) {

        if (obj == null) {
            throw supplier.get();
        }

        return obj;
    }

    /**
     * Check the object to be not null. If the object is null this method throws an exception from the factory.
     *
     * @param <T>     the object's type.
     * @param <F>     the argument's type.
     * @param obj     the checked object.
     * @param arg     the argument for the exception factory.
     * @param factory the exception factory.
     * @return the object.
     * @since 9.0.2
     */
    public static <T, F> @NotNull T notNull(
        @Nullable T obj,
        @NotNull F arg,
        @NotNull Function<F, ? extends RuntimeException> factory
    ) {

        if (obj == null) {
            throw factory.apply(arg);
        }

        return obj;
    }

    /**
     * Check the object to be not null. If the object is null this method throws an exception from the factory.
     *
     * @param <T>     the object's type.
     * @param obj     the checked object.
     * @param arg     the argument for the exception factory.
     * @param factory the exception factory.
     * @return the object.
     * @since 9.0.3
     */
    public static <T> @NotNull T notNull(
        @Nullable T obj,
        long arg,
        @NotNull LongFunction<? extends RuntimeException> factory
    ) {

        if (obj == null) {
            throw factory.apply(arg);
        }

        return obj;
    }

    /**
     * Returns the another object if the first object is null.
     *
     * @param obj     the object.
     * @param another the another object.
     * @param <T> the object's type.
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
     * @param <T> the object's type.
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
