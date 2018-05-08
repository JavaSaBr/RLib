package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;

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
    private final UnsafeObjectDictionary<K, V> dictionary;

    /**
     * The next entry.
     */
    private ObjectEntry<K, V> next;

    /**
     * The current entry.
     */
    private ObjectEntry<K, V> current;

    /**
     * The current index.
     */
    private int index;

    public ObjectDictionaryIterator(@NotNull final UnsafeObjectDictionary<K, V> dictionary) {
        this.dictionary = dictionary;
        if (dictionary.size() > 0) {
            final ObjectEntry<K, V>[] content = dictionary.content();
            while (index < content.length && (next = content[index++]) == null);
        }
    }

    /**
     * Get the dictionary.
     *
     * @return the dictionary.
     */
    private @NotNull UnsafeObjectDictionary<K, V> getDictionary() {
        return dictionary;
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
    private @NotNull ObjectEntry<K, V> nextEntry() {

        final UnsafeObjectDictionary<K, V> dictionary = getDictionary();

        final ObjectEntry<K, V>[] content = dictionary.content();
        final ObjectEntry<K, V> entry = next;

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

        final K key = current.getKey();
        current = null;

        final UnsafeObjectDictionary<K, V> dictionary = getDictionary();
        dictionary.removeEntryForKey(key);
    }
}