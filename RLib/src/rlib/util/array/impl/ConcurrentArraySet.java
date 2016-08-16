package rlib.util.array.impl;

/**
 * Реализация {@link ConcurrentArray} с проверкой на уникальность вставляемых элементов.
 *
 * @author Ronn
 * @see ConcurrentArray
 */
public class ConcurrentArraySet<E> extends ConcurrentArray<E> {

    private static final long serialVersionUID = -3394386864246350866L;

    public ConcurrentArraySet(final Class<E> type) {
        super(type);
    }

    public ConcurrentArraySet(final Class<E> type, final int size) {
        super(type, size);
    }

    @Override
    public ConcurrentArray<E> add(final E element) {
        return contains(element) ? this : super.add(element);
    }
}
