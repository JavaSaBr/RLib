package rlib.util;

import java.util.concurrent.locks.Lock;

import rlib.concurrent.lock.LockFactory;

/**
 * @author JavaSaBr
 */
public final class Synchronizer implements Lockable {

    /**
     * Блокировщик.
     */
    private final Lock sync;

    /**
     * Флаг блокировки.
     */
    public volatile boolean locked;

    public Synchronizer() {
        this.sync = LockFactory.newReentrantLock();
    }

    /**
     * @return the locked
     */
    public final boolean isLocked() {
        return locked;
    }

    /**
     * @param locked the locked to set
     */
    public final void setLocked(final boolean locked) {
        this.locked = locked;
    }

    @Override
    public void lock() {
        sync.lock();
    }

    @Override
    public void unlock() {
        sync.unlock();
    }
}
