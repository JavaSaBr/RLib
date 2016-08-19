package rlib.util.dictionary;

/**
 * Реализация словаря с объектным ключем.
 *
 * @author JavaSaBr
 */
public class FastObjectDictionary<K, V> extends AbstractObjectDictionary<K, V> {

    /**
     * Кол-во элементов в таблице.
     */
    private int size;

    protected FastObjectDictionary() {
        this(Dictionary.DEFAULT_LOAD_FACTOR, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected FastObjectDictionary(final float loadFactor) {
        this(loadFactor, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected FastObjectDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
        this.size = 0;
    }

    protected FastObjectDictionary(final int initCapacity) {
        this(Dictionary.DEFAULT_LOAD_FACTOR, initCapacity);
    }

    @Override
    public void clear() {
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
    public int size() {
        return size;
    }
}
