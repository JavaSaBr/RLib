package rlib.concurrent.lock.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.atomic.AtomicReference;
import rlib.concurrent.util.ThreadUtils;

/**
 * Реализация примитивного блокировщика при помощи {@link AtomicInteger} но с
 * поддержкой рекурсивной блокировки. Рекамендуется приминется в местах с не
 * более чем средней конкурнции и с короткими секциями блокировки.
 * 
 * @author Ronn
 */
@SuppressWarnings("restriction")
public final class ReentrantAtomicLock implements Lock {

	/** статус блокировки */
	@sun.misc.Contended("lock")
	private final AtomicReference<Thread> status;
	/** уровень повторного вхождения */
	@sun.misc.Contended("lock")
	private final AtomicInteger level;

	/** простой счетчик для выполнения простых операций в цикле CAS */
	private int counter;

	public ReentrantAtomicLock() {
		this.status = new AtomicReference<>();
		this.level = new AtomicInteger();
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

	/**
	 * Обновление счетчика.
	 */
	public void setCounter(final int counter) {
		this.counter = counter;
	}

	@Override
	public boolean tryLock() {

		final Thread currentThread = Thread.currentThread();
		final AtomicReference<Thread> status = getStatus();

		if(status.get() == currentThread) {
			level.incrementAndGet();
			return true;
		}

		if(status.compareAndSet(null, currentThread)) {
			level.incrementAndGet();
			return true;
		}

		return false;
	}

	@Override
	public boolean tryLock(final long time, final TimeUnit unit) throws InterruptedException {

		final Thread currentThread = Thread.currentThread();
		final AtomicReference<Thread> status = getStatus();

		if(status.get() == currentThread) {
			level.incrementAndGet();
			return true;
		}

		if(status.compareAndSet(null, currentThread)) {
			level.incrementAndGet();
			return true;
		}

		final long resultTime = unit.toMillis(time);

		if(resultTime > 1) {
			ThreadUtils.sleep(resultTime);
		}

		if(status.compareAndSet(null, currentThread)) {
			level.incrementAndGet();
			return true;
		}

		return false;
	}

	@Override
	public void unlock() {

		final Thread thread = Thread.currentThread();
		final AtomicReference<Thread> status = getStatus();

		if(status.get() != thread) {
			return;
		}

		if(level.decrementAndGet() == 0) {
			status.set(null);
		}
	}
}
