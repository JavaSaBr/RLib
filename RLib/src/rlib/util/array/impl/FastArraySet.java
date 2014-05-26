package rlib.util.array.impl;

import rlib.util.array.Array;

/**
 * Реализация {@link FastArray} с проверкой на уникальность элемента при
 * вставке.
 * 
 * @see FastArray
 * @author Ronn
 */
public class FastArraySet<E> extends FastArray<E> {

	private static final long serialVersionUID = 1L;

	public FastArraySet(final Class<E> type) {
		super(type);
	}

	public FastArraySet(final Class<E> type, final int size) {
		super(type, size);
	}

	@Override
	public FastArray<E> add(final E element) {

		if(contains(element)) {
			return this;
		}

		return super.add(element);
	}

	@Override
	protected void processAdd(final Array<? extends E> elements, final int selfSize, final int targetSize) {
		for(final E element : elements.array()) {

			if(element == null) {
				break;
			}

			if(!contains(element)) {
				unsafeAdd(element);
			}
		}
	}

	@Override
	protected void processAdd(final E[] elements, final int selfSize, final int targetSize) {
		for(final E element : elements) {

			if(element == null) {
				break;
			}

			if(!contains(element)) {
				unsafeAdd(element);
			}
		}
	}
}
