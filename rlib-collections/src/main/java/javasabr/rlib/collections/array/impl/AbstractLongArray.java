package javasabr.rlib.collections.array.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javasabr.rlib.collections.array.LongArray;
import javasabr.rlib.collections.array.UnsafeLongArray;
import javasabr.rlib.common.util.ArrayUtils;
import lombok.AccessLevel;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractLongArray implements UnsafeLongArray {

  @Override
  public long first() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return unsafeGet(0);
  }

  @Override
  public long last() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return unsafeGet(size() - 1);
  }

  @Override
  public long get(int index) {
    checkIndex(index);
    return unsafeGet(index);
  }

  @Override
  public long unsafeGet(int index) {
    return wrapped()[index];
  }

  protected void checkIndex(int index) {
    if (index < 0 || index >= size()) {
      throw new ArrayIndexOutOfBoundsException();
    }
  }

  @Override
  public boolean contains(long value) {
    long[] wrapped = wrapped();
    for (int i = 0, length = size(); i < length; i++) {
      if (value == wrapped[i]) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean containsAll(LongArray array) {
    if (array.isEmpty()) {
      return true;
    }
    long[] wrapped = array.asUnsafe().wrapped();
    for (int i = 0, length = array.size(); i < length; i++) {
      if (!contains(wrapped[i])) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean containsAll(long[] array) {
    if (array.length < 1) {
      return true;
    }
    for (long value : array) {
      if (!contains(value)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int indexOf(long value) {
    long[] wrapped = wrapped();
    for (int i = 0, length = size(); i < length; i++) {
      if (value == wrapped[i]) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public int lastIndexOf(long value) {
    long[] wrapped = wrapped();
    for (int i = size() - 1; i >= 0; i--) {
      if (value == wrapped[i]) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public Iterator<Long> iterator() {
    return new DefaultLongArrayIterator(this);
  }

  @Override
  public long[] toArray() {
    return Arrays.copyOf(wrapped(), size());
  }

  @Override
  public boolean equals(Object another) {
    if (!(another instanceof LongArray array)) {
      return false;
    }
    long[] wrapped = array.asUnsafe().wrapped();
    return Arrays.equals(wrapped(), 0, size(), wrapped, 0, array.size());
  }

  @Override
  public int hashCode() {
    long[] wrapped = wrapped();
    int result = 1;
    for (int i = 0, wrappedLength = size(); i < wrappedLength; i++) {
      result = 31 * result + Long.hashCode(wrapped[i]);
    }
    return result;
  }

  @Override
  public String toString() {
    return ArrayUtils.toString(wrapped(), 0, size(), ",", false, true);
  }

  @Override
  public UnsafeLongArray asUnsafe() {
    return this;
  }
}
