package com.ss.rlib.common.util.linkedlist;

import com.ss.rlib.common.function.LongBiObjectConsumer;
import com.ss.rlib.common.function.TripleConsumer;
import com.ss.rlib.common.function.TriplePredicate;
import com.ss.rlib.common.util.linkedlist.impl.Node;
import com.ss.rlib.common.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Deque;
import java.util.function.*;

/**
 * Интерфей с для реализации связанного списка. Главное преймущество, это переиспользование узлов списка и быстрая
 * итерация с уменьшением нагрузки на GC. Создаются с помощью {@link LinkedListFactory}.
 * <pre>
 * for(Node&lt;E&gt; node = getFirstNode(); node != null; node = node.getNext()) {
 * 	? item = node.getItem();
 * 	// handle item
 * }
 * </pre>
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public interface LinkedList<E> extends Deque<E>, Cloneable, Serializable, Reusable {

    @Override
    default void forEach(@NotNull final Consumer<? super E> consumer) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            consumer.accept(node.getItem());
        }
    }

    /**
     * Apply a function to each filtered element.
     *
     * @param condition the condition.
     * @param function  the function.
     */
    default void forEach(@NotNull Predicate<E> condition, @NotNull Consumer<? super E> function) {
        for (var node = getFirstNode(); node != null; node = node.getNext()) {
            var item = node.getItem();
            if (condition.test(item)) {
                function.accept(item);
            }
        }
    }

    /**
     * Apply the function to each element which is an instance of the passed type.
     *
     * @param type     the interested element's type.
     * @param function the function.
     * @param <T>      the element's type.
     */
    default <T> void applyIfType(@NotNull Class<T> type, @NotNull Consumer<? super T> function) {
        for (var node = getFirstNode(); node != null; node = node.getNext()) {
            var item = node.getItem();
            if (type.isInstance(item)) {
                function.accept(type.cast(item));
            }
        }
    }

    /**
     * Apply a function to each element.
     *
     * @param <T>      the type of an argument.
     * @param argument the argument.
     * @param function the function.
     */
    default <T> void forEach(@Nullable final T argument, @NotNull final BiConsumer<T, E> function) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            function.accept(argument, node.getItem());
        }
    }

    /**
     * Apply a function to each filtered element.
     *
     * @param <T>       the type of an argument.
     * @param argument  the argument.
     * @param condition the condition.
     * @param function  the function.
     */
    default <T> void forEach(@Nullable final T argument, @NotNull final BiPredicate<E, T> condition,
                             @NotNull final BiConsumer<T, E> function) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            final E item = node.getItem();
            if (condition.test(item, argument)) function.accept(argument, item);
        }
    }

    /**
     * Apply a function to each filtered element.
     *
     * @param <F>       the type of a first argument.
     * @param <S>       the type of a second argument.
     * @param first     the first argument.
     * @param second    the second argument.
     * @param condition the condition.
     * @param function  the function.
     */
    default <F, S> void forEach(@Nullable final F first, @Nullable final S second,
                                @NotNull final TriplePredicate<E, F, S> condition,
                                @NotNull final TripleConsumer<F, S, E> function) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            final E item = node.getItem();
            if (condition.test(item, first, second)) function.accept(first, second, item);
        }
    }

    /**
     * Apply a function to each element.
     *
     * @param <F>      the type of a first argument.
     * @param <S>      the type of a second argument.
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     */
    default <F, S> void forEach(@Nullable final F first, @Nullable final S second,
                                @NotNull final TripleConsumer<F, S, E> function) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            function.accept(first, second, node.getItem());
        }
    }

    /**
     * Apply a function to each element.
     *
     * @param <F>      the type of a second argument.
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     */
    default <F> void forEach(final long first, final F second, @NotNull final LongBiObjectConsumer<F, E> function) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            function.accept(first, second, node.getItem());
        }
    }

    /**
     * Apply a function to each element to replace an original element.
     *
     * @param function the function.
     */
    default void apply(@NotNull final Function<? super E, ? extends E> function) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            node.setItem(function.apply(node.getItem()));
        }
    }

    /**
     * Gets an element for an index.
     *
     * @param index the index of an element.
     * @return the element for the index.
     */
    E get(int index);

    /**
     * Get a first node.
     *
     * @return the first node.
     */
    @Nullable
    Node<E> getFirstNode();

    /**
     * Get a last node.
     *
     * @return the last node.
     */
    @Nullable
    Node<E> getLastNode();

    /**
     * Finds an index of an object in this list.
     *
     * @param object the object to find.
     * @return the index of the object or -1.
     */
    int indexOf(Object object);

    /**
     * Returns the (non-null) Node at the specified element index.
     *
     * @param index the index of a node.
     * @return the node.
     */
    default Node<E> node(int index) {

        final int size = size();

        if (index < (size >> 1)) {

            Node<E> node = getFirstNode();

            for (int i = 0; i < index; i++) {
                node = node.getNext();
            }

            return node;

        } else {

            Node<E> node = getLastNode();

            for (int i = size() - 1; i > index; i--) {
                node = node.getPrev();
            }

            return node;
        }
    }

    /**
     * Removes an element for an index without reordering.
     *
     * @param index the index of the element.
     * @return the removed element.
     */
    default E remove(int index) {
        return unlink(node(index));
    }

    /**
     * Take and remove a first element.
     *
     * @return the first element or null.
     */
    @Nullable
    E take();

    /**
     * Remove a node from this list.
     *
     * @param node the node.
     * @return the removed element.
     */
    @Nullable
    E unlink(@NotNull Node<E> node);

}
