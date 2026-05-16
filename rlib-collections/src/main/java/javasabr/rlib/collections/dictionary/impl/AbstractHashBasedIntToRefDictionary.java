package javasabr.rlib.collections.dictionary.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.IntArray;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.collections.array.MutableIntArray;
import javasabr.rlib.collections.array.UnsafeMutableArray;
import javasabr.rlib.collections.array.UnsafeMutableIntArray;
import javasabr.rlib.collections.dictionary.IntToRefDictionary;
import javasabr.rlib.collections.dictionary.LinkedHashIntToRefEntry;
import javasabr.rlib.collections.dictionary.UnsafeIntToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.util.LinkedEntryUtils;
import javasabr.rlib.functions.IntObjConsumer;
import org.jspecify.annotations.Nullable;

public abstract class AbstractHashBasedIntToRefDictionary<V, E extends LinkedHashIntToRefEntry<V, E>>
    extends AbstractHashBasedDictionary<Integer, V>
    implements UnsafeIntToRefDictionary<V, E> {

  @Override
  public boolean containsKey(Integer key) {
    return findEntry(key) != null;
  }

  @Override
  public boolean containsKey(int key) {
    return findEntry(key) != null;
  }

  @Override
  public boolean containsValue(V value) {
    for (E entry : entries()) {
      for (E next = entry; next != null; next = next.next()) {
        if (Objects.equals(value, next.value())) {
          return true;
        }
      }
    }
    return false;
  }

  @Nullable
  @Override
  public V get(Integer key) {
    return get(key.intValue());
  }

  @Nullable
  @Override
  public V get(int key) {
    E entry = findEntry(key);
    return entry == null ? null : entry.value();
  }

  @Nullable
  @Override
  public V getOrDefault(Integer key, V def) {
    return getOrDefault(key.intValue(), def);
  }

  @Nullable
  @Override
  public V getOrDefault(int key, V def) {
    E entry = findEntry(key);
    return entry == null ? def : entry.value();
  }

  @Override
  public Optional<V> getOptional(Integer key) {
    return getOptional(key.intValue());
  }

  @Override
  public Optional<V> getOptional(int key) {
    return Optional.ofNullable(get(key));
  }

  @Override
  public Iterator<V> iterator() {
    if (isEmpty()) {
      return Collections.emptyIterator();
    } else {
      return new LinkedRefEntryIterator<>(entries());
    }
  }

  @Nullable
  protected E findEntry(int key) {
    @Nullable E[] entries = entries();
    int hash = hash(key);
    int entryIndex = indexFor(hash, entries.length);
    for (E entry = entries[entryIndex]; entry != null; entry = entry.next()) {
      if (entry.hash() == hash && key == entry.key()) {
        return entry;
      }
    }
    return null;
  }

  @Override
  public void forEach(IntObjConsumer<V> consumer) {
    for (E entry : entries()) {
      while (entry != null) {
        //noinspection DataFlowIssue
        consumer.accept(entry.key(), entry.value());
        entry = entry.next();
      }
    }
  }

  @Override
  public IntArray keys() {
    if (isEmpty()) {
      return IntArray.empty();
    } else {
      return IntArray.copyOf(keys(ArrayFactory.mutableIntArray()));
    }
  }

  @Override
  public MutableIntArray keys(MutableIntArray container) {
    if (isEmpty()) {
      return container;
    }
    UnsafeMutableIntArray unsafe = container.asUnsafe();
    unsafe.prepareForSize(container.size() + size());
    for (E entry : entries()) {
      while (entry != null) {
        unsafe.unsafeAdd(entry.key());
        entry = entry.next();
      }
    }
    return container;
  }

  @Override
  public Array<Integer> keys(Class<Integer> type) {
    if (isEmpty()) {
      return Array.empty(type);
    } else {
      return keys(MutableArray.ofType(type));
    }
  }

  @Override
  public MutableArray<Integer> keys(MutableArray<Integer> container) {
    if (isEmpty()) {
      return container;
    }
    UnsafeMutableArray<Integer> unsafe = container.asUnsafe();
    unsafe.prepareForSize(container.size() + size());
    for (E entry : entries()) {
      while (entry != null) {
        unsafe.unsafeAdd(entry.key());
        entry = entry.next();
      }
    }
    return container;
  }

  @Override
  public <C extends Collection<Integer>> C keys(C container) {
    if (isEmpty()) {
      return container;
    }
    for (E entry : entries()) {
      while (entry != null) {
        container.add(entry.key());
        entry = entry.next();
      }
    }
    return container;
  }

  @Override
  public Array<V> values(Class<V> type) {
    if (isEmpty()) {
      return Array.empty(type);
    } else {
      return Array.copyOf(values(ArrayFactory.mutableArray(type, size())));
    }
  }

  @Override
  public <C extends Collection<V>> C values(C container) {
    return LinkedEntryUtils.values(entries(), size(), container);
  }

  @Override
  public MutableArray<V> values(MutableArray<V> container) {
    return LinkedEntryUtils.values(entries(), size(), container);
  }

  @Override
  public int values(MutableArray<V> container, int startIndex, int limit) {
    return LinkedEntryUtils.values(entries(), size(), container, startIndex, limit);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof IntToRefDictionary<?> another)) {
      return false;
    } else if (size() != another.size()) {
      return false;
    }
    for (E entry : entries()) {
      while (entry != null) {
        if (!another.containsKey(entry.key())) {
          return false;
        }
        V value = entry.value();
        if (!Objects.equals(value, another.get(entry.key()))) {
          return false;
        }
        entry = entry.next();
      }
    }
    return true;
  }
  
  @Override
  public String toString() {
    return LinkedEntryUtils.toString(
        entries(),
        size(),
        (builder, entry) -> builder.append(entry.key()));
  }
}
