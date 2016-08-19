package rlib.concurrent.lock.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.AsyncReadSyncWriteLock;

/**
 * Реализация блокировщика с возможность асинхронного чтения и синхронной записи на основе атомиков,
 * без возможности использования вложенных вызовов с приоритетом на запись. <p> Выгодно использовать
 * посравнению с ReentrantReadWriteLock в случае, когда записи намного реже чтения и в случае если
 * необходимо уменьшить нагрузку на GC. <p> Смысл этой реализации в создании легковесного
 * блокировщика, который для синхронизации не создает временных объектов.
 *
 * @author JavaSaBr
 */
public final class PrimitiveAtomicReadWriteLock implements AsyncReadSyncWriteLock, Lock {

    private static final int STATUS_WRITE_LOCKED = 1;
    private static final int STATUS_WRITE_UNLOCKED = 0;

    private static final int STATUS_READ_UNLOCKED = 0;
    private static final int STATUS_READ_LOCKED = -200000;

    /**
     * Статус блокировки на запись.
     */
    @sun.misc.Contended("status")
    private final AtomicInteger writeStatus;

    /**
     * Кол-во ожидающих потоков на запись.
     */
    @sun.misc.Contended("status")
    private final AtomicInteger writeCount;

    /**
     * Кол-во читающих потоков.
     */
    @sun.misc.Contended("status")
    private final AtomicInteger readCount;

    /**
     * Поле для слива результата временных вычислений.
     */
    private int sink;

    public PrimitiveAtomicReadWriteLock() {
        this.writeCount = new AtomicInteger(0);
        this.writeStatus = new AtomicInteger(0);
        this.readCount = new AtomicInteger(0);
        this.sink = 1;
    }

    @Override
    public void asyncLock() {
        while (!tryReadLock()) {
            localCalculate();
        }
    }

    @Override
    public void asyncUnlock() {
        readCount.decrementAndGet();
    }

    /**
     * @return кол-во читающих потоков.
     */
    private AtomicInteger getReadCount() {
        return readCount;
    }

    /**
     * @return кол-во ожидающих потоков на запись.
     */
    private AtomicInteger getWriteCount() {
        return writeCount;
    }

    /**
     * @return статус блокировки на запись.
     */
    private AtomicInteger getWriteStatus() {
        return writeStatus;
    }

    /**
     * Выполнение локальных вычислений для занятия потока.
     */
    private void localCalculate() {

        final int value = sink;
        int newValue = value * value;
        newValue = value >>> 1;
        newValue = value & newValue;
        newValue = value >>> 1;
        newValue = value & newValue;
        newValue = value >>> 1;
        newValue = value & newValue;

        sink = newValue;
    }

    @Override
    public void lock() {
        syncLock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void syncLock() {

        // добавляемся в счетчик ожидающих записи потоков
        final AtomicInteger writeCount = getWriteCount();
        writeCount.incrementAndGet();

        // ждем завершения чтения другими потоками потоков и блокируем чтение
        final AtomicInteger readCount = getReadCount();
        while (readCount.get() != STATUS_READ_UNLOCKED || !readCount.compareAndSet(STATUS_READ_UNLOCKED, STATUS_READ_LOCKED)) {
            localCalculate();
        }

        // помечаем блокировку на процесс записи этим потоком
        final AtomicInteger writeStatus = getWriteStatus();
        while (writeStatus.get() != STATUS_WRITE_UNLOCKED || !writeStatus.compareAndSet(STATUS_WRITE_UNLOCKED, STATUS_WRITE_LOCKED)) {
            localCalculate();
        }
    }

    @Override
    public void syncUnlock() {

        // помечаем завершение записи этим потоком
        final AtomicInteger writeStatus = getWriteStatus();
        writeStatus.set(STATUS_WRITE_UNLOCKED);

        // разблокируем возможность читать другими потоками
        final AtomicInteger readCount = getReadCount();
        readCount.set(STATUS_READ_UNLOCKED);

        // отмечаемся из счетчика ожидающих запись потоков
        final AtomicInteger writeCount = getWriteCount();
        writeCount.decrementAndGet();
    }

    @Override
    public boolean tryLock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock(final long time, final TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    /**
     * Попытка добавиться к читающим потокам.
     */
    private boolean tryReadLock() {

        // проверем отсутствия желающих записать потоков
        final AtomicInteger writeCount = getWriteCount();
        if (writeCount.get() != 0) return false;

        final AtomicInteger readCount = getReadCount();

        // добавляемся к читающим потокам и если после добавления оказалось что
        // мы добавились после начало записи, возвращаем в исходную позицию
        // счетчик и пробуем еще раз.
        final int value = readCount.get();
        return value != STATUS_READ_LOCKED && readCount.compareAndSet(value, value + 1);
    }

    @Override
    public void unlock() {
        syncUnlock();
    }
}
