package rlib.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.util.ThreadUtils;

/**
 * Примитвная реализация блокировщика на основе атомика.
 * 
 * @author Ronn
 */
@SuppressWarnings("restriction")
public final class PrimitiveAtomicLock implements Lock {

	public static final int STATUS_LOCKED = 1;
	public static final int STATUS_UNLOCKED = 0;

	/** статус блокировки */
	@sun.misc.Contended
	private final AtomicInteger status;

	/** простой счетчик для выполнения простых операций в цикле CAS */
	private int counter;

	protected PrimitiveAtomicLock() {
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
		while(!status.compareAndSet(STATUS_UNLOCKED, STATUS_LOCKED)) {

			// выполняем пачку элементарных бессмысленных операций для
			// обеспечения интервала между проверками
			final int currentCounter = getAndIncrementCounter();
			int newValue = currentCounter ^ currentCounter;

			newValue = currentCounter >>> 1;
			newValue = currentCounter & newValue;

			setCounter(newValue);
			setCounter(currentCounter);
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
		if(status.compareAndSet(STATUS_UNLOCKED, STATUS_LOCKED)) {
			return true;
		} else {
			ThreadUtils.sleep(unit.toMillis(time));
			return status.compareAndSet(STATUS_UNLOCKED, STATUS_LOCKED);
		}
	}

	@Override
	public void unlock() {
		final AtomicInteger status = getStatus();
		status.set(STATUS_UNLOCKED);
	}
}
