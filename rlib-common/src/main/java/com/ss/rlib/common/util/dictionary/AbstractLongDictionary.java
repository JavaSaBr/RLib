package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.function.LongBiObjectConsumer;
import com.ss.rlib.common.function.LongObjectConsumer;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.array.LongArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;

/**
 * The base implementation of the {@link LongDictionary}.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public abstract class AbstractLongDictionary<V> extends AbstractDictionary<LongKey, V, LongEntry<V>>
    implements UnsafeLongDictionary<V> {

    protected AbstractLongDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractLongDictionary(float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractLongDictionary(int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    protected AbstractLongDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
    }

    @Override
    protected @NotNull Class<? super LongEntry<V>> getEntryType() {
        return LongEntry.class;
    }

    /**
     * Add new entry to this dictionary.
     *
     * @param hash  the hash of the key.
     * @param key   the key.
     * @param value the value of the key.
     * @param index the index of bucket.
     */
    private void addEntry(int hash, long key, @NotNull V value, int index) {

        var entries = entries();
        var entry = entries[index];

        var newEntry = entryPool.take(LongEntry::new);
        newEntry.set(hash, key, value, entry);

        entries[index] = newEntry;

        if (incrementSizeAndGet() >= getThreshold()) {
            resize(2 * entries.length);
        }
    }

    @Override
    public final boolean containsKey(long key) {
        return getEntry(key) != null;
    }

    @Override
    public final @Nullable V get(long key) {
        var entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public @NotNull V getOrCompute(long key, @NotNull Supplier<@NotNull V> factory) {

        var entry = getEntry(key);

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
    public @NotNull V getOrCompute(long key, @NotNull LongFunction<@NotNull V> factory) {

        var entry = getEntry(key);

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
    public <T> @Nullable V getOrCompute(
        long key,
        @NotNull T argument,
        @NotNull Function<@NotNull T, @NotNull V> factory
    ) {

        var entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(argument));
            entry = getEntry(key);
        }

        if (entry == null) {
            throw new IllegalStateException("The factory " + factory + " returned a null value.");
        }

        return entry.getValue();
    }

    /**
     * Get the entry with value for the key.
     *
     * @param key the key.
     * @return the entry or null.
     */
    private @Nullable LongEntry<V> getEntry(long key) {

        var entries = entries();

        int hash = hash(key);
        int index = indexFor(hash, entries.length);

        for (var entry = entries[index]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key == entry.getKey()) {
                return entry;
            }
        }

        return null;
    }

    @Override
    public final Iterator<V> iterator() {
        return new LongDictionaryIterator<>(this);
    }

    @Override
    public @NotNull LongArray keyArray(@NotNull LongArray container) {

        for (var entry : entries()) {
            while (entry != null) {
                container.add(entry.getKey());
                entry = entry.getNext();
            }
        }

        return container;
    }

    @Override
    public void copyTo(@NotNull Dictionary<? super LongKey, ? super V> dictionary) {

        if (isEmpty() || !(dictionary instanceof LongDictionary)) {
            return;
        }

        var targetDictionary = ClassUtils.<LongDictionary<V>>unsafeNNCast(dictionary);

        for (var entry : entries()) {
            while (entry != null) {
                targetDictionary.put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public V put(long key, @NotNull V value) {

        var entries = entries();
        var hash = hash(key);
        var i = indexFor(hash, entries.length);

        for (var entry = entries[i]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key == entry.getKey()) {
                return entry.setValue(value);
            }
        }

        addEntry(hash, key, value, i);

        return null;
    }

    @Override
    public @Nullable V remove(long key) {

        var oldEntry = removeEntryForKey(key);

        V value = oldEntry == null ? null : oldEntry.getValue();

        if (oldEntry != null) {
            entryPool.put(oldEntry);
        }

        return value;
    }

    @Override
    public @Nullable LongEntry<V> removeEntryForKey(long key) {

        var entries = entries();
        var hash = hash(key);
        var i = indexFor(hash, entries.length);

        var prevEntry = entries[i];
        var entry = prevEntry;

        while (entry != null) {

            var nextEntry = entry.getNext();

            if (entry.getHash() == hash && key == entry.getKey()) {
                decrementSizeAndGet();

                if (prevEntry == entry) {
                    entries[i] = nextEntry;
                } else {
                    prevEntry.setNext(nextEntry);
                }

                return entry;
            }

            prevEntry = entry;
            entry = nextEntry;
        }

        return null;
    }

    @Override
    public final String toString() {

        var size = size();

        var builder = new StringBuilder(getClass().getSimpleName());
        builder
            .append(" size = ")
            .append(size)
            .append(" :\n");

        var entries = entries();

        for (var entry : entries) {
            while (entry != null) {
                builder
                    .append("[")
                    .append(entry.getKey())
                    .append(" - ")
                    .append(entry.getValue())
                    .append("]")
                    .append("\n");
                entry = entry.getNext();
            }
        }

        if (size > 0) {
            builder.delete(builder.length() - 1, builder.length());
        }

        return builder.toString();
    }

    @Override
    public <T> void forEach(@NotNull T argument, @NotNull LongBiObjectConsumer<@NotNull V, @NotNull T> consumer) {
        for (var entry : entries()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue(), argument);
                entry = entry.getNext();
            }
        }
    }

    @Override
    public void forEach(@NotNull LongObjectConsumer<@NotNull V> consumer) {
        for (var entry : entries()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }
}
