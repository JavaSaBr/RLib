package rlib.concurrent.lock.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import rlib.concurrent.lock.AsynReadSynWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * Реализация обертки {@link ReentrantReadWriteLock} для реализации интерфейса
 * {@link AsynReadSynWriteLock}.
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
