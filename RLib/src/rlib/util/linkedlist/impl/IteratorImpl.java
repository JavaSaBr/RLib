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

	/** режим итерирования с начала в конец */
	public static final int NEXT = 1;
	/** режим итерирования с конца в начало */
	public static final int PREV = 2;

	/** итерируемый список */
	private final LinkedList<E> list;

	/** последний возвращаемый элемнт */
	private Node<E> lastReturned;
	/** следующий элемент */
	private Node<E> next;

	/** режим итератора */
	private final int mode;

	/** следующий индекс */
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

	private Node<E> getNext() {
		return next;
	}

	@Override
	public boolean hasNext() {
		return mode == NEXT ? nextIndex < list.size() : mode == PREV ? nextIndex > 0 : false;
	}

	@Override
	public E next() {

		if(!hasNext()) {
			throw new NoSuchElementException();
		}

		if(mode == NEXT) {

			final Node<E> next = getNext();
			final Node<E> lastReturned = next;

			setNext(next.getNext());
			setLastReturned(lastReturned);

			nextIndex++;

			return lastReturned.getItem();
		} else if(mode == PREV) {

			final Node<E> next = getNext();
			final Node<E> lastReturned = next;

			setNext(next.getPrev());
			setLastReturned(lastReturned);

			nextIndex--;

			return lastReturned.getItem();
		}

		return null;
	}

	@Override
	public void remove() {

		final Node<E> lastReturned = getLastReturned();

		if(lastReturned == null) {
			throw new IllegalStateException();
		}

		list.unlink(lastReturned);

		if(mode == NEXT) {
			nextIndex--;
		} else if(mode == PREV) {
			nextIndex++;
		}

		setLastReturned(null);
	}

	private void setLastReturned(final Node<E> lastReturned) {
		this.lastReturned = lastReturned;
	}

	private void setNext(final Node<E> next) {
		this.next = next;
	}
}