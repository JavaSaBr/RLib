package javasabr.rlib.collections.array;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import javasabr.rlib.collections.array.impl.DefaultMutableArray;

/**
 * A mutable array interface that extends {@link Array} with modification operations.
 *
 * @param <E> the type of elements in this array
 * @since 10.0.0
 */
public interface MutableArray<E> extends Array<E>, Collection<E> {

  /**
   * Creates a new mutable array of the specified type.
   *
   * @param <E> the type of elements
   * @param type the component type of the array
   * @return a new mutable array
   * @since 10.0.0
   */
  static <E> MutableArray<E> ofType(Class<? super E> type) {
    return new DefaultMutableArray<>(type);
  }

  /**
   * Adds all elements from the specified array.
   *
   * @param array the array to add elements from
   * @return true if this array changed as a result
   * @since 10.0.0
   */
  boolean addAll(Array<? extends E> array);

  /**
   * Adds all elements from the specified mutable array.
   *
   * @param array the array to add elements from
   * @return true if this array changed as a result
   * @since 10.0.0
   */
  default boolean addAll(MutableArray<? extends E> array) {
    return addAll((Array<? extends E>) array);
  }

  /**
   * Adds all elements from the specified Java array.
   *
   * @param array the array to add elements from
   * @return true if this array changed as a result
   * @since 10.0.0
   */
  boolean addAll(E[] array);

  /**
   * Removes the element at the specified index.
   *
   * @param index the index of the element to remove
   * @return the element previously at the specified position
   * @since 10.0.0
   */
  E remove(int index);

  /**
   * Replaces the element at the specified index.
   *
   * @param index the index of the element to replace
   * @param element the new element
   * @since 10.0.0
   */
  void replace(int index, E element);

  @Override
  Stream<E> stream();

  @Override
  default <T> T[] toArray(IntFunction<T[]> generator) {
    return Collection.super.toArray(generator);
  }

  @Override
  default Iterator<E> iterator() {
    return Array.super.iterator();
  }

  @Override
  default void forEach(Consumer<? super E> action) {
    Array.super.forEach(action);
  }

  /**
   * Returns an unsafe mutable view of this array.
   *
   * @return an unsafe mutable view
   * @since 10.0.0
   */
  @Override
  UnsafeMutableArray<E> asUnsafe();

  /**
   * Sorts this array using natural ordering.
   *
   * @since 10.0.0
   */
  void sort();

  /**
   * Sorts this array using the specified comparator.
   *
   * @param comparator the comparator to determine element order
   * @since 10.0.0
   */
  void sort(Comparator<E> comparator);
}
