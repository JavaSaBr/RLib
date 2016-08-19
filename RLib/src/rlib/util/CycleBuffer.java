package rlib.util;

import java.lang.reflect.Array;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Реализация циклического пула объектов.
 *
 * @author JavaSaBr
 */
public final class CycleBuffer<T> {

    /**
     * Массив-буффер объектов.
     */
    private final T[] buffer;

    /**
     * Обработчик объекта перед его получением.
     */
    private final Consumer<T> handler;

    /**
     * Индекс следующего объекта.
     */
    private int order;

    public CycleBuffer(final Class<?> type, final int size, final Supplier<T> factory) {
        this(type, size, factory, null);
    }

    public CycleBuffer(final Class<?> type, final int size, final Supplier<T> factory, final Consumer<T> handler) {

        if (size < 2) {
            throw new RuntimeException("size is less to 2.");
        }

        this.buffer = (T[]) Array.newInstance(type, size);

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
