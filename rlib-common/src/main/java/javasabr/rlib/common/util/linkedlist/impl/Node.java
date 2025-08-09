package javasabr.rlib.common.util.linkedlist.impl;

import javasabr.rlib.common.util.pools.Reusable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The node of the LinkedList.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public final class Node<E> implements Reusable {

  /**
   * The item.
   */
  private @Nullable E item;

  /**
   * The prev node.
   */
  private @Nullable Node<E> prev;

  /**
   * The next node.
   */
  private @Nullable Node<E> next;

  @Override
  public void free() {
    item = null;
    prev = null;
    next = null;
  }

  /**
   * Gets item.
   *
   * @return yhe item.
   */
  public @Nullable E getItem() {
    return item;
  }

  /**
   * Sets item.
   *
   * @param item yhe item.
   */
  public void setItem(@Nullable E item) {
    this.item = item;
  }

  /**
   * Gets next.
   *
   * @return yhe next node.
   */
  public @Nullable Node<E> getNext() {
    return next;
  }

  /**
   * Sets next.
   *
   * @param next yhe next node.
   */
  public void setNext(@Nullable Node<E> next) {
    this.next = next;
  }

  /**
   * Gets prev.
   *
   * @return yhe prev node.
   */
  public @Nullable Node<E> getPrev() {
    return prev;
  }

  /**
   * Sets prev.
   *
   * @param prev yhe prev node.
   */
  public void setPrev(@Nullable Node<E> prev) {
    this.prev = prev;
  }
}
