package rlib.concurrent.lock.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.util.ThreadUtils;

/**
 * Реализация примитивного блокировщика при помощи {@link AtomicInteger} без поддержки рекурсивной
 * блокировки. Рекамендуется приминется в местах с не более чем средней конкурнции и с короткими
 * секциями блокировки.
 *
 * @author Ronn
 */
@SuppressWarnings("restriction")
public final class PrimitiveAtomicLock implements Lock {

    public static final int STATUS_LOCKED = 1;
    public static final int STATUS_UNLOCKED = 0;

    /**
     * Статус блокировки.
     */
    @sun.misc.Contended
    private final AtomicInteger status;

    /**
     * Простой счетчик для выполнения простых операций в цикле CAS.
     */
    private int counter;

    public PrimitiveAtomicLock() {
        this.status = new AtomicInteger();
    }

    /**
     * @return получение и инкрементирования счетчика.
     */
    private int getAndIncrementCounter() {
        return counter++;
    }

    /**
     * @return статус блокировки.
     */
    private AtomicInteger getStatus() {
        return status;
    }

    @Override
    public void lock() {

        final AtomicInteger status = getStatus();

        while (!status.compareAndSet(STATUS_UNLOCKED, STATUS_LOCKED)) {

            // выполняем пачку элементарных бессмысленных операций для
            // обеспечения интервала между проверками
            final int currentCounter = getAndIncrementCounter();
            int newValue = currentCounter ^ currentCounter;

            newValue = currentCounter >>> 1;
            newValue = currentCounter & newValue;
            newValue = currentCounter ^ newValue;
            newValue = newValue << currentCounter;
            newValue = newValue | currentCounter;

            setCounter(newValue);
        }
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
    public boolean tryLock() {
        return status.compareAndSet(STATUS_UNLOCKED, STATUS_LOCKED);
    }

    @Override
    public boolean tryLock(final long time, final TimeUnit unit) throws InterruptedException {

        final AtomicInteger status = getStatus();

        if (status.compareAndSet(STATUS_UNLOCKED, STATUS_LOCKED)) {
            return true;
        }

        final long resultTime = unit.toMillis(time);

        if (resultTime > 1) {
            ThreadUtils.sleep(resultTime);
        }

        return status.compareAndSet(STATUS_UNLOCKED, STATUS_LOCKED);
    }

    @Override
    public void unlock() {
        final AtomicInteger status = getStatus();
        status.set(STATUS_UNLOCKED);
    }
}
