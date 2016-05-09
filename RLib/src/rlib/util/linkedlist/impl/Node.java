package rlib.util.linkedlist.impl;

import rlib.util.linkedlist.LinkedList;
import rlib.util.pools.Reusable;

/**
 * Реализация узла для {@link LinkedList}.
 *
 * @author Ronn
 */
public final class Node<E> implements Reusable {

    /**
     * Содержимый элемент.
     */
    private E item;

    /**
     * Ссылка на предыдущий узел.
     */
    private Node<E> prev;

    /**
     * Ссылка на след. узел.
     */
    private Node<E> next;

    @Override
    public void free() {
        item = null;
        prev = null;
        next = null;
    }

    /**
     * @return хранимый элемент.
     */
    public E getItem() {
        return item;
    }

    /**
     * @param item хранимый элемент.
     */
    public void setItem(final E item) {
        this.item = item;
    }

    /**
     * @return следующий узел.
     */
    public Node<E> getNext() {
        return next;
    }

    /**
     * @param next следующий узел.
     */
    public void setNext(final Node<E> next) {
        this.next = next;
    }

    /**
     * @return предыдущий узел.
     */
    public Node<E> getPrev() {
        return prev;
    }

    /**
     * @param prev предыдущий узел.
     */
    public void setPrev(final Node<E> prev) {
        this.prev = prev;
    }
}
