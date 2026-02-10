package javasabr.rlib.common.util;

import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A circular buffer that cycles through pre-allocated objects.
 * <p>
 * Useful for reusing objects in hot paths to reduce garbage collection overhead.
 *
 * @param <T> the type of objects in the buffer
 * @since 10.0.0
 */
@NullMarked
public final class CycleBuffer<T> {

  private final T[] buffer;
  private final @Nullable Consumer<T> handler;
  private int order;

  /**
   * Creates a new cycle buffer.
   *
   * @param type the element type
   * @param size the buffer size (must be at least 2)
   * @param factory the factory to create elements
   */
  public CycleBuffer(final Class<?> type, final int size, final Supplier<T> factory) {
    this(type, size, factory, null);
  }

  /**
   * Creates a new cycle buffer with an optional handler.
   *
   * @param type the element type
   * @param size the buffer size (must be at least 2)
   * @param factory the factory to create elements
   * @param handler optional handler called when retrieving an element
   * @throws RuntimeException if size is less than 2
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
   * Returns the next object from the buffer, cycling back to the start when the end is reached.
   *
   * @return the next object from the buffer
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
