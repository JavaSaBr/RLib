package rlib.util.linkedlist.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import rlib.util.linkedlist.LinkedList;

/**
 * Реализация итератара для {@link LinkedList}
 *
 * @author Ronn
 */
public class IteratorImpl<E> implements Iterator<E> {

    /**
     * Режим итерирования с начала в конец.
     */
    public static final int NEXT = 1;

    /**
     * Режим итерирования с конца в начало.
     */
    public static final int PREV = 2;

    /**
     * Итерируемый список.
     */
    private final LinkedList<E> list;

    /**
     * Последний возвращаемый элемент.
     */
    private Node<E> lastReturned;

    /**
     * Следующий элемент.
     */
    private Node<E> next;

    /**
     * Режим итератора.
     */
    private final int mode;

    /**
     * Следующий индекс.
     */
    private int nextIndex;

    protected IteratorImpl(final LinkedList<E> list, final int mode) {
        this.nextIndex = 0;
        this.mode = mode;
        this.list = list;
        setNext(mode == NEXT ? list.getFirstNode() : mode == PREV ? list.getLastNode() : null);
    }

    private Node<E> getLastReturned() {
        return lastReturned;
    }

    private void setLastReturned(final Node<E> lastReturned) {
        this.lastReturned = lastReturned;
    }

    private Node<E> getNext() {
        return next;
    }

    private void setNext(final Node<E> next) {
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