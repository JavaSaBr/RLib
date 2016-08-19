package rlib.util.dictionary;

/**
 * Реализация словаря с использованием примитивного int ключа.
 *
 * @author JavaSaBr
 */
public class FastIntegerDictionary<V> extends AbstractIntegerDictionary<V> implements UnsafeIntegerDictionary<V> {

    /**
     * Кол-во элементов в словаре.
     */
    private int size;

    protected FastIntegerDictionary() {
        this(Dictionary.DEFAULT_LOAD_FACTOR, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected FastIntegerDictionary(final float loadFactor) {
        this(loadFactor, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected FastIntegerDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
        this.size = 0;
    }

    protected FastIntegerDictionary(final int initCapacity) {
        this(Dictionary.DEFAULT_LOAD_FACTOR, initCapacity);
    }

    @Override
    public final void clear() {
        super.clear();
        size = 0;
    }

    @Override
    protected int decrementSizeAndGet() {
        return --size;
    }

    @Override
    protected int incrementSizeAndGet() {
        return ++size;
    }

    @Override
    public final int size() {
        return size;
    }
}
