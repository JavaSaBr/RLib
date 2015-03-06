package rlib.util.dictionary;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.AsynReadSynWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * Реализация конкурентнго словаря с примитивным ключем int, где миханизм
 * синхранизации выступает {@link ReentrantReadWriteLock}.
 *
 * @author Ronn
 */
public class ConcurrentIntegerDictionary<V> extends AbstractIntegerDictionary<V> implements ConcurrentDictionary<IntKey, V> {

	/** блокировщик */
	private final AsynReadSynWriteLock locker;
	/** кол-во элементов в словаре */
	private final AtomicInteger size;

	protected ConcurrentIntegerDictionary() {
		this(Dictionary.DEFAULT_LOAD_FACTOR, Dictionary.DEFAULT_INITIAL_CAPACITY);
	}

	protected ConcurrentIntegerDictionary(final float loadFactor) {
		this(loadFactor, Dictionary.DEFAULT_INITIAL_CAPACITY);
	}

	protected ConcurrentIntegerDictionary(final float loadFactor, final int initCapacity) {
		super(loadFactor, initCapacity);
		this.size = new AtomicInteger();
		this.locker = createLocker();
	}

	protected ConcurrentIntegerDictionary(final int initCapacity) {
		this(Dictionary.DEFAULT_LOAD_FACTOR, initCapacity);
	}

	@Override
	public void clear() {
		super.clear();
		size.set(0);
	}

	protected AsynReadSynWriteLock createLocker() {
		return LockFactory.newARSWLock();
	}

	@Override
	protected int decrementSizeAndGet() {
		return size.decrementAndGet();
	}

	@Override
	protected int incrementSizeAndGet() {
		return size.incrementAndGet();
	}

	@Override
	public void readLock() {
		locker.asynLock();
	}

	@Override
	public void readUnlock() {
		locker.asynUnlock();
	}

	@Override
	public final int size() {
		return size.get();
	}

	@Override
	public void writeLock() {
		locker.synLock();
	}

	@Override
	public void writeUnlock() {
		locker.synUnlock();
	}
}
