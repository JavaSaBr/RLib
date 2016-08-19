package rlib.concurrent.util;

import java.util.function.Function;

import rlib.function.ObjectIntFunction;
import rlib.function.ObjectLongFunction;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.Lockable;

/**
 * Набор утильных методов для работы в сфере кокнуренции.
 *
 * @author JavaSaBr
 */
public final class ConcurrentUtils {

    private static final Logger LOGGER = LoggerManager.getLogger(ConcurrentUtils.class);

    /**
     * Отпускание ожидающих потоков на этом объекте.
     */
    public static void notifyAll(final Object object) {
        synchronized (object) {
            object.notifyAll();
        }
    }

    /**
     * Отпускание ожидающих потоков на этом объекте.
     */
    public static void notifyAllInSynchronize(final Object object) {
        object.notifyAll();
    }

    /**
     * Отпускание ожидающих потоков на этом объекте и становится самому в ожидание.
     */
    public static void notifyAndWait(final Object object) {
        synchronized (object) {
            notifyAllInSynchronize(object);
            waitInSynchronize(object);
        }
    }

    /**
     * Ождивать на этом объекте.
     */
    public static void wait(final Object object) {
        synchronized (object) {
            try {
                object.wait();
            } catch (final InterruptedException e) {
                LOGGER.warning(e);
            }
        }
    }

    /**
     * Ожидать определнное время на этом объекте.
     */
    public static void wait(final Object object, final long time) {
        synchronized (object) {
            try {
                object.wait(time);
            } catch (final InterruptedException e) {
                LOGGER.warning(e);
            }
        }
    }

    /**
     * Ождивать на этом объекте.
     */
    public static void waitInSynchronize(final Object object) {
        try {
            object.wait();
        } catch (final InterruptedException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * Ождивать на этом объекте.
     */
    public static void waitInSynchronize(final Object object, long time) {
        try {
            object.wait(time);
        } catch (final InterruptedException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * Вытащить что-то из объекта в синхронизированном состоянии.
     *
     * @param sync     объект поддерживающий синхронизацию.
     * @param function функция вытаскивания результата.
     * @return результатработы функции.
     */
    public static <T extends Lockable, R> R getInSynchronized(final T sync, final Function<T, R> function) {
        sync.lock();
        try {
            return function.apply(sync);
        } finally {
            sync.unlock();
        }
    }

    /**
     * Вытащить что-то из объекта в синхронизированном состоянии.
     *
     * @param sync     объект поддерживающий синхронизацию.
     * @param argument дополнительный числовой аргумент.
     * @param function функция вытаскивания результата.
     * @return результатработы функции.
     */
    public static <T extends Lockable, R> R getInSynchronized(final T sync, final int argument, final ObjectIntFunction<T, R> function) {
        sync.lock();
        try {
            return function.apply(sync, argument);
        } finally {
            sync.unlock();
        }
    }

    /**
     * Вытащить что-то из объекта в синхронизированном состоянии.
     *
     * @param sync     объект поддерживающий синхронизацию.
     * @param argument дополнительный числовой аргумент.
     * @param function функция вытаскивания результата.
     * @return результатработы функции.
     */
    public static <T extends Lockable, R> R getInSynchronizedL(final T sync, final long argument, final ObjectLongFunction<T, R> function) {
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
