package com.ss.rlib.common.util.linkedlist.impl;

import com.ss.rlib.common.util.pools.PoolFactory;
import com.ss.rlib.common.util.pools.ReusablePool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

/**
 * The non thread-safe implementation of the LinkedList.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class FastLinkedList<E> extends AbstractLinkedList<E> {

    private static final long serialVersionUID = 6627882787737291879L;

    /**
     * The node pool.
     */
    @NotNull
    private final ReusablePool<Node<E>> pool;

    /**
     * The first element.
     */
    @Nullable
    private Node<E> first;

    /**
     * The second element.
     */
    @Nullable
    private Node<E> last;

    /**
     * The size.
     */
    private int size;

    /**
     * Instantiates a new Fast linked list.
     *
     * @param type the type
     */
    public FastLinkedList(@NotNull final Class<?> type) {
        super(type);
        this.pool = PoolFactory.newReusablePool(Node.class);
    }

    @Override
    public boolean add(@NotNull final E element) {
        linkLast(element);
        return true;
    }

    @Override
    public void addFirst(@NotNull final E element) {
        linkFirst(element);
    }

    @Override
    public void addLast(@NotNull final E element) {
        linkLast(element);
    }

    @Override
    public void apply(@NotNull final Function<? super E, ? extends E> function) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            node.setItem(function.apply(node.getItem()));
        }
    }

    @Override
    public void clear() {

        final ReusablePool<Node<E>> pool = getPool();

        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            pool.put(node);
        }

        setFirstNode(null);
        setLastNode(null);

        size = 0;
    }

    @NotNull
    @Override
    public Iterator<E> descendingIterator() {
        return new IteratorImpl<>(this, IteratorImpl.PREV);
    }

    @Override
    public E get(final int index) {
        return index < size() >> 1 ? getFirst(index) : getLast(index);
    }

    /**
     * Gets first.
     *
     * @param index the index
     * @return the first
     */
    @Nullable
    protected final E getFirst(final int index) {

        int i = 0;

        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            if (i == index) return node.getItem();
            i++;
        }

        return null;
    }

    @Nullable
    @Override
    public final Node<E> getFirstNode() {
        return first;
    }

    /**
     * Sets first node.
     *
     * @param first the first node.
     */
    protected void setFirstNode(@Nullable final Node<E> first) {
        this.first = first;
    }

    /**
     * Gets last.
     *
     * @param index the index
     * @return the last
     */
    @Nullable
    protected final E getLast(final int index) {

        int i = size() - 1;

        for (Node<E> node = getLastNode(); node != null; node = node.getPrev()) {
            if (i == index) return node.getItem();
            i--;
        }

        return null;
    }

    @Nullable
    @Override
    public final Node<E> getLastNode() {
        return last;
    }

    /**
     * Sets last node.
     *
     * @param last the last node.
     */
    protected void setLastNode(@Nullable final Node<E> last) {
        this.last = last;
    }

    /**
     * Get a new node.
     *
     * @param prev the prev node.
     * @param item the item.
     * @param next the next node.
     * @return the new node.
     */
    @NotNull
    protected Node<E> getNewNode(@Nullable final Node<E> prev, @NotNull final E item, @Nullable final Node<E> next) {

        final Node<E> node = getPool().take(Node::new);
        node.setItem(item);
        node.setNext(next);
        node.setPrev(prev);

        return node;
    }

    /**
     * Gets pool.
     *
     * @return the pool.
     */
    @NotNull
    protected ReusablePool<Node<E>> getPool() {
        return pool;
    }

    /**
     * Insert after.
     *
     * @param node the node
     * @param item the item
     */
    protected final void insertAfter(@NotNull final Node<E> node, final E item) {

        final Node<E> next = node.getNext();
        final Node<E> newNode = getNewNode(node, item, next);

        if (next == null) {
            setLastNode(newNode);
        } else {
            next.setPrev(newNode);
        }

        node.setNext(newNode);
    }

    /**
     * Insert before.
     *
     * @param node the node
     * @param item the item
     */
    protected final void insertBefore(@NotNull final Node<E> node, final E item) {

        final Node<E> prev = node.getPrev();
        final Node<E> newNode = getNewNode(prev, item, node);

        if (prev == null) {
            setFirstNode(newNode);
        } else {
            prev.setNext(newNode);
        }

        node.setPrev(newNode);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new IteratorImpl<>(this, IteratorImpl.NEXT);
    }

    /**
     * Link first.
     *
     * @param item the item
     */
    protected final void linkFirst(@NotNull final E item) {
        Objects.requireNonNull(item);

        final Node<E> first = getFirstNode();
        final Node<E> newNode = getNewNode(null, item, first);

        setFirstNode(newNode);

        if (first == null) {
            setLastNode(newNode);
        } else {
            first.setPrev(newNode);
        }

        size++;
    }

    /**
     * Link last.
     *
     * @param item the item
     */
    protected final void linkLast(@NotNull final E item) {
        Objects.requireNonNull(item);

        final Node<E> last = getLastNode();
        final Node<E> newNode = getNewNode(last, item, null);

        setLastNode(newNode);

        if (last == null) {
            setFirstNode(newNode);
        } else {
            last.setNext(newNode);
        }

        size++;
    }

    @Override
    public E poll() {
        final Node<E> first = getFirstNode();
        return first == null ? null : unlinkFirst(first);
    }

    @Override
    public E pollFirst() {
        final Node<E> first = getFirstNode();
        return first == null ? null : unlinkFirst(first);
    }

    @Override
    public E pollLast() {
        final Node<E> last = getLastNode();
        return last == null ? null : unlinkLast(last);
    }

    @Override
    public E removeFirst() {

        final Node<E> first = getFirstNode();

        if (first == null) {
            throw new NoSuchElementException();
        }

        return unlinkFirst(first);
    }

    @Override
    public E removeLast() {

        final Node<E> last = getLastNode();

        if (last == null) {
            throw new NoSuchElementException();
        }

        return unlinkLast(last);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E take() {
        return removeFirst();
    }

    @NotNull
    @Override
    public Object[] toArray() {

        final Object[] array = (Object[]) java.lang.reflect.Array.newInstance(getType(), size());

        int index = 0;

        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            array[index++] = node.getItem();
        }

        return array;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(@NotNull T[] array) {

        final int size = size();

        if (array.length < size) {
            array = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
        }

        int i = 0;

        final Object[] result = array;

        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            result[i++] = node.getItem();
        }

        return array;
    }

    @Override
    public String toString() {
        return super.toString() + "\n " + pool;
    }

    @Override
    public final E unlink(@NotNull final Node<E> node) {

        final E element = node.getItem();

        final Node<E> next = node.getNext();
        final Node<E> prev = node.getPrev();

        if (prev == null) {
            setFirstNode(next);
        } else {
            prev.setNext(next);
        }

        if (next == null) {
            setLastNode(prev);
        } else {
            next.setPrev(prev);
        }

        size--;

        getPool().put(node);

        return element;
    }

    /**
     * Unlink first e.
     *
     * @param node the node
     * @return the e
     */
    protected final E unlinkFirst(@NotNull final Node<E> node) {

        final E element = node.getItem();

        final Node<E> next = node.getNext();

        setFirstNode(next);

        if (next == null) {
            setLastNode(null);
        } else {
            next.setPrev(null);
        }

        size--;

        getPool().put(node);

        return element;
    }

    /**
     * Unlink last e.
     *
     * @param node the node
     * @return the e
     */
    protected final E unlinkLast(@NotNull final Node<E> node) {

        final E element = node.getItem();

        final Node<E> prev = node.getPrev();

        setLastNode(prev);

        if (prev == null) {
            setFirstNode(null);
        } else {
            prev.setNext(null);
        }

        size--;

        getPool().put(node);

        return element;
    }
}
