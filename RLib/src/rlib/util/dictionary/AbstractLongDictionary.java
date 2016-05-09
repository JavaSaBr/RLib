package rlib.util.dictionary;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.LongArray;
import rlib.util.pools.ReusablePool;
import rlib.util.pools.PoolFactory;

/**
 * Базоваря реализация словаря с примитивным ключем long.
 *
 * @author Ronn
 */
public abstract class AbstractLongDictionary<V> extends AbstractDictionary<LongKey, V> implements UnsafeLongDictionary<V> {

    /**
     * Пул ячеяк.
     */
    private final ReusablePool<LongEntry<V>> entryPool;

    /**
     * Таблица элементов.
     */
    private LongEntry<V>[] table;

    /**
     * Следующий размер для метода изминения размера (capacity * load factor).
     */
    private int threshold;

    /**
     * Фактор загружеености.
     */
    private float loadFactor;

    protected AbstractLongDictionary() {
        this(Dictionary.DEFAULT_LOAD_FACTOR, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractLongDictionary(final float loadFactor) {
        this(loadFactor, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    protected AbstractLongDictionary(final float loadFactor, final int initCapacity) {
        this.loadFactor = loadFactor;
        this.threshold = (int) (initCapacity * loadFactor);
        this.table = new LongEntry[Dictionary.DEFAULT_INITIAL_CAPACITY];
        this.entryPool = PoolFactory.newFoldablePool(LongEntry.class);
    }

    protected AbstractLongDictionary(final int initCapacity) {
        this(Dictionary.DEFAULT_LOAD_FACTOR, initCapacity);
    }

    /**
     * Добавляет новую ячейку в таблицу.
     *
     * @param hash  хэш значение.
     * @param key   значение ключа.
     * @param value значение по ключу.
     * @param index индекс ячейки.
     */
    private final void addEntry(final int hash, final long key, final V value, final int index) {

        final ReusablePool<LongEntry<V>> entryPool = getEntryPool();

        final LongEntry<V>[] content = content();
        final LongEntry<V> entry = content[index];

        LongEntry<V> newEntry = entryPool.take();

        if (newEntry == null) {
            newEntry = new LongEntry<V>();
        }

        newEntry.set(hash, key, value, entry);

        content[index] = newEntry;

        if (incrementSizeAndGet() >= threshold) {
            resize(2 * content.length);
        }
    }

    @Override
    public void apply(final Function<? super V, V> function) {
        for (LongEntry<V> entry : content()) {
            while (entry != null) {
                entry.setValue(function.apply(entry.getValue()));
                entry = entry.getNext();
            }
        }
    }

    @Override
    public void clear() {

        final ReusablePool<LongEntry<V>> entryPool = getEntryPool();
        final LongEntry<V>[] content = content();

        LongEntry<V> next = null;

        for (LongEntry<V> entry : content) {
            while (entry != null) {
                next = entry.getNext();
                entryPool.put(entry);
                entry = next;
            }
        }

        ArrayUtils.clear(content);
    }

    @Override
    public final boolean containsKey(final long key) {
        return getEntry(key) != null;
    }

    @Override
    public final boolean containsValue(final V value) {

        if (value == null) {
            throw new NullPointerException("value is null.");
        }

        for (final LongEntry<V> element : content()) {
            for (LongEntry<V> entry = element; entry != null; entry = entry.getNext()) {
                if (value.equals(entry.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public LongEntry<V>[] content() {
        return table;
    }

    @Override
    public void forEach(final Consumer<? super V> consumer) {
        for (LongEntry<V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public final V get(final long key) {
        final LongEntry<V> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    /**
     * Получение ячейки по ключу.
     *
     * @param key ключ ячейки.
     * @return ячейка.
     */
    private final LongEntry<V> getEntry(final long key) {

        final int hash = hash(key);

        final LongEntry<V>[] table = content();

        for (LongEntry<V> entry = table[indexFor(hash, table.length)]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key == entry.getKey()) {
                return entry;
            }
        }

        return null;
    }

    private ReusablePool<LongEntry<V>> getEntryPool() {
        return entryPool;
    }

    @Override
    public DictionaryType getType() {
        return DictionaryType.LONG;
    }

    @Override
    public final Iterator<V> iterator() {
        return new LongDictionaryIterator<>(this);
    }

    @Override
    public LongArray keyLongArray(final LongArray container) {

        for (LongEntry<V> entry : content()) {
            while (entry != null) {
                container.add(entry.getKey());
                entry = entry.getNext();
            }
        }

        return container;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void moveTo(final Dictionary<? super LongKey, ? super V> dictionary) {

        if (isEmpty() || getType() != dictionary.getType()) {
            return;
        }

        final LongDictionary<V> longDictionary = (LongDictionary<V>) dictionary;

        super.moveTo(dictionary);

        for (LongEntry<V> entry : content()) {
            while (entry != null) {
                longDictionary.put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public final V put(final long key, final V value) {

        final LongEntry<V>[] content = content();

        final int hash = hash(key);
        final int i = indexFor(hash, content.length);

        for (LongEntry<V> entry = content[i]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key == entry.getKey()) {
                return entry.setValue(value);
            }
        }

        addEntry(hash, key, value, i);

        return null;
    }

    @Override
    public final V remove(final long key) {

        final LongEntry<V> old = removeEntryForKey(key);
        final V value = old == null ? null : old.getValue();

        final ReusablePool<LongEntry<V>> entryPool = getEntryPool();
        entryPool.put(old);

        return value;
    }

    @Override
    public final LongEntry<V> removeEntryForKey(final long key) {

        final int hash = hash(key);

        final LongEntry<V>[] content = content();

        final int i = indexFor(hash, content.length);

        LongEntry<V> prev = content[i];
        LongEntry<V> entry = prev;

        while (entry != null) {

            final LongEntry<V> next = entry.getNext();

            if (entry.getHash() == hash && key == entry.getKey()) {

                decrementSizeAndGet();

                if (prev == entry) {
                    content[i] = next;
                } else {
                    prev.setNext(next);
                }

                return entry;
            }

            prev = entry;
            entry = next;
        }

        return entry;
    }

    /**
     * Перестройка таблицы под новый размер.
     *
     * @param newLength новый размер.
     */
    @SuppressWarnings("unchecked")
    private final void resize(final int newLength) {

        final LongEntry<V>[] oldContent = content();

        final int oldLength = oldContent.length;

        if (oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        final LongEntry<V>[] newTable = new LongEntry[newLength];
        transfer(newTable);

        this.table = newTable;
        this.threshold = (int) (newLength * loadFactor);
    }

    @Override
    public final String toString() {

        final int size = size();

        final StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder.append(" size = ").append(size).append(" :\n");

        final LongEntry<V>[] content = content();

        for (int i = 0, length = content.length; i < length; i++) {

            LongEntry<V> entry = content[i];

            while (entry != null) {

                builder.append("[").append(entry.getKey()).append(" - ").append(entry.getValue()).append("]");
                builder.append("\n");

                entry = entry.getNext();
            }
        }

        if (size > 0) {
            builder.delete(builder.length() - 1, builder.length());
        }

        return builder.toString();
    }

    /**
     * Перенос всех записей из старой таблице в новую.
     *
     * @param newTable новая таблица.
     */
    private final void transfer(final LongEntry<V>[] newTable) {

        final LongEntry<V>[] original = content();

        final int newCapacity = newTable.length;

        for (int j = 0, length = original.length; j < length; j++) {

            LongEntry<V> entry = original[j];

            if (entry == null) {
                continue;
            }

            do {

                final LongEntry<V> next = entry.getNext();

                final int i = indexFor(entry.getHash(), newCapacity);

                entry.setNext(newTable[i]);
                newTable[i] = entry;
                entry = next;

            } while (entry != null);
        }
    }

    @Override
    public Array<V> values(final Array<V> container) {

        for (LongEntry<V> entry : content()) {
            while (entry != null) {
                container.add(entry.getValue());
                entry = entry.getNext();
            }
        }

        return container;
    }
}
