package com.ss.rlib.common.util.linkedlist.impl;

import com.ss.rlib.common.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The node of the LinkedList.
 *
 * @param <E> the type parameter
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
     * Gets item.
     *
     * @return yhe item.
     */
    @NotNull
    public E getItem() {
        return item;
    }

    /**
     * Sets item.
     *
     * @param item yhe item.
     */
    public void setItem(@Nullable final E item) {
        this.item = item;
    }

    /**
     * Gets next.
     *
     * @return yhe next node.
     */
    @Nullable
    public Node<E> getNext() {
        return next;
    }

    /**
     * Sets next.
     *
     * @param next yhe next node.
     */
    public void setNext(@Nullable final Node<E> next) {
        this.next = next;
    }

    /**
     * Gets prev.
     *
     * @return yhe prev node.
     */
    @Nullable
    public Node<E> getPrev() {
        return prev;
    }

    /**
     * Sets prev.
     *
     * @param prev yhe prev node.
     */
    public void setPrev(@Nullable final Node<E> prev) {
        this.prev = prev;
    }
}
