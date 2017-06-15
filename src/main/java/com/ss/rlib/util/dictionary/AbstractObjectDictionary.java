package com.ss.rlib.util.dictionary;

import static com.ss.rlib.util.ClassUtils.unsafeCast;

import com.ss.rlib.function.FourObjectConsumer;
import com.ss.rlib.function.TripleConsumer;
import com.ss.rlib.util.ArrayUtils;
import com.ss.rlib.util.ClassUtils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import com.ss.rlib.util.array.UnsafeArray;
import com.ss.rlib.util.pools.PoolFactory;
import com.ss.rlib.util.pools.ReusablePool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The base implementation of the {@link ObjectDictionary}.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public abstract class AbstractObjectDictionary<K, V> extends AbstractDictionary<K, V> implements UnsafeObjectDictionary<K, V> {

    /**
     * The pool with entries.
     */
    private final ReusablePool<ObjectEntry<K, V>> entryPool;

    /**
     * The load factor.
     */
    private float loadFactor;

    /**
     * Instantiates a new Abstract object dictionary.
     */
    protected AbstractObjectDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Instantiates a new Abstract object dictionary.
     *
     * @param loadFactor the load factor
     */
    protected AbstractObjectDictionary(final float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Instantiates a new Abstract object dictionary.
     *
     * @param initCapacity the init capacity
     */
    protected AbstractObjectDictionary(final int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    /**
     * Instantiates a new Abstract object dictionary.
     *
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     */
    protected AbstractObjectDictionary(final float loadFactor, final int initCapacity) {
        this.loadFactor = loadFactor;
        this.entryPool = PoolFactory.newReusablePool(ObjectEntry.class);
        setThreshold((int) (initCapacity * loadFactor));
        setContent(ClassUtils.unsafeCast(new ObjectEntry[DEFAULT_INITIAL_CAPACITY]));
        setSize(0);
    }

    /**
     * Set new array of entries of this {@link Dictionary}.
     *
     * @param newContent the new array of entries.
     */
    protected abstract void setContent(@NotNull final ObjectEntry<K, V>[] newContent);

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
    private void addEntry(final int hash, final K key, final V value, final int index) {

        final ObjectEntry<K, V>[] content = content();
        final ObjectEntry<K, V> entry = content[index];

        final ObjectEntry<K, V> newEntry = entryPool.take(ObjectEntry::new);
        newEntry.set(hash, key, value, entry);

        content[index] = newEntry;

        if (incrementSizeAndGet() >= getThreshold()) {
            resize(2 * content.length);
        }
    }

    @Override
    public void apply(@NotNull final Function<? super V, V> function) {
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

        ObjectEntry<K, V> next;

        for (ObjectEntry<K, V> entry : content) {
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
    public final boolean containsKey(@NotNull final K key) {
        return getEntry(key) != null;
    }

    @Override
    public final boolean containsValue(@NotNull final V value) {
        for (final ObjectEntry<K, V> element : content()) {
            for (ObjectEntry<K, V> entry = element; entry != null; entry = entry.getNext()) {
                if (value.equals(entry.getValue())) return true;
            }
        }
        return false;
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

    @Nullable
    @Override
    public final V get(@NotNull final K key) {
        final ObjectEntry<K, V> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    @Nullable
    @Override
    public V get(@NotNull final K key, @NotNull final Supplier<V> factory) {

        ObjectEntry<K, V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.get());
            entry = getEntry(key);
        }

        return entry == null ? null : entry.getValue();
    }

    @Nullable
    @Override
    public V get(@NotNull final K key, @NotNull final Function<K, V> factory) {

        ObjectEntry<K, V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(key));
            entry = getEntry(key);
        }

        return entry == null ? null : entry.getValue();
    }

    @Nullable
    @Override
    public <T> V get(@NotNull final K key, @Nullable final T argument, @NotNull final Function<T, V> factory) {

        ObjectEntry<K, V> entry = getEntry(key);

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
    private ObjectEntry<K, V> getEntry(final K key) {

        final ObjectEntry<K, V>[] content = content();
        final int hash = hash(key.hashCode());

        for (ObjectEntry<K, V> entry = content[indexFor(hash, content.length)]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key.equals(entry.getKey())) return entry;
        }

        return null;
    }

    /**
     * Gets entry pool.
     *
     * @return the pool with entries.
     */
    @NotNull
    protected ReusablePool<ObjectEntry<K, V>> getEntryPool() {
        return entryPool;
    }

    @NotNull
    @Override
    public DictionaryType getType() {
        return DictionaryType.OBJECT;
    }

    @Override
    public final Iterator<V> iterator() {
        return new ObjectDictionaryIterator<>(this);
    }

    @NotNull
    @Override
    public final Array<K> keyArray(@NotNull final Array<K> container) {
        final UnsafeArray<K> unsafeArray = container.asUnsafe();
        unsafeArray.prepareForSize(container.size() + size());

        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                unsafeArray.unsafeAdd(entry.getKey());
                entry = entry.getNext();
            }
        }

        return container;
    }

    @NotNull
    @Override
    public Array<K> keyArray(@NotNull final Class<K> type) {
        return keyArray(ArrayFactory.newArray(type));
    }

    @Override
    public void moveTo(@NotNull final Dictionary<? super K, ? super V> dictionary) {
        if (isEmpty() || dictionary.getType() != getType()) return;

        final ObjectDictionary<K, V> objectDictionary = ClassUtils.unsafeCast(dictionary);

        super.moveTo(objectDictionary);

        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                objectDictionary.put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public final V put(@NotNull final K key, @Nullable final V value) {

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

    @Nullable
    @Override
    public final V remove(final K key) {

        final ObjectEntry<K, V> old = removeEntryForKey(key);
        final V value = old == null ? null : old.getValue();

        final ReusablePool<ObjectEntry<K, V>> pool = getEntryPool();
        if (old != null) pool.put(old);

        return value;
    }

    @Nullable
    @Override
    public ObjectEntry<K, V> removeEntryForKey(@NotNull final K key) {

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
     * Resize the array of buckets of this dictionary.
     *
     * @param newLength the new size.
     */
    private void resize(final int newLength) {

        final ObjectEntry<K, V>[] oldContent = content();

        final int oldLength = oldContent.length;

        if (oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
            setThreshold(Integer.MAX_VALUE);
            return;
        }

        final ObjectEntry<K, V>[] newContent = ClassUtils.unsafeCast(new ObjectEntry[newLength]);
        transfer(newContent);
        setContent(newContent);
        setThreshold((int) (newLength * loadFactor));
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
     * Transfer current entries to new buckets.
     *
     * @param newTable the new array of buckets.
     */
    private void transfer(final ObjectEntry<K, V>[] newTable) {

        final ObjectEntry<K, V>[] original = content();
        final int newCapacity = newTable.length;

        for (ObjectEntry<K, V> entry : original) {
            if (entry == null) continue;
            do {

                final ObjectEntry<K, V> next = entry.getNext();
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

        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                unsafeArray.unsafeAdd(entry.getValue());
                entry = entry.getNext();
            }
        }

        return container;
    }

    @Override
    public void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public <T> void forEach(@Nullable final T argument, @NotNull final TripleConsumer<T, K, V> consumer) {
        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                consumer.accept(argument, entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public <F, S> void forEach(@Nullable final F first, @Nullable final S second, @NotNull final FourObjectConsumer<F, S, K, V> consumer) {
        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                consumer.accept(first, second, entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }
}
