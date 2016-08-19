package rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

/**
 * Реализация {@link ConcurrentArray} с проверкой на уникальность вставляемых элементов.
 *
 * @author JavaSaBr
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

    @NotNull
    @Override
    public ConcurrentArray<E> add(@NotNull final E element) {
        return contains(element) ? this : super.add(element);
    }
}
