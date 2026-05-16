package javasabr.rlib.collections.dictionary.impl.util;

import java.util.Collection;
import java.util.function.BiFunction;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.collections.array.UnsafeMutableArray;
import javasabr.rlib.collections.dictionary.LinkedEntry;
import javasabr.rlib.collections.dictionary.RefEntry;
import org.jspecify.annotations.Nullable;

public class LinkedEntryUtils {

  public static <V, E extends LinkedEntry<E> & RefEntry<V>, C extends Collection<V>> C values(
      @Nullable E[] entries,
      int size,
      C container) {
    if (size < 1) {
      return container;
    }
    for (E entry : entries) {
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
  
  public static <V, E extends LinkedEntry<E> & RefEntry<V>> MutableArray<V> values(
      @Nullable E[] entries,
      int size,
      MutableArray<V> container) {
    if (size < 1) {
      return container;
    }
    UnsafeMutableArray<V> unsafe = container.asUnsafe();
    unsafe.prepareForSize(container.size() + size);
    for (E entry : entries) {
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
  
  public static <V, E extends LinkedEntry<E> & RefEntry<V>> int values(
      @Nullable E[] entries,
      int size,
      MutableArray<V> container,
      int startIndex,
      int limit) {
    if (size < 1) {
      return -1;
    }
    UnsafeMutableArray<V> unsafe = container.asUnsafe();
    unsafe.prepareForSize(container.size() + Math.min(limit, size));
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

  public static <V, E extends LinkedEntry<E> & RefEntry<V>> String toString(
      @Nullable E[] entries,
      int size,
      BiFunction<StringBuilder, E, StringBuilder> keyAppender) {
    if (size < 1) {
      return "[]";
    }
    var builder = new StringBuilder("[");
    for (E entry : entries) {
      while (entry != null) {
        builder.append('\'');
        keyAppender
            .apply(builder, entry)
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
