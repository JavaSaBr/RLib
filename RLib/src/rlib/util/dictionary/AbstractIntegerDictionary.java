package rlib.util.dictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import rlib.function.IntBiObjectConsumer;
import rlib.function.IntObjectConsumer;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.IntegerArray;
import rlib.util.array.UnsafeArray;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

import static rlib.util.ClassUtils.unsafeCast;

/**
 * The base implementation of {@link IntegerDictionary}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractIntegerDictionary<V> extends AbstractDictionary<IntKey, V> implements UnsafeIntegerDictionary<V> {

    /**
     * The poll with entries.
     */
    private final ReusablePool<IntegerEntry<V>> entryPool;

    /**
     * The load factor.
     */
    private final float loadFactor;

    protected AbstractIntegerDictionary(final float loadFactor, final int initCapacity) {
        this.loadFactor = loadFactor;
        this.entryPool = PoolFactory.newReusablePool(IntegerEntry.class);
        setThreshold((int) (initCapacity * loadFactor));
        setContent(unsafeCast(new IntegerEntry[DEFAULT_INITIAL_CAPACITY]));
        setSize(0);
    }

    /**
     * Set new array of entries of this {@link Dictionary}.
     *
     * @param newContent the new array of entries.
     */
    protected abstract void setContent(@NotNull final IntegerEntry<V>[] newContent);

    /**
     * Set the next size value at which to resize (capacity * load factor).
     *
     * @param newThreshold the next size.
     */
    protected abstract void setThreshold(final int newThreshold);

    /**
     * @return the current next size.
     */
    protected abstract int getThreshold();

    /**
     * Set the new size of this {@link Dictionary}.
     *
     * @param size the new size.
     */
    protected abstract void setSize(final int size);

    /**
     * Add new entry to this dictionary.
     *
     * @param hash  the hash of the key.
     * @param key   the key.
     * @param value the value of the key.
     * @param index the index of bucket.
     */
    protected final void addEntry(final int hash, final int key, final V value, final int index) {

        final ReusablePool<IntegerEntry<V>> entryPool = getEntryPool();

        final IntegerEntry<V>[] table = content();
        final IntegerEntry<V> entry = table[index];

        final IntegerEntry<V> newEntry = entryPool.take(IntegerEntry::new);
        newEntry.set(hash, key, value, entry);

        table[index] = newEntry;

        if (incrementSizeAndGet() >= getThreshold()) {
            resize(2 * table.length);
        }
    }

    @Override
    public void apply(@NotNull final Function<? super V, V> function) {
        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                entry.setValue(function.apply(entry.getValue()));
                entry = entry.getNext();
            }
        }
    }

    @Override
    public void clear() {

        final ReusablePool<IntegerEntry<V>> entryPool = getEntryPool();
        final IntegerEntry<V>[] content = content();

        IntegerEntry<V> next;

        for (IntegerEntry<V> entry : content) {
            while (entry != null) {
                next = entry.getNext();
                entryPool.put(entry);
                entry = next;
            }
        }

        ArrayUtils.clear(content);
        setSize(0);
    }

    @Override
    public final boolean containsKey(final int key) {
        return getEntry(key) != null;
    }

    @Override
    public final boolean containsValue(@NotNull final V value) {

        for (final IntegerEntry<V> element : content()) {
            for (IntegerEntry<V> entry = element; entry != null; entry = entry.getNext()) {
                if (value.equals(entry.getValue())) return true;
            }
        }

        return false;
    }

    @Override
    public void forEach(final Consumer<? super V> consumer) {
        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public <T> void forEach(@Nullable final T argument, @NotNull final IntBiObjectConsumer<V, T> consumer) {
        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue(), argument);
                entry = entry.getNext();
            }
        }
    }

    @Override
    public void forEach(@NotNull final IntObjectConsumer<V> consumer) {
        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Nullable
    @Override
    public final V get(final int key) {
        final IntegerEntry<V> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    @Nullable
    @Override
    public V get(final int key, @NotNull final Supplier<V> factory) {

        IntegerEntry<V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.get());
            entry = getEntry(key);
        }

        return entry == null ? null : entry.getValue();
    }

    @Nullable
    @Override
    public V get(final int key, @NotNull final IntFunction<V> factory) {

        IntegerEntry<V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(key));
            entry = getEntry(key);
        }

        return entry == null ? null : entry.getValue();
    }

    @Nullable
    @Override
    public <T> V get(final int key, @Nullable final T argument, @NotNull final Function<T, V> factory) {

        IntegerEntry<V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(argument));
            entry = getEntry(key);
        }

        return entry == null ? null : entry.getValue();
    }

    /**
     * Get the entry with value for the key.
     *
     * @param key the key.
     * @return the entry or null.
     */
    @Nullable
    private IntegerEntry<V> getEntry(final int key) {

        final IntegerEntry<V>[] table = content();
        final int hash = hash(key);

        for (IntegerEntry<V> entry = table[indexFor(hash, table.length)]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key == entry.getKey()) return entry;
        }

        return null;
    }

    /**
     * @return the pool with entries.
     */
    @NotNull
    protected ReusablePool<IntegerEntry<V>> getEntryPool() {
        return entryPool;
    }

    @NotNull
    @Override
    public DictionaryType getType() {
        return DictionaryType.INTEGER;
    }

    @Override
    public final Iterator<V> iterator() {
        return new IntegerDictionaryIterator<>(this);
    }

    @NotNull
    @Override
    public IntegerArray keyIntegerArray(@NotNull final IntegerArray container) {

        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                container.add(entry.getKey());
                entry = entry.getNext();
            }
        }

        return container;
    }

    @Override
    public void moveTo(@NotNull final Dictionary<? super IntKey, ? super V> dictionary) {
        if (isEmpty() || dictionary.getType() != getType()) return;

        final IntegerDictionary<V> integerDictionary = unsafeCast(dictionary);

        super.moveTo(dictionary);

        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                integerDictionary.put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public final V put(final int key, @Nullable final V value) {

        final IntegerEntry<V>[] content = content();

        final int hash = hash(key);
        final int i = indexFor(hash, content.length);

        for (IntegerEntry<V> entry = content[i]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key == entry.getKey()) return entry.setValue(value);
        }

        addEntry(hash, key, value, i);
        return null;
    }

    @Nullable
    @Override
    public final V remove(final int key) {

        final IntegerEntry<V> old = removeEntryForKey(key);
        final V value = old == null ? null : old.getValue();

        final ReusablePool<IntegerEntry<V>> entryPool = getEntryPool();
        if (old != null) entryPool.put(old);

        return value;
    }

    /**
     * Remove the entry for the key.
     *
     * @param key the key of the entry.
     * @return removed entry or null.
     */
    @Nullable
    @Override
    public final IntegerEntry<V> removeEntryForKey(final int key) {

        final IntegerEntry<V>[] content = content();

        final int hash = hash(key);
        final int i = indexFor(hash, content.length);

        IntegerEntry<V> prev = content[i];
        IntegerEntry<V> entry = prev;

        while (entry != null) {

            final IntegerEntry<V> next = entry.getNext();

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
     * Resize the array of buckets of this dictionary.
     *
     * @param newLength the new size.
     */
    private void resize(final int newLength) {

        final IntegerEntry<V>[] oldContent = content();

        final int oldLength = oldContent.length;

        if (oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
            setThreshold(Integer.MAX_VALUE);
            return;
        }

        final IntegerEntry<V>[] newContent = unsafeCast(new IntegerEntry[newLength]);
        transfer(newContent);
        setContent(newContent);
        setThreshold((int) (newLength * loadFactor));
    }

    @Override
    public final String toString() {

        final int size = size();

        final StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder.append(" size = ").append(size).append(" : ");

        for (IntegerEntry<V> entry : content()) {
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
     * Transfer current entries to new buckets.
     *
     * @param newTable the new array of buckets.
     */
    private void transfer(final IntegerEntry<V>[] newTable) {

        final int newCapacity = newTable.length;

        for (IntegerEntry<V> entry : content()) {
            if (entry == null) continue;
            do {

                final IntegerEntry<V> next = entry.getNext();

                final int i = indexFor(entry.getHash(), newCapacity);

                entry.setNext(newTable[i]);
                newTable[i] = entry;
                entry = next;

            } while (entry != null);
        }
    }

    @NotNull
    @Override
    public Array<V> values(@NotNull final Array<V> container) {
        final UnsafeArray<V> unsafeArray = container.asUnsafe();
        unsafeArray.prepareForSize(container.size() + size());

        for (IntegerEntry<V> entry : content()) {
            while (entry != null) {
                final V value = entry.getValue();
                if (value != null) unsafeArray.unsafeAdd(value);
                entry = entry.getNext();
            }
        }

        return container;
    }
}
