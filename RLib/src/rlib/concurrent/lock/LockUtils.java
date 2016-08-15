package rlib.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import rlib.function.FunctionInt;
import rlib.function.ObjectIntFunction;
import rlib.util.Lockable;

/**
 * Утильный класс для работы с блокерами.
 *
 * @author Ronn
 */
public class LockUtils {

    /**
     * Выполнить какие-то действия над блокуемым объектом в блоке.
     *
     * @param lockable объект который надо заблокировать и выполнить какие-то действия.
     * @param consumer фунция с дейсвтиями над объектом.
     */
    public static <L extends Lockable> void runInLock(final L lockable, final Consumer<L> consumer) {
        if (lockable == null) return;
        lockable.lock();
        try {
            consumer.accept(lockable);
        } finally {
            lockable.unlock();
        }
    }

    /**
     * Выполнить какие-то действия над блокуемым объектом в блоке.
     *
     * @param lockable объект который надо заблокировать и выполнить какие-то действия.
     * @param argument дополнительный аргумент.
     * @param consumer фунция с дейсвтиями над объектом.
     */
    public static <L extends Lockable, F> void runInLock(final L lockable, final F argument, BiConsumer<L, F> consumer) {
        if (lockable == null) return;
        lockable.lock();
        try {
            consumer.accept(lockable, argument);
        } finally {
            lockable.unlock();
        }
    }

    /**
     * Выполнить какие-то действия над блокуемым объектом в блокированном блоке с возвратом
     * результата.
     *
     * @param lockable объект который надо заблокировать и выполнить какие-то действия.
     * @param function фунция с дейсвтиями над объектом.
     * @return результат работы функции либо null.
     */
    public static <L extends Lockable, R> R getInLock(final L lockable, final Function<L, R> function) {
        if (lockable == null) return null;
        lockable.lock();
        try {
            return function.apply(lockable);
        } finally {
            lockable.unlock();
        }
    }

    /**
     * Выполнить какие-то действия над блокуемым объектом в блокированном блоке с возвратом
     * результата.
     *
     * @param lockable объект который надо заблокировать и выполнить какие-то действия.
     * @param function фунция с дейсвтиями над объектом.
     * @return результат работы функции либо -1.
     */
    public static <L extends Lockable> int getInLockInt(final L lockable, final FunctionInt<L> function) {
        if (lockable == null) return -1;
        lockable.lock();
        try {
            return function.apply(lockable);
        } finally {
            lockable.unlock();
        }
    }

    /**
     * Выполнить какие-то действия над блокуемым объектом в блокированном блоке с возвратом
     * результата.
     *
     * @param lockable объект который надо заблокировать и выполнить какие-то действия.
     * @param argument дополнительный аргумент.
     * @param function фунция с дейсвтиями над объектом.
     * @return результат работы функции либо null.
     */
    public static <L extends Lockable, F, R> R getInLock(final L lockable, final F argument, BiFunction<L, F, R> function) {
        if (lockable == null) return null;
        lockable.lock();
        try {
            return function.apply(lockable, argument);
        } finally {
            lockable.unlock();
        }
    }

    /**
     * Выполнить какие-то действия над блокуемым объектом в блокированном блоке с возвратом
     * результата.
     *
     * @param lockable объект который надо заблокировать и выполнить какие-то действия.
     * @param argument дополнительный аргумент.
     * @param function фунция с дейсвтиями над объектом.
     * @return результат работы функции либо null.
     */
    public static <L extends Lockable, R> R getInLock(final L lockable, final int argument, ObjectIntFunction<L, R> function) {
        if (lockable == null) return null;
        lockable.lock();
        try {
            return function.apply(lockable, argument);
        } finally {
            lockable.unlock();
        }
    }

    /**
     * Заблокировать 2 объекта.
     *
     * @param first  первый блокированный объект.
     * @param second второй блокированный объект.
     */
    public static void lock(final Lockable first, final Lock second) {
        first.lock();
        second.lock();
    }

    /**
     * Заблокировать 2 объекта.
     *
     * @param first  первый блокированный объект.
     * @param second второй блокированный объект.
     */
    public static void lock(final Lockable first, final Lockable second) {
        first.lock();
        second.lock();
    }

    /**
     * Разблокировать 2 объекта.
     *
     * @param first  первый блокированный объект.
     * @param second второй блокированный объект.
     */
    public static void unlock(final Lockable first, final Lock second) {
        first.unlock();
        second.unlock();
    }

    /**
     * Разблокировать 2 объекта.
     *
     * @param first  первый блокированный объект.
     * @param second второй блокированный объект.
     */
    public static void unlock(final Lockable first, final Lockable second) {
        first.unlock();
        second.unlock();
    }
}
