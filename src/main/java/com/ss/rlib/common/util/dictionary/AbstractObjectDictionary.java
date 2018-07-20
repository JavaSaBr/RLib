package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.function.FourObjectConsumer;
import com.ss.rlib.common.function.TripleConsumer;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.UnsafeArray;
import com.ss.rlib.common.util.pools.PoolFactory;
import com.ss.rlib.common.util.pools.ReusablePool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.*;

/**
 * The base implementation of the {@link ObjectDictionary}.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public abstract class AbstractObjectDictionary<K, V> extends AbstractDictionary<K, V>
        implements UnsafeObjectDictionary<K, V> {

    /**
     * The pool with entries.
     */
    @NotNull
    private final ReusablePool<ObjectEntry<K, V>> entryPool;

    /**
     * The load factor.
     */
    private float loadFactor;

    protected AbstractObjectDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractObjectDictionary(float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractObjectDictionary(int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    protected AbstractObjectDictionary(float loadFactor, int initCapacity) {
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
    protected abstract void setContent(@NotNull ObjectEntry<K, V>[] newContent);

    /**
     * Set the next size value at which to resize (capacity * load factor).
     *
     * @param newThreshold the next size.
     */
    protected abstract void setThreshold(int newThreshold);

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
    protected abstract void setSize(int size);

    /**
     * Add new entry to this dictionary.
     *
     * @param hash  the hash of the key.
     * @param key   the key.
     * @param value the value of the key.
     * @param index the index of bucket.
     */
    private void addEntry(int hash, K key, V value, int index) {

        ObjectEntry<K, V>[] content = content();
        ObjectEntry<K, V> entry = content[index];

        ObjectEntry<K, V> newEntry = entryPool.take(ObjectEntry::new);
        newEntry.set(hash, key, value, entry);

        content[index] = newEntry;

        if (incrementSizeAndGet() >= getThreshold()) {
            resize(2 * content.length);
        }
    }

    @Override
    public void apply(@NotNull Function<? super V, V> function) {
        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                entry.setValue(function.apply(entry.getValue()));
                entry = entry.getNext();
            }
        }
    }

    @Override
    public void clear() {

        ReusablePool<ObjectEntry<K, V>> entryPool = getEntryPool();
        ObjectEntry<K, V>[] content = content();

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
    public final boolean containsKey(@NotNull K key) {
        return getEntry(key) != null;
    }

    @Override
    public final boolean containsValue(@Nullable V value) {

        for (ObjectEntry<K, V> element : content()) {
            for (ObjectEntry<K, V> entry = element; entry != null; entry = entry.getNext()) {
                if (Objects.equals(value, entry.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void forEach(Consumer<? super V> consumer) {
        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                consumer.accept(entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public @Nullable V get(@NotNull K key) {
        ObjectEntry<K, V> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public @NotNull V getOrCompute(@NotNull K key, @NotNull Supplier<@NotNull V> factory) {

        ObjectEntry<K, V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.get());
            entry = getEntry(key);
        }

        if (entry == null) {
            throw new IllegalStateException("The factory " + factory + " returned a null value.");
        }

        return entry.getValue();
    }

    @Override
    public @NotNull V getOrCompute(@NotNull K key, @NotNull Function<@NotNull K, @NotNull V> factory) {

        ObjectEntry<K, V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(key));
            entry = getEntry(key);
        }

        if (entry == null) {
            throw new IllegalStateException("The factory " + factory + " returned a null value.");
        }

        return entry.getValue();
    }

    @Override
    public <T> @NotNull V getOrCompute(
            @NotNull K key,
            @NotNull T argument,
            @NotNull Function<@NotNull T, @NotNull V> factory
    ) {

        ObjectEntry<K, V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(argument));
            entry = getEntry(key);
        }

        if (entry == null) {
            throw new IllegalStateException("The factory " + factory + " returned a null value.");
        }

        return entry.getValue();
    }

    @Override
    public <T> @NotNull V getOrCompute(
            @NotNull K key,
            @NotNull T argument,
            @NotNull BiFunction<@NotNull K, @NotNull T, @NotNull V> factory
    ) {

        ObjectEntry<K, V> entry = getEntry(key);

        if (entry == null) {
            put(key, ObjectUtils.notNull(factory.apply(key, argument)));
            entry = getEntry(key);
        }

        if (entry == null) {
            throw new IllegalStateException("The factory " + factory + " returned a null value.");
        }

        return entry.getValue();
    }

    /**
     * Get an entry for the key.
     *
     * @param key the key.
     * @return the entry or null.
     */
    private @Nullable ObjectEntry<K, V> getEntry(K key) {

        ObjectEntry<K, V>[] content = content();
        int hash = hash(key.hashCode());

        for (ObjectEntry<K, V> entry = content[indexFor(hash, content.length)];
                 entry != null;
                 entry = entry.getNext()) {

            if (entry.getHash() == hash && key.equals(entry.getKey())) {
                return entry;
            }
        }

        return null;
    }

    /**
     * Get entry pool.
     *
     * @return the pool with entries.
     */
    protected @NotNull ReusablePool<ObjectEntry<K, V>> getEntryPool() {
        return entryPool;
    }

    @Override
    public @NotNull DictionaryType getType() {
        return DictionaryType.OBJECT;
    }

    @Override
    public final Iterator<V> iterator() {
        return new ObjectDictionaryIterator<>(this);
    }

    @Override
    public final @NotNull Array<K> keyArray(@NotNull Array<K> container) {

        UnsafeArray<K> unsafeArray = container.asUnsafe();
        unsafeArray.prepareForSize(container.size() + size());

        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                unsafeArray.unsafeAdd(entry.getKey());
                entry = entry.getNext();
            }
        }

        return container;
    }

    @Override
    public void copyTo(@NotNull Dictionary<? super K, ? super V> dictionary) {
        super.copyTo(dictionary);

        if (isEmpty() || dictionary.getType() != getType()) {
            return;
        }

        ObjectDictionary<K, V> target = ClassUtils.unsafeCast(dictionary);

        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                target.put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public final @Nullable V put(@NotNull K key, @Nullable V value) {

        ObjectEntry<K, V>[] content = content();

        int hash = hash(key.hashCode());
        int i = indexFor(hash, content.length);

        for (ObjectEntry<K, V> entry = content[i]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key.equals(entry.getKey())) {
                return entry.setValue(value);
            }
        }

        addEntry(hash, key, value, i);
        return null;
    }

    @Override
    public final @Nullable V remove(K key) {

        ObjectEntry<K, V> old = removeEntryForKey(key);
        V value = old == null ? null : old.getValue();

        ReusablePool<ObjectEntry<K, V>> pool = getEntryPool();

        if (old != null) {
            pool.put(old);
        }

        return value;
    }

    @Override
    public @Nullable ObjectEntry<K, V> removeEntryForKey(@NotNull K key) {

        ObjectEntry<K, V>[] content = content();

        int hash = hash(key.hashCode());
        int i = indexFor(hash, content.length);

        ObjectEntry<K, V> prev = content[i];
        ObjectEntry<K, V> entry = prev;

        while (entry != null) {

            ObjectEntry<K, V> next = entry.getNext();

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
    private void resize(int newLength) {

        ObjectEntry<K, V>[] oldContent = content();

        int oldLength = oldContent.length;

        if (oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
            setThreshold(Integer.MAX_VALUE);
            return;
        }

        ObjectEntry<K, V>[] newContent =
                ClassUtils.unsafeCast(new ObjectEntry[newLength]);

        transfer(newContent);
        setContent(newContent);
        setThreshold((int) (newLength * loadFactor));
    }

    @Override
    public final String toString() {

        int size = size();

        StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder.append(" size = ").append(size).append(" : ");

        ObjectEntry<K, V>[] table = content();

        for (ObjectEntry<K, V> entry : table) {
            while (entry != null) {

                builder.append("[")
                        .append(entry.getKey())
                        .append(" - ")
                        .append(entry.getValue())
                        .append("]\n");

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
    private void transfer(ObjectEntry<K, V>[] newTable) {

        ObjectEntry<K, V>[] original = content();
        int newCapacity = newTable.length;

        for (ObjectEntry<K, V> entry : original) {

            if (entry == null) {
                continue;
            }

            do {

                ObjectEntry<K, V> next = entry.getNext();
                int i = indexFor(entry.getHash(), newCapacity);

                entry.setNext(newTable[i]);
                newTable[i] = entry;
                entry = next;

            } while (entry != null);
        }
    }

    @Override
    public @NotNull Array<V> values(@NotNull Array<V> container) {

        UnsafeArray<V> unsafeArray = container.asUnsafe();
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
    public <T> void forEach(
            @NotNull T argument,
            @NotNull TripleConsumer<@NotNull ? super T, @NotNull ? super K, @NotNull ? super V> consumer
    ) {
        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                consumer.accept(argument, entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public <F, S> void forEach(
            @NotNull F first,
            @NotNull S second,
            @NotNull FourObjectConsumer<@NotNull ? super F, @NotNull ? super S, @NotNull ? super K, @NotNull ? super V> consumer
    ) {
        for (ObjectEntry<K, V> entry : content()) {
            while (entry != null) {
                consumer.accept(first, second, entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }
}
