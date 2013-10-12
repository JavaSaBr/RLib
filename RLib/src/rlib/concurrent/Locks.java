package rlib.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import rlib.util.Synchronized;

/**
 * Класс для создание блокировщиков.
 * 
 * @author Ronn
 */
public abstract class Locks {

	/**
	 * Заблокировать 2 объекта.
	 * 
	 * @param first первый блокированный объект.
	 * @param second второй блокированный объект.
	 */
	public static void lock(final Synchronized first, final Lock second) {
		first.lock();
		second.lock();
	}

	/**
	 * Заблокировать 2 объекта.
	 * 
	 * @param first первый блокированный объект.
	 * @param second второй блокированный объект.
	 */
	public static void lock(final Synchronized first, final Synchronized second) {
		first.lock();
		second.lock();
	}

	/**
	 * @return новый блокировщик.
	 */
	public static final AsynReadSynWriteLock newARSWLock() {
		return new SimpleReadWriteLock();
	}

	/**
	 * @return новый блокировщик.
	 */
	public static final Lock newLock() {
		return new FinalLock();
	}

	/**
	 * @return новый блокировщик.
	 */
	public static final ReadWriteLock newRWLock() {
		return new FinalReadWriteLock();
	}

	/**
	 * Разблокировать 2 объекта.
	 * 
	 * @param first первый блокированный объект.
	 * @param second второй блокированный объект.
	 */
	public static void unlock(final Synchronized first, final Lock second) {
		first.unlock();
		second.unlock();
	}

	/**
	 * Разблокировать 2 объекта.
	 * 
	 * @param first первый блокированный объект.
	 * @param second второй блокированный объект.
	 */
	public static void unlock(final Synchronized first, final Synchronized second) {
		first.unlock();
		second.unlock();
	}
}
