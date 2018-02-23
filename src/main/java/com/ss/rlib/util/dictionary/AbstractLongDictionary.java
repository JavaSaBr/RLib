package com.ss.rlib.util.dictionary;

import com.ss.rlib.function.LongBiObjectConsumer;
import com.ss.rlib.function.LongObjectConsumer;
import com.ss.rlib.util.ArrayUtils;
import com.ss.rlib.util.ClassUtils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.LongArray;
import com.ss.rlib.util.array.UnsafeArray;
import com.ss.rlib.util.pools.PoolFactory;
import com.ss.rlib.util.pools.ReusablePool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;

/**
 * The base implementation of the {@link LongDictionary}.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public abstract class AbstractLongDictionary<V> extends AbstractDictionary<LongKey, V> implements UnsafeLongDictionary<V> {

    /**
     * The pool with entries.
     */
    private final ReusablePool<LongEntry<V>> entryPool;

    /**
     * The load factor.
     */
    private float loadFactor;

    /**
     * Instantiates a new Abstract long dictionary.
     */
    protected AbstractLongDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Instantiates a new Abstract long dictionary.
     *
     * @param loadFactor the load factor
     */
    protected AbstractLongDictionary(final float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Instantiates a new Abstract long dictionary.
     *
     * @param initCapacity the init capacity
     */
    protected AbstractLongDictionary(final int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    /**
     * Instantiates a new Abstract long dictionary.
     *
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     */
    protected AbstractLongDictionary(final float loadFactor, final int initCapacity) {
        this.loadFactor = loadFactor;
        this.entryPool = PoolFactory.newReusablePool(LongEntry.class);
        setThreshold((int) (initCapacity * loadFactor));
        setContent(ClassUtils.unsafeCast(new LongEntry[DEFAULT_INITIAL_CAPACITY]));
        setSize(0);
    }

    /**
     * Set new array of entries of this {@link Dictionary}.
     *
     * @param newContent the new array of entries.
     */
    protected abstract void setContent(@NotNull final LongEntry<V>[] newContent);

    /**
     * Set the next size value at which to resize (capacity * load factor).
     *
     * @param newThreshold the next size.
     */
    protected abstract void setThreshold(final int newThreshold);

    /**
     * Gets threshold.
     *
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
    private void addEntry(final int hash, final long key, final V value, final int index) {

        final ReusablePool<LongEntry<V>> entryPool = getEntryPool();

        final LongEntry<V>[] content = content();
        final LongEntry<V> entry = content[index];

        final LongEntry<V> newEntry = entryPool.take(LongEntry::new);
        newEntry.set(hash, key, value, entry);

        content[index] = newEntry;

        if (incrementSizeAndGet() >= getThreshold()) {
            resize(2 * content.length);
        }
    }

    @Override
    public void apply(@NotNull final Function<? super V, V> function) {
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

        LongEntry<V> next;

        for (LongEntry<V> entry : content) {
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
    public final boolean containsKey(final long key) {
        return getEntry(key) != null;
    }

    @Override
    public final boolean containsValue(@Nullable final V value) {

        for (final LongEntry<V> element : content()) {
            for (LongEntry<V> entry = element; entry != null; entry = entry.getNext()) {
                if (Objects.equals(value, entry.getValue())) {
                    return true;
                }
            }
        }

        return false;
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

    @Nullable
    @Override
    public final V get(final long key) {
        final LongEntry<V> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    @Nullable
    @Override
    public V get(final long key, @NotNull final Supplier<V> factory) {

        LongEntry<V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.get());
            entry = getEntry(key);
        }

        return entry == null ? null : entry.getValue();
    }

    @Nullable
    @Override
    public V get(final long key, @NotNull final LongFunction<V> factory) {

        LongEntry<V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(key));
            entry = getEntry(key);
        }

        return entry == null ? null : entry.getValue();
    }

    @Nullable
    @Override
    public <T> V get(final long key, @Nullable final T argument, @NotNull final Function<T, V> factory) {

        LongEntry<V> entry = getEntry(key);

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
    private LongEntry<V> getEntry(final long key) {

        final LongEntry<V>[] table = content();
        final int hash = hash(key);

        for (LongEntry<V> entry = table[indexFor(hash, table.length)]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key == entry.getKey()) {
                return entry;
            }
        }

        return null;
    }

    /**
     * Gets entry pool.
     *
     * @return the pool with entries.
     */
    @NotNull
    protected ReusablePool<LongEntry<V>> getEntryPool() {
        return entryPool;
    }

    @NotNull
    @Override
    public DictionaryType getType() {
        return DictionaryType.LONG;
    }

    @Override
    public final Iterator<V> iterator() {
        return new LongDictionaryIterator<>(this);
    }

    @NotNull
    @Override
    public LongArray keyLongArray(@NotNull final LongArray container) {

        for (LongEntry<V> entry : content()) {
            while (entry != null) {
                container.add(entry.getKey());
                entry = entry.getNext();
            }
        }

        return container;
    }

    @Override
    public void copyTo(@NotNull final Dictionary<? super LongKey, ? super V> dictionary) {
        if (isEmpty() || dictionary.getType() != getType()) return;

        final LongDictionary<V> longDictionary = ClassUtils.unsafeCast(dictionary);

        super.copyTo(dictionary);

        for (LongEntry<V> entry : content()) {
            while (entry != null) {
                longDictionary.put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public final V put(final long key, @Nullable final V value) {

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

    @Nullable
    @Override
    public final V remove(final long key) {

        final LongEntry<V> old = removeEntryForKey(key);
        final V value = old == null ? null : old.getValue();

        final ReusablePool<LongEntry<V>> entryPool = getEntryPool();
        if (old != null) entryPool.put(old);

        return value;
    }

    @Override
    public final LongEntry<V> removeEntryForKey(final long key) {

        final LongEntry<V>[] content = content();

        final int hash = hash(key);
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

        return null;
    }

    /**
     * Resize the array of buckets of this dictionary.
     *
     * @param newLength the new size.
     */
    private void resize(final int newLength) {

        final LongEntry<V>[] oldContent = content();

        final int oldLength = oldContent.length;

        if (oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
            setThreshold(Integer.MAX_VALUE);
            return;
        }

        final LongEntry<V>[] newContent = ClassUtils.unsafeCast(new LongEntry[newLength]);
        transfer(newContent);
        setContent(newContent);
        setThreshold((int) (newLength * loadFactor));
    }

    @Override
    public final String toString() {

        final int size = size();

        final StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder.append(" size = ").append(size).append(" :\n");

        final LongEntry<V>[] content = content();

        for (LongEntry<V> entry : content) {
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
     * Transfer current entries to new buckets.
     *
     * @param newTable the new array of buckets.
     */
    private void transfer(final LongEntry<V>[] newTable) {

        final LongEntry<V>[] original = content();
        final int newCapacity = newTable.length;

        for (LongEntry<V> entry : original) {
            if (entry == null) continue;
            do {

                final LongEntry<V> next = entry.getNext();
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

        for (LongEntry<V> entry : content()) {
            while (entry != null) {
                final V value = entry.getValue();
                if (value != null) unsafeArray.unsafeAdd(value);
                entry = entry.getNext();
            }
        }

        return container;
    }

    @Override
    public <T> void forEach(@Nullable final T argument, @NotNull final LongBiObjectConsumer<V, T> consumer) {
        for (LongEntry<V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue(), argument);
                entry = entry.getNext();
            }
        }
    }

    @Override
    public void forEach(@NotNull final LongObjectConsumer<V> consumer) {
        for (LongEntry<V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }
}
