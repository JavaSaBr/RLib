package rlib.concurrent.lock.impl;

import org.jetbrains.annotations.NotNull;

import sun.misc.Contended;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.atomic.AtomicReference;

/**
 * The implementation of the {@link Lock} based on using {@link AtomicInteger} with supporting
 * reentrant calls.
 *
 * @author JavaSaBr
 */
public class ReentrantAtomicLock implements Lock {

    /**
     * The status of lock.
     */
    @Contended("lock")
    private final AtomicReference<Thread> status;

    /**
     * The level of locking.
     */
    @Contended("level")
    private final AtomicInteger level;

    /**
     * The field for consuming CPU.
     */
    private int sink;

    public ReentrantAtomicLock() {
        this.status = new AtomicReference<>();
        this.level = new AtomicInteger();
        this.sink = 1;
    }

    @Override
    public void lock() {

        final Thread thread = Thread.currentThread();

        try {
            if (status.get() == thread) return;
            while (!status.compareAndSet(null, thread)) consumeCPU();
        } finally {
            level.incrementAndGet();
        }
    }

    protected void consumeCPU() {

        final int value = sink;
        int newValue = value * value;
        newValue += value >>> 1;
        newValue += value & newValue;
        newValue += value ^ newValue;
        newValue += newValue << value;
        newValue += newValue | value;

        sink = newValue;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock() {

        final Thread currentThread = Thread.currentThread();

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
    public boolean tryLock(final long time, @NotNull final TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unlock() {

        final Thread thread = Thread.currentThread();
        if (status.get() != thread) return;

        if (level.decrementAndGet() == 0) {
            status.set(null);
        }
    }
}
