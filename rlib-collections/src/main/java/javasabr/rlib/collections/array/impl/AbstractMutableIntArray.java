package javasabr.rlib.collections.array.impl;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import javasabr.rlib.collections.array.IntArray;
import javasabr.rlib.collections.array.UnsafeMutableIntArray;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractMutableIntArray extends AbstractIntArray implements UnsafeMutableIntArray {

  @Override
  public boolean add(int value) {
    prepareForSize(size() + 1);
    unsafeAdd(value);
    return true;
  }

  @Override
  public boolean addAll(IntArray array) {
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
  public boolean addAll(int[] array) {
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
  public UnsafeMutableIntArray unsafeAdd(int value) {
    wrapped()[getAndIncrementSize()] = value;
    return this;
  }

  @Override
  public void replace(int index, int value) {
    checkIndex(index);
    unsafeSet(index, value);
  }

  @Override
  public UnsafeMutableIntArray unsafeSet(int index, int value) {
    wrapped()[index] = value;
    return this;
  }

  @Override
  public int removeByIndex(int index) {
    checkIndex(index);
    return unsafeRemoveByIndex(index);
  }

  @Override
  public boolean remove(int value) {
    int index = indexOf(value);
    if (index < 0) {
      return false;
    }
    unsafeRemoveByIndex(index);
    return true;
  }

  @Override
  public boolean removeAll(IntArray array) {
    if (array.isEmpty()) {
      return false;
    }
    int removed = 0;
    for (int value : array) {
      if (remove(value)) {
        removed++;
      }
    }
    return removed > 0;
  }

  @Override
  public boolean removeAll(int[] array) {
    if (array.length < 1) {
      return false;
    }
    int removed = 0;
    for (int value : array) {
      if (remove(value)) {
        removed++;
      }
    }
    return removed > 0;
  }

  @Override
  public int unsafeRemoveByIndex(int index) {
    int numMoved = size() - index - 1;
    int[] wrapped = wrapped();
    int value = wrapped[index];
    if (numMoved > 0) {
      System.arraycopy(wrapped, index + 1, wrapped, index, numMoved);
    }
    wrapped[decrementAnGetSize()] = 0;
    return value;
  }

  @Override
  public void clear() {
    int size = size();
    if (size > 0) {
      Arrays.fill(wrapped(), 0, size, 0);
      size(0);
    }
  }

  @Override
  public IntStream stream() {
    return StreamSupport.intStream(spliterator(), false);
  }

  @Override
  public Spliterator.OfInt spliterator() {
    return Spliterators.spliterator(wrapped(), 0, size(), Spliterator.NONNULL);
  }

  protected void processAdd(IntArray array, int currentSize, int elementsToAdd) {
    System.arraycopy(array.asUnsafe().wrapped(), 0, wrapped(), currentSize, elementsToAdd);
    size(currentSize + elementsToAdd);
  }

  protected void processAdd(int[] array, int currentSize, int elementsToAdd) {
    System.arraycopy(array, 0, wrapped(), currentSize, elementsToAdd);
    size(currentSize + elementsToAdd);
  }

  protected abstract void size(int size);

  protected abstract int getAndIncrementSize();
  protected abstract int decrementAnGetSize();

  protected abstract void wrapped(int[] wrapped);

  @Override
  public UnsafeMutableIntArray prepareForSize(int expectedSize) {
    int[] wrapped = wrapped();
    if (expectedSize > wrapped.length) {
      int newLength = Math.max((wrapped.length * 3) / 2, expectedSize);
      wrapped(Arrays.copyOf(wrapped, newLength));
    }
    return this;
  }


  @Override
  public UnsafeMutableIntArray trimToSize() {
    int[] wrapped = wrapped();
    int size = size();
    if (size == wrapped.length) {
      return this;
    }
    wrapped(Arrays.copyOfRange(wrapped, 0, size));
    return this;
  }

  @Override
  public UnsafeMutableIntArray asUnsafe() {
    return this;
  }

  protected static void validateCapacity(int capacity) {
    if (capacity < 0) {
      throw new IllegalArgumentException("Capacity cannot be negative");
    }
  }
}
