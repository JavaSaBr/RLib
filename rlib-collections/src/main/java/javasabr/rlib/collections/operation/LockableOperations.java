package javasabr.rlib.collections.operation;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import javasabr.rlib.functions.BiObjToBooleanFunction;
import javasabr.rlib.functions.ObjIntFunction;
import javasabr.rlib.functions.TriConsumer;
import javasabr.rlib.functions.TriFunction;

/**
 * Provides thread-safe operations on a lockable source with automatic lock management.
 *
 * @param <S> the type of the lockable source
 * @since 10.0.0
 */
public interface LockableOperations<S extends LockableSource> {

  /**
   * Executes a function within a read lock and returns the result.
   *
   * @param <R> the type of the result
   * @param function the function to execute
   * @return the result of the function
   * @since 10.0.0
   */
  <R> R getInReadLock(Function<S, R> function);

  /**
   * Executes a function within a read lock with one argument and returns the result.
   *
   * @param <R> the type of the result
   * @param <A> the type of the argument
   * @param arg1 the argument
   * @param function the function to execute
   * @return the result of the function
   * @since 10.0.0
   */
  <R, A> R getInReadLock(A arg1, BiFunction<S, A, R> function);

  /**
   * Executes a function within a read lock with an int argument and returns the result.
   *
   * @param <R> the type of the result
   * @param arg1 the int argument
   * @param function the function to execute
   * @return the result of the function
   * @since 10.0.0
   */
  <R> R getInReadLock(int arg1, ObjIntFunction<S, R> function);

  /**
   * Executes a function within a read lock with two arguments and returns the result.
   *
   * @param <R> the type of the result
   * @param <A> the type of the first argument
   * @param <B> the type of the second argument
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @param function the function to execute
   * @return the result of the function
   * @since 10.0.0
   */
  <R, A, B> R getInReadLock(A arg1, B arg2, TriFunction<S, A, B, R> function);

  /**
   * Executes a boolean function within a read lock with one argument.
   *
   * @param <A> the type of the argument
   * @param arg1 the argument
   * @param function the function to execute
   * @return the boolean result
   * @since 10.0.0
   */
  <A> boolean getBooleanInReadLock(A arg1, BiObjToBooleanFunction<S, A> function);

  /**
   * Executes a consumer within a read lock with one argument.
   *
   * @param <A> the type of the argument
   * @param arg1 the argument
   * @param function the consumer to execute
   * @since 10.0.0
   */
  <A> void inReadLock(A arg1, BiConsumer<S, A> function);

  /**
   * Executes a function within a write lock with one argument and returns the result.
   *
   * @param <R> the type of the result
   * @param <A> the type of the argument
   * @param arg1 the argument
   * @param function the function to execute
   * @return the result of the function
   * @since 10.0.0
   */
  <R, A> R getInWriteLock(A arg1, BiFunction<S, A, R> function);

  /**
   * Executes a function within a write lock with two arguments and returns the result.
   *
   * @param <R> the type of the result
   * @param <A> the type of the first argument
   * @param <B> the type of the second argument
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @param function the function to execute
   * @return the result of the function
   * @since 10.0.0
   */
  <R, A, B> R getInWriteLock(A arg1, B arg2, TriFunction<S, A, B, R> function);

  /**
   * Executes a consumer within a write lock.
   *
   * @param function the consumer to execute
   * @since 10.0.0
   */
  void inWriteLock(Consumer<S> function);

  /**
   * Executes a consumer within a write lock with one argument.
   *
   * @param <A> the type of the argument
   * @param arg1 the argument
   * @param function the consumer to execute
   * @since 10.0.0
   */
  <A> void inWriteLock(A arg1, BiConsumer<S, A> function);

  /**
   * Executes a consumer within a write lock with two arguments.
   *
   * @param <A> the type of the first argument
   * @param <B> the type of the second argument
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @param function the consumer to execute
   * @since 10.0.0
   */
  <A, B> void inWriteLock(A arg1, B arg2, TriConsumer<S, A, B> function);

}
