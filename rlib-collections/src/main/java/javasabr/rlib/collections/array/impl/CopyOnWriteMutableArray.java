package javasabr.rlib.collections.array.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.concurrent.atomic.AtomicReference;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.UnsafeMutableArray;
import javasabr.rlib.common.ThreadSafe;
import javasabr.rlib.common.util.ArrayUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class CopyOnWriteMutableArray<E> extends AbstractMutableArray<E> implements ThreadSafe {

  protected static final int LIMIT_ATTEMPTS = 1000;

  AtomicReference<@Nullable E[]> wrapped;

  public CopyOnWriteMutableArray(Class<? super E> type) {
    super(type);
    this.wrapped = new AtomicReference<>(ArrayUtils.create(type, 0));
  }

  @Override
  protected void size(int size) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isEmpty() {
    return wrapped.get().length < 1;
  }

  @Override
  public int size() {
    return wrapped.get().length;
  }

  @Override
  public @Nullable E[] wrapped() {
    return wrapped.get();
  }

  @Override
  protected void wrapped(@Nullable E[] wrapped) {
    this.wrapped.set(wrapped);
  }

  @Override
  public boolean add(E element) {
    unsafeAdd(element);
    return true;
  }

  @Override
  public UnsafeMutableArray<E> unsafeAdd(E element) {
    for (int i = 0; i < LIMIT_ATTEMPTS; i++) {
      @Nullable E[] currentArray = wrapped.get();
      @Nullable E[] copy = Arrays.copyOf(currentArray, currentArray.length + 1);
      copy[currentArray.length] = element;
      if (wrapped.compareAndSet(currentArray, copy)) {
        return this;
      }
    }
    throw new ConcurrentModificationException("Cannot successfully add new element");
  }

  @Override
  protected void processAdd(E[] array, int currentSize, int elementsToAdd) {
    for (int i = 0; i < LIMIT_ATTEMPTS; i++) {
      @Nullable E[] currentArray = wrapped.get();
      @Nullable E[] copy = Arrays.copyOf(currentArray, currentSize + elementsToAdd);
      System.arraycopy(array, 0, copy, currentSize, elementsToAdd);
      if (wrapped.compareAndSet(currentArray, copy)) {
        return;
      }
    }
    throw new ConcurrentModificationException("Cannot add new elements");
  }

  @Override
  protected void processAdd(Array<? extends E> array, int currentSize, int elementsToAdd) {
    for (int i = 0; i < LIMIT_ATTEMPTS; i++) {
      @Nullable E[] currentArray = wrapped.get();
      @Nullable E[] copy = Arrays.copyOf(currentArray, currentSize + elementsToAdd);
      System.arraycopy(array.asUnsafe().wrapped(), 0, copy, currentSize, elementsToAdd);
      if (wrapped.compareAndSet(currentArray, copy)) {
        return;
      }
    }
    throw new ConcurrentModificationException("Cannot add new elements");
  }

  @Override
  public boolean addAll(Collection<? extends E> collection) {
    if (collection.isEmpty()) {
      return false;
    }
    for (int i = 0; i < LIMIT_ATTEMPTS; i++) {
      @Nullable E[] currentArray = wrapped.get();
      int currentSize = currentArray.length;

      @Nullable E[] copy = Arrays.copyOf(currentArray, currentSize + collection.size());
      for (E element : collection) {
        copy[currentSize++] = element;
      }

      if (wrapped.compareAndSet(currentArray, copy)) {
        return true;
      }
    }
    throw new ConcurrentModificationException("Cannot add new elements");
  }

  @Override
  public UnsafeMutableArray<E> unsafeSet(int index, E element) {
    for (int i = 0; i < LIMIT_ATTEMPTS; i++) {
      @Nullable E[] currentArray = wrapped.get();
      @Nullable E[] copy = Arrays.copyOf(currentArray, currentArray.length);
      copy[index] = element;
      if (wrapped.compareAndSet(currentArray, copy)) {
        return this;
      }
    }
    throw new ConcurrentModificationException("Cannot successfully set new element");
  }

  @Override
  public E unsafeRemove(int index) {

    for (int i = 0; i < LIMIT_ATTEMPTS; i++) {

      @Nullable E[] currentArray = wrapped.get();
      int currentSize = currentArray.length;
      int lastInex = currentSize - 1;

      @Nullable E[] copy = Arrays.copyOf(currentArray, lastInex);

      if (index == lastInex) {
        if (wrapped.compareAndSet(currentArray, copy)) {
          return currentArray[index];
        }
        continue;
      }

      int numMoved = currentSize - index - 1;

      if (numMoved > 0) {
        System.arraycopy(currentArray, index + 1, copy, index, numMoved);
      }

      if (wrapped.compareAndSet(currentArray, copy)) {
        return currentArray[index];
      }
    }
    throw new ConcurrentModificationException("Cannot successfully remove element");
  }

  @Override
  public void clear() {
    if (isEmpty()) {
      return;
    }

    @Nullable E[] emptyArray = null;

    for (int i = 0; i < LIMIT_ATTEMPTS; i++) {
      @Nullable E[] currentArray = wrapped.get();
      if (currentArray.length < 1) {
        return;
      }
      if (emptyArray == null) {
        emptyArray = ArrayUtils.create(type, 0);
      }
      if (wrapped.compareAndSet(currentArray, emptyArray)) {
        return;
      }
    }
    throw new ConcurrentModificationException("Cannot successfully clear this array");
  }

  @Override
  public E unsafeGet(int index) {
    return wrapped.get()[index];
  }

  /* disabled methods */

  @Override
  public UnsafeMutableArray<E> prepareForSize(int expectedSize) {
    return this;
  }

  @Override
  public UnsafeMutableArray<E> trimToSize() {
    return this;
  }

  @Override
  protected int getAndIncrementSize() {
    return 0;
  }

  @Override
  protected int decrementAnGetSize() {
    return 0;
  }

  @Override
  public void sort() {
    for (int i = 0; i < LIMIT_ATTEMPTS; i++) {
      @Nullable E[] original = wrapped.get();
      @Nullable E[] copy = Arrays.copyOf(original, original.length);
      sortInternalArray(copy, copy.length);
      if (wrapped.compareAndSet(original, copy)) {
        return;
      }
    }
    throw new ConcurrentModificationException("Cannot successfully sort this array");
  }

  @Override
  public void sort(Comparator<E> comparator) {
    for (int i = 0; i < LIMIT_ATTEMPTS; i++) {
      @Nullable E[] original = wrapped.get();
      @Nullable E[] copy = Arrays.copyOf(original, original.length);
      Arrays.sort(copy, comparator);
      if (wrapped.compareAndSet(original, copy)) {
        return;
      }
    }
    throw new ConcurrentModificationException("Cannot successfully sort this array");
  }
}
