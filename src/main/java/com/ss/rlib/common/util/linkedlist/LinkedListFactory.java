package com.ss.rlib.common.util.linkedlist;

import com.ss.rlib.common.util.linkedlist.impl.SortedLinkedList;
import org.jetbrains.annotations.NotNull;

import com.ss.rlib.common.util.linkedlist.impl.FastLinkedList;

/**
 * The factory of linked list implementations.
 *
 * @author JavaSaBr
 */
public final class LinkedListFactory {

    /**
     * New linked list linked list.
     *
     * @param <E>  the type parameter
     * @param type the type
     * @return the linked list
     */
    public static <E> LinkedList<E> newLinkedList(@NotNull final Class<?> type) {
        return new FastLinkedList<>(type);
    }

    /**
     * New sorted linked list linked list.
     *
     * @param <E>  the type parameter
     * @param type the type
     * @return the linked list
     */
    public static <E extends Comparable<E>> LinkedList<E> newSortedLinkedList(@NotNull final Class<?> type) {
        return new SortedLinkedList<>(type);
    }

    private LinkedListFactory() {
        throw new RuntimeException();
    }
}
