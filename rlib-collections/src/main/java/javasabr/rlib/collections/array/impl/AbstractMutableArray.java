package javasabr.rlib.collections.array.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.UnsafeMutableArray;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractMutableArray<E> extends AbstractArray<E> implements UnsafeMutableArray<E> {

  protected AbstractMutableArray(Class<? super E> type) {
    super(type);
  }

  @Override
  public boolean add(E element) {
    Objects.requireNonNull(element);
    prepareForSize(size() + 1);
    unsafeAdd(element);
    return true;
  }

  @Override
  public boolean addAll(Array<? extends E> array) {
    if (array.isEmpty()) {
      return false;
    }
    int size = size();
    int elementsToAdd = array.size();
    prepareForSize(size + elementsToAdd);
    processAdd(array, size, elementsToAdd);
    return true;
  }

  @Override
  public boolean addAll(E[] array) {
    if (array.length < 1) {
      return false;
    }
    int size = size();
    int elementsToAdd = array.length;
    prepareForSize(size + elementsToAdd);
    processAdd(array, size, elementsToAdd);
    return true;
  }

  @Override
  public boolean addAll(Collection<? extends E> collection) {
    if (collection.isEmpty()) {
      return false;
    }
    int size = size();
    int elementsToAdd = collection.size();
    prepareForSize(size + elementsToAdd);
    for (E element : collection) {
      Objects.requireNonNull(element);
      unsafeAdd(element);
    }
    return true;
  }

  @Override
  public UnsafeMutableArray<E> unsafeAdd(E element) {
    wrapped()[getAndIncrementSize()] = element;
    return this;
  }

  @Override
  public void replace(int index, E element) {
    checkIndex(index);
    unsafeSet(index, element);
  }

  @Override
  public UnsafeMutableArray<E> unsafeSet(int index, E element) {
    wrapped()[index] = element;
    return this;
  }

  @Override
  public E remove(int index) {
    checkIndex(index);
    return unsafeRemove(index);
  }

  @Override
  public boolean remove(Object element) {
    int index = indexOf(element);
    if (index < 0) {
      return false;
    }
    remove(index);
    return true;
  }

  @Override
  public E unsafeRemove(int index) {

    int numMoved = size() - index - 1;

    @Nullable E[] wrapped = wrapped();
    E element = wrapped[index];

    if (numMoved > 0) {
      System.arraycopy(wrapped, index + 1, wrapped, index, numMoved);
    }

    wrapped[decrementAnGetSize()] = null;

    //noinspection DataFlowIssue
    return element;
  }

  @Override
  public boolean removeAll(Collection<?> collection) {
    if (collection.isEmpty()) {
      return false;
    }
    int removed = 0;
    for (Object element : collection) {
      if (remove(element)) {
        removed++;
      }
    }
    return removed > 0;
  }

  @Override
  public boolean retainAll(Collection<?> collection) {

    if (collection.isEmpty()) {
      int size = size();
      clear();
      return size > 0;
    }

    int removed = 0;
    @Nullable E[] wrapped = wrapped();

    for (int i = 0, length = size(); i < length; i++) {
      if (!collection.contains(wrapped[i])) {
        unsafeRemove(i);
        length--;
        removed++;
      }
    }

    return removed > 0;
  }

  @Override
  public void clear() {
    int size = size();
    if (size > 0) {
      Arrays.fill(wrapped(), 0, size, null);
      size(0);
    }
  }

  @Override
  public Iterator<E> iterator() {
    return new DefaultMutableArrayIterator<>(this);
  }

  @Override
  public Stream<E> stream() {
    return StreamSupport.stream(spliterator(), false);
  }

  @Override
  public Spliterator<E> spliterator() {
    return Spliterators.spliterator(wrapped(), 0, size(), Spliterator.NONNULL);
  }

  protected void processAdd(Array<? extends E> array, int currentSize, int elementsToAdd) {
    System.arraycopy(array.asUnsafe().wrapped(), 0, wrapped(), currentSize, elementsToAdd);
    size(currentSize + elementsToAdd);
  }

  protected void processAdd(E[] array, int currentSize, int elementsToAdd) {
    System.arraycopy(array, 0, wrapped(), currentSize, elementsToAdd);
    size(currentSize + elementsToAdd);
  }

  protected abstract void size(int size);

  protected abstract int getAndIncrementSize();
  protected abstract int decrementAnGetSize();

  protected abstract void wrapped(@Nullable E[] wrapped);

  @Override
  public UnsafeMutableArray<E> prepareForSize(int expectedSize) {
    @Nullable E[] wrapped = wrapped();
    if (expectedSize > wrapped.length) {
      int newLength = Math.max((wrapped.length * 3) / 2, expectedSize);
      wrapped(Arrays.copyOf(wrapped, newLength));
    }
    return this;
  }


  @Override
  public UnsafeMutableArray<E> trimToSize() {
    @Nullable E[] wrapped = wrapped();
    int size = size();

    if (size == wrapped.length) {
      return this;
    }
    wrapped(Arrays.copyOfRange(wrapped, 0, size));
    return this;
  }

  @Override
  public UnsafeMutableArray<E> asUnsafe() {
    return this;
  }

  @Override

  public void sort() {
    sortInternalArray(wrapped(), size());
  }

  @SuppressWarnings({
      "rawtypes",
      "unchecked"
  })
  protected void sortInternalArray(@Nullable E[] array, int size) {
    if (Comparable.class.isAssignableFrom(type())) {
      Comparable[] wrapped = (Comparable[]) array;
      Arrays.sort(wrapped, 0, size, Comparator.naturalOrder());
    } else {
      throw new IllegalStateException(
          "Cannot sort array of non-Comparable elements without an explicit comparator");
    }
  }
  
  @Override
  public void sort(Comparator<E> comparator) {
    Arrays.sort(wrapped(), 0, size(), comparator);
  }

  protected static void validateCapacity(int capacity) {
    if (capacity < 0) {
      throw new IllegalArgumentException("Capacity cannot be negative");
    }
  }
}
