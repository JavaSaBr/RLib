package rlib.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.StampedLock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.impl.FinalLock;
import rlib.concurrent.lock.impl.FinalReadWriteLock;
import rlib.concurrent.lock.impl.FinalStampedLock;
import rlib.concurrent.lock.impl.PrimitiveAtomicLock;
import rlib.concurrent.lock.impl.PrimitiveAtomicReadWriteLock;
import rlib.concurrent.lock.impl.ReentrantAtomicLock;
import rlib.concurrent.lock.impl.SimpleReadWriteLock;

/**
 * Реализация фабрики для создание различных блокировщиков.
 *
 * @author JavaSaBr
 */
public class LockFactory {

    /**
     * Создание обернутого блокировщика ReentrantReadWriteLock для синхронной записи и асинхронного
     * чтения.
     *
     * @return новый блокировщик.
     */
    public static AsyncReadSyncWriteLock newARSWLock() {
        return new SimpleReadWriteLock();
    }

    /**
     * Creates new stamped lock.
     *
     * @return the new stamped lock.
     */
    public static StampedLock newStampedLock() {
        return new FinalStampedLock();
    }

    /**
     * Создание финализированного наследника ReentrantLock.
     *
     * @return новый блокировщик.
     */
    public static Lock newLock() {
        return new FinalLock();
    }

    /**
     * Создание нового блокировщика для синхронной записи и асинхронного чтения на основе {@link
     * AtomicInteger} без поддержки рекурсивного блокирования.
     *
     * @return новый блокировщик.
     * @see PrimitiveAtomicReadWriteLock
     */
    public static AsyncReadSyncWriteLock newPrimitiveAtomicARSWLock() {
        return new PrimitiveAtomicReadWriteLock();
    }

    /**
     * Создание примитивного блокировщика на основе {@link AtomicInteger} без поддержки рекурсивной
     * блокировки.
     *
     * @return новый блокировщик.
     * @see PrimitiveAtomicLock
     */
    public static Lock newPrimitiveAtomicLock() {
        return new PrimitiveAtomicLock();
    }

    /**
     * Создание финализированного наследника ReentrantReadWriteLock.
     *
     * @return новый блокировщик.
     */
    public static ReadWriteLock newRWLock() {
        return new FinalReadWriteLock();
    }

    /**
     * Создание блокировщика на {@link AtomicInteger} с поддержкой рекурсивной блокировки.
     *
     * @return новый блокировщик.
     * @see ReentrantAtomicLock
     */
    public static Lock newReentrantAtomicLock() {
        return new ReentrantAtomicLock();
    }
}
