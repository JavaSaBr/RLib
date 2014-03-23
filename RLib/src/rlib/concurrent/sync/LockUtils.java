package rlib.concurrent.sync;

import java.util.concurrent.locks.Lock;

import rlib.util.Synchronized;

/**
 * Утильный класс для работы с блокерами.
 * 
 * @author Ronn
 */
public class LockUtils {

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
