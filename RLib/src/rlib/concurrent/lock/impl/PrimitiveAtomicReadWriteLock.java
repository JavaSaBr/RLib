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
 * @author Ronn
 */
@SuppressWarnings("restriction")
public final class PrimitiveAtomicReadWriteLock implements AsyncReadSyncWriteLock, Lock {

    public static final int STATUS_WRITE_COUNT_LOCKED = -1;
    public static final int STATUS_NOT_WRITE = 0;
    public static final int STATUS_WRITE_LOCKED = 1;
    public static final int STATUS_WRITE_UNLOCKED = 0;

    public static final int STATUS_READ_UNLOCKED = 0;
    public static final int STATUS_READ_LOCKED = -200000;
    public static final int STATUS_READ_POST_INCREMENT_LOCKED = STATUS_READ_LOCKED + 1;

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
     * Простой счетчик для выполнения простых операций в цикле CAS.
     */
    private int counter;

    public PrimitiveAtomicReadWriteLock() {
        this.writeCount = new AtomicInteger(0);
        this.writeStatus = new AtomicInteger(0);
        this.readCount = new AtomicInteger(0);
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
     * @return получение и инкрементирования счетчика.
     */
    private int getAndIncrementCounter() {
        return counter++;
    }

    /**
     * @return кол-во читающих потоков.
     */
    protected AtomicInteger getReadCount() {
        return readCount;
    }

    /**
     * @return кол-во ожидающих потоков на запись.
     */
    protected AtomicInteger getWriteCount() {
        return writeCount;
    }

    /**
     * @return статус блокировки на запись.
     */
    protected AtomicInteger getWriteStatus() {
        return writeStatus;
    }

    /**
     * Выполнение локальных вычислений для занятия потока.
     */
    protected void localCalculate() {

        final int currentCounter = getAndIncrementCounter();
        int newValue = currentCounter ^ currentCounter;

        newValue = currentCounter >>> 1;
        newValue = currentCounter & newValue;
        newValue = currentCounter >>> 1;
        newValue = currentCounter & newValue;
        newValue = currentCounter >>> 1;
        newValue = currentCounter & newValue;

        setCounter(newValue);
        setCounter(currentCounter);
    }

    @Override
    public void lock() {
        syncLock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new RuntimeException("not supported.");
    }

    @Override
    public Condition newCondition() {
        throw new RuntimeException("not supported.");
    }

    /**
     * Обновление счетчика.
     */
    public void setCounter(final int counter) {
        this.counter = counter;
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
        throw new RuntimeException("not supported.");
    }

    @Override
    public boolean tryLock(final long time, final TimeUnit unit) throws InterruptedException {
        throw new RuntimeException("not supported.");
    }

    /**
     * Попытка добавиться к читающим потокам.
     */
    private boolean tryReadLock() {

        // проверем отсутствия желающих записать потоков
        final AtomicInteger writeCount = getWriteCount();

        if (writeCount.get() != 0) {
            return false;
        }

        final AtomicInteger readCount = getReadCount();

        // добавляемся к читающим потокам и если после добавления оказалось что
        // мы добавились после начало записи, возвращаем в исходную позицию
        // счетчик и пробуем еще раз.
        final int value = readCount.get();

        if (value == STATUS_READ_LOCKED) {
            return false;
        }

        return readCount.compareAndSet(value, value + 1);
    }

    @Override
    public void unlock() {
        syncUnlock();
    }
}
