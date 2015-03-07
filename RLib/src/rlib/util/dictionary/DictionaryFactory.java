package rlib.util.dictionary;

/**
 * Фабрика по созаднию словарей.
 * 
 * @author Ronn
 */
public final class DictionaryFactory {

	/**
	 * @return новый {@link ConcurrentLockIntegerDictionary}.
	 */
	public static final <V> ConcurrentIntegerDictionary<V> newConcurrentIntegerDictionary() {
		return new ConcurrentLockIntegerDictionary<V>();
	}

	/**
	 * @return новый {@link ConcurrentAtomicIntegerDictionary}.
	 */
	public static final <V> ConcurrentIntegerDictionary<V> newConcurrentAtomicIntegerDictionary() {
		return new ConcurrentAtomicIntegerDictionary<V>();
	}

	/**
	 * @return новый {@link ConcurrentLockLongDictionary}.
	 */
	public static final <V> ConcurrentLongDictionary<V> newConcurrentLongDictionary() {
		return new ConcurrentLockLongDictionary<V>();
	}

	/**
	 * @return новый {@link ConcurrentLockLongDictionary}.
	 */
	public static final <V> ConcurrentLongDictionary<V> newConcurrentAtomicLongDictionary() {
		return new ConcurrentAtomicLongDictionary<V>();
	}

	/**
	 * @return новый {@link ConcurrentLockObjectDictionary}.
	 */
	public static final <K, V> ConcurrentObjectDictionary<K, V> newConcurrentObjectDictionary() {
		return new ConcurrentLockObjectDictionary<K, V>();
	}

	/**
	 * @return новый {@link ConcurrentAtomicObjectDictionary}.
	 */
	public static final <K, V> ConcurrentObjectDictionary<K, V> newConcurrentAtomicObjectDictionary() {
		return new ConcurrentAtomicObjectDictionary<K, V>();
	}

	/**
	 * @return новый {@link FastIntegerDictionary}.
	 */
	public static final <V> IntegerDictionary<V> newIntegerDictionary() {
		return new FastIntegerDictionary<V>();
	}

	/**
	 * @return новый {@link FastLongDictionary}.
	 */
	public static final <V> LongDictionary<V> newLongDictionary() {
		return new FastLongDictionary<V>();
	}

	/**
	 * @return новый {@link FastObjectDictionary}.
	 */
	public static final <K, V> ObjectDictionary<K, V> newObjectDictionary() {
		return new FastObjectDictionary<K, V>();
	}

	private DictionaryFactory() {
		throw new IllegalArgumentException();
	}
}
