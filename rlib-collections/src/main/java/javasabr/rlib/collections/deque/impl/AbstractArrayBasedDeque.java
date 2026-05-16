package javasabr.rlib.collections.deque.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import javasabr.rlib.common.util.ArrayUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractArrayBasedDeque<E> implements Deque<E> {

  protected static final int SMALL_SIZE = 100;
  protected static final int SMALL_STEP = 10;
  protected static final int BIG_STEP = 50;
  protected static final int DEFAULT_CAPACITY = 20;
  protected static final float REBALANCE_FACTOR = 0.8F;
  protected static final int MIN_SIZE_FOR_REBALANCE = 50;

  Class<? super E> type;
  float rebalanceFactor;

  protected AbstractArrayBasedDeque(Class<? super E> type, float rebalanceFactor) {
    this.type = type;
    this.rebalanceFactor = rebalanceFactor;
  }

  protected abstract int head();
  protected abstract void head(int head);

  protected abstract int tail();
  protected abstract void tail(int tail);

  protected abstract int rebalanceTrigger();

  protected abstract void rebalanceTrigger(int rebalanceTrigger);

  protected abstract @Nullable E[] items();
  protected abstract void items(E[] items);

  protected void resetIndexes() {
    @Nullable E[] items = items();
    int head = items.length / 2;
    head(head);
    tail(head);
    updateRebalanceTrigger(items);
  }

  protected void updateRebalanceTrigger(@Nullable E[] items) {
    int length = items.length;
    int rebalanceTrigger = length > MIN_SIZE_FOR_REBALANCE ? (int) (length * rebalanceFactor) : Integer.MAX_VALUE;
    rebalanceTrigger(rebalanceTrigger);
  }

  @Override
  public void addFirst(E element) {

    @Nullable E[] items = items();
    int size = size();
    int head = head();

    if (head == 0) {
      items = increaseLeft(items, size);
    }

    int index = size > 0 ? decrementHeadAndGet() : head;
    items[index] = element;

    incrementSize();
  }

  @Override
  public boolean offerFirst(E element) {
    addFirst(element);
    return true;
  }

  @Override
  public void addLast(E element) {

    @Nullable E[] items = items();
    int size = size();
    int tail = tail();

    if (tail == items.length - 1) {
      items = increaseRight(items, size);
    }

    int index = size > 0 ? incrementTailAndGet() : tail;
    items[index] = element;

    incrementSize();
  }

  @Override
  public boolean offerLast(E element) {
    addLast(element);
    return true;
  }

  protected abstract int decrementHeadAndGet();
  protected abstract int incrementHeadAndGet();

  protected abstract int incrementTailAndGet();
  protected abstract int decrementTailAndGet();

  protected abstract void incrementSize();
  protected abstract void decrementSize();

  protected abstract int decrementSizeAndGet();

  protected @Nullable E[] increaseLeft(@Nullable E[] items, int size) {

    int stepSize = stepSize(items);
    E[] newItems = ArrayUtils.create(type, items.length + stepSize);

    System.arraycopy(items, 0, newItems, stepSize, size);

    items(newItems);
    head(stepSize);
    tail(tail() + stepSize);
    updateRebalanceTrigger(items);
    return newItems;
  }

  protected @Nullable E[] increaseRight(@Nullable E[] items, int size) {

    int stepSize = stepSize(items);
    E[] newItems = ArrayUtils.create(type, items.length + stepSize);
    int head = head();

    System.arraycopy(items, head, newItems, head, size);

    items(newItems);
    updateRebalanceTrigger(items);
    return newItems;
  }

  private void rebalance() {

    @Nullable E[] items = items();
    int size = size();
    int offset = size / 2;
    int suggestedHead = items.length / 2 - offset;
    int suggestedTail = suggestedHead + size - 1;
    int head = head();
    int tail = tail();

    if (head < suggestedHead && tail > suggestedTail) {
      return;
    }

    System.arraycopy(items, head, items, suggestedHead, size);

    head(suggestedHead);
    tail(suggestedTail);

    if (head > suggestedHead) {
      Arrays.fill(items, suggestedHead + size, tail + 1, null);
    } else if (tail < suggestedTail) {
      Arrays.fill(items, head, suggestedHead, null);
    }
  }

  @Override
  public E removeFirst() {

    if (isEmpty()) {
      throw new NoSuchElementException();
    }

    @Nullable E[] items = items();
    int head = head();

    E item = items[head];
    items[head] = null;

    int newSize = decrementSizeAndGet();
    if (newSize == 0) {
      resetIndexes();
      //noinspection DataFlowIssue
      return item;
    }

    int newHead = incrementHeadAndGet();
    if (head == tail()) {
      tail(newHead);
    }
    if (newHead > rebalanceTrigger()) {
      rebalance();
    }

    //noinspection DataFlowIssue
    return item;
  }

  @Override
  public E removeLast() {

    if (isEmpty()) {
      throw new NoSuchElementException();
    }

    @Nullable E[] items = items();
    int tail = tail();

    E item = items[tail];
    items[tail] = null;

    int newSize = decrementSizeAndGet();
    if (newSize == 0) {
      resetIndexes();
      //noinspection DataFlowIssue
      return item;
    }

    int newTail = decrementTailAndGet();
    if (tail == head()) {
      head(newTail);
    }
    if (items.length - tail > rebalanceTrigger()) {
      rebalance();
    }

    //noinspection DataFlowIssue
    return item;
  }

  @Nullable
  @Override
  public E pollFirst() {
    if (isEmpty()) {
      return null;
    } else {
      return removeFirst();
    }
  }

  @Nullable
  @Override
  public E pollLast() {
    if (isEmpty()) {
      return null;
    } else {
      return removeLast();
    }
  }

  @Nullable
  @Override
  public E peekFirst() {
    if (isEmpty()) {
      return null;
    } else {
      return items()[head()];
    }
  }

  @Nullable
  @Override
  public E peekLast() {
    if (isEmpty()) {
      return null;
    } else {
      return items()[tail()];
    }
  }

  @Override
  public E getFirst() {
    E first = peekFirst();
    if (first == null) {
      throw new NoSuchElementException();
    }
    return first;
  }

  @Override
  public E getLast() {
    E last = peekLast();
    if (last == null) {
      throw new NoSuchElementException();
    }
    return last;
  }

  @Override
  public boolean removeFirstOccurrence(Object o) {
    return false;
  }

  @Override
  public boolean removeLastOccurrence(Object o) {
    return false;
  }

  @Override
  public boolean add(E element) {
    offerLast(element);
    return true;
  }

  @Override
  public boolean offer(E element) {
    offerLast(element);
    return true;
  }

  @Override
  public E remove() {
    return removeFirst();
  }

  @Nullable
  @Override
  public E poll() {
    return pollFirst();
  }

  @Override
  public E element() {
    return getFirst();
  }

  @Nullable
  @Override
  public E peek() {
    return peekFirst();
  }

  @Override
  public boolean addAll(Collection<? extends E> collection) {
    for (E element : collection) {
      addLast(element);
    }
    return true;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return false;
  }

  @Override
  public void clear() {
    if (!isEmpty()) {
      @Nullable E[] items = items();
      Arrays.fill(items, head(), tail(), null);
      head(items.length);
      tail(-1);
    }
  }

  @Override
  public void push(E element) {
    addFirst(element);
  }

  @Override
  public E pop() {
    return removeFirst();
  }

  @Override
  public boolean remove(Object object) {

    if (isEmpty()) {
      return false;
    }

    @Nullable E[] items = items();
    int head = head();
    int tail = tail();

    if (Objects.equals(items[head], object)) {
      removeFirst();
      return true;
    } else if (head != tail && Objects.equals(items[tail], object)) {
      removeLast();
      return true;
    }

    for (int i = head + 1; i < tail; i++) {
      E item = items[i];

      if (!Objects.equals(item, object)) {
        continue;
      }

      int distanceToHead = i - head;
      int distanceToTail = tail - i;

      if (distanceToHead < distanceToTail) {
        // [n][n][o][o][t][o]|[o][o][n][n] -> [n][n][n][o][o][o]|[o][o][n][n]
        System.arraycopy(items, head, items, head + 1, distanceToHead);
        items[head] = null;
        incrementHeadAndGet();
        decrementSize();
      } else {
        // [n][n][o][o]|[o][o][t][o][n][n] -> [n][n][o][o]|[o][o][o][n][n][n]
        System.arraycopy(items, i + 1, items, i, distanceToTail);
        items[tail] = null;
        decrementTailAndGet();
        decrementSize();
      }

      return true;
    }

    return false;
  }

  @Override
  public boolean containsAll(Collection<?> collection) {
    for (Object object : collection) {
      if (!contains(object)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean contains(Object object) {

    if (isEmpty()) {
      return false;
    }

    @Nullable E[] items = items();
    for (int i = head(), limit = tail(); i <= limit; i++) {
      E item = items[i];
      if (Objects.equals(item, object)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean isEmpty() {
    return size() < 1;
  }

  @Override
  public Iterator<E> iterator() {
    return new ArrayBasedDequeIterator<>(this);
  }

  @Override
  public Iterator<E> descendingIterator() {
    return new ArrayBasedDequeDescendingIterator<>(this);
  }

  @Override
  public Object[] toArray() {

    if (isEmpty()) {
      return ArrayUtils.EMPTY_OBJECT_ARRAY;
    }

    @Nullable E[] items = items();
    Object[] result = new Object[size()];

    for (int i = head(), j = 0, limit = tail(); i <= limit; i++) {
      //noinspection DataFlowIssue
      result[j++] = items[i];
    }

    return result;
  }

  @Override
  public <T> T[] toArray(T[] container) {

    int size = size();
    if (size < 1) {
      return container;
    } else if (size > container.length) {
      container = ArrayUtils.create(container, size);
    }

    @Nullable E[] items = items();

    for (int i = head(), j = 0, limit = tail(); i <= limit; i++) {
      //noinspection DataFlowIssue,unchecked
      container[j++] = (T) items[i];
    }

    return container;
  }

  @Override
  public String toString() {
    return ArrayUtils.toString(items(), head(), size(), ", ", false, true);
  }

  protected static <T> int stepSize(@Nullable T[] items) {
    return items.length > SMALL_SIZE ? BIG_STEP : SMALL_STEP;
  }
}
