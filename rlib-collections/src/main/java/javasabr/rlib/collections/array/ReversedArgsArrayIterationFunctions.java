package javasabr.rlib.collections.array;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import javasabr.rlib.functions.TriConsumer;
import org.jspecify.annotations.Nullable;

/**
 * Provides iteration functions for arrays with reversed argument order in callbacks.
 *
 * @param <E> the type of elements in the array
 * @since 10.0.0
 */
public interface ReversedArgsArrayIterationFunctions<E> {

  /**
   * Finds any element matching the filter predicate with reversed argument order.
   *
   * @param <A> the type of the argument
   * @param arg1 the argument to pass to the filter
   * @param filter the predicate to match elements (argument first, then element)
   * @return the matching element or null
   * @since 10.0.0
   */
  @Nullable
  <A> E findAny(A arg1, BiPredicate<A, ? super E> filter);

  /**
   * Performs an action for each element with reversed argument order.
   *
   * @param <A> the type of the argument
   * @param arg1 the argument to pass to the consumer
   * @param consumer the action to perform (argument first, then element)
   * @return this for method chaining
   * @since 10.0.0
   */
  <A> ReversedArgsArrayIterationFunctions<E> forEach(A arg1, BiConsumer<A, ? super E> consumer);

  /**
   * Performs an action for each element with two additional arguments in reversed order.
   *
   * @param <A> the type of the first argument
   * @param <B> the type of the second argument
   * @param arg1 the first argument to pass to the consumer
   * @param arg2 the second argument to pass to the consumer
   * @param consumer the action to perform (arguments first, then element)
   * @return this for method chaining
   * @since 10.0.0
   */
  <A, B> ReversedArgsArrayIterationFunctions<E> forEach(A arg1, B arg2, TriConsumer<A, B, ? super E> consumer);

  /**
   * Returns whether any element matches the filter predicate with reversed argument order.
   *
   * @param <A> the type of the argument
   * @param arg1 the argument to pass to the filter
   * @param filter the predicate to match elements (argument first, then element)
   * @return true if any element matches
   * @since 10.0.0
   */
  <A> boolean anyMatch(A arg1, BiPredicate<A, ? super E> filter);
}
