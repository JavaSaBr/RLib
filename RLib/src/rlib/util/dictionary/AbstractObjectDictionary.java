package rlib.util.dictionary;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import rlib.function.TripleConsumer;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

/**
 * Базовая реализация словаря с объектным ключем.
 *
 * @author Ronn
 */
public abstract class AbstractObjectDictionary<K, V> extends AbstractDictionary<K, V> implements UnsafeObjectDictionary<K, V> {

    /**
     * Пул ячеяк.
     */
    private final ReusablePool<ObjectEntry<K, V>> entryPool;

    /**
     * Массив ячеяк словаря.
     */
    private ObjectEntry<K, V>[] content;

    /**
     * Следующий размер для метода изминения размера (capacity * load factor).
     */
    private int threshold;

    /**
     * Фактор загружеености.
     */
    private float loadFactor;

    protected AbstractObjectDictionary() {
        this(Dictionary.DEFAULT_LOAD_FACTOR, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractObjectDictionary(final float loadFactor) {
        this(loadFactor, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractObjectDictionary(final float loadFactor, final int initCapacity) {
        this.loadFactor = loadFactor;
        this.threshold = (int) (initCapacity * loadFactor);
        this.content = new ObjectEntry[Dictionary.DEFAULT_INITIAL_CAPACITY];
        this.entryPool = PoolFactory.newFoldablePool(ObjectEntry.class);
    }

    protected AbstractObjectDictionary(final int initCapacity) {
        this(Dictionary.DEFAULT_LOAD_FACTOR, initCapacity);
    }

    @Override
    public void accept(final BiConsumer<? super K, ? super V> consumer) {
        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    /**
     * Добавляет новую ячейку в таблицу.
     *
     * @param hash  хэш значение.
     * @param key   значение ключа.
     * @param value значение по ключу.
     * @param index индекс ячейки.
     */
    private void addEntry(final int hash, final K key, final V value, final int index) {

        final ObjectEntry<K, V>[] content = content();
        final ObjectEntry<K, V> entry = content[index];

        final ObjectEntry<K, V> newEntry = entryPool.take(ObjectEntry::new);
        newEntry.set(hash, key, value, entry);

        content[index] = newEntry;

        if (incrementSizeAndGet() >= threshold) {
            resize(2 * content.length);
        }
    }

    @Override
    public void apply(final Function<? super V, V> function) {
        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                entry.setValue(function.apply(entry.getValue()));
                entry = entry.getNext();
            }
        }
    }

    @Override
    public void clear() {

        final ReusablePool<ObjectEntry<K, V>> entryPool = getEntryPool();

        final ObjectEntry<K, V>[] content = content();
        ObjectEntry<K, V> next = null;

        for (ObjectEntry<K, V> entry : content) {
            while (entry != null) {
                next = entry.getNext();
                entryPool.put(entry);
                entry = next;
            }
        }

        ArrayUtils.clear(content);
    }

    @Override
    public final boolean containsKey(final K key) {
        return getEntry(key) != null;
    }

    @Override
    public final boolean containsValue(final V value) {

        if (value == null) {
            throw new NullPointerException("value is null.");
        }

        for (final ObjectEntry<K, V> element : content()) {
            for (ObjectEntry<K, V> entry = element; entry != null; entry = entry.getNext()) {
                if (value.equals(entry.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ObjectEntry<K, V>[] content() {
        return content;
    }

    @Override
    public void forEach(final Consumer<? super V> consumer) {
        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public final V get(final K key) {

        if (key == null) {
            throw new NullPointerException("key is null.");
        }

        final ObjectEntry<K, V> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public V get(final K key, final Supplier<V> factory) {

        ObjectEntry<K, V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.get());
        }

        entry = getEntry(key);

        return entry == null ? null : entry.getValue();
    }

    @Override
    public <T> V get(final K key, final T argument, final Function<T, V> factory) {

        ObjectEntry<K, V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(argument));
        }

        entry = getEntry(key);

        return entry == null ? null : entry.getValue();
    }

    /**
     * Получение ячейки по ключу.
     *
     * @param key ключ ячейки.
     * @return ячейка.
     */
    private ObjectEntry<K, V> getEntry(final K key) {

        final int hash = hash(key.hashCode());

        final ObjectEntry<K, V>[] content = content();

        for (ObjectEntry<K, V> entry = content[indexFor(hash, content.length)]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key.equals(entry.getKey())) {
                return entry;
            }
        }

        return null;
    }

    /**
     * @return пул ячеяк.
     */
    protected ReusablePool<ObjectEntry<K, V>> getEntryPool() {
        return entryPool;
    }

    @Override
    public DictionaryType getType() {
        return DictionaryType.OBJECT;
    }

    @Override
    public final Iterator<V> iterator() {
        return new ObjectDictionaryIterator<>(this);
    }

    @Override
    public final Array<K> keyArray(final Array<K> container) {
        container.checkSize(container.size() + size());

        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                container.unsafeAdd(entry.getKey());
                entry = entry.getNext();
            }
        }

        return container;
    }

    @Override
    public Array<K> keyArray(final Class<K> type) {
        return keyArray(ArrayFactory.newArray(type));
    }

    @Override
    public void moveTo(final Dictionary<? super K, ? super V> dictionary) {

        if (isEmpty() || dictionary.getType() != getType()) {
            return;
        }

        final ObjectDictionary<K, V> objectDictionary = (ObjectDictionary<K, V>) dictionary;

        super.moveTo(objectDictionary);

        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                objectDictionary.put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public final V put(final K key, final V value) {

        if (key == null) {
            throw new NullPointerException("key is null.");
        }

        final ObjectEntry<K, V>[] content = content();

        final int hash = hash(key.hashCode());
        final int i = indexFor(hash, content.length);

        for (ObjectEntry<K, V> entry = content[i]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key.equals(entry.getKey())) {
                return entry.setValue(value);
            }
        }

        addEntry(hash, key, value, i);

        return null;
    }

    @Override
    public final V remove(final K key) {

        if (key == null) {
            throw new NullPointerException("key is null.");
        }

        final ObjectEntry<K, V> old = removeEntryForKey(key);
        final V value = old == null ? null : old.getValue();

        final ReusablePool<ObjectEntry<K, V>> pool = getEntryPool();
        pool.put(old);

        return value;
    }

    @Override
    public ObjectEntry<K, V> removeEntryForKey(final K key) {

        final ObjectEntry<K, V>[] content = content();

        final int hash = hash(key.hashCode());
        final int i = indexFor(hash, content.length);

        ObjectEntry<K, V> prev = content[i];
        ObjectEntry<K, V> entry = prev;

        while (entry != null) {

            final ObjectEntry<K, V> next = entry.getNext();

            if (entry.getHash() == hash && key.equals(entry.getKey())) {

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

        return null;
    }

    /**
     * Перестройка таблицы под новый размер.
     *
     * @param newLength новый размер.
     */
    private void resize(final int newLength) {

        final ObjectEntry<K, V>[] oldContent = content();

        final int oldLength = oldContent.length;

        if (oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        final ObjectEntry<K, V>[] newContent = new ObjectEntry[newLength];
        transfer(newContent);

        this.content = newContent;
        this.threshold = (int) (newLength * loadFactor);
    }

    @Override
    public final String toString() {

        final int size = size();

        final StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder.append(" size = ").append(size).append(" : ");

        final ObjectEntry<K, V>[] table = content();

        for (ObjectEntry<K, V> entry : table) {
            while (entry != null) {
                builder.append("[").append(entry.getKey()).append(" - ").append(entry.getValue()).append("]\n");
                entry = entry.getNext();
            }
        }

        if (size > 0) {
            builder.replace(builder.length() - 1, builder.length(), ".");
        }

        return builder.toString();
    }

    /**
     * Перенос всех записей из старой таблице в новую.
     *
     * @param newTable новая таблица.
     */
    private void transfer(final ObjectEntry<K, V>[] newTable) {

        final ObjectEntry<K, V>[] original = content;
        final int newCapacity = newTable.length;

        for (ObjectEntry<K, V> entry : original) {

            if (entry == null) {
                continue;
            }

            do {

                final ObjectEntry<K, V> next = entry.getNext();

                final int i = indexFor(entry.getHash(), newCapacity);

                entry.setNext(newTable[i]);
                newTable[i] = entry;
                entry = next;

            } while (entry != null);
        }
    }

    @Override
    public Array<V> values(final Array<V> container) {
        container.checkSize(container.size() + size());

        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                container.unsafeAdd(entry.getValue());
                entry = entry.getNext();
            }
        }

        return container;
    }

    @Override
    public void forEach(final BiConsumer<K, V> consumer) {
        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public <T> void forEach(final T argument, final TripleConsumer<K, V, T> consumer) {
        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue(), argument);
                entry = entry.getNext();
            }
        }
    }
}
