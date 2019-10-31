package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.function.IntBiObjectConsumer;
import com.ss.rlib.common.function.IntObjectConsumer;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.array.IntegerArray;
import com.ss.rlib.common.util.array.MutableIntegerArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/**
 * The base implementation of {@link IntegerDictionary}.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public abstract class AbstractIntegerDictionary<V> extends AbstractDictionary<IntKey, V, IntegerEntry<V>> implements
    UnsafeIntegerDictionary<V> {

    protected AbstractIntegerDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractIntegerDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
    }

    @Override
    protected @NotNull Class<? super IntegerEntry<V>> getEntryType() {
        return IntegerEntry.class;
    }

    /**
     * Add a new entry to this dictionary.
     *
     * @param hash  the hash of the key.
     * @param key   the key.
     * @param value the value of the key.
     * @param index the index of bucket.
     */
    protected final void addEntry(int hash, int key, @NotNull V value, int index) {

        var entries = entries();
        var entry = entries[index];

        var newEntry = entryPool.take(IntegerEntry::new);
        newEntry.set(hash, key, value, entry);

        entries[index] = newEntry;

        if (incrementSizeAndGet() >= getThreshold()) {
            resize(2 * entries.length);
        }
    }

    @Override
    public final boolean containsKey(int key) {
        return getEntry(key) != null;
    }

    @Override
    public void forEach(@NotNull IntObjectConsumer<@NotNull ? super V> consumer) {
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
        @NotNull IntBiObjectConsumer<@NotNull ? super V, @NotNull ? super T> consumer
    ) {
        for (var entry : entries()) {
            while (entry != null) {
                consumer.accept(entry.getKey(), entry.getValue(), argument);
                entry = entry.getNext();
            }
        }
    }

    @Override
    public final @Nullable V get(int key) {
        var entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public @NotNull V getOrCompute(int key, @NotNull Supplier<@NotNull V> factory) {

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
    public @NotNull V getOrCompute(int key, @NotNull IntFunction<@NotNull V> factory) {

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
        int key,
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

    private @Nullable IntegerEntry<V> getEntry(int key) {

        var entries = entries();
        var index = indexFor(hash(key), entries.length);

        for (IntegerEntry<V> entry = entries[index]; entry != null; entry = entry.getNext()) {
            if (key == entry.getKey()) {
                return entry;
            }
        }

        return null;
    }

    @Override
    public final @NotNull Iterator<V> iterator() {
        return new IntegerDictionaryIterator<>(this);
    }

    @Override
    public @NotNull IntegerArray keyArray(@NotNull MutableIntegerArray container) {

        for (var entry : entries()) {
            while (entry != null) {
                container.add(entry.getKey());
                entry = entry.getNext();
            }
        }

        return container;
    }

    @Override
    public void copyTo(@NotNull Dictionary<? super IntKey, ? super V> dictionary) {

        if (isEmpty() || !(dictionary instanceof IntegerDictionary)) {
            return;
        }

        IntegerDictionary<V> target = ClassUtils.unsafeNNCast(dictionary);

        for (IntegerEntry<V> entry : entries()) {
            while (entry != null) {
                target.put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @Override
    public final @Nullable V put(int key, @NotNull V value) {

        var entries = entries();

        var hash = hash(key);
        var entryIndex = indexFor(hash, entries.length);

        for (var entry = entries[entryIndex]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key == entry.getKey()) {
                return entry.setValue(value);
            }
        }

        addEntry(hash, key, value, entryIndex);
        return null;
    }


    @Override
    public @Nullable V remove(int key) {

        var old = removeEntryForKey(key);

        if (old == null) {
            return null;
        }

        var value = old.getValue();

        entryPool.put(old);

        return value;
    }

    /**
     * Remove the entry for the key.
     *
     * @param key the key of the entry.
     * @return removed entry or null.
     */
    @Override
    public final @Nullable IntegerEntry<V> removeEntryForKey(int key) {

        var entries = entries();

        int i = indexFor(hash(key), entries.length);

        var prev = entries[i];
        var entry = prev;

        while (entry != null) {

            var next = entry.getNext();

            if (key == entry.getKey()) {
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

        var size = size();

        var builder = new StringBuilder(getClass().getSimpleName());
        builder
            .append(" size = ")
            .append(size)
            .append(" : ");

        for (var entry : entries()) {
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
            builder.replace(builder.length() - 1, builder.length(), ".");
        }

        return builder.toString();
    }
}
