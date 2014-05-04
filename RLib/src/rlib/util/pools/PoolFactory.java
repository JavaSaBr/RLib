package rlib.util.pools;

/**
 * Фабрика пулов.
 * 
 * @author Ronn
 */
public final class PoolFactory {

	/**
	 * Создание нового потокобезопасного объектного пула.
	 * 
	 * @see AtomicFoldablePool
	 * @param type тип объектов пула.
	 * @return новый объектный пул.
	 */
	public static final <T extends Foldable> FoldablePool<T> newAtomicFoldablePool(final Class<? extends Foldable> type) {
		return new AtomicFoldablePool<T>(type);
	}

	/**
	 * Создание нового потокобезопасного объектного пула.
	 * 
	 * @see ConcurrentFoldablePool
	 * @param type тип объектов пула.
	 * @return новый объектный пул.
	 */
	public static final <T extends Foldable> FoldablePool<T> newConcurrentFoldablePool(final Class<? extends Foldable> type) {
		return new ConcurrentFoldablePool<T>(type);
	}

	/**
	 * Создание нового не потокобезопасного объектного пула.
	 * 
	 * @see FastFoldablePool
	 * @param type тип объектов пула.
	 * @return новый объектный пул.
	 */
	public static final <T extends Foldable> FoldablePool<T> newFoldablePool(final Class<? extends Foldable> type) {
		return new FastFoldablePool<T>(type);
	}

	/**
	 * Создание нового не потокобезопасного объектного пула.
	 * 
	 * @see FastPool
	 * @param type тип объектов пула.
	 * @return новый объектный пул.
	 */
	public static final <T> Pool<T> newPool(final Class<?> type) {
		return new FastPool<T>(type);
	}

	/**
	 * Создание нового потокобезопасного объектного пула.
	 * 
	 * @see SynchronizedFoldablePool
	 * @param type тип объектов пула.
	 * @return новый объектный пул.
	 */
	public static final <T extends Foldable> FoldablePool<T> newSynchronizedFoldablePool(final Class<? extends Foldable> type) {
		return new SynchronizedFoldablePool<T>(type);
	}

	private PoolFactory() {
		throw new IllegalArgumentException();
	}
}
