package rlib.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
    public <L extends Lockable> void runInLock(final L lockable, final Consumer<L> consumer) {
        if (lockable == null) return;
        consumer.accept(lockable);
    }

    /**
     * Выполнить какие-то действия над блокуемым объектом в блоке.
     *
     * @param lockable объект который надо заблокировать и выполнить какие-то действия.
     * @param argument дополнительный аргумент.
     * @param consumer фунция с дейсвтиями над объектом.
     */
    public <L extends Lockable, F> void runInLock(final L lockable, final F argument, BiConsumer<L, F> consumer) {
        if (lockable == null) return;
        consumer.accept(lockable, argument);
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
