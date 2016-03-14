package rlib.util.linkedlist.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

import rlib.util.linkedlist.LinkedList;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.PoolFactory;

/**
 * Реадизация не потокобезопасного {@link LinkedList}.
 *
 * @author Ronn
 */
public class FastLinkedList<E> extends AbstractLinkedList<E> {

    private static final long serialVersionUID = 6627882787737291879L;

    /**
     * Пул узлов.
     */
    private final FoldablePool<Node<E>> pool;

    /**
     * Первый элемент списка.
     */
    private Node<E> first;
    /**
     * Последний элемент списка.
     */
    private Node<E> last;

    /**
     * Размер списка.
     */
    private int size;

    public FastLinkedList(final Class<?> type) {
        super(type);
        this.pool = PoolFactory.newFoldablePool(Node.class);
    }

    @Override
    public boolean add(final E element) {

        if (element == null) {
            throw new NullPointerException("element is null.");
        }

        linkLast(element);
        return true;
    }

    @Override
    public void addFirst(final E element) {
        linkFirst(element);
    }

    @Override
    public void addLast(final E element) {
        linkLast(element);
    }

    @Override
    public void apply(final Function<? super E, ? extends E> function) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            node.setItem(function.apply(node.getItem()));
        }
    }

    @Override
    public void clear() {

        final FoldablePool<Node<E>> pool = getPool();

        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            pool.put(node);
        }

        setFirstNode(null);
        setLastNode(null);

        size = 0;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new IteratorImpl<E>(this, IteratorImpl.PREV);
    }

    @Override
    public E get(final int index) {
        return index < size() >> 1 ? getFirst(index) : getLast(index);
    }

    protected final E getFirst(final int index) {

        int i = 0;

        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {

            if (i == index) {
                return node.getItem();
            }

            i++;
        }

        return null;
    }

    @Override
    public final Node<E> getFirstNode() {
        return first;
    }

    /**
     * @param first первый узел.
     */
    protected void setFirstNode(final Node<E> first) {
        this.first = first;
    }

    protected final E getLast(final int index) {

        int i = size() - 1;

        for (Node<E> node = getLastNode(); node != null; node = node.getPrev()) {

            if (i == index) {
                return node.getItem();
            }

            i--;
        }

        return null;
    }

    @Override
    public final Node<E> getLastNode() {
        return last;
    }

    /**
     * @param last последний узел.
     */
    protected void setLastNode(final Node<E> last) {
        this.last = last;
    }

    /**
     * Получение нового узла по указанным параметрам.
     *
     * @param prev предыдущий узел.
     * @param item хранимый итем.
     * @param next следующий узел.
     * @return новый узел.
     */
    protected Node<E> getNewNode(final Node<E> prev, final E item, final Node<E> next) {

        Node<E> node = getPool().take();

        if (node == null) {
            node = new Node<E>();
        }

        node.setItem(item);
        node.setNext(next);
        node.setPrev(prev);

        return node;
    }

    /**
     * @return пул узлов.
     */
    protected FoldablePool<Node<E>> getPool() {
        return pool;
    }

    protected final void insertAfter(final Node<E> node, final E item) {

        final Node<E> next = node.getNext();
        final Node<E> newNode = getNewNode(node, item, next);

        if (next == null) {
            setLastNode(newNode);
        } else {
            next.setPrev(newNode);
        }

        node.setNext(newNode);
    }

    protected final void insertBefore(final Node<E> node, final E item) {

        final Node<E> prev = node.getPrev();
        final Node<E> newNode = getNewNode(prev, item, node);

        if (prev == null) {
            setFirstNode(newNode);
        } else {
            prev.setNext(newNode);
        }

        node.setPrev(newNode);
    }

    @Override
    public Iterator<E> iterator() {
        return new IteratorImpl<E>(this, IteratorImpl.NEXT);
    }

    protected final void linkFirst(final E eitem) {

        final Node<E> first = getFirstNode();
        final Node<E> newNode = getNewNode(null, eitem, first);

        setFirstNode(newNode);

        if (first == null) {
            setLastNode(newNode);
        } else {
            first.setPrev(newNode);
        }

        size++;
    }

    protected final void linkLast(final E item) {

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

    @Override
    public Object[] toArray() {

        final Object[] array = (Object[]) java.lang.reflect.Array.newInstance(getType(), size());

        int index = 0;

        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            array[index++] = node.getItem();
        }

        return array;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] array) {

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
    public final E unlink(final Node<E> node) {

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

    protected final E unlinkFirst(final Node<E> node) {

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

    protected final E unlinkLast(final Node<E> node) {

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
