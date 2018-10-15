package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.function.FourObjectConsumer;
import com.ss.rlib.common.function.TripleConsumer;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.UnsafeArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

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

        ObjectEntry<K, V>[] entries = entries();
        ObjectEntry<K, V> entry = entries[index];

        ObjectEntry<K, V> newEntry = entryPool.take(ObjectEntry::new);
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

        ObjectEntry<K, V>[] entries = entries();
        int hash = hash(key.hashCode());
        int index = indexFor(hash, entries.length);

        for (ObjectEntry<K, V> entry = entries[index]; entry != null; entry = entry.getNext()) {
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

        UnsafeArray<K> unsafeArray = container.asUnsafe();
        unsafeArray.prepareForSize(container.size() + size());

        for (ObjectEntry<K, V> entry : entries()) {
            while (entry != null) {
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

        ObjectDictionary<K, V> target = ClassUtils.unsafeNNCast(dictionary);

        for (ObjectEntry<K, V> entry : entries()) {
            while (entry != null) {
                target.put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public @Nullable V put(@NotNull K key, @Nullable V value) {

        ObjectEntry<K, V>[] entries = entries();

        int hash = hash(key.hashCode());
        int i = indexFor(hash, entries.length);

        for (ObjectEntry<K, V> entry = entries[i]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key.equals(entry.getKey())) {
                return entry.setValue(value);
            }
        }

        addEntry(hash, key, value, i);
        return null;
    }

    @Override
    public @Nullable V remove(@NotNull K key) {

        ObjectEntry<K, V> old = removeEntryForKey(key);

        V value = old == null ? null : old.getValue();

        if (old != null) {
            entryPool.put(old);
        }

        return value;
    }

    @Override
    public @Nullable ObjectEntry<K, V> removeEntryForKey(@NotNull K key) {

        ObjectEntry<K, V>[] entries = entries();

        int hash = hash(key.hashCode());
        int i = indexFor(hash, entries.length);

        ObjectEntry<K, V> prev = entries[i];
        ObjectEntry<K, V> entry = prev;

        while (entry != null) {

            ObjectEntry<K, V> next = entry.getNext();

            if (entry.getHash() == hash && key.equals(entry.getKey())) {
                decrementSizeAndGet();

                if (prev == entry) {
                    entries[i] = next;
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

        StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder.append(" size = ")
                .append(size)
                .append(" : ");

        ObjectEntry<K, V>[] table = entries();

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

    @Override
    public void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
        for (ObjectEntry<K, V> entry : entries()) {
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
        for (ObjectEntry<K, V> entry : entries()) {
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
        for (ObjectEntry<K, V> entry : entries()) {
            while (entry != null) {
                consumer.accept(first, second, entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }
}
