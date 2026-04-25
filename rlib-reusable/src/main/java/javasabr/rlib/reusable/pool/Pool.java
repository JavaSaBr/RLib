package javasabr.rlib.reusable.pool;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;
import javasabr.rlib.functions.ObjLongFunction;
import org.jspecify.annotations.Nullable;

/**
 * A pool for storing and reusing objects.
 *
 * @param <E> the element type
 * @since 10.0.0
 */
public interface Pool<E> {

  /**
   * Puts an object back into the pool.
   *
   * @param object the object to put
   * @since 10.0.0
   */
  void put(E object);

  /**
   * Takes an object from the pool if available.
   *
   * @return the object, or null if the pool is empty
   * @since 10.0.0
   */
  @Nullable
  E take();

  /**
   * Takes an object from the pool, or creates one using the factory if empty.
   *
   * @param factory the factory to create a new object
   * @return the pooled or newly created object
   * @since 10.0.0
   */
  default E take(Supplier<E> factory) {
    E take = take();
    return take != null ? take : factory.get();
  }

  /**
   * Takes an object from the pool, or creates one using the factory if empty.
   *
   * @param <T> the factory argument type
   * @param arg1 the factory argument
   * @param factory the factory to create a new object
   * @return the pooled or newly created object
   * @since 10.0.0
   */
  default <T> E take(T arg1, Function<T, E> factory) {
    E take = take();
    return take != null ? take : factory.apply(arg1);
  }

  /**
   * Takes an object from the pool, or creates one using the factory if empty.
   *
   * @param arg1 the factory argument
   * @param factory the factory to create a new object
   * @return the pooled or newly created object
   * @since 10.0.0
   */
  default E take(long arg1, LongFunction<E> factory) {
    E take = take();
    return take != null ? take : factory.apply(arg1);
  }

  /**
   * Takes an object from the pool, or creates one using the factory if empty.
   *
   * @param <F> the first factory argument type
   * @param arg1 the first factory argument
   * @param arg2 the second factory argument
   * @param factory the factory to create a new object
   * @return the pooled or newly created object
   * @since 10.0.0
   */
  default <F> E take(
      F arg1,
      long arg2,
      ObjLongFunction<F, E> factory) {
    E take = take();
    return take != null ? take : factory.apply(arg1, arg2);
  }

  /**
   * Takes an object from the pool, or creates one using the factory if empty.
   *
   * @param <F> the first factory argument type
   * @param <S> the second factory argument type
   * @param arg1 the first factory argument
   * @param arg2 the second factory argument
   * @param factory the factory to create a new object
   * @return the pooled or newly created object
   * @since 10.0.0
   */
  default <F, S> E take(
      F arg1,
      S arg2,
      BiFunction<F, S, E> factory) {
    E take = take();
    return take != null ? take : factory.apply(arg1, arg2);
  }
}
