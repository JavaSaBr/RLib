package rlib.util.dictionary;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.AsynReadSynWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * Реализация конкуретного словаря с объектным ключем, используя механизмом
 * синхронизации в виде {@link ReentrantReadWriteLock}.
 *
 * @author Ronn
 */
public class ConcurrentObjectDictionary<K, V> extends AbstractObjectDictionary<K, V> implements ConcurrentDictionary<K, V> {

	/** блокировщики */
	private final AsynReadSynWriteLock locker;
	/** кол-во элементов в таблице */
	private final AtomicInteger size;

	protected ConcurrentObjectDictionary() {
		this(Dictionary.DEFAULT_LOAD_FACTOR, Dictionary.DEFAULT_INITIAL_CAPACITY);
	}

	protected ConcurrentObjectDictionary(final float loadFactor) {
		this(loadFactor, Dictionary.DEFAULT_INITIAL_CAPACITY);
	}

	protected ConcurrentObjectDictionary(final float loadFactor, final int initCapacity) {
		this.size = new AtomicInteger();
		this.locker = createLocker();
	}

	protected ConcurrentObjectDictionary(final int initCapacity) {
		this(Dictionary.DEFAULT_LOAD_FACTOR, initCapacity);
	}

	@Override
	public final void clear() {
		super.clear();
		size.getAndSet(0);
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
	public final void readLock() {
		locker.asynLock();
	}

	@Override
	public final void readUnlock() {
		locker.asynUnlock();
	}

	@Override
	public final int size() {
		return size.get();
	}

	@Override
	public final void writeLock() {
		locker.synLock();
	}

	@Override
	public final void writeUnlock() {
		locker.synUnlock();
	}
}
