package rlib.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Реализация фабрики для создание различных блокировщиков.
 * 
 * @author Ronn
 */
public class LockFactory {

	/**
	 * Создание обернутого блокировщика ReentrantReadWriteLock для синхронной
	 * записи и асинхронного чтения.
	 * 
	 * @return новый блокировщик.
	 */
	public static final AsynReadSynWriteLock newARSWLock() {
		return new SimpleReadWriteLock();
	}

	/**
	 * Создание финализированного наследника ReentrantLock.
	 * 
	 * @return новый блокировщик.
	 */
	public static final Lock newLock() {
		return new FinalLock();
	}

	/**
	 * Создание нового блокировщика для синхронной записи и асинхронного чтения
	 * на основе Atomic без поддержки рекурсивного блокирования.
	 * 
	 * @return новый блокировщик.
	 */
	public static final AsynReadSynWriteLock newPrimitiveAtomicARSWLock() {
		return new PrimitiveAtomicReadWriteLock();
	}

	/**
	 * Создание примитивного блокировщика на основе Atomic без поддержки
	 * рекурсивной блокировки.
	 * 
	 * @return новый блокировщик.
	 */
	public static final Lock newPrimitiveAtomicLock() {
		return new PrimitiveAtomicLock();
	}

	/**
	 * Создание финализированного наследника ReentrantReadWriteLock.
	 * 
	 * @return новый блокировщик.
	 */
	public static final ReadWriteLock newRWLock() {
		return new FinalReadWriteLock();
	}

	/**
	 * Создание простой реализации блокировщика на основе Atomic с поддержкой
	 * рекурсивной блокировки.
	 * 
	 * @return новый блокировщик.
	 */
	public static final Lock newThreadAtomicLock() {
		return new ThreadAtomicLock();
	}
}
