package rlib.util.array.impl;

/**
 * Финальная версия быстрого массива.
 *
 * @author Ronn
 */
public final class FinalFastArray<E> extends FastArray<E> {

    public FinalFastArray(final Class<E> type) {
        super(type);
    }

    public FinalFastArray(final Class<E> type, final int size) {
        super(type, size);
    }
}
