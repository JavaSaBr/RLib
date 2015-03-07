package rlib.util.dictionary;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.PoolFactory;

/**
 * Базовая реализация словаря с объектным ключем.
 * 
 * @author Ronn
 */
public abstract class AbstractObjectDictionary<K, V> extends AbstractDictionary<K, V> implements UnsafeObjectDictionary<K, V> {

	/** пул ячеяк */
	private final FoldablePool<ObjectDictionaryEntry<K, V>> entryPool;

	/** масив ячеяк словаря */
	private ObjectDictionaryEntry<K, V>[] content;

	/** следующий размер для метода изминения размера (capacity * load factor) */
	private int threshold;

	/** фактор загружеености */
	private float loadFactor;

	protected AbstractObjectDictionary() {
		this(Dictionary.DEFAULT_LOAD_FACTOR, Dictionary.DEFAULT_INITIAL_CAPACITY);
	}

	protected AbstractObjectDictionary(final float loadFactor) {
		this(loadFactor, Dictionary.DEFAULT_INITIAL_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	protected AbstractObjectDictionary(final float loadFactor, final int initCapacity) {
		this.loadFactor = loadFactor;
		this.threshold = (int) (initCapacity * loadFactor);
		this.content = new ObjectDictionaryEntry[Dictionary.DEFAULT_INITIAL_CAPACITY];
		this.entryPool = PoolFactory.newFoldablePool(ObjectDictionaryEntry.class);
	}

	protected AbstractObjectDictionary(final int initCapacity) {
		this(Dictionary.DEFAULT_LOAD_FACTOR, initCapacity);
	}

	@Override
	public void accept(final BiConsumer<? super K, ? super V> consumer) {
		for(ObjectDictionaryEntry<K, V> entry : content()) {
			while(entry != null) {
				consumer.accept(entry.getKey(), entry.getValue());
				entry = entry.getNext();
			}
		}
	}

	/**
	 * Добавляет новую ячейку в таблицу.
	 *
	 * @param hash хэш значение.
	 * @param key значение ключа.
	 * @param value значение по ключу.
	 * @param index индекс ячейки.
	 */
	private final void addEntry(final int hash, final K key, final V value, final int index) {

		final ObjectDictionaryEntry<K, V>[] content = content();
		final ObjectDictionaryEntry<K, V> entry = content[index];
		ObjectDictionaryEntry<K, V> newEntry = entryPool.take();

		if(newEntry == null) {
			newEntry = new ObjectDictionaryEntry<K, V>();
		}

		newEntry.set(hash, key, value, entry);
		content[index] = newEntry;

		if(incrementSizeAndGet() >= threshold) {
			resize(2 * content.length);
		}
	}

	@Override
	public void apply(final Function<? super V, V> function) {
		for(ObjectDictionaryEntry<K, V> entry : content()) {
			while(entry != null) {
				entry.setValue(function.apply(entry.getValue()));
				entry = entry.getNext();
			}
		}
	}

	@Override
	public void clear() {

		final FoldablePool<ObjectDictionaryEntry<K, V>> entryPool = getEntryPool();

		final ObjectDictionaryEntry<K, V>[] content = content();
		ObjectDictionaryEntry<K, V> next = null;

		for(ObjectDictionaryEntry<K, V> entry : content) {
			while(entry != null) {
				next = entry.getNext();
				entryPool.put(entry);
				entry = next;
			}
		}

		ArrayUtils.clear(content);
	}

	@Override
	public final boolean containsKey(final K key) {
		return getEntry(key) != null;
	}

	@Override
	public final boolean containsValue(final V value) {

		if(value == null) {
			throw new NullPointerException("value is null.");
		}

		for(final ObjectDictionaryEntry<K, V> element : content()) {
			for(ObjectDictionaryEntry<K, V> entry = element; entry != null; entry = entry.getNext()) {
				if(value.equals(entry.getValue())) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void forEach(final Consumer<? super V> consumer) {
		for(ObjectDictionaryEntry<K, V> entry : content()) {
			while(entry != null) {
				consumer.accept(entry.getValue());
				entry = entry.getNext();
			}
		}
	}

	@Override
	public final V get(final K key) {

		if(key == null) {
			throw new NullPointerException("key is null.");
		}

		final ObjectDictionaryEntry<K, V> entry = getEntry(key);
		return entry == null ? null : entry.getValue();
	}

	/**
	 * Получение ячейки по ключу.
	 *
	 * @param key ключ ячейки.
	 * @return ячейка.
	 */
	private final ObjectDictionaryEntry<K, V> getEntry(final K key) {

		final int hash = hash(key.hashCode());

		final ObjectDictionaryEntry<K, V>[] content = content();

		for(ObjectDictionaryEntry<K, V> entry = content[indexFor(hash, content.length)]; entry != null; entry = entry.getNext()) {
			if(entry.getHash() == hash && key.equals(entry.getKey())) {
				return entry;
			}
		}

		return null;
	}

	/**
	 * @return пул ячеяк.
	 */
	protected FoldablePool<ObjectDictionaryEntry<K, V>> getEntryPool() {
		return entryPool;
	}

	@Override
	public DictionaryType getType() {
		return DictionaryType.OBJECT;
	}

	@Override
	public final Iterator<V> iterator() {
		return new ObjectDictionaryIterator<>(this);
	}

	@Override
	public final Array<K> keyArray(final Array<K> container) {

		for(ObjectDictionaryEntry<K, V> entry : content()) {
			while(entry != null) {
				container.add(entry.getKey());
				entry = entry.getNext();
			}
		}

		return container;
	}

	@Override
	public Array<K> keyArray(final Class<K> type) {
		return keyArray(ArrayFactory.newArray(type));
	}

	@Override
	@SuppressWarnings("unchecked")
	public void moveTo(final Dictionary<? super K, ? super V> dictionary) {

		if(isEmpty() || dictionary.getType() != getType()) {
			return;
		}

		final ObjectDictionary<K, V> objectDictionary = (ObjectDictionary<K, V>) dictionary;

		super.moveTo(objectDictionary);

		for(ObjectDictionaryEntry<K, V> entry : content()) {
			while(entry != null) {
				objectDictionary.put(entry.getKey(), entry.getValue());
				entry = entry.getNext();
			}
		}
	}

	@Override
	public final V put(final K key, final V value) {

		if(key == null) {
			throw new NullPointerException("key is null.");
		}

		final ObjectDictionaryEntry<K, V>[] content = content();

		final int hash = hash(key.hashCode());
		final int i = indexFor(hash, content.length);

		for(ObjectDictionaryEntry<K, V> entry = content[i]; entry != null; entry = entry.getNext()) {
			if(entry.getHash() == hash && key.equals(entry.getKey())) {
				return entry.setValue(value);
			}
		}

		addEntry(hash, key, value, i);

		return null;
	}

	@Override
	public final V remove(final K key) {

		if(key == null) {
			throw new NullPointerException("key is null.");
		}

		final ObjectDictionaryEntry<K, V> old = removeEntryForKey(key);
		final V value = old == null ? null : old.getValue();

		final FoldablePool<ObjectDictionaryEntry<K, V>> pool = getEntryPool();
		pool.put(old);

		return value;
	}

	@Override
	public ObjectDictionaryEntry<K, V> removeEntryForKey(final K key) {

		final ObjectDictionaryEntry<K, V>[] content = content();

		final int hash = hash(key.hashCode());
		final int i = indexFor(hash, content.length);

		ObjectDictionaryEntry<K, V> prev = content[i];
		ObjectDictionaryEntry<K, V> entry = prev;

		while(entry != null) {

			final ObjectDictionaryEntry<K, V> next = entry.getNext();

			if(entry.getHash() == hash && key.equals(entry.getKey())) {

				decrementSizeAndGet();

				if(prev == entry) {
					content[i] = next;
				} else {
					prev.setNext(next);
				}

				return entry;
			}

			prev = entry;
			entry = next;
		}

		return entry;
	}

	/**
	 * Перестройка таблицы под новый размер.
	 *
	 * @param newLength новый размер.
	 */
	@SuppressWarnings("unchecked")
	private final void resize(final int newLength) {

		final ObjectDictionaryEntry<K, V>[] oldContent = content();

		final int oldLength = oldContent.length;

		if(oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
			threshold = Integer.MAX_VALUE;
			return;
		}

		final ObjectDictionaryEntry<K, V>[] newContent = new ObjectDictionaryEntry[newLength];
		transfer(newContent);

		this.content = newContent;
		this.threshold = (int) (newLength * loadFactor);
	}

	@Override
	public ObjectDictionaryEntry<K, V>[] content() {
		return content;
	}

	@Override
	public final String toString() {

		final int size = size();

		final StringBuilder builder = new StringBuilder(getClass().getSimpleName());
		builder.append(" size = ").append(size).append(" : ");

		final ObjectDictionaryEntry<K, V>[] table = content();

		for(int i = 0, length = table.length; i < length; i++) {

			ObjectDictionaryEntry<K, V> entry = table[i];

			while(entry != null) {
				builder.append("[").append(entry.getKey()).append(" - ").append(entry.getValue()).append("]\n");
				entry = entry.getNext();
			}
		}

		if(size > 0) {
			builder.replace(builder.length() - 1, builder.length(), ".");
		}

		return builder.toString();
	}

	/**
	 * Перенос всех записей из старой таблице в новую.
	 *
	 * @param newTable новая таблица.
	 */
	private final void transfer(final ObjectDictionaryEntry<K, V>[] newTable) {

		final ObjectDictionaryEntry<K, V>[] original = content;
		final int newCapacity = newTable.length;

		for(int j = 0, length = original.length; j < length; j++) {

			ObjectDictionaryEntry<K, V> entry = original[j];

			if(entry == null) {
				continue;
			}

			do {

				final ObjectDictionaryEntry<K, V> next = entry.getNext();

				final int i = indexFor(entry.getHash(), newCapacity);

				entry.setNext(newTable[i]);
				newTable[i] = entry;
				entry = next;

			} while(entry != null);
		}
	}

	@Override
	public Array<V> values(final Array<V> container) {

		for(ObjectDictionaryEntry<K, V> entry : content()) {
			while(entry != null) {
				container.add(entry.getValue());
				entry = entry.getNext();
			}
		}

		return container;
	}

	@Override
	public Array<V> values(final Class<V> type) {
		return values(ArrayFactory.newArray(type));
	}
}
