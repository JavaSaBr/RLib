package rlib.util.dictionary;

/**
 * Реализация быстрого словаря с примитивным ключем long.
 * 
 * @author Ronn
 */
public class FastLongDictionary<V> extends AbstractLongDictionary<V> {

	/** кол-во элементов в словаре */
	private int size;

	protected FastLongDictionary() {
		this(Dictionary.DEFAULT_LOAD_FACTOR, Dictionary.DEFAULT_INITIAL_CAPACITY);
	}

	protected FastLongDictionary(final float loadFactor) {
		this(loadFactor, Dictionary.DEFAULT_INITIAL_CAPACITY);
	}

	protected FastLongDictionary(final float loadFactor, final int initCapacity) {
		super(loadFactor, initCapacity);
		this.size = 0;
	}

	protected FastLongDictionary(final int initCapacity) {
		this(Dictionary.DEFAULT_LOAD_FACTOR, initCapacity);
	}

	@Override
	public final void clear() {
		super.clear();
		size = 0;
	}

	@Override
	public final int size() {
		return size;
	}

	@Override
	protected int incrementSizeAndGet() {
		return ++size;
	}

	@Override
	protected int decrementSizeAndGet() {
		return --size;
	}
}
