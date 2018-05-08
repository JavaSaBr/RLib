package com.ss.rlib.common.concurrent.lock;

import com.ss.rlib.common.function.FunctionInt;
import com.ss.rlib.common.function.ObjectIntFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The class with utility methods to work with locks.
 *
 * @author JavaSaBr
 */
public class LockUtils {

    /**
     * Execute a function in locked block.
     *
     * @param <L>      the type parameter
     * @param lockable the lockable object.
     * @param consumer the function.
     */
    public static <L extends Lockable> void runInLock(@Nullable final L lockable, @NotNull final Consumer<L> consumer) {
        if (lockable == null) return;
        lockable.lock();
        try {
            consumer.accept(lockable);
        } finally {
            lockable.unlock();
        }
    }

    /**
     * Execute a function in locked block.
     *
     * @param <L>      the type parameter
     * @param <F>      the type parameter
     * @param lockable the lockable object.
     * @param argument the additional argument.
     * @param consumer the function.
     */
    public static <L extends Lockable, F> void runInLock(@Nullable final L lockable, @Nullable final F argument,
                                                         @NotNull final BiConsumer<L, F> consumer) {
        if (lockable == null) return;
        lockable.lock();
        try {
            consumer.accept(lockable, argument);
        } finally {
            lockable.unlock();
        }
    }

    /**
     * Execute a function in locked block.
     *
     * @param <L>      the type parameter
     * @param <R>      the type parameter
     * @param lockable the lockable object.
     * @param function the function.
     * @return результат работы функции либо null.
     */
    @Nullable
    public static <L extends Lockable, R> R getInLock(@Nullable final L lockable, @NotNull final Function<L, R> function) {
        if (lockable == null) return null;
        lockable.lock();
        try {
            return function.apply(lockable);
        } finally {
            lockable.unlock();
        }
    }

    /**
     * Execute a function in locked block.
     *
     * @param <L>      the type parameter
     * @param lockable the lockable object.
     * @param function the function.
     * @return результат работы функции либо -1.
     */
    public static <L extends Lockable> int getInLockInt(@Nullable final L lockable, @NotNull final FunctionInt<L> function) {
        if (lockable == null) return -1;
        lockable.lock();
        try {
            return function.apply(lockable);
        } finally {
            lockable.unlock();
        }
    }

    /**
     * Execute a function in locked block.
     *
     * @param <L>      the type parameter
     * @param <F>      the type parameter
     * @param <R>      the type parameter
     * @param lockable the lockable object.
     * @param argument the additional argument.
     * @param function the function.
     * @return результат работы функции либо null.
     */
    @Nullable
    public static <L extends Lockable, F, R> R getInLock(@Nullable final L lockable, @Nullable final F argument, @NotNull final BiFunction<L, F, R> function) {
        if (lockable == null) return null;
        lockable.lock();
        try {
            return function.apply(lockable, argument);
        } finally {
            lockable.unlock();
        }
    }

    /**
     * Execute a function in locked block.
     *
     * @param <L>      the type parameter
     * @param <R>      the type parameter
     * @param lockable the lockable object.
     * @param argument the additional argument.
     * @param function the function.
     * @return результат работы функции либо null.
     */
    @Nullable
    public static <L extends Lockable, R> R getInLock(@Nullable final L lockable, final int argument, @NotNull final ObjectIntFunction<L, R> function) {
        if (lockable == null) return null;
        lockable.lock();
        try {
            return function.apply(lockable, argument);
        } finally {
            lockable.unlock();
        }
    }

    /**
     * Lock two locks.
     *
     * @param first  the first lock.
     * @param second the second lock.
     */
    public static void lock(@NotNull final Lockable first, @NotNull final Lock second) {
        first.lock();
        second.lock();
    }

    /**
     * Lock two lockable objects.
     *
     * @param first  the first lockable object.
     * @param second the second lockable object.
     */
    public static void lock(@NotNull final Lockable first, @NotNull final Lockable second) {
        first.lock();
        second.lock();
    }

    /**
     * Lock two comparable and lockable objects.
     *
     * @param <T>    the type parameter
     * @param first  the first lockable object.
     * @param second the second lockable object.
     */
    public static <T extends Comparable<T> & Lockable> void lock(@NotNull final T first, @NotNull final T second) {

        final int result = first.compareTo(second);

        if (result == 0 || result == -1) {
            first.lock();
            second.lock();
        } else {
            second.lock();
            first.lock();
        }
    }

    /**
     * Unlock two locks.
     *
     * @param first  the first lock.
     * @param second the second lock.
     */
    public static void unlock(@NotNull final Lock first, @NotNull final Lock second) {
        first.unlock();
        second.unlock();
    }

    /**
     * Unlock two lockable objects.
     *
     * @param first  the first lockable object.
     * @param second the second lockable object.
     */
    public static void unlock(@NotNull final Lockable first, @NotNull final Lockable second) {
        first.unlock();
        second.unlock();
    }

    /**
     * Unlock two comparable and lockable objects.
     *
     * @param <T>    the type parameter
     * @param first  the first lockable object.
     * @param second the second lockable object.
     */
    public static <T extends Comparable<T> & Lockable> void unlock(@NotNull final T first, @NotNull final T second) {

        final int result = first.compareTo(second);

        if (result == 0 || result == -1) {
            second.unlock();
            first.unlock();
        } else {
            first.unlock();
            second.unlock();
        }
    }
}
