package rlib.concurrent.sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Удобная обертка над асинхронно читающего и синхронно записывающего блокера.
 * 
 * @author Ronn
 */
public final class SimpleReadWriteLock implements AsynReadSynWriteLock {

	/** блокировщик записи */
	private final Lock readLock;
	/** блокировщик чтения */
	private final Lock writeLock;

	public SimpleReadWriteLock() {
		final ReadWriteLock readWriteLock = LockFactory.newRWLock();
		readLock = readWriteLock.readLock();
		writeLock = readWriteLock.writeLock();
	}

	@Override
	public void asynLock() {
		readLock.lock();
	}

	@Override
	public void asynUnlock() {
		readLock.unlock();
	}

	@Override
	public void synLock() {
		writeLock.lock();
	}

	@Override
	public void synUnlock() {
		writeLock.unlock();
	}
}
