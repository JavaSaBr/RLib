package rlib.util;

import java.util.concurrent.locks.Lock;

import rlib.concurrent.lock.LockFactory;

/**
 * @author Ronn
 */
public final class Synchronizer implements Synchronized {

	/** блокировщик */
	private final Lock sync;

	/** флаг блокировки */
	public volatile boolean locked;

	public Synchronizer() {
		this.sync = LockFactory.newLock();
	}

	/**
	 * @return the locked
	 */
	public final boolean isLocked() {
		return locked;
	}

	@Override
	public void lock() {
		sync.lock();
	}

	/**
	 * @param locked the locked to set
	 */
	public final void setLocked(final boolean locked) {
		this.locked = locked;
	}

	@Override
	public void unlock() {
		sync.unlock();
	}
}
