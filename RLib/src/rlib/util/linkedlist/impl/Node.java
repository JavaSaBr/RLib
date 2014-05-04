package rlib.util.linkedlist.impl;

import rlib.util.linkedlist.LinkedList;
import rlib.util.pools.Foldable;

/**
 * Реализация узла для {@link LinkedList}.
 * 
 * @author Ronn
 */
public final class Node<E> implements Foldable {

	/** содержимый итем */
	private E item;

	/** ссылка на предыдущий узел */
	private Node<E> prev;
	/** ссылка на след. узел */
	private Node<E> next;

	@Override
	public void finalyze() {
		item = null;
		prev = null;
		next = null;
	}

	/**
	 * @return хранимый итем.
	 */
	public E getItem() {
		return item;
	}

	/**
	 * @return следующий узел.
	 */
	public Node<E> getNext() {
		return next;
	}

	/**
	 * @return предыдущий узел.
	 */
	public Node<E> getPrev() {
		return prev;
	}

	/**
	 * @param item хранимый итем.
	 */
	public void setItem(final E item) {
		this.item = item;
	}

	/**
	 * @param next следующий узел.
	 */
	public void setNext(final Node<E> next) {
		this.next = next;
	}

	/**
	 * @param prev предыдущий узел.
	 */
	public void setPrev(final Node<E> prev) {
		this.prev = prev;
	}
}
