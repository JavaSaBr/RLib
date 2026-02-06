package javasabr.rlib.collections.deque;

import java.util.Deque;
import javasabr.rlib.collections.deque.impl.DefaultArrayBasedDeque;
import javasabr.rlib.collections.deque.impl.DefaultLinkedListBasedDeque;
import lombok.experimental.UtilityClass;

/**
 * Factory for creating various deque implementations.
 *
 * @author JavaSaBr
 * @since 10.0.0
 */
@UtilityClass
public class DequeFactory {

  /**
   * Creates a new deque backed by a linked list.
   *
   * @param <E> the type of elements
   * @return a new linked list based deque
   * @since 10.0.0
   */
  public static <E> Deque<E> linkedListBased() {
    return new DefaultLinkedListBasedDeque<>();
  }

  /**
   * Creates a new deque backed by an array.
   *
   * @param <E> the type of elements
   * @param type the component type of the array
   * @return a new array based deque
   * @since 10.0.0
   */
  public static <E> Deque<E> arrayBased(Class<? super E> type) {
    return new DefaultArrayBasedDeque<>(type);
  }

  /**
   * Creates a new deque backed by an array with initial capacity.
   *
   * @param <E> the type of elements
   * @param type the component type of the array
   * @param capacity the initial capacity
   * @return a new array based deque
   * @since 10.0.0
   */
  public static <E> Deque<E> arrayBased(Class<? super E> type, int capacity) {
    return new DefaultArrayBasedDeque<>(type, capacity);
  }
}
