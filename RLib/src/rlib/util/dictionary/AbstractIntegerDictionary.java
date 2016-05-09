package rlib.util.dictionary;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import rlib.function.IntBiObjectConsumer;
import rlib.function.IntObjectConsumer;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.IntegerArray;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

/**
 * Базовая реализация словаря с примитивным ключем int.
 *
 * @author Ronn
 */
public abstract class AbstractIntegerDictionary<V> extends AbstractDictionary<IntKey, V> implements UnsafeIntegerDictionary<V> {

    /**
     * Пул ячеяк.
     */
    private final ReusablePool<IntegerDictionaryEntry<V>> entryPool;

    /**
     * Фактор загруженности.
     */
    private final float loadFactor;

    /**
     * Таблица элементов.
     */
    private IntegerDictionaryEntry<V>[] content;

    /**
     * Следующий размер для метода изминения размера (capacity * load factor).
     */
    private int threshold;

    @SuppressWarnings("unchecked")
    protected AbstractIntegerDictionary(final float loadFactor, final int initCapacity) {
        this.loadFactor = loadFactor;
        this.threshold = (int) (initCapacity * loadFactor);
        this.content = new IntegerDictionaryEntry[Dictionary.DEFAULT_INITIAL_CAPACITY];
        this.entryPool = PoolFactory.newFoldablePool(IntegerDictionaryEntry.class);
    }

    /**
     * Добавляет новую ячейку в таблицу.
     *
     * @param hash  хэш значение.
     * @param key   значение ключа.
     * @param value значение по ключу.
     * @param index индекс ячейки.
     */
    protected final void addEntry(final int hash, final int key, final V value, final int index) {

        final ReusablePool<IntegerDictionaryEntry<V>> entryPool = getEntryPool();

        final IntegerDictionaryEntry<V>[] table = content();
        final IntegerDictionaryEntry<V> entry = table[index];

        IntegerDictionaryEntry<V> newEntry = entryPool.take();

        if (newEntry == null) {
            newEntry = new IntegerDictionaryEntry<V>();
        }

        newEntry.set(hash, key, value, entry);

        table[index] = newEntry;

        if (incrementSizeAndGet() >= threshold) {
            resize(2 * table.length);
        }
    }

    @Override
    public void apply(final Function<? super V, V> function) {
        for (IntegerDictionaryEntry<V> entry : content()) {
            while (entry != null) {
                entry.setValue(function.apply(entry.getValue()));
                entry = entry.getNext();
            }
        }
    }

    @Override
    public void clear() {

        final ReusablePool<IntegerDictionaryEntry<V>> entryPool = getEntryPool();
        final IntegerDictionaryEntry<V>[] content = content();

        IntegerDictionaryEntry<V> next;

        for (IntegerDictionaryEntry<V> entry : content) {
            while (entry != null) {
                next = entry.getNext();
                entryPool.put(entry);
                entry = next;
            }
        }

        ArrayUtils.clear(content);
    }

    @Override
    public final boolean containsKey(final int key) {
        return getEntry(key) != null;
    }

    @Override
    public final boolean containsValue(final V value) {

        if (value == null) {
            throw new NullPointerException("value is null.");
        }

        for (final IntegerDictionaryEntry<V> element : content()) {
            for (IntegerDictionaryEntry<V> entry = element; entry != null; entry = entry.getNext()) {
                if (value.equals(entry.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @return массив ячеяк.
     */
    @Override
    public final IntegerDictionaryEntry<V>[] content() {
        return content;
    }

    @Override
    public void forEach(final Consumer<? super V> consumer) {
        for (IntegerDictionaryEntry<V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public <T> void forEach(final T argument, final IntBiObjectConsumer<V, T> consumer) {
        for (IntegerDictionaryEntry<V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue(), argument);
                entry = entry.getNext();
            }
        }
    }

    @Override
    public void forEach(final IntObjectConsumer<V> consumer) {
        for (IntegerDictionaryEntry<V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public final V get(final int key) {
        final IntegerDictionaryEntry<V> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public V get(final int key, final Supplier<V> factory) {

        IntegerDictionaryEntry<V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.get());
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
    private IntegerDictionaryEntry<V> getEntry(final int key) {

        final int hash = hash(key);

        final IntegerDictionaryEntry<V>[] table = content();

        for (IntegerDictionaryEntry<V> entry = table[indexFor(hash, table.length)]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key == entry.getKey()) {
                return entry;
            }
        }

        return null;
    }

    /**
     * @return пул ячеяк.
     */
    private ReusablePool<IntegerDictionaryEntry<V>> getEntryPool() {
        return entryPool;
    }

    @Override
    public DictionaryType getType() {
        return DictionaryType.INTEGER;
    }

    @Override
    public final Iterator<V> iterator() {
        return new IntegerDictionaryIterator<>(this);
    }

    @Override
    public IntegerArray keyIntegerArray(final IntegerArray container) {

        for (IntegerDictionaryEntry<V> entry : content()) {
            while (entry != null) {
                container.add(entry.getKey());
                entry = entry.getNext();
            }
        }

        return container;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void moveTo(final Dictionary<? super IntKey, ? super V> dictionary) {

        if (isEmpty() || dictionary.getType() != getType()) {
            return;
        }

        final IntegerDictionary<V> integerDictionary = (IntegerDictionary<V>) dictionary;

        super.moveTo(dictionary);

        for (IntegerDictionaryEntry<V> entry : content()) {
            while (entry != null) {
                integerDictionary.put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public final V put(final int key, final V value) {

        final int hash = hash(key);

        final IntegerDictionaryEntry<V>[] content = content();

        final int i = indexFor(hash, content.length);

        for (IntegerDictionaryEntry<V> entry = content[i]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key == entry.getKey()) {
                return entry.setValue(value);
            }
        }

        addEntry(hash, key, value, i);

        return null;
    }

    @Override
    public final V remove(final int key) {

        final IntegerDictionaryEntry<V> old = removeEntryForKey(key);
        final V value = old == null ? null : old.getValue();

        final ReusablePool<IntegerDictionaryEntry<V>> entryPool = getEntryPool();
        entryPool.put(old);

        return value;
    }

    /**
     * Удаление значения из ячейки по указанному ключу.
     *
     * @param key ключ ячейки.
     * @return удаленная ячейка.
     */
    @Override
    public final IntegerDictionaryEntry<V> removeEntryForKey(final int key) {

        final int hash = hash(key);

        final IntegerDictionaryEntry<V>[] content = content();

        final int i = indexFor(hash, content.length);

        IntegerDictionaryEntry<V> prev = content[i];
        IntegerDictionaryEntry<V> entry = prev;

        while (entry != null) {

            final IntegerDictionaryEntry<V> next = entry.getNext();

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

        return null;
    }

    /**
     * Перестройка таблицы под новый размер.
     *
     * @param newLength новый размер.
     */
    @SuppressWarnings("unchecked")
    private void resize(final int newLength) {

        final IntegerDictionaryEntry<V>[] oldContent = content();

        final int oldLength = oldContent.length;

        if (oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        final IntegerDictionaryEntry<V>[] newContent = new IntegerDictionaryEntry[newLength];
        transfer(newContent);

        this.content = newContent;
        this.threshold = (int) (newLength * loadFactor);
    }

    @Override
    public final String toString() {

        final int size = size();

        final StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder.append(" size = ").append(size).append(" : ");

        for (IntegerDictionaryEntry<V> entry : content()) {
            while (entry != null) {
                builder.append("[").append(entry.getKey()).append(" - ").append(entry.getValue()).append("]");
                builder.append("\n");
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
    private void transfer(final IntegerDictionaryEntry<V>[] newTable) {

        final int newCapacity = newTable.length;

        for (IntegerDictionaryEntry<V> entry : content()) {
            if (entry != null) {
                do {

                    final IntegerDictionaryEntry<V> next = entry.getNext();

                    final int i = indexFor(entry.getHash(), newCapacity);

                    entry.setNext(newTable[i]);
                    newTable[i] = entry;
                    entry = next;

                } while (entry != null);
            }
        }
    }

    @Override
    public Array<V> values(final Array<V> container) {

        for (IntegerDictionaryEntry<V> entry : content()) {
            while (entry != null) {
                container.add(entry.getValue());
                entry = entry.getNext();
            }
        }

        return container;
    }
}
