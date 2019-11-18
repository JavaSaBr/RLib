package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.function.*;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * The base implementation of the {@link ObjectDictionary}.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public abstract class AbstractObjectDictionary<K, V> extends AbstractDictionary<K, V, ObjectEntry<K, V>>
        implements UnsafeObjectDictionary<K, V> {

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
        super(loadFactor, initCapacity);
    }

    @Override
    protected @NotNull Class<? super ObjectEntry<K, V>> getEntryType() {
        return ObjectEntry.class;
    }

    /**
     * Add new entry to this dictionary.
     *
     * @param hash  the hash of the key.
     * @param key   the key.
     * @param value the value of the key.
     * @param index the index of bucket.
     */
    private void addEntry(int hash, K key, V value, int index) {

        var entries = entries();
        var entry = entries[index];

        var newEntry = entryPool.take(ObjectEntry::new);
        newEntry.set(hash, key, value, entry);

        entries[index] = newEntry;

        if (incrementSizeAndGet() >= getThreshold()) {
            resize(2 * entries.length);
        }
    }

    @Override
    public final boolean containsKey(@NotNull K key) {
        return getEntry(key) != null;
    }

    @Override
    public @Nullable V get(@NotNull K key) {
        var entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public @NotNull V getOrCompute(@NotNull K key, @NotNull NotNullSupplier<V> factory) {

        var entry = getEntry(key);

        if (entry == null) {
            put(key, factory.get());
            entry = getEntry(key);
        }

        if (entry == null) {
            throw new IllegalStateException("Factory " + factory + " returned a null value.");
        }

        //noinspection ConstantConditions
        return entry.getValue();
    }

    @Override
    public @NotNull V getOrCompute(@NotNull K key, @NotNull NotNullFunction<K, V> factory) {

        var entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(key));
            entry = getEntry(key);
        }

        if (entry == null) {
            throw new IllegalStateException("Factory " + factory + " returned a null value.");
        }

        //noinspection ConstantConditions
        return entry.getValue();
    }

    @Override
    public <T> @NotNull V getOrCompute(@NotNull K key, @NotNull T argument, @NotNull NotNullFunction<T, V> factory) {

        var entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(argument));
            entry = getEntry(key);
        }

        if (entry == null) {
            throw new IllegalStateException("Factory " + factory + " returned a null value.");
        }

        //noinspection ConstantConditions
        return entry.getValue();
    }

    @Override
    public <T> @NotNull V getOrCompute(
        @NotNull K key,
        @NotNull T argument,
        @NotNull NotNullBiFunction<K, T, V> factory
    ) {

        var entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(key, argument));
            entry = getEntry(key);
        }

        if (entry == null) {
            throw new IllegalStateException("Factory " + factory + " returned a null value.");
        }

        //noinspection ConstantConditions
        return entry.getValue();
    }

    /**
     * Get an entry for the key.
     *
     * @param key the key.
     * @return the entry or null.
     */
    private @Nullable ObjectEntry<K, V> getEntry(K key) {

        var entries = entries();

        var hash = hash(key.hashCode());
        var entryIndex = indexFor(hash, entries.length);

        for (var entry = entries[entryIndex]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key.equals(entry.getKey())) {
                return entry;
            }
        }

        return null;
    }

    @Override
    public final Iterator<V> iterator() {
        return new ObjectDictionaryIterator<>(this);
    }

    @Override
    public final @NotNull Array<K> keyArray(@NotNull Array<K> container) {

        var unsafeArray = container.asUnsafe();
        unsafeArray.prepareForSize(container.size() + size());

        for (var entry : entries()) {
            while (entry != null) {
                //noinspection ConstantConditions
                unsafeArray.unsafeAdd(entry.getKey());
                entry = entry.getNext();
            }
        }

        return container;
    }

    @Override
    public void copyTo(@NotNull Dictionary<? super K, ? super V> dictionary) {

        if (isEmpty() || !(dictionary instanceof ObjectDictionary)) {
            return;
        }

        var target = ClassUtils.<ObjectDictionary<K, V>>unsafeNNCast(dictionary);

        for (var entry : entries()) {
            while (entry != null) {
                //noinspection ConstantConditions
                target.put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public @Nullable V put(@NotNull K key, @NotNull V value) {

        var entries = entries();

        var hash = hash(key.hashCode());
        var entryIndex = indexFor(hash, entries.length);

        for (var entry = entries[entryIndex]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key.equals(entry.getKey())) {
                return entry.setValue(value);
            }
        }

        addEntry(hash, key, value, entryIndex);
        return null;
    }

    @Override
    public @Nullable V remove(@NotNull K key) {

        var old = removeEntryForKey(key);

        if (old == null) {
            return null;
        }

        var value = old.getValue();

        entryPool.put(old);

        return value;
    }

    @Override
    public @Nullable ObjectEntry<K, V> removeEntryForKey(@NotNull K key) {

        var entries = entries();

        var hash = hash(key.hashCode());
        var entryIndex = indexFor(hash, entries.length);

        var prev = entries[entryIndex];
        var entry = prev;

        while (entry != null) {

            var next = entry.getNext();

            if (entry.getHash() == hash && key.equals(entry.getKey())) {
                decrementSizeAndGet();

                if (prev == entry) {
                    entries[entryIndex] = next;
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

    @Override
    public final String toString() {

        int size = size();

        var builder = new StringBuilder(getClass().getSimpleName())
            .append(" size = ")
            .append(size)
            .append(" : ");

        var entries = entries();

        for (var entry : entries) {
            while (entry != null) {
                builder
                    .append("[")
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

    @Override
    public void forEach(@NotNull NotNullBiConsumer<? super K, ? super V> consumer) {
        for (var entry : entries()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public <T> void forEach(
        @NotNull T argument,
        @NotNull NotNullTripleConsumer<? super T, ? super K, ? super V> consumer
    ) {
        for (var entry : entries()) {
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
        for (var entry : entries()) {
            while (entry != null) {
                consumer.accept(first, second, entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }
}
