package rlib.util.dictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Реализация итератора для словаря с обхектным ключем.
 * 
 * @author Ronn
 */
public class ObjectDictionaryIterator<K, V> implements Iterator<V> {

	/** итерируемый словарь */
	private final UnsafeObjectDictionary<K, V> dictionary;

	/** следующая ячейка */
	private ObjectDictionaryEntry<K, V> next;
	/** текущая ячейка */
	private ObjectDictionaryEntry<K, V> current;

	/** текущий индекс в массиве */
	private int index;

	public ObjectDictionaryIterator(UnsafeObjectDictionary<K, V> dictionary) {
		this.dictionary = dictionary;

		final ObjectDictionaryEntry<K, V>[] content = dictionary.content();

		if(dictionary.size() > 0) {
			while(index < content.length && (next = content[index++]) == null);
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
	 * @return итерируемый словарь.
	 */
	private UnsafeObjectDictionary<K, V> getDictionary() {
		return dictionary;
	}

	/**
	 * @return следующая занятая ячейка.
	 */
	private ObjectDictionaryEntry<K, V> nextEntry() {

		final UnsafeObjectDictionary<K, V> dictionary = getDictionary();

		final ObjectDictionaryEntry<K, V>[] content = dictionary.content();
		final ObjectDictionaryEntry<K, V> entry = next;

		if(entry == null) {
			throw new NoSuchElementException();
		}

		if((next = entry.getNext()) == null) {
			while(index < content.length && (next = content[index++]) == null);
		}

		current = entry;
		return entry;
	}

	@Override
	public void remove() {

		if(current == null) {
			throw new IllegalStateException();
		}

		final K key = current.getKey();
		current = null;

		final UnsafeObjectDictionary<K, V> dictionary = getDictionary();
		dictionary.removeEntryForKey(key);
	}
}