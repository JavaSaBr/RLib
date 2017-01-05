package rlib.util.linkedlist.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import rlib.util.pools.Reusable;

/**
 * The node of the LinkedList.
 *
 * @author JavaSaBr
 */
public final class Node<E> implements Reusable {

    /**
     * The item.
     */
    private E item;

    /**
     * The prev node.
     */
    @Nullable
    private Node<E> prev;

    /**
     * The next node.
     */
    @Nullable
    private Node<E> next;

    @Override
    public void free() {
        item = null;
        prev = null;
        next = null;
    }

    /**
     * @return yhe item.
     */
    @NotNull
    public E getItem() {
        return item;
    }

    /**
     * @param item yhe item.
     */
    public void setItem(@Nullable final E item) {
        this.item = item;
    }

    /**
     * @return yhe next node.
     */
    @Nullable
    public Node<E> getNext() {
        return next;
    }

    /**
     * @param next yhe next node.
     */
    public void setNext(@Nullable final Node<E> next) {
        this.next = next;
    }

    /**
     * @return yhe prev node.
     */
    @Nullable
    public Node<E> getPrev() {
        return prev;
    }

    /**
     * @param prev yhe prev node.
     */
    public void setPrev(@Nullable final Node<E> prev) {
        this.prev = prev;
    }
}
