package javasabr.rlib.collections.dictionary.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.LongArray;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.collections.array.MutableLongArray;
import javasabr.rlib.collections.array.UnsafeMutableArray;
import javasabr.rlib.collections.array.UnsafeMutableLongArray;
import javasabr.rlib.collections.dictionary.LinkedHashLongToRefEntry;
import javasabr.rlib.collections.dictionary.LongToRefDictionary;
import javasabr.rlib.collections.dictionary.UnsafeLongToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.util.LinkedEntryUtils;
import javasabr.rlib.functions.LongObjConsumer;
import org.jspecify.annotations.Nullable;

public abstract class AbstractHashBasedLongToRefDictionary<V, E extends LinkedHashLongToRefEntry<V, E>>
    extends AbstractHashBasedDictionary<Long, V>
    implements UnsafeLongToRefDictionary<V, E> {

  @Override
  public boolean containsKey(Long key) {
    return findEntry(key) != null;
  }

  @Override
  public boolean containsKey(long key) {
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
  public V get(Long key) {
    return get(key.longValue());
  }

  @Nullable
  @Override
  public V get(long key) {
    E entry = findEntry(key);
    return entry == null ? null : entry.value();
  }

  @Nullable
  @Override
  public V getOrDefault(Long key, V def) {
    return getOrDefault(key.longValue(), def);
  }

  @Nullable
  @Override
  public V getOrDefault(long key, V def) {
    E entry = findEntry(key);
    return entry == null ? def : entry.value();
  }

  @Override
  public Optional<V> getOptional(Long key) {
    return getOptional(key.longValue());
  }

  @Override
  public Optional<V> getOptional(long key) {
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
  protected E findEntry(long key) {
    @Nullable E[] entries = entries();
    int hash = hash(Long.hashCode(key));
    int entryIndex = indexFor(hash, entries.length);
    for (E entry = entries[entryIndex]; entry != null; entry = entry.next()) {
      if (entry.hash() == hash && key == entry.key()) {
        return entry;
      }
    }
    return null;
  }

  @Override
  public void forEach(LongObjConsumer<V> consumer) {
    for (E entry : entries()) {
      while (entry != null) {
        //noinspection DataFlowIssue
        consumer.accept(entry.key(), entry.value());
        entry = entry.next();
      }
    }
  }

  @Override
  public LongArray keys() {
    if (isEmpty()) {
      return LongArray.empty();
    } else {
      return LongArray.copyOf(keys(ArrayFactory.mutableLongArray()));
    }
  }

  @Override
  public MutableLongArray keys(MutableLongArray container) {
    if (isEmpty()) {
      return container;
    }
    UnsafeMutableLongArray unsafe = container.asUnsafe();
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
  public Array<Long> keys(Class<Long> type) {
    if (isEmpty()) {
      return Array.empty(type);
    } else {
      return keys(MutableArray.ofType(type));
    }
  }

  @Override
  public MutableArray<Long> keys(MutableArray<Long> container) {
    if (isEmpty()) {
      return container;
    }
    UnsafeMutableArray<Long> unsafe = container.asUnsafe();
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
  public <C extends Collection<Long>> C keys(C container) {
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
    if (!(obj instanceof LongToRefDictionary<?> another)) {
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
