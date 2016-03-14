package rlib.util.linkedlist.impl;

import java.util.Collection;
import java.util.NoSuchElementException;

import rlib.util.linkedlist.LinkedList;

/**
 * Базовая реализация связанного списка.
 *
 * @author Ronn
 */
public abstract class AbstractLinkedList<E> implements LinkedList<E> {

    private static final long serialVersionUID = 8034712584065781997L;

    /**
     * Тип элементов в коллекции.
     */
    protected final Class<E> type;

    @SuppressWarnings("unchecked")
    public AbstractLinkedList(final Class<?> type) {
        this.type = (Class<E>) type;
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {

        for (final E object : collection) {
            if (!add(object)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean contains(final Object object) {
        return indexOf(object) != -1;
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {

        for (final Object object : collection) {
            if (!contains(object)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public void finalyze() {
        clear();
    }

    @Override
    public E getFirst() {

        final Node<E> first = getFirstNode();

        if (first == null) {
            throw new NoSuchElementException();
        }

        return first.getItem();
    }

    @Override
    public E getLast() {

        final Node<E> last = getLastNode();

        if (last == null) {
            throw new NoSuchElementException();
        }

        return last.getItem();
    }

    /**
     * @return тип элементов в коллекции.
     */
    protected Class<E> getType() {
        return type;
    }

    @Override
    public int indexOf(final Object object) {

        int index = 0;

        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {

            final E item = node.getItem();

            if (item.equals(object)) {
                return index;
            }

            index++;
        }

        return -1;
    }

    @Override
    public boolean isEmpty() {
        return size() < 1;
    }

    @Override
    public boolean offer(final E element) {
        return add(element);
    }

    @Override
    public boolean offerFirst(final E element) {
        addFirst(element);
        return true;
    }

    @Override
    public boolean offerLast(final E element) {
        addLast(element);
        return true;
    }

    @Override
    public E peek() {
        final Node<E> first = getFirstNode();
        return first == null ? null : first.getItem();
    }

    @Override
    public E peekFirst() {
        final Node<E> first = getFirstNode();
        return first == null ? null : first.getItem();
    }

    @Override
    public E peekLast() {
        final Node<E> last = getLastNode();
        return last == null ? null : last.getItem();
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public void push(final E element) {
        addFirst(element);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public boolean remove(final Object object) {

        if (object == null) {
            throw new NullPointerException("object is null.");
        }

        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            if (object.equals(node.getItem())) {
                unlink(node);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {

        for (final Object object : collection) {
            if (!remove(object)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean removeFirstOccurrence(final Object object) {

        if (object == null) {
            throw new NullPointerException("not fond object.");
        }

        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            if (object.equals(node.getItem())) {
                unlink(node);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean removeLastOccurrence(final Object object) {

        if (object == null) {
            throw new NullPointerException("not fond object.");
        }

        for (Node<E> node = getLastNode(); node != null; node = node.getPrev()) {
            if (object.equals(node.getItem())) {
                unlink(node);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {

        for (final E object : this) {
            if (!collection.contains(object) && !remove(object)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder(getClass().getSimpleName());

        builder.append(" size = ").append(size()).append(" : [");

        if (!isEmpty()) {

            for (final E element : this) {
                builder.append(element).append(", ");
            }

            if (builder.indexOf(",") != -1) {
                builder.delete(builder.length() - 2, builder.length());
            }
        }

        builder.append("]");

        return builder.toString();
    }
}
