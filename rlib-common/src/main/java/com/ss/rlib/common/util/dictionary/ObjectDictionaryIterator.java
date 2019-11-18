package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The iterator to iterate {@link ObjectDictionary}.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public class ObjectDictionaryIterator<K, V> implements Iterator<V> {

    /**
     * The dictionary.
     */
    private final @NotNull UnsafeObjectDictionary<K, V> dictionary;

    /**
     * The next entry.
     */
    private @Nullable ObjectEntry<K, V> next;

    /**
     * The current entry.
     */
    private @Nullable ObjectEntry<K, V> current;

    /**
     * The current index.
     */
    private int index;

    public ObjectDictionaryIterator(@NotNull UnsafeObjectDictionary<K, V> dictionary) {
        this.dictionary = dictionary;

        if (dictionary.size() > 0) {
            var entries = dictionary.entries();
            while (index < entries.length && (next = entries[index++]) == null);
        }
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public @NotNull V next() {
        //noinspection ConstantConditions
        return nextEntry().getValue();
    }

    /**
     * Get the next entry.
     *
     * @return the next entry.
     */
    private @NotNull ObjectEntry<K, V> nextEntry() {

        var entries = dictionary.entries();
        var entry = next;

        if (entry == null) {
            throw new NoSuchElementException();
        }

        if ((next = entry.getNext()) == null) {
            while (index < entries.length && (next = entries[index++]) == null);
        }

        current = entry;

        return entry;
    }

    @Override
    public void remove() {

        if (current == null) {
            throw new IllegalStateException();
        }

        K key = current.getKey();

        current = null;

        //noinspection ConstantConditions
        dictionary.removeEntryForKey(key);
    }
}
