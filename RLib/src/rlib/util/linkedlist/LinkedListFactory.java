package rlib.util.linkedlist;

import org.jetbrains.annotations.NotNull;

import rlib.util.linkedlist.impl.FastLinkedList;
import rlib.util.linkedlist.impl.SortedLinkedList;

/**
 * The factory of linked list implementations.
 *
 * @author JavaSaBr
 */
public final class LinkedListFactory {

    public static <E> LinkedList<E> newLinkedList(@NotNull final Class<?> type) {
        return new FastLinkedList<>(type);
    }

    public static <E extends Comparable<E>> LinkedList<E> newSortedLinkedList(@NotNull final Class<?> type) {
        return new SortedLinkedList<>(type);
    }

    private LinkedListFactory() {
        throw new RuntimeException();
    }
}
