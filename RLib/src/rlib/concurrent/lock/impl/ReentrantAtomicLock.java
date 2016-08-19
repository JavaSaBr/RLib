package rlib.concurrent.lock.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.atomic.AtomicReference;
import rlib.concurrent.util.ThreadUtils;

/**
 * Реализация примитивного блокировщика при помощи {@link AtomicInteger} но с поддержкой рекурсивной
 * блокировки. Рекамендуется приминется в местах с не более чем средней конкурнции и с короткими
 * секциями блокировки.
 *
 * @author JavaSaBr
 */
public final class ReentrantAtomicLock implements Lock {

    /**
     * Статус блокировки.
     */
    @sun.misc.Contended("lock")
    private final AtomicReference<Thread> status;

    /**
     * Уровень повторного вхождения.
     */
    @sun.misc.Contended("lock")
    private final AtomicInteger level;

    /**
     * Поле для слива результата временных вычислений.
     */
    private int sink;

    public ReentrantAtomicLock() {
        this.status = new AtomicReference<>();
        this.level = new AtomicInteger();
        this.sink = 1;
    }

    /**
     * @return статус блокировки.
     */
    private AtomicReference<Thread> getStatus() {
        return status;
    }

    @Override
    public void lock() {

        final Thread thread = Thread.currentThread();
        final AtomicReference<Thread> status = getStatus();

        try {

            if (status.get() == thread) return;
            while (!status.compareAndSet(null, thread)) {

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

        } finally {
            level.incrementAndGet();
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new RuntimeException("not supperted.");
    }

    @Override
    public Condition newCondition() {
        throw new RuntimeException("not supperted.");
    }

    @Override
    public boolean tryLock() {

        final Thread currentThread = Thread.currentThread();
        final AtomicReference<Thread> status = getStatus();

        if (status.get() == currentThread) {
            level.incrementAndGet();
            return true;
        }

        if (status.compareAndSet(null, currentThread)) {
            level.incrementAndGet();
            return true;
        }

        return false;
    }

    @Override
    public boolean tryLock(final long time, final TimeUnit unit) throws InterruptedException {

        final Thread currentThread = Thread.currentThread();
        final AtomicReference<Thread> status = getStatus();

        if (status.get() == currentThread) {
            level.incrementAndGet();
            return true;
        }

        if (status.compareAndSet(null, currentThread)) {
            level.incrementAndGet();
            return true;
        }

        final long resultTime = unit.toMillis(time);

        if (resultTime > 1) {
            ThreadUtils.sleep(resultTime);
        }

        if (status.compareAndSet(null, currentThread)) {
            level.incrementAndGet();
            return true;
        }

        return false;
    }

    @Override
    public void unlock() {

        final Thread thread = Thread.currentThread();
        final AtomicReference<Thread> status = getStatus();
        if (status.get() != thread) return;

        if (level.decrementAndGet() == 0) {
            status.set(null);
        }
    }
}
