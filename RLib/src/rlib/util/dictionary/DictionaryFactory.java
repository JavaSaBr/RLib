package rlib.util.dictionary;

/**
 * Фабрика по созаднию словарей.
 * 
 * @author Ronn
 */
public final class DictionaryFactory {

	/**
	 * @return новый {@link ConcurrentIntegerDictionary}.
	 */
	public static final <V> IntegerDictionary<V> newConcurrentIntegerDictionary() {
		return new ConcurrentIntegerDictionary<V>();
	}

	/**
	 * @return новый {@link ConcurrentAtomicIntegerDictionary}.
	 */
	public static final <V> IntegerDictionary<V> newConcurrentAtomicIntegerDictionary() {
		return new ConcurrentAtomicIntegerDictionary<V>();
	}

	/**
	 * @return новый {@link ConcurrentLongDictionary}.
	 */
	public static final <V> LongDictionary<V> newConcurrentLongDictionary() {
		return new ConcurrentLongDictionary<V>();
	}

	/**
	 * @return новый {@link ConcurrentLongDictionary}.
	 */
	public static final <V> LongDictionary<V> newConcurrentAtomicLongDictionary() {
		return new ConcurrentAtomicLongDictionary<V>();
	}

	/**
	 * @return новый {@link ConcurrentObjectDictionary}.
	 */
	public static final <K, V> ObjectDictionary<K, V> newConcurrentObjectDictionary() {
		return new ConcurrentObjectDictionary<K, V>();
	}

	/**
	 * @return новый {@link ConcurrentAtomicObjectDictionary}.
	 */
	public static final <K, V> ObjectDictionary<K, V> newConcurrentAtomicObjectDictionary() {
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
