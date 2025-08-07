package javasabr.rlib.common.plugin.extension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javasabr.rlib.common.function.TriplePredicate;
import javasabr.rlib.common.util.ArrayUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The class to present an extension point.
 *
 * @author JavaSaBr
 */
@NullMarked
public class ExtensionPoint<T> implements Iterable<T> {

  @NullMarked
  private static class State<T> {

    /**
     * The read only list of extensions.
     */
    private final List<T> extensions;

    /**
     * The array for fast access.
     */
    private final Object[] array;

    public State() {
      this.extensions = Collections.emptyList();
      this.array = ArrayUtils.EMPTY_OBJECT_ARRAY;
    }

    public State(List<T> extensions, Object[] array) {
      this.extensions = extensions;
      this.array = array;
    }

    /**
     * Append the extension to the current state as new state.
     *
     * @param extension the extension.
     * @return the new state.
     */
    public State<T> append(T extension) {

      List<T> result = new ArrayList<>(extensions);
      result.add(extension);

      boolean canBeSorted = result
          .stream()
          .allMatch(Comparable.class::isInstance);

      if (canBeSorted) {
        result.sort((first, second) -> ((Comparable<T>) first).compareTo(second));
      }

      List<T> extensions = Collections.unmodifiableList(result);
      Object[] array = result.toArray();

      return new State<>(extensions, array);
    }

    /**
     * Append the additional extensions to the current state as new state.
     *
     * @param additionalExtensions the additional extension.
     * @return the new state.
     */
    public State<T> append(T[] additionalExtensions) {

      List<T> result = new ArrayList<>(extensions);
      result.addAll(Arrays.asList(additionalExtensions));

      boolean canBeSorted = result
          .stream()
          .allMatch(Comparable.class::isInstance);

      if (canBeSorted) {
        result.sort((first, second) -> ((Comparable<T>) first).compareTo(second));
      }

      List<T> extensions = Collections.unmodifiableList(result);
      Object[] array = result.toArray();

      return new State<>(extensions, array);
    }
  }

  /**
   * The reference to the current state.
   */
  private final AtomicReference<State<T>> state;

  public ExtensionPoint() {
    this.state = new AtomicReference<>(new State<>());
  }

  /**
   * Register a new extension.
   *
   * @param extension the new extension.
   * @return this point.
   */
  public ExtensionPoint<T> register(T extension) {

    State<T> currentState = state.get();
    State<T> newState = currentState.append(extension);

    while (!state.compareAndSet(currentState, newState)) {
      currentState = state.get();
      newState = currentState.append(extension);
    }

    return this;
  }

  /**
   * Register the new extensions.
   *
   * @param extensions the new extensions.
   * @return this point.
   */
  @SafeVarargs
  public final ExtensionPoint<T> register(T... extensions) {

    State<T> currentState = state.get();
    State<T> newState = currentState.append(extensions);

    while (!state.compareAndSet(currentState, newState)) {
      currentState = state.get();
      newState = currentState.append(extensions);
    }

    return this;
  }

  /**
   * Get all registered extensions.
   *
   * @return the all registered extensions.
   */
  public List<T> getExtensions() {
    return state.get().extensions;
  }

  /**
   * Handle each extension.
   *
   * @param consumer the consumer.
   */
  @Override
  public void forEach(Consumer<? super T> consumer) {

    Object[] array = state.get().array;

    for (Object obj : array) {
      consumer.accept((T) obj);
    }
  }

  /**
   * Handle each extension.
   *
   * @param first the first argument.
   * @param consumer the consumer.
   * @param <F> the argument's type.
   */
  public <F> void forEach(F first, BiConsumer<? super T, F> consumer) {

    Object[] array = state.get().array;

    for (Object obj : array) {
      consumer.accept((T) obj, first);
    }
  }

  /**
   * Find an extension using the condition.
   *
   * @param predicate the condition.
   * @return the found extension or null.
   */
  public @Nullable T findAny(Predicate<? super T> predicate) {

    for (Object element : state.get().array) {
      if (predicate.test((T) element)) {
        return (T) element;
      }
    }

    return null;
  }

  /**
   * Find an extension using the condition.
   *
   * @param <F> the argument's type.
   * @param argument the argument.
   * @param predicate the condition.
   * @return the found extension or null.
   */
  public <F> @Nullable T findAny(@Nullable F argument, BiPredicate<? super T, F> predicate) {

    for (Object element : state.get().array) {
      if (predicate.test((T) element, argument)) {
        return (T) element;
      }
    }

    return null;
  }

  /**
   * Return true if there is at least an extension for the condition.
   *
   * @param <F> the argument's type.
   * @param argument the argument.
   * @param predicate the condition.
   * @return true if there is at least an extension for the condition.
   */
  public <F> boolean anyMatch(@Nullable F argument, BiPredicate<? super T, F> predicate) {
    return findAny(argument, predicate) != null;
  }

  /**
   * Find an extension using the inverted condition.
   *
   * @param <F> the argument's type.
   * @param argument the argument.
   * @param predicate the condition.
   * @return the found extension or null.
   */
  public <F> @Nullable T findAnyNot(@Nullable F argument, BiPredicate<? super T, F> predicate) {

    for (Object element : state.get().array) {
      if (!predicate.test((T) element, argument)) {
        return (T) element;
      }
    }

    return null;
  }

  /**
   * Return true if there is at least an extension for the inverted condition.
   *
   * @param <F> the argument's type.
   * @param argument the argument.
   * @param predicate the condition.
   * @return true if there is at least an extension for the inverted condition.
   */
  public <F> boolean anyMatchNot(@Nullable F argument, BiPredicate<? super T, F> predicate) {
    return findAnyNot(argument, predicate) != null;
  }

  /**
   * Find an extension using the condition.
   *
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   * @param first the first argument.
   * @param second the second argument.
   * @param predicate the condition.
   * @return the found extension or null.
   */
  public <F, S> @Nullable T findAny(
      @Nullable F first,
      @Nullable S second,
      TriplePredicate<? super T, F, S> predicate) {

    for (Object element : state.get().array) {
      if (predicate.test((T) element, first, second)) {
        return (T) element;
      }
    }

    return null;
  }

  /**
   * Return true if there is at least an extension for the condition.
   *
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   * @param first the first argument.
   * @param second the second argument.
   * @param predicate the condition.
   * @return true if there is at least an extension for the condition.
   */
  public <F, S> boolean anyMatch(
      @Nullable F first,
      @Nullable S second,
      TriplePredicate<? super T, F, S> predicate) {
    return findAny(first, second, predicate) != null;
  }

  /**
   * Find an extension using the inverted condition.
   *
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   * @param first the first argument.
   * @param second the second argument.
   * @param predicate the condition.
   * @return the found extension or null.
   */
  public <F, S> @Nullable T findAnyNot(
      @Nullable F first,
      @Nullable S second,
      TriplePredicate<? super T, F, S> predicate) {

    for (Object element : state.get().array) {
      if (!predicate.test((T) element, first, second)) {
        return (T) element;
      }
    }

    return null;
  }

  /**
   * Return true if there is at least an extension for the inverted condition.
   *
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   * @param first the first argument.
   * @param second the second argument.
   * @param predicate the condition.
   * @return the found extension or null.
   */
  public <F, S> boolean anyMatchNot(
      @Nullable F first,
      @Nullable S second,
      TriplePredicate<? super T, F, S> predicate) {
    return findAnyNot(first, second, predicate) != null;
  }

  /**
   * Search an extension using the condition.
   *
   * @param <F> the argument's type.
   * @param argument the argument.
   * @param predicate the condition.
   * @return the found extension or null.
   */
  public <F> @Nullable T anyMatchR(@Nullable F argument, BiPredicate<F, ? super T> predicate) {

    for (Object element : state.get().array) {
      if (predicate.test(argument, (T) element)) {
        return (T) element;
      }
    }

    return null;
  }

  @Override
  public Iterator<T> iterator() {
    return state.get().extensions.iterator();
  }

  /**
   * Get a stream of extensions.
   *
   * @return the stream of extensions.
   */
  public Stream<T> stream() {
    return StreamSupport.stream(spliterator(), false);
  }

  @Override
  public Spliterator<T> spliterator() {
    return Spliterators.spliterator(state.get().array, 0);
  }
}
