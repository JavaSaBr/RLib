package javasabr.rlib.collections.dictionary.impl.gc.optimized;

import javasabr.rlib.collections.dictionary.LinkedHashIntToRefEntry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

@Getter
@Setter
@Accessors(fluent = true, chain = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReusableLinkedHashIntToRefEntry<V> implements
    LinkedHashIntToRefEntry<V, ReusableLinkedHashIntToRefEntry<V>> {

  @Nullable
  ReusableLinkedHashIntToRefEntry<V> next;

  int key;
  @Nullable
  V value;

  int hash;

  public ReusableLinkedHashIntToRefEntry(
      @Nullable ReusableLinkedHashIntToRefEntry<V> next,
      int key,
      @Nullable V value,
      int hash) {
    this.next = next;
    this.key = key;
    this.value = value;
    this.hash = hash;
  }

  public ReusableLinkedHashIntToRefEntry<V> clear() {
    this.next = null;
    this.value = null;
    return this;
  }

  public ReusableLinkedHashIntToRefEntry<V> reinit(
      @Nullable ReusableLinkedHashIntToRefEntry<V> next,
      int key,
      @Nullable V value,
      int hash) {
    this.next = next;
    this.key = key;
    this.value = value;
    this.hash = hash;
    return this;
  }
}
