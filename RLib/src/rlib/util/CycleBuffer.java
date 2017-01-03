package rlib.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The implementation of cycle buffer of some objects.
 *
 * @author JavaSaBr
 */
public final class CycleBuffer<T> {

    /**
     * The array with buffered objects.
     */
    private final T[] buffer;

    /**
     * The handler of getting object from buffer.
     */
    private final Consumer<T> handler;

    /**
     * The index of next object.
     */
    private int order;

    public CycleBuffer(final Class<?> type, final int size, final Supplier<T> factory) {
        this(type, size, factory, null);
    }

    public CycleBuffer(final Class<?> type, final int size, final Supplier<T> factory, final Consumer<T> handler) {
        if (size < 2) throw new RuntimeException("size is less to 2.");

        this.buffer = ArrayUtils.create(type, size);

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = factory.get();
        }

        this.handler = handler;
    }

    /**
     * @return следующий объект.
     */
    public T next() {
        if (order >= buffer.length) order = 0;
        final T result = buffer[order++];
        if (handler != null) handler.accept(result);
        return result;
    }
}
