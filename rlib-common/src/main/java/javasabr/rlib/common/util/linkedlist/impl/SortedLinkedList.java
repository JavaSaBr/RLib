package javasabr.rlib.common.util.linkedlist.impl;

import java.io.Serial;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

/**
 * The implementation of {@link FastLinkedList} with to sort on put an element.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public class SortedLinkedList<E extends Comparable<E>> extends FastLinkedList<E> {

  @Serial
  private static final long serialVersionUID = -8115760928469233254L;

  /**
   * Instantiates a new Sorted linked list.
   *
   * @param type the type
   */
  public SortedLinkedList(Class<?> type) {
    super(type);
  }

  @Override
  public boolean add(@NonNull E element) {

    for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
      E item = node.getItem();
      if (item != null && element.compareTo(item) < 0) {
        insertBefore(node, element);
        return true;
      }
    }

    linkLast(element);
    return true;
  }
}
