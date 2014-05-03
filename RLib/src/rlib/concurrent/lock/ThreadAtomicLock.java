package rlib.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.atomic.AtomicReference;

/**
 * Потокая реализация блокировщика на основе атомика.
 * 
 * @author Ronn
 */
public final class ThreadAtomicLock implements Lock {

	/** статус блокировки */
	private final AtomicReference<Thread> status;

	/** уровень повторного вхождения */
	private final AtomicInteger level;

	protected ThreadAtomicLock() {
		this.status = new AtomicReference<>();
		this.level = new AtomicInteger();
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

			if(status.get() == thread) {
				return;
			}

			while(!status.compareAndSet(null, thread)) {
				;
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
		throw new RuntimeException("not supperted.");
	}

	@Override
	public boolean tryLock(final long time, final TimeUnit unit) throws InterruptedException {
		throw new RuntimeException("not supperted.");
	}

	@Override
	public void unlock() {

		final Thread thread = Thread.currentThread();
		final AtomicReference<Thread> status = getStatus();

		if(status.get() != thread) {
			return;
		}

		if(level.decrementAndGet() == 0) {
			status.getAndSet(null);
		}
	}
}
