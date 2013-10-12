package rlib.util.linkedlist;

import java.util.Collection;

import rlib.util.array.FuncElement;

/**
 * Базовая реализация связанного списка.
 * 
 * @author Ronn
 */
public abstract class AbstractLinkedList<E> implements LinkedList<E> {

	private static final long serialVersionUID = 8034712584065781997L;

	/** тип элементов в коллекции */
	protected final Class<E> type;

	@SuppressWarnings("unchecked")
	public AbstractLinkedList(final Class<?> type) {
		this.type = (Class<E>) type;
	}

	@Override
	public boolean addAll(final Collection<? extends E> collection) {

		for(final E object : collection) {
			if(!add(object)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void apply(final FuncElement<? super E> func) {
		for(final E element : this) {
			func.apply(element);
		}
	}

	@Override
	public boolean containsAll(final Collection<?> collection) {

		for(final Object object : collection) {
			if(!contains(object)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void finalyze() {
		clear();
	}

	/**
	 * @return тип элементов в коллекции.
	 */
	protected Class<E> getType() {
		return type;
	}

	@Override
	public boolean isEmpty() {
		return size() < 1;
	}

	@Override
	public void readLock() {
	}

	@Override
	public void readUnlock() {
	}

	@Override
	public void reinit() {
	}

	@Override
	public boolean removeAll(final Collection<?> collection) {

		for(final Object object : collection) {
			if(!remove(object)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean retainAll(final Collection<?> collection) {

		for(final E object : this) {
			if(!collection.contains(object) && !remove(object)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {

		final StringBuilder builder = new StringBuilder(getClass().getSimpleName());

		builder.append(" size = ").append(size()).append(" : [");

		if(!isEmpty()) {

			for(final E element : this) {
				builder.append(element).append(", ");
			}

			if(builder.indexOf(",") != -1) {
				builder.delete(builder.length() - 2, builder.length());
			}
		}

		builder.append("]");

		return builder.toString();
	}

	@Override
	public void writeLock() {
	}

	@Override
	public void writeUnlock() {
	}
}
