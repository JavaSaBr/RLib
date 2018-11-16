package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The iterator to iterate {@link IntegerDictionary}.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public class IntegerDictionaryIterator<V> implements Iterator<V> {

    /**
     * The dictionary.
     */
    @NotNull
    private final UnsafeIntegerDictionary<V> dictionary;

    /**
     * The next entry.
     */
    @Nullable
    private IntegerEntry<V> next;

    /**
     * The current entry.
     */
    @Nullable
    private IntegerEntry<V> current;

    /**
     * The current index.
     */
    private int index;

    public IntegerDictionaryIterator(@NotNull UnsafeIntegerDictionary<V> dictionary) {
        this.dictionary = dictionary;

        if (dictionary.size() > 0) {
            IntegerEntry<V>[] entries = dictionary.entries();
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
    private IntegerEntry<V> nextEntry() {

        IntegerEntry<V>[] content = dictionary.entries();
        IntegerEntry<V> entry = next;

        if (entry == null) {
            throw new NoSuchElementException();
        }

        if ((next = entry.getNext()) == null) {
            while (index < content.length && (next = content[index++]) == null) ;
        }

        current = entry;

        return entry;
    }

    @Override
    public void remove() {

        if (current == null) {
            throw new IllegalStateException();
        }

        int key = current.getKey();

        current = null;

        dictionary.removeEntryForKey(key);
    }
}