package javasabr.rlib.collections.array;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import javasabr.rlib.functions.BiObjLongConsumer;
import javasabr.rlib.functions.ObjIntPredicate;
import javasabr.rlib.functions.TriConsumer;
import org.jspecify.annotations.Nullable;

/**
 * Provides iteration functions for arrays with support for additional arguments.
 *
 * @param <E> the type of elements in the array
 * @since 10.0.0
 */
public interface ArrayIterationFunctions<E> {

  /**
   * Returns iteration functions with reversed argument order.
   *
   * @return reversed argument iteration functions
   * @since 10.0.0
   */
  ReversedArgsArrayIterationFunctions<E> reversedArgs();

  /**
   * Finds any element matching the filter predicate.
   *
   * @param <A> the type of the argument
   * @param arg1 the argument to pass to the filter
   * @param filter the predicate to match elements
   * @return the matching element or null
   * @since 10.0.0
   */
  @Nullable
  <A> E findAny(A arg1, BiPredicate<? super E, A> filter);

  /**
   * Finds any element matching the filter predicate with an int argument.
   *
   * @param arg1 the int argument to pass to the filter
   * @param filter the predicate to match elements
   * @return the matching element or null
   * @since 10.0.0
   */
  @Nullable
  E findAny(int arg1, ObjIntPredicate<? super E> filter);

  /**
   * Performs an action for each element with an additional argument.
   *
   * @param <A> the type of the argument
   * @param arg1 the argument to pass to the consumer
   * @param consumer the action to perform
   * @return this for method chaining
   * @since 10.0.0
   */
  <A> ArrayIterationFunctions<E> forEach(A arg1, BiConsumer<? super E, A> consumer);

  /**
   * Performs an action for each element with two additional arguments.
   *
   * @param <A> the type of the first argument
   * @param <B> the type of the second argument
   * @param arg1 the first argument to pass to the consumer
   * @param arg2 the second argument to pass to the consumer
   * @param consumer the action to perform
   * @return this for method chaining
   * @since 10.0.0
   */
  <A, B> ArrayIterationFunctions<E> forEach(A arg1, B arg2, TriConsumer<? super E, A, B> consumer);

  /**
   * Performs an action for each element with an object and long argument.
   *
   * @param <A> the type of the first argument
   * @param arg1 the first argument to pass to the consumer
   * @param arg2 the long argument to pass to the consumer
   * @param consumer the action to perform
   * @return this for method chaining
   * @since 10.0.0
   */
  <A> ArrayIterationFunctions<E> forEach(A arg1, long arg2, BiObjLongConsumer<? super E, A> consumer);

  /**
   * Returns whether any element matches the filter predicate.
   *
   * @param <A> the type of the argument
   * @param arg1 the argument to pass to the filter
   * @param filter the predicate to match elements
   * @return true if any element matches
   * @since 10.0.0
   */
  <A> boolean anyMatch(A arg1, BiPredicate<? super E, A> filter);
}
