package rlib.util.dictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Реализация итератора для словаря с примитивным ключем long.
 *
 * @author Ronn
 */
public class LongDictionaryIterator<V> implements Iterator<V> {

    /**
     * Мтерируемый словарь.
     */
    private final UnsafeLongDictionary<V> dictionary;

    /**
     * Следующий entry.
     */
    private LongEntry<V> next;

    /**
     * Текущий entry.
     */
    private LongEntry<V> current;

    /**
     * Текущий индекс в таблице.
     */
    private int index;

    public LongDictionaryIterator(final UnsafeLongDictionary<V> dictionary) {
        this.dictionary = dictionary;

        final LongEntry<V>[] content = dictionary.content();

        if (dictionary.size() > 0) {
            while (index < content.length && (next = content[index++]) == null);
        }
    }

    /**
     * @return итерируемый словарь.
     */
    private UnsafeLongDictionary<V> getDictionary() {
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
    private LongEntry<V> nextEntry() {

        final UnsafeLongDictionary<V> dictionary = getDictionary();

        final LongEntry<V>[] content = dictionary.content();
        final LongEntry<V> entry = next;

        if (entry == null) {
            throw new NoSuchElementException();
        }

        if ((next = entry.getNext()) == null) {
            while (index < content.length && (next = content[index++]) == null);
        }

        current = entry;

        return entry;
    }

    @Override
    public void remove() {

        if (current == null) {
            throw new IllegalStateException();
        }

        final long key = current.getKey();

        current = null;

        final UnsafeLongDictionary<V> dictionary = getDictionary();
        dictionary.removeEntryForKey(key);
    }
}
