package com.ss.rlib.common.util.linkedlist.impl;

import com.ss.rlib.common.util.linkedlist.LinkedList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import com.ss.rlib.common.util.linkedlist.LinkedList;

/**
 * The implementation of an iterator for the LinkedList.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class IteratorImpl<E> implements Iterator<E> {

    /**
     * The iteration mode from start to end.
     */
    public static final int NEXT = 1;

    /**
     * The iteration mode from end to start.
     */
    public static final int PREV = 2;

    /**
     * The linked list.
     */
    @NotNull
    private final LinkedList<E> list;

    /**
     * The last returned node.
     */
    @Nullable
    private Node<E> lastReturned;

    /**
     * The next node.
     */
    private Node<E> next;

    /**
     * The iteration mode.
     */
    private final int mode;

    /**
     * The next index.
     */
    private int nextIndex;

    /**
     * Instantiates a new Iterator.
     *
     * @param list the list
     * @param mode the mode
     */
    protected IteratorImpl(@NotNull final LinkedList<E> list, final int mode) {
        this.nextIndex = 0;
        this.mode = mode;
        this.list = list;
        setNext(mode == NEXT ? list.getFirstNode() : mode == PREV ? list.getLastNode() : null);
    }

    @Nullable
    private Node<E> getLastReturned() {
        return lastReturned;
    }

    private void setLastReturned(@Nullable final Node<E> lastReturned) {
        this.lastReturned = lastReturned;
    }

    @NotNull
    private Node<E> getNext() {
        return Objects.requireNonNull(next);
    }

    private void setNext(@Nullable final Node<E> next) {
        this.next = next;
    }

    @Override
    public boolean hasNext() {
        return mode == NEXT ? nextIndex < list.size() : mode == PREV && nextIndex > 0;
    }

    @Override
    public E next() {

        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        if (mode == NEXT) {

            final Node<E> next = getNext();

            setNext(next.getNext());
            setLastReturned(next);

            nextIndex++;

            return next.getItem();
        } else if (mode == PREV) {

            final Node<E> next = getNext();

            setNext(next.getPrev());
            setLastReturned(next);

            nextIndex--;

            return next.getItem();
        }

        return null;
    }

    @Override
    public void remove() {

        final Node<E> lastReturned = getLastReturned();

        if (lastReturned == null) {
            throw new IllegalStateException();
        }

        list.unlink(lastReturned);

        if (mode == NEXT) {
            nextIndex--;
        } else if (mode == PREV) {
            nextIndex++;
        }

        setLastReturned(null);
    }
}