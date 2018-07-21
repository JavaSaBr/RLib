package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The iterator to iterate {@link LongDictionary}.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public class LongDictionaryIterator<V> implements Iterator<V> {

    /**
     * The dictionary.
     */
    @NotNull
    private final UnsafeLongDictionary<V> dictionary;

    /**
     * The next entry.
     */
    @Nullable
    private LongEntry<V> next;

    /**
     * The current entry.
     */
    @Nullable
    private LongEntry<V> current;

    /**
     * The current index.
     */
    private int index;

    public LongDictionaryIterator(@NotNull UnsafeLongDictionary<V> dictionary) {
        this.dictionary = dictionary;

        if (dictionary.size() > 0) {
            LongEntry<V>[] entries = dictionary.entries();
            while (index < entries.length && (next = entries[index++]) == null) ;
        }
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public V next() {
        return nextEntry().getValue();
    }

    /**
     * Get the next entry.
     *
     * @return the next entry.
     */
    private LongEntry<V> nextEntry() {

        LongEntry<V>[] entries = dictionary.entries();
        LongEntry<V> entry = next;

        if (entry == null) {
            throw new NoSuchElementException();
        }

        if ((next = entry.getNext()) == null) {
            while (index < entries.length && (next = entries[index++]) == null) ;
        }

        current = entry;

        return entry;
    }

    @Override
    public void remove() {

        if (current == null) {
            throw new IllegalStateException();
        }

        long key = current.getKey();

        current = null;

        dictionary.removeEntryForKey(key);
    }
}
