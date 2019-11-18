package com.ss.rlib.common.util.array;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The interface to mark an array as read only array.
 *
 * @param <E> the element's type.
 * @author JavaSaBr
 */
public interface ReadOnlyArray<E> extends Array<E> {

    @Override
    @Deprecated
    void apply(@NotNull Function<? super E, ? extends E> function);

    @Override
    @Deprecated
    boolean add(E e);

    @Override
    @Deprecated
    boolean addAll(@NotNull E[] array);

    @Override
    @Deprecated
    boolean addAll(@NotNull Array<? extends E> array);

    @Override
    @Deprecated
    boolean addAll(@NotNull Collection<? extends E> c);

    @Override
    @Deprecated
    boolean fastRemove(@NotNull Object object);

    @Override
    @Deprecated
    @NotNull E remove(int index);

    @Override
    @Deprecated
    boolean removeIf(@NotNull Predicate<? super E> filter);

    @Override
    @Deprecated
    boolean retainAll(@NotNull Collection<?> target) ;

    @Override
    @Deprecated
    boolean retainAll(@NotNull Array<?> target);

    @Override
    @Deprecated
    boolean removeAll(@NotNull Collection<?> target);

    @Override
    @Deprecated
    boolean removeAll(@NotNull Array<?> target);

    @Override
    @Deprecated
    boolean remove(@NotNull Object object);

    @Override
    @Deprecated
    void clear();

    @Override
    @Deprecated
    @NotNull Array<E> sort(@NotNull ArrayComparator<E> comparator);
}
