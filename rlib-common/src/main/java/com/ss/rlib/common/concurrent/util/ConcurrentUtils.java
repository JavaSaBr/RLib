package com.ss.rlib.common.concurrent.util;

import com.ss.rlib.common.function.ObjectIntFunction;
import com.ss.rlib.common.function.ObjectLongFunction;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.common.concurrent.lock.Lockable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * THe utility class with methods to work in concurrent cases.
 *
 * @author JavaSaBr
 */
public final class ConcurrentUtils {

    private static final Logger LOGGER = LoggerManager.getLogger(ConcurrentUtils.class);

    /**
     * Notify all threads.
     *
     * @param object the object
     */
    public static void notifyAll(@NotNull final Object object) {
        synchronized (object) {
            object.notifyAll();
        }
    }

    /**
     * Notify all threads from a synchronized block.
     *
     * @param object the object
     */
    public static void notifyAllInSynchronize(@NotNull final Object object) {
        object.notifyAll();
    }

    /**
     * Notify all threads and wait.
     *
     * @param object the object
     */
    public static void notifyAndWait(@NotNull final Object object) {
        synchronized (object) {
            notifyAllInSynchronize(object);
            waitInSynchronize(object);
        }
    }

    /**
     * Wait.
     *
     * @param object the object
     */
    public static void wait(@NotNull final Object object) {
        synchronized (object) {
            try {
                object.wait();
            } catch (final InterruptedException e) {
                LOGGER.warning(e);
            }
        }
    }

    /**
     * Wait.
     *
     * @param object the object.
     * @param time   the time in ms.
     */
    public static void wait(@NotNull final Object object, final long time) {
        synchronized (object) {
            try {
                object.wait(time);
            } catch (final InterruptedException e) {
                LOGGER.warning(e);
            }
        }
    }

    /**
     * Wait from a synchronized block.
     *
     * @param object the object
     */
    public static void waitInSynchronize(@NotNull final Object object) {
        try {
            object.wait();
        } catch (final InterruptedException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * Wait from a synchronized block.
     *
     * @param object the object.
     * @param time   the time in ms.
     */
    public static void waitInSynchronize(@NotNull final Object object, long time) {
        try {
            object.wait(time);
        } catch (final InterruptedException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * Apply a function in locked block.
     *
     * @param <T>      the type parameter
     * @param <R>      the type parameter
     * @param sync     the synchronizer.
     * @param function the function.
     * @return the result from the function.
     */
    @Nullable
    public static <T extends Lockable, R> R get(@NotNull final T sync, @NotNull final Function<T, R> function) {
        sync.lock();
        try {
            return function.apply(sync);
        } finally {
            sync.unlock();
        }
    }

    /**
     * Apply a function in locked block.
     *
     * @param <T>      the type parameter
     * @param <R>      the type parameter
     * @param sync     the synchronizer.
     * @param argument the argument.
     * @param function the function.
     * @return the result from the function.
     */
    @Nullable
    public static <T extends Lockable, R> R get(@NotNull final T sync, final int argument, @NotNull final ObjectIntFunction<T, R> function) {
        sync.lock();
        try {
            return function.apply(sync, argument);
        } finally {
            sync.unlock();
        }
    }

    /**
     * Apply a function in locked block.
     *
     * @param <T>      the type parameter
     * @param <R>      the type parameter
     * @param sync     the synchronizer.
     * @param argument the argument.
     * @param function the function.
     * @return the result from the function.
     */
    @Nullable
    public static <T extends Lockable, R> R getInL(@NotNull final T sync, final long argument, @NotNull final ObjectLongFunction<T, R> function) {
        sync.lock();
        try {
            return function.apply(sync, argument);
        } finally {
            sync.unlock();
        }
    }

    private ConcurrentUtils() {
        throw new RuntimeException();
    }
}
