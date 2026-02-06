package javasabr.rlib.collections.array;

import java.util.Collection;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * A builder for constructing immutable {@link Array} instances.
 *
 * @param <E> the type of elements in the array being built
 * @since 10.0.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class ArrayBuilder<E> {

  MutableArray<E> elements;

  /**
   * Creates a new array builder with the specified component type.
   *
   * @param type the component type of the array
   * @since 10.0.0
   */
  public ArrayBuilder(Class<? super E> type) {
    this.elements = ArrayFactory.mutableArray(type);
  }

  /**
   * Adds an element to the array being built.
   *
   * @param element the element to add
   * @return this builder for method chaining
   * @since 10.0.0
   */
  public ArrayBuilder<E> add(E element) {
    elements.add(element);
    return this;
  }

  /**
   * Adds multiple elements to the array being built.
   *
   * @param other the elements to add
   * @return this builder for method chaining
   * @since 10.0.0
   */
  @SafeVarargs
  public final ArrayBuilder<E> add(E... other) {
    elements.addAll(other);
    return this;
  }

  /**
   * Adds all elements from a collection to the array being built.
   *
   * @param other the collection of elements to add
   * @return this builder for method chaining
   * @since 10.0.0
   */
  public ArrayBuilder<E> add(Collection<E> other) {
    elements.addAll(other);
    return this;
  }

  /**
   * Adds all elements from an array to the array being built.
   *
   * @param other the array of elements to add
   * @return this builder for method chaining
   * @since 10.0.0
   */
  public ArrayBuilder<E> add(Array<E> other) {
    elements.addAll(other);
    return this;
  }

  /**
   * Builds and returns an immutable array containing all added elements.
   *
   * @return an immutable array containing all added elements
   * @since 10.0.0
   */
  public Array<E> build() {
    return Array.copyOf(elements);
  }
}
