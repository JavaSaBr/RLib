package rlib.util.array.impl;

/**
 * Финальная версия конкурентного массива.
 *
 * @author Ron
 */
public final class FinalConcurrentAtomicArray<E> extends ConcurrentAtomicArray<E> {

    public FinalConcurrentAtomicArray(final Class<E> type) {
        super(type);
    }

    public FinalConcurrentAtomicArray(final Class<E> type, final int size) {
        super(type, size);
    }
}
