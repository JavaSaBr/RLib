package rlib.concurrent.lock.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.atomic.AtomicInteger;

/**
 * Реализация примитивного блокировщика при помощи {@link AtomicInteger} без поддержки рекурсивной
 * блокировки. Рекамендуется приминется в местах с не более чем средней конкурнции и с короткими
 * секциями блокировки.
 *
 * @author Ronn
 */
public final class PrimitiveAtomicLock implements Lock {

    private static final int STATUS_LOCKED = 1;
    private static final int STATUS_UNLOCKED = 0;

    /**
     * Статус блокировки.
     */
    @sun.misc.Contended
    private final AtomicInteger status;

    /**
     * Поле для слива результата временных вычислений.
     */
    private int sink;

    public PrimitiveAtomicLock() {
        this.status = new AtomicInteger();
        this.sink = 1;
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
            final int value = sink;
            int newValue = value * value;
            newValue = value >>> 1;
            newValue = value & newValue;
            newValue = value ^ newValue;
            newValue = newValue << value;
            newValue = newValue | value;

            sink = newValue;
        }
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
    public boolean tryLock() {
        return status.compareAndSet(STATUS_UNLOCKED, STATUS_LOCKED);
    }

    @Override
    public boolean tryLock(final long time, final TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unlock() {
        final AtomicInteger status = getStatus();
        status.set(STATUS_UNLOCKED);
    }
}
