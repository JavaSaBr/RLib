package javasabr.rlib.common.util.pools;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;
import javasabr.rlib.common.function.ObjectLongFunction;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The interface for implementing a pool for storing and reusing any objects.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public interface Pool<E> {

  /**
   * Return true if this pool is empty.
   *
   * @return true if this pool is empty.
   */
  boolean isEmpty();

  /**
   * Put the object to this pool.
   *
   * @param object the object.
   */
  void put(E object);

  /**
   * Remove the object from this pool.
   *
   * @param object the object.
   */
  void remove(E object);

  /**
   * Take an object from this pool.
   *
   * @return taken object or null if this pool is empty.
   */
  @Nullable
  E take();

  /**
   * Take an object from this pool or create a new object.
   *
   * @param factory the factory to create new object if this pool is empty.
   * @return taken or created object.
   */
  default E take(Supplier<E> factory) {
    E take = take();
    return take != null ? take : factory.get();
  }

  /**
   * Take an object from this pool or create a new object.
   *
   * @param argument the argument.
   * @param factory the factory to create new object if this pool is empty.
   * @param <T> the argument's type.
   * @return taken or created object.
   */
  default <T> E take(T argument, Function<T, E> factory) {
    E take = take();
    return take != null ? take : factory.apply(argument);
  }

  /**
   * Take an object from this pool or create a new object.
   *
   * @param argument the argument.
   * @param factory the factory to create new object if this pool is empty.
   * @return taken or created object.
   */
  default E take(long argument, LongFunction<E> factory) {
    E take = take();
    return take != null ? take : factory.apply(argument);
  }

  /**
   * Take an object from this pool or create a new object.
   *
   * @param first the first argument.
   * @param second the second argument.
   * @param factory the factory to create new object if this pool is empty.
   * @param <F> the first argument's type.
   * @return taken or created object.
   */
  default <F> E take(
      F first,
      long second,
      ObjectLongFunction<F, E> factory) {
    E take = take();
    return take != null ? take : factory.apply(first, second);
  }

  /**
   * Take an object from this pool or create a new object.
   *
   * @param first the first argument.
   * @param second the second argument.
   * @param factory the factory to create new object if this pool is empty.
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   * @return taken or created object.
   */
  default <F, S> E take(
      F first,
      S second,
      BiFunction<F, S, E> factory) {
    E take = take();
    return take != null ? take : factory.apply(first, second);
  }
}
