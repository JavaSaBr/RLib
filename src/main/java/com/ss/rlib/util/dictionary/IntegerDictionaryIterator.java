package com.ss.rlib.util.dictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Реализация итератора для словарей с примитивным ключем int.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public class IntegerDictionaryIterator<V> implements Iterator<V> {

    /**
     * итерируемый словарь
     */
    private final UnsafeIntegerDictionary<V> dictionary;

    /**
     * следующая ячейка
     */
    private IntegerEntry<V> next;
    /**
     * текущая ячейка
     */
    private IntegerEntry<V> current;

    /**
     * текущий индекс в массиве
     */
    private int index;

    /**
     * Instantiates a new Integer dictionary iterator.
     *
     * @param dictionary the dictionary
     */
    public IntegerDictionaryIterator(final UnsafeIntegerDictionary<V> dictionary) {
        this.dictionary = dictionary;

        final IntegerEntry<V>[] content = dictionary.content();

        if (dictionary.size() > 0) {
            while (index < content.length && (next = content[index++]) == null) ;
        }
    }

    /**
     * @return итерируемый словарь.
     */
    private UnsafeIntegerDictionary<V> getDictionary() {
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
     * @return следующая занятая ячейка.
     */
    private IntegerEntry<V> nextEntry() {

        final UnsafeIntegerDictionary<V> dictionary = getDictionary();

        final IntegerEntry<V>[] content = dictionary.content();
        final IntegerEntry<V> entry = next;

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

        final int key = current.getKey();

        current = null;

        final UnsafeIntegerDictionary<V> dictionary = getDictionary();
        dictionary.removeEntryForKey(key);
    }
}