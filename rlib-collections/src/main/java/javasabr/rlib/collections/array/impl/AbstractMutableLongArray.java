package javasabr.rlib.collections.array.impl;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;
import javasabr.rlib.collections.array.LongArray;
import javasabr.rlib.collections.array.UnsafeMutableLongArray;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractMutableLongArray extends AbstractLongArray implements UnsafeMutableLongArray {

  @Override
  public boolean add(long value) {
    prepareForSize(size() + 1);
    unsafeAdd(value);
    return true;
  }

  @Override
  public boolean addAll(LongArray array) {
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
  public boolean addAll(long[] array) {
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
  public UnsafeMutableLongArray unsafeAdd(long value) {
    wrapped()[getAndIncrementSize()] = value;
    return this;
  }

  @Override
  public void replace(int index, long value) {
    checkIndex(index);
    unsafeSet(index, value);
  }

  @Override
  public UnsafeMutableLongArray unsafeSet(int index, long value) {
    wrapped()[index] = value;
    return this;
  }

  @Override
  public long removeByIndex(int index) {
    checkIndex(index);
    return unsafeRemoveByIndex(index);
  }

  @Override
  public boolean remove(long value) {
    int index = indexOf(value);
    if (index < 0) {
      return false;
    }
    unsafeRemoveByIndex(index);
    return true;
  }

  @Override
  public boolean removeAll(LongArray array) {
    if (array.isEmpty()) {
      return false;
    }
    int removed = 0;
    for (long value : array) {
      if (remove(value)) {
        removed++;
      }
    }
    return removed > 0;
  }

  @Override
  public boolean removeAll(long[] array) {
    if (array.length < 1) {
      return false;
    }
    int removed = 0;
    for (long value : array) {
      if (remove(value)) {
        removed++;
      }
    }
    return removed > 0;
  }

  @Override
  public long unsafeRemoveByIndex(int index) {
    int numMoved = size() - index - 1;
    long[] wrapped = wrapped();
    long value = wrapped[index];
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
  public LongStream stream() {
    return StreamSupport.longStream(spliterator(), false);
  }

  @Override
  public Spliterator.OfLong spliterator() {
    return Spliterators.spliterator(wrapped(), 0, size(), Spliterator.NONNULL);
  }

  protected void processAdd(LongArray array, int currentSize, int elementsToAdd) {
    System.arraycopy(array.asUnsafe().wrapped(), 0, wrapped(), currentSize, elementsToAdd);
    size(currentSize + elementsToAdd);
  }

  protected void processAdd(long[] array, int currentSize, int elementsToAdd) {
    System.arraycopy(array, 0, wrapped(), currentSize, elementsToAdd);
    size(currentSize + elementsToAdd);
  }

  protected abstract void size(int size);

  protected abstract int getAndIncrementSize();
  protected abstract int decrementAnGetSize();

  protected abstract void wrapped(long[] wrapped);

  @Override
  public UnsafeMutableLongArray prepareForSize(int expectedSize) {
    long[] wrapped = wrapped();
    if (expectedSize > wrapped.length) {
      int newLength = Math.max((wrapped.length * 3) / 2, expectedSize);
      wrapped(Arrays.copyOf(wrapped, newLength));
    }
    return this;
  }


  @Override
  public UnsafeMutableLongArray trimToSize() {
    long[] wrapped = wrapped();
    int size = size();
    if (size == wrapped.length) {
      return this;
    }
    wrapped(Arrays.copyOfRange(wrapped, 0, size));
    return this;
  }

  @Override
  public UnsafeMutableLongArray asUnsafe() {
    return this;
  }

  protected static void validateCapacity(int capacity) {
    if (capacity < 0) {
      throw new IllegalArgumentException("Capacity cannot be negative");
    }
  }
}
