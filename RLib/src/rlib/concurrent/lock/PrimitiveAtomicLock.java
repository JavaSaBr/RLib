package rlib.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.atomic.AtomicInteger;

/**
 * Примитвная реализация блокировщика на основе атомика.
 * 
 * @author Ronn
 */
public final class PrimitiveAtomicLock implements Lock {

	public static final int STATUS_LOCKED = 1;
	public static final int STATUS_UNLOCKED = 0;

	/** статус блокировки */
	private final AtomicInteger status;

	protected PrimitiveAtomicLock() {
		this.status = new AtomicInteger();
	}

	/**
	 * @return статус блокировки.
	 */
	private AtomicInteger getStatus() {
		return status;
	}

	@Override
	public void lock() {
		AtomicInteger status = getStatus();
		while(!status.compareAndSet(STATUS_UNLOCKED, STATUS_LOCKED));
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		throw new RuntimeException("not supperted.");
	}

	@Override
	public boolean tryLock() {
		throw new RuntimeException("not supperted.");
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		throw new RuntimeException("not supperted.");
	}

	@Override
	public void unlock() {
		AtomicInteger status = getStatus();
		while(!status.compareAndSet(STATUS_LOCKED, STATUS_UNLOCKED));
	}

	@Override
	public Condition newCondition() {
		throw new RuntimeException("not supperted.");
	}
}
