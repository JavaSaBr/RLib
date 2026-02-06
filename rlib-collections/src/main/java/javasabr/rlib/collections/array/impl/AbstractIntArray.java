package javasabr.rlib.collections.array.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javasabr.rlib.collections.array.IntArray;
import javasabr.rlib.collections.array.UnsafeIntArray;
import javasabr.rlib.common.util.ArrayUtils;
import lombok.AccessLevel;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractIntArray implements UnsafeIntArray {

  @Override
  public int first() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return unsafeGet(0);
  }

  @Override
  public int last() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return unsafeGet(size() - 1);
  }

  @Override
  public int get(int index) {
    checkIndex(index);
    return unsafeGet(index);
  }

  @Override
  public int unsafeGet(int index) {
    return wrapped()[index];
  }

  protected void checkIndex(int index) {
    if (index < 0 || index >= size()) {
      throw new ArrayIndexOutOfBoundsException();
    }
  }

  @Override
  public boolean contains(int value) {
    int[] wrapped = wrapped();
    for (int i = 0, length = size(); i < length; i++) {
      if (value == wrapped[i]) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean containsAll(IntArray array) {
    if (array.isEmpty()) {
      return true;
    }

    int[] wrapped = array.asUnsafe().wrapped();
    for (int i = 0, length = array.size(); i < length; i++) {
      if (!contains(wrapped[i])) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean containsAll(int[] array) {
    if (array.length < 1) {
      return true;
    }
    for (int value : array) {
      if (!contains(value)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int indexOf(int value) {
    int[] wrapped = wrapped();
    for (int i = 0, length = size(); i < length; i++) {
      if (value == wrapped[i]) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public int lastIndexOf(int value) {
    int[] wrapped = wrapped();
    for (int i = size() - 1; i >= 0; i--) {
      if (value == wrapped[i]) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public Iterator<Integer> iterator() {
    return new DefaultIntArrayIterator(this);
  }

  @Override
  public int[] toArray() {
    return Arrays.copyOf(wrapped(), size());
  }

  @Override
  public boolean equals(Object another) {
    if (!(another instanceof IntArray array)) {
      return false;
    }
    int[] wrapped = array.asUnsafe().wrapped();
    return Arrays.equals(wrapped(), 0, size(), wrapped, 0, array.size());
  }

  @Override
  public int hashCode() {
    int[] wrapped = wrapped();
    int result = 1;
    for (int i = 0, wrappedLength = size(); i < wrappedLength; i++) {
      result = 31 * result + wrapped[i];
    }
    return result;
  }

  @Override
  public String toString() {
    return ArrayUtils.toString(wrapped(), 0, size(), ",", false, true);
  }

  @Override
  public UnsafeIntArray asUnsafe() {
    return this;
  }
}
