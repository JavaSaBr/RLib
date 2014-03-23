package rlib.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Реализация фабрики для созжание различных блокировщиков.
 * 
 * @author Ronn
 */
public class LockFactory {

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
	 * Примитивный атомик блокер, не поддерживает рекурсивное использованиЕ,
	 * зато для примитивных блокировок он самый легковесный.
	 * 
	 * @return новый примитивный атомикб локер.
	 */
	public static final Lock newPrimitiveAtomicLoc() {
		return new PrimitiveAtomicLock();
	}

	/**
	 * Простоя реализация блокера на основе атомика и ссылки на поток с учетом
	 * повторных вызовов.
	 * 
	 * @return новый примитивный блокер.
	 */
	public static final Lock newThreadAtomicLock() {
		return new ThreadAtomicLock();
	}
}
