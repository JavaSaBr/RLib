package javasabr.rlib.common.util.dictionary;

import java.util.Iterator;
import javasabr.rlib.common.function.FourObjectConsumer;
import javasabr.rlib.common.function.NotNullBiConsumer;
import javasabr.rlib.common.function.NotNullBiFunction;
import javasabr.rlib.common.function.NotNullFunction;
import javasabr.rlib.common.function.NotNullSupplier;
import javasabr.rlib.common.function.NotNullTripleConsumer;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.common.util.array.Array;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The base implementation of the {@link ObjectDictionary}.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
@NullMarked
public abstract class AbstractObjectDictionary<K, V> extends AbstractDictionary<K, V, ObjectEntry<K, V>> implements
    UnsafeObjectDictionary<K, V> {

  protected AbstractObjectDictionary() {
    this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
  }

  protected AbstractObjectDictionary(float loadFactor) {
    this(loadFactor, DEFAULT_INITIAL_CAPACITY);
  }

  protected AbstractObjectDictionary(int initCapacity) {
    this(DEFAULT_LOAD_FACTOR, initCapacity);
  }

  protected AbstractObjectDictionary(float loadFactor, int initCapacity) {
    super(loadFactor, initCapacity);
  }

  @Override
  protected Class<? super ObjectEntry<K, V>> getEntryType() {
    return ObjectEntry.class;
  }

  /**
   * Add new entry to this dictionary.
   *
   * @param hash the hash of the key.
   * @param key the key.
   * @param value the value of the key.
   * @param index the index of bucket.
   */
  private void addEntry(int hash, K key, V value, int index) {

    var entries = entries();
    var entry = entries[index];

    var newEntry = entryPool.take(ObjectEntry::new);
    newEntry.set(hash, key, value, entry);

    entries[index] = newEntry;

    if (incrementSizeAndGet() >= getThreshold()) {
      resize(2 * entries.length);
    }
  }

  @Override
  public final boolean containsKey(K key) {
    return getEntry(key) != null;
  }

  @Override
  public @Nullable V get(K key) {
    var entry = getEntry(key);
    return entry == null ? null : entry.getValue();
  }

  @Override
  public V getOrCompute(K key, NotNullSupplier<V> factory) {

    var entry = getEntry(key);

    if (entry == null) {
      put(key, factory.get());
      entry = getEntry(key);
    }

    if (entry == null) {
      throw new IllegalStateException("Factory " + factory + " returned a null value.");
    }

    //noinspection ConstantConditions
    return entry.getValue();
  }

  @Override
  public V getOrCompute(K key, NotNullFunction<K, V> factory) {

    var entry = getEntry(key);

    if (entry == null) {
      put(key, factory.apply(key));
      entry = getEntry(key);
    }

    if (entry == null) {
      throw new IllegalStateException("Factory " + factory + " returned a null value.");
    }

    //noinspection ConstantConditions
    return entry.getValue();
  }

  @Override
  public <T> V getOrCompute(K key, T argument, NotNullFunction<T, V> factory) {

    var entry = getEntry(key);

    if (entry == null) {
      put(key, factory.apply(argument));
      entry = getEntry(key);
    }

    if (entry == null) {
      throw new IllegalStateException("Factory " + factory + " returned a null value.");
    }

    //noinspection ConstantConditions
    return entry.getValue();
  }

  @Override
  public <T> V getOrCompute(K key, T argument, NotNullBiFunction<K, T, V> factory) {

    var entry = getEntry(key);

    if (entry == null) {
      put(key, factory.apply(key, argument));
      entry = getEntry(key);
    }

    if (entry == null) {
      throw new IllegalStateException("Factory " + factory + " returned a null value.");
    }

    //noinspection ConstantConditions
    return entry.getValue();
  }

  /**
   * Get an entry for the key.
   *
   * @param key the key.
   * @return the entry or null.
   */
  private @Nullable ObjectEntry<K, V> getEntry(K key) {

    var entries = entries();

    var hash = hash(key.hashCode());
    var entryIndex = indexFor(hash, entries.length);

    for (var entry = entries[entryIndex]; entry != null; entry = entry.getNext()) {
      if (entry.getHash() == hash && key.equals(entry.getKey())) {
        return entry;
      }
    }

    return null;
  }

  @Override
  public final Iterator<V> iterator() {
    return new ObjectDictionaryIterator<>(this);
  }

  @Override
  public final Array<K> keyArray(Array<K> container) {

    var unsafeArray = container.asUnsafe();
    unsafeArray.prepareForSize(container.size() + size());

    for (var entry : entries()) {
      while (entry != null) {
        //noinspection ConstantConditions
        unsafeArray.unsafeAdd(entry.getKey());
        entry = entry.getNext();
      }
    }

    return container;
  }

  @Override
  public void copyTo(Dictionary<? super K, ? super V> dictionary) {

    if (isEmpty() || !(dictionary instanceof ObjectDictionary)) {
      return;
    }

    var target = ClassUtils.<ObjectDictionary<K, V>>unsafeNNCast(dictionary);

    for (var entry : entries()) {
      while (entry != null) {
        //noinspection ConstantConditions
        target.put(entry.getKey(), entry.getValue());
        entry = entry.getNext();
      }
    }
  }

  @Override
  public @Nullable V put(K key, V value) {

    var entries = entries();

    var hash = hash(key.hashCode());
    var entryIndex = indexFor(hash, entries.length);

    for (var entry = entries[entryIndex]; entry != null; entry = entry.getNext()) {
      if (entry.getHash() == hash && key.equals(entry.getKey())) {
        return entry.setValue(value);
      }
    }

    addEntry(hash, key, value, entryIndex);
    return null;
  }

  @Override
  public @Nullable V remove(K key) {

    var old = removeEntryForKey(key);

    if (old == null) {
      return null;
    }

    var value = old.getValue();

    entryPool.put(old);

    return value;
  }

  @Override
  public @Nullable ObjectEntry<K, V> removeEntryForKey(K key) {

    var entries = entries();

    var hash = hash(key.hashCode());
    var entryIndex = indexFor(hash, entries.length);

    var prev = entries[entryIndex];
    var entry = prev;

    while (entry != null) {

      var next = entry.getNext();

      if (entry.getHash() == hash && key.equals(entry.getKey())) {
        decrementSizeAndGet();

        if (prev == entry) {
          entries[entryIndex] = next;
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

    int size = size();

    var builder = new StringBuilder(getClass().getSimpleName())
        .append(" size = ")
        .append(size)
        .append(" : ");

    var entries = entries();

    for (var entry : entries) {
      while (entry != null) {
        builder
            .append("[")
            .append(entry.getKey())
            .append(" - ")
            .append(entry.getValue())
            .append("]\n");
        entry = entry.getNext();
      }
    }

    if (size > 0) {
      builder.replace(builder.length() - 1, builder.length(), ".");
    }

    return builder.toString();
  }

  @Override
  public void forEach(NotNullBiConsumer<? super K, ? super V> consumer) {
    for (var entry : entries()) {
      while (entry != null) {
        consumer.accept(entry.getKey(), entry.getValue());
        entry = entry.getNext();
      }
    }
  }

  @Override
  public <T> void forEach(
      T argument,
      NotNullTripleConsumer<? super T, ? super K, ? super V> consumer) {
    for (var entry : entries()) {
      while (entry != null) {
        consumer.accept(argument, entry.getKey(), entry.getValue());
        entry = entry.getNext();
      }
    }
  }

  @Override
  public <F, S> void forEach(
      F first,
      S second,
      FourObjectConsumer<? super F, ? super S, ? super K, ? super V> consumer) {
    for (var entry : entries()) {
      while (entry != null) {
        consumer.accept(first, second, entry.getKey(), entry.getValue());
        entry = entry.getNext();
      }
    }
  }
}
