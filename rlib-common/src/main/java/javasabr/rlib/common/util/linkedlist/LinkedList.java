package javasabr.rlib.common.util.linkedlist;

import java.io.Serializable;
import java.util.Deque;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import javasabr.rlib.common.function.LongBiObjectConsumer;
import javasabr.rlib.common.function.TripleConsumer;
import javasabr.rlib.common.function.TriplePredicate;
import javasabr.rlib.common.util.linkedlist.impl.Node;
import javasabr.rlib.common.util.pools.Reusable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Интерфей с для реализации связанного списка. Главное преймущество, это переиспользование узлов списка и быстрая
 * итерация с уменьшением нагрузки на GC. Создаются с помощью {@link LinkedListFactory}.
 * <pre>
 * for(Node&lt;E&gt; node = getFirstNode(); node != null; node = node.getNext()) {
 * 	? item = node.getItem();
 * 	// handle item
 * }
 * </pre>
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public interface LinkedList<E> extends Deque<E>, Cloneable, Serializable, Reusable {

  @Override
  default void forEach(Consumer<? super E> consumer) {
    for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
      consumer.accept(node.getItem());
    }
  }

  /**
   * Apply a function to each filtered element.
   *
   * @param condition the condition.
   * @param function the function.
   */
  default void forEach(Predicate<E> condition, Consumer<? super E> function) {
    for (var node = getFirstNode(); node != null; node = node.getNext()) {
      var item = node.getItem();
      if (condition.test(item)) {
        function.accept(item);
      }
    }
  }

  /**
   * Apply the function to each element which is an instance of the passed type.
   *
   * @param type the interested element's type.
   * @param function the function.
   * @param <T> the element's type.
   */
  default <T> void applyIfType(Class<T> type, Consumer<? super T> function) {
    for (var node = getFirstNode(); node != null; node = node.getNext()) {
      var item = node.getItem();
      if (type.isInstance(item)) {
        function.accept(type.cast(item));
      }
    }
  }

  /**
   * Apply a function to each element.
   *
   * @param <T> the type of an argument.
   * @param argument the argument.
   * @param function the function.
   */
  default <T> void forEach(@Nullable T argument, BiConsumer<T, E> function) {
    for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
      function.accept(argument, node.getItem());
    }
  }

  /**
   * Apply a function to each filtered element.
   *
   * @param <T> the type of an argument.
   * @param argument the argument.
   * @param condition the condition.
   * @param function the function.
   */
  default <T> void forEach(
      @Nullable T argument,
      BiPredicate<E, @Nullable T> condition,
      BiConsumer<@Nullable T, E> function) {
    for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
      final E item = node.getItem();
      if (condition.test(item, argument)) {
        function.accept(argument, item);
      }
    }
  }

  /**
   * Apply a function to each filtered element.
   *
   * @param <F> the type of a first argument.
   * @param <S> the type of a second argument.
   * @param first the first argument.
   * @param second the second argument.
   * @param condition the condition.
   * @param function the function.
   */
  default <F, S> void forEach(
      @Nullable F first,
      @Nullable S second,
      TriplePredicate<E, F, S> condition,
      TripleConsumer<F, S, E> function) {
    for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
      final E item = node.getItem();
      if (condition.test(item, first, second)) {
        function.accept(first, second, item);
      }
    }
  }

  /**
   * Apply a function to each element.
   *
   * @param <F> the type of a first argument.
   * @param <S> the type of a second argument.
   * @param first the first argument.
   * @param second the second argument.
   * @param function the function.
   */
  default <F, S> void forEach(
      @Nullable final F first,
      @Nullable final S second,
      TripleConsumer<F, S, E> function) {
    for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
      function.accept(first, second, node.getItem());
    }
  }

  /**
   * Apply a function to each element.
   *
   * @param <F> the type of a second argument.
   * @param first the first argument.
   * @param second the second argument.
   * @param function the function.
   */
  default <F> void forEach(long first, final F second, LongBiObjectConsumer<F, E> function) {
    for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
      function.accept(first, second, node.getItem());
    }
  }

  /**
   * Apply a function to each element to replace an original element.
   *
   * @param function the function.
   */
  default void apply(Function<? super E, ? extends E> function) {
    for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
      node.setItem(function.apply(node.getItem()));
    }
  }

  /**
   * Gets an element for an index.
   *
   * @param index the index of an element.
   * @return the element for the index.
   */
  E get(int index);

  /**
   * Get a first node.
   *
   * @return the first node.
   */
  @Nullable Node<E> getFirstNode();

  /**
   * Get a last node.
   *
   * @return the last node.
   */
  @Nullable Node<E> getLastNode();

  /**
   * Finds an index of an object in this list.
   *
   * @param object the object to find.
   * @return the index of the object or -1.
   */
  int indexOf(Object object);

  /**
   * Returns the (non-null) Node at the specified element index.
   *
   * @param index the index of a node.
   * @return the node.
   */
  default Node<E> node(int index) {

    final int size = size();

    if (index < (size >> 1)) {
      Node<E> node = getFirstNode();
      for (int i = 0; i < index && node != null; i++) {
        node = node.getNext();
      }
      return Objects.requireNonNull(node);
    } else {
      Node<E> node = getLastNode();
      for (int i = size() - 1; i > index && node != null; i--) {
        node = node.getPrev();
      }
      return Objects.requireNonNull(node);
    }
  }

  /**
   * Removes an element for an index without reordering.
   *
   * @param index the index of the element.
   * @return the removed element.
   */
  @Nullable
  default E remove(int index) {
    return unlink(node(index));
  }

  /**
   * Take and remove a first element.
   *
   * @return the first element or null.
   */
  @Nullable E take();

  /**
   * Remove a node from this list.
   *
   * @param node the node.
   * @return the removed element.
   */
  @Nullable E unlink(Node<E> node);
}
