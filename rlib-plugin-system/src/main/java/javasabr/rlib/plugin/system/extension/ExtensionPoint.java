package javasabr.rlib.plugin.system.extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javasabr.rlib.common.util.ArrayUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * A thread-safe extension point that holds a collection of extensions.
 *
 * @param <T> the type of extensions
 * @since 10.0.0
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class ExtensionPoint<T> implements Iterable<T> {

  protected record State<T>(List<T> extensions, Object[] array) {

    public static <T> State<T> empty() {
      return new State<>(List.of(), ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    @SafeVarargs
    public final State<T> append(T... additionalExtensions) {

      List<T> result = new ArrayList<>(extensions);
      result.addAll(List.of(additionalExtensions));

      boolean canBeSorted = result
          .stream()
          .allMatch(Comparable.class::isInstance);

      if (canBeSorted) {
        result.sort((first, second) -> ((Comparable<T>) first).compareTo(second));
      }

      return new State<>(List.copyOf(result), result.toArray());
    }
  }

  AtomicReference<State<T>> state;

  /**
   * Creates an empty extension point.
   *
   * @since 10.0.0
   */
  public ExtensionPoint() {
    this.state = new AtomicReference<>(State.empty());
  }

  /**
   * Registers an extension to this extension point.
   *
   * @param extension the extension to register
   * @return this extension point
   * @since 10.0.0
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
   * Registers multiple extensions to this extension point.
   *
   * @param extensions the extensions to register
   * @return this extension point
   * @since 10.0.0
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
   * Returns all registered extensions.
   *
   * @return an unmodifiable list of extensions
   * @since 10.0.0
   */
  public List<T> extensions() {
    return state.get().extensions;
  }

  @Override
  public void forEach(Consumer<? super T> consumer) {
    Object[] array = state.get().array;
    for (Object obj : array) {
      consumer.accept((T) obj);
    }
  }

  @Override
  public Iterator<T> iterator() {
    return state.get().extensions.iterator();
  }

  /**
   * Returns a stream of all registered extensions.
   *
   * @return a stream of extensions
   * @since 10.0.0
   */
  public Stream<T> stream() {
    return StreamSupport.stream(spliterator(), false);
  }

  @Override
  public Spliterator<T> spliterator() {
    return Spliterators.spliterator(state.get().array, 0);
  }
}
