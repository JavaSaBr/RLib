package javasabr.rlib.collections.dictionary.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.collections.array.UnsafeMutableArray;
import javasabr.rlib.collections.dictionary.LinkedHashEntry;
import javasabr.rlib.collections.dictionary.RefToRefDictionary;
import javasabr.rlib.collections.dictionary.UnsafeRefToRefDictionary;
import org.jspecify.annotations.Nullable;

public abstract class AbstractHashBasedRefToRefDictionary<K, V, E extends LinkedHashEntry<K, V, E>>
    extends AbstractHashBasedDictionary<K, V>
    implements UnsafeRefToRefDictionary<K, V, E> {

  @Override
  public boolean containsKey(K key) {
    return findEntry(key) != null;
  }

  @Override
  public boolean containsValue(V value) {

    for (E entry : entries()) {
      for (E nextEntry = entry; nextEntry != null; nextEntry = nextEntry.next()) {
        if (Objects.equals(value, nextEntry.value())) {
          return true;
        }
      }
    }

    return false;
  }

  @Nullable
  @Override
  public V get(K key) {
    E entry = findEntry(key);
    return entry == null ? null : entry.value();
  }

  @Nullable
  @Override
  public V getOrDefault(K key, V def) {
    E entry = findEntry(key);
    return entry == null ? def : entry.value();
  }

  @Override
  public Optional<V> getOptional(K key) {
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
  protected E findEntry(K key) {

    @Nullable E[] entries = entries();
    int hash = hash(key.hashCode());
    int entryIndex = indexFor(hash, entries.length);

    for (E entry = entries[entryIndex]; entry != null; entry = entry.next()) {
      if (entry.hash() == hash && key.equals(entry.key())) {
        return entry;
      }
    }

    return null;
  }

  @Override
  public void forEach(BiConsumer<K, V> consumer) {
    for (E entry : entries()) {
      while (entry != null) {
        //noinspection DataFlowIssue
        consumer.accept(entry.key(), entry.value());
        entry = entry.next();
      }
    }
  }

  @Override
  public Array<K> keys(Class<K> type) {
    if (isEmpty()) {
      return Array.empty(type);
    } else {
      return Array.copyOf(keys(ArrayFactory.mutableArray(type, size())));
    }
  }

  @Override
  public <C extends Collection<K>> C keys(C container) {
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
  public MutableArray<K> keys(MutableArray<K> container) {
    if (isEmpty()) {
      return container;
    }

    UnsafeMutableArray<K> unsafe = container.asUnsafe();
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
  public Array<V> values(Class<V> type) {
    if (isEmpty()) {
      return Array.empty(type);
    } else {
      return Array.copyOf(values(ArrayFactory.mutableArray(type, size())));
    }
  }

  @Override
  public <C extends Collection<V>> C values(C container) {
    if (isEmpty()) {
      return container;
    }
    for (E entry : entries()) {
      while (entry != null) {
        V value = entry.value();
        if (value != null) {
          container.add(value);
        }
        entry = entry.next();
      }
    }
    return container;
  }

  @Override
  public MutableArray<V> values(MutableArray<V> container) {
    if (isEmpty()) {
      return container;
    }
    UnsafeMutableArray<V> unsafe = container.asUnsafe();
    unsafe.prepareForSize(container.size() + size());
    for (E entry : entries()) {
      while (entry != null) {
        V value = entry.value();
        if (value != null) {
          unsafe.unsafeAdd(value);
        }
        entry = entry.next();
      }
    }
    return container;
  }

  @Override
  public int values(MutableArray<V> container, int startIndex, int limit) {
    if (isEmpty()) {
      return -1;
    }
    UnsafeMutableArray<V> unsafe = container.asUnsafe();
    unsafe.prepareForSize(Math.min(limit, size()));
    E[] entries = entries();
    for (int i = startIndex, length = entries.length; i < length; i++) {
      if (unsafe.size() >= limit) {
        return i;
      }
      E entry = entries[i];
      while (entry != null) {
        V value = entry.value();
        if (value != null) {
          unsafe.unsafeAdd(value);
        }
        entry = entry.next();
      }
    }
    return -1;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(entries());
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof RefToRefDictionary<?, ?> another)) {
      return false;
    } else if (size() != another.size()) {
      return false;
    }

    RefToRefDictionary<Object, Object> toCompare = (RefToRefDictionary<Object, Object>) obj;

    for (E entry : entries()) {
      while (entry != null) {
        if (!toCompare.containsKey(entry.key())) {
          return false;
        }
        V value = entry.value();
        Object anotherValue = toCompare.get(entry.key());
        if (!Objects.equals(value, anotherValue)) {
          return false;
        }
        entry = entry.next();
      }
    }
    return true;
  }

  @Override
  public String toString() {
    if (isEmpty()) {
      return "[]";
    }
    StringBuilder builder = new StringBuilder("[");
    for (E entry : entries()) {
      while (entry != null) {
        builder
            .append('\'')
            .append(entry.key())
            .append('\'')
            .append(":")
            .append('\'')
            .append(entry.value())
            .append('\'')
            .append(", ");

        entry = entry.next();
      }
    }
    if (builder.length() > 1) {
      builder.delete(builder.length() - 2, builder.length());
    }
    builder.append("]");
    return builder.toString();
  }
}
