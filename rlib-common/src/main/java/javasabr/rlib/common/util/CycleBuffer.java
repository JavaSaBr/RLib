package javasabr.rlib.common.util;

import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The implementation of cycle buffer of some objects.
 *
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public final class CycleBuffer<T> {

  /**
   * The array with buffered objects.
   */
  private final T[] buffer;

  /**
   * The handler of getting object from buffer.
   */
  private final @Nullable Consumer<T> handler;

  /**
   * The index of next object.
   */
  private int order;

  /**
   * Instantiates a new Cycle buffer.
   *
   * @param type the type
   * @param size the size
   * @param factory the factory
   */
  public CycleBuffer(final Class<?> type, final int size, final Supplier<T> factory) {
    this(type, size, factory, null);
  }

  /**
   * Instantiates a new Cycle buffer.
   *
   * @param type the type
   * @param size the size
   * @param factory the factory
   * @param handler the handler
   */
  public CycleBuffer(
      Class<?> type,
      int size,
      Supplier<T> factory,
      @Nullable Consumer<T> handler) {

    if (size < 2) {
      throw new RuntimeException("size is less to 2.");
    }

    this.buffer = ArrayUtils.create(type, size);

    for (int i = 0; i < buffer.length; i++) {
      buffer[i] = factory.get();
    }

    this.handler = handler;
  }

  /**
   * Get a next free object.
   *
   * @return the next free object.
   */
  public T next() {
    if (order >= buffer.length) {
      order = 0;
    }
    final T result = buffer[order++];
    if (handler != null) {
      handler.accept(result);
    }
    return result;
  }
}
