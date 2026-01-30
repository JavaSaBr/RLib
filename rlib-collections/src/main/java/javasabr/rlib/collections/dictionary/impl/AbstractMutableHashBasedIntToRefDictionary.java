package javasabr.rlib.collections.dictionary.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import javasabr.rlib.collections.dictionary.IntToRefDictionary;
import javasabr.rlib.collections.dictionary.LinkedHashIntToRefEntry;
import javasabr.rlib.collections.dictionary.MutableIntToRefDictionary;
import javasabr.rlib.collections.dictionary.UnsafeMutableIntToRefDictionary;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractMutableHashBasedIntToRefDictionary<V, E extends LinkedHashIntToRefEntry<V, E>>
    extends AbstractHashBasedIntToRefDictionary<V, E> implements UnsafeMutableIntToRefDictionary<V, E> {

  protected static final int DEFAULT_INITIAL_CAPACITY = 16;
  protected static final int DEFAULT_MAXIMUM_CAPACITY = 1 << 30;
  protected static final float DEFAULT_LOAD_FACTOR = 0.75f;

  final float loadFactor;

  @Nullable
  @Override
  public V getOrCompute(int key, IntFunction<V> factory) {

    E entry = findEntry(key);
    if (entry != null) {
      return entry.value();
    }

    V newValue = factory.apply(key);
    put(key, newValue);
    return newValue;
  }

  @Nullable
  @Override
  public V getOrCompute(int key, Supplier<V> factory) {
    E entry = findEntry(key);
    if (entry != null) {
      return entry.value();
    }

    V newValue = factory.get();
    put(key, newValue);
    return newValue;
  }

  @Nullable
  @Override
  public <T> V getOrCompute(int key, T arg1, Function<T, V> factory) {
    E entry = findEntry(key);
    if (entry != null) {
      return entry.value();
    }

    V newValue = factory.apply(arg1);
    put(key, newValue);
    return newValue;
  }

  @Nullable
  @Override
  public V put(int key, V value) {

    @Nullable E[] entries = entries();
    int hash = hash(key);
    int entryIndex = indexFor(hash, entries.length);

    for (E entry = entries[entryIndex]; entry != null; entry = entry.next()) {
      if (entry.hash() == hash && key == entry.key()) {
        V prev = entry.value();
        entry.value(value);
        return prev;
      }
    }

    addEntry(hash, key, value, entryIndex);
    return null;
  }

  @Nullable
  @Override
  public V putIfAbsent(int key, V value) {
    @Nullable E[] entries = entries();
    int hash = hash(key);
    int entryIndex = indexFor(hash, entries.length);

    for (E entry = entries[entryIndex]; entry != null; entry = entry.next()) {
      if (entry.hash() == hash && key == entry.key()) {
        return entry.value();
      }
    }

    addEntry(hash, key, value, entryIndex);
    return null;
  }
  
  @Override
  public Optional<V> putOptional(int key, V value) {
    return Optional.ofNullable(put(key, value));
  }

  @Override
  public void putAll(IntToRefDictionary<? extends V> dictionary) {
    if (dictionary instanceof AbstractHashBasedIntToRefDictionary<?, ?> hashBased) {
      for (var entry : hashBased.entries()) {
        while (entry != null) {
          //noinspection DataFlowIssue,unchecked
          put(entry.key(), (V) entry.value());
          entry = entry.next();
        }
      }
    } else {
      dictionary.forEach(this::put);
    }
  }

  @Override
  public MutableIntToRefDictionary<V> append(IntToRefDictionary<? extends V> dictionary) {
    putAll(dictionary);
    return this;
  }

  @Nullable
  @Override
  public V remove(int key) {
    E removed = removeEntryForKey(key);
    if (removed == null) {
      return null;
    }
    return removed.value();
  }

  @Override
  public boolean remove(int key, V expectedValue) {
    @Nullable E[] entries = entries();
    int hash = hash(key);
    int entryIndex = indexFor(hash, entries.length);

    E previousEntry = entries[entryIndex];
    E entry = previousEntry;

    while (entry != null) {
      E nextEntry = entry.next();
      if (entry.hash() == hash && key == entry.key()) {
        if (Objects.equals(entry.value(), expectedValue)) {
          decrementSize();
          if (previousEntry == entry) {
            entries[entryIndex] = nextEntry;
          } else {
            previousEntry.next(nextEntry);
          }
          return true;
        } else {
          return false;
        }
      }
      previousEntry = entry;
      entry = nextEntry;
    }

    return false;
  }

  @Override
  public Optional<V> removeOptional(int key) {
    return Optional.ofNullable(remove(key));
  }

  @Nullable
  protected E removeEntryForKey(int key) {

    @Nullable E[] entries = entries();
    int hash = hash(key);
    int entryIndex = indexFor(hash, entries.length);

    E previosEntry = entries[entryIndex];
    E entry = previosEntry;

    while (entry != null) {
      E nextEntry = entry.next();
      if (entry.hash() == hash && key == entry.key()) {
        decrementSize();
        if (previosEntry == entry) {
          entries[entryIndex] = nextEntry;
        } else {
          previosEntry.next(nextEntry);
        }
        return entry;
      }
      previosEntry = entry;
      entry = nextEntry;
    }

    return null;
  }

  protected void addEntry(int hash, int key, @Nullable V value, int index) {

    @Nullable E[] entries = entries();
    E currentEntry = entries[index];

    var newEntry = allocate(hash, key, value, currentEntry);
    entries[index] = newEntry;

    if (incrementSize() >= threshold()) {
      resize(2 * entries.length);
    }
  }

  protected abstract int threshold();
  protected abstract void threshold(int threshold);

  protected abstract int incrementSize();
  protected abstract int decrementSize();

  protected abstract void entries(@Nullable E[] entries);

  protected abstract E allocate(int hash, int key, @Nullable V value, @Nullable E next);

  @Nullable
  protected abstract E[] allocate(int length);

  protected final void resize(int newLength) {

    @Nullable E[] currentEntries = entries();
    int oldLength = currentEntries.length;

    if (oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
      threshold(Integer.MAX_VALUE);
      return;
    }

    @Nullable E[] newEntries = allocate(newLength);

    transfer(currentEntries, newEntries);
    entries(newEntries);
    threshold((int) (newLength * loadFactor));
  }

  protected void transfer(
      @Nullable E[] currentEntries,
      @Nullable E[] newEntries) {

    int newCapacity = newEntries.length;

    for (E entry : currentEntries) {
      if (entry == null) {
        continue;
      }
      do {

        E nextEntry = entry.next();
        int newIndex = indexFor(entry.hash(), newCapacity);

        entry.next(newEntries[newIndex]);
        newEntries[newIndex] = entry;
        entry = nextEntry;

      } while (entry != null);
    }
  }
}
