package javasabr.rlib.common.util.linkedlist.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import javasabr.rlib.common.util.linkedlist.LinkedList;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The implementation of an iterator for the LinkedList.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public class IteratorImpl<E> implements Iterator<E> {

  /**
   * The iteration mode from start to end.
   */
  public static final int NEXT = 1;

  /**
   * The iteration mode from end to start.
   */
  public static final int PREV = 2;

  /**
   * The linked list.
   */
  private final LinkedList<E> list;

  /**
   * The last returned node.
   */
  @Nullable
  private Node<E> lastReturned;

  /**
   * The next node.
   */
  private Node<E> next;

  /**
   * The iteration mode.
   */
  private final int mode;

  /**
   * The next index.
   */
  private int nextIndex;

  /**
   * Instantiates a new Iterator.
   *
   * @param list the list
   * @param mode the mode
   */
  protected IteratorImpl(LinkedList<E> list, int mode) {
    this.nextIndex = 0;
    this.mode = mode;
    this.list = list;
    setNext(mode == NEXT ? list.getFirstNode() : mode == PREV ? list.getLastNode() : null);
  }

  @Nullable
  private Node<E> getLastReturned() {
    return lastReturned;
  }

  private void setLastReturned(@Nullable Node<E> lastReturned) {
    this.lastReturned = lastReturned;
  }

  private Node<E> getNext() {
    return Objects.requireNonNull(next);
  }

  private void setNext(@Nullable Node<E> next) {
    this.next = next;
  }

  @Override
  public boolean hasNext() {
    return mode == NEXT ? nextIndex < list.size() : mode == PREV && nextIndex > 0;
  }

  @Override
  public E next() {

    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    if (mode == NEXT) {

      Node<E> next = getNext();

      setNext(next.getNext());
      setLastReturned(next);

      nextIndex++;

      return next.getItem();
    } else if (mode == PREV) {

      Node<E> next = getNext();

      setNext(next.getPrev());
      setLastReturned(next);

      nextIndex--;

      return next.getItem();
    }

    throw new NoSuchElementException();
  }

  @Override
  public void remove() {

    Node<E> lastReturned = getLastReturned();
    if (lastReturned == null) {
      throw new IllegalStateException();
    }

    list.unlink(lastReturned);

    if (mode == NEXT) {
      nextIndex--;
    } else if (mode == PREV) {
      nextIndex++;
    }

    setLastReturned(null);
  }
}