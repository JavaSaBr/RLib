package com.ss.rlib.common.util.linkedlist.impl;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The implementation of {@link FastLinkedList} with to sort on put an element.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class SortedLinkedList<E extends Comparable<E>> extends FastLinkedList<E> {

    private static final long serialVersionUID = -8115760928469233254L;

    /**
     * Instantiates a new Sorted linked list.
     *
     * @param type the type
     */
    public SortedLinkedList(final Class<?> type) {
        super(type);
    }

    @Override
    public boolean add(@NotNull final E element) {
        Objects.requireNonNull(element);

        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {

            final E item = node.getItem();

            if (element.compareTo(item) < 0) {
                insertBefore(node, element);
                return true;
            }
        }

        linkLast(element);
        return true;
    }
}
