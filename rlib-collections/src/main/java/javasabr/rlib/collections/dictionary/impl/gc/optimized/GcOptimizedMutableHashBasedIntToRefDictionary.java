package javasabr.rlib.collections.dictionary.impl.gc.optimized;

import java.util.Arrays;
import java.util.Deque;
import javasabr.rlib.collections.deque.DequeFactory;
import javasabr.rlib.collections.dictionary.IntToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.AbstractMutableHashBasedIntToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.ImmutableHashBasedIntToRefDictionary;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class GcOptimizedMutableHashBasedIntToRefDictionary<V> extends
    AbstractMutableHashBasedIntToRefDictionary<V, ReusableLinkedHashIntToRefEntry<V>> {

  private static final int MAX_POOL_SIZE = 40;

  final Deque<ReusableLinkedHashIntToRefEntry<V>> entryPool;
  @Nullable ReusableLinkedHashIntToRefEntry<V>[] entries;
  int size;
  int threshold;

  public GcOptimizedMutableHashBasedIntToRefDictionary() {
    this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
  }

  public GcOptimizedMutableHashBasedIntToRefDictionary(int initCapacity, float loadFactor) {
    super(loadFactor);
    //noinspection unchecked
    this.entries = new ReusableLinkedHashIntToRefEntry[initCapacity];
    this.threshold = (int) (initCapacity * loadFactor);
    this.entryPool = DequeFactory.arrayBased(ReusableLinkedHashIntToRefEntry.class);
  }

  @Override
  public boolean isEmpty() {
    return size < 1;
  }

  @Override
  protected int incrementSize() {
    return size++;
  }

  @Override
  protected int decrementSize() {
    return size--;
  }

  @Override
  public void clear() {
    for (ReusableLinkedHashIntToRefEntry<V> entry : entries()) {
      while (entry != null) {
        ReusableLinkedHashIntToRefEntry<V> next = entry.next();
        deallocate(entry);
        entry = next;
      }
    }
    Arrays.fill(entries, null);
    size = 0;
  }

  @Override
  protected void threshold(int threshold) {
    this.threshold = threshold;
  }

  @Override
  protected void entries(@Nullable ReusableLinkedHashIntToRefEntry<V>[] entries) {
    this.entries = entries;
  }

  @Nullable
  @Override
  public V remove(int key) {
    ReusableLinkedHashIntToRefEntry<V> entry = removeEntryForKey(key);
    if (entry == null) {
      return null;
    }
    V value = entry.value();
    deallocate(entry);
    return value;
  }

  @Override
  protected ReusableLinkedHashIntToRefEntry<V> allocate(
      int hash,
      int key,
      @Nullable V value,
      @Nullable ReusableLinkedHashIntToRefEntry<V> next) {
    ReusableLinkedHashIntToRefEntry<V> reused = entryPool.pollLast();
    if (reused != null) {
      return reused.reinit(next, key, value, hash);
    }
    return new ReusableLinkedHashIntToRefEntry<>(next, key, value, hash);
  }

  private void deallocate(ReusableLinkedHashIntToRefEntry<V> entry) {
    if (entryPool.size() < MAX_POOL_SIZE) {
      entryPool.addLast(entry.clear());
    }
  }

  @Nullable
  @Override
  protected ReusableLinkedHashIntToRefEntry<V>[] allocate(int length) {
    //noinspection unchecked
    return new ReusableLinkedHashIntToRefEntry[length];
  }

  @Override
  public IntToRefDictionary<V> toReadOnly() {
    @Nullable ReusableLinkedHashIntToRefEntry<V>[] copied = Arrays.copyOf(entries, entries.length);
    return new ImmutableHashBasedIntToRefDictionary<>(copied, size);
  }
}
