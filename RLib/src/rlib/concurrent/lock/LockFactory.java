package rlib.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.impl.FinalLock;
import rlib.concurrent.lock.impl.FinalReadWriteLock;
import rlib.concurrent.lock.impl.PrimitiveAtomicLock;
import rlib.concurrent.lock.impl.PrimitiveAtomicReadWriteLock;
import rlib.concurrent.lock.impl.ReentrantAtomicLock;
import rlib.concurrent.lock.impl.SimpleReadWriteLock;

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
	 * на основе {@link AtomicInteger} без поддержки рекурсивного блокирования.
	 * 
	 * @see PrimitiveAtomicReadWriteLock
	 * @return новый блокировщик.
	 */
	public static final AsynReadSynWriteLock newPrimitiveAtomicARSWLock() {
		return new PrimitiveAtomicReadWriteLock();
	}

	/**
	 * Создание примитивного блокировщика на основе {@link AtomicInteger} без
	 * поддержки рекурсивной блокировки.
	 * 
	 * @see PrimitiveAtomicLock
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
	 * Создание блокировщика на {@link AtomicInteger} с поддержкой рекурсивной
	 * блокировки.
	 * 
	 * @see ReentrantAtomicLock
	 * @return новый блокировщик.
	 */
	public static final Lock newReentrantAtomicLock() {
		return new ReentrantAtomicLock();
	}
}
