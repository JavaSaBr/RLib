package rlib.util.dictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Реализация итератора для словаря с обхектным ключем.
 *
 * @author Ronn
 */
public class ObjectDictionaryIterator<K, V> implements Iterator<V> {

    /**
     * Итерируемый словарь.
     */
    private final UnsafeObjectDictionary<K, V> dictionary;

    /**
     * Следующая ячейка.
     */
    private ObjectEntry<K, V> next;

    /**
     * Текущая ячейка.
     */
    private ObjectEntry<K, V> current;

    /**
     * Текущий индекс в массиве.
     */
    private int index;

    public ObjectDictionaryIterator(UnsafeObjectDictionary<K, V> dictionary) {
        this.dictionary = dictionary;

        final ObjectEntry<K, V>[] content = dictionary.content();

        if (dictionary.size() > 0) {
            while (index < content.length && (next = content[index++]) == null) ;
        }
    }

    /**
     * @return итерируемый словарь.
     */
    private UnsafeObjectDictionary<K, V> getDictionary() {
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
    private ObjectEntry<K, V> nextEntry() {

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