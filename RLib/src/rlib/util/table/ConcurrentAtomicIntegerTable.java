package rlib.util.table;

import rlib.concurrent.lock.AsynReadSynWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * Реализация конкурентной таблицы с использованием атомарного синхронизатора
 * для таблиц с примитивным int ключом.
 *
 * @author Ronn
 */
public class ConcurrentAtomicIntegerTable<V> extends ConcurrentIntegerTable<V> {

	public ConcurrentAtomicIntegerTable() {
		super();
	}

	public ConcurrentAtomicIntegerTable(float loadFactor, int initCapacity) {
		super(loadFactor, initCapacity);
	}

	public ConcurrentAtomicIntegerTable(float loadFactor) {
		super(loadFactor);
	}

	public ConcurrentAtomicIntegerTable(int initCapacity) {
		super(initCapacity);
	}

	@Override
	protected AsynReadSynWriteLock createLocker() {
		return LockFactory.newPrimitiveAtomicARSWLock();
	}
}
