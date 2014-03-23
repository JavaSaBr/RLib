package rlib.concurrent.atomic;

import rlib.util.pools.Foldable;

/**
 * Дополнение к стандартной реализации.
 * 
 * @author Ronn
 */
public class AtomicInteger extends java.util.concurrent.atomic.AtomicInteger implements Foldable {

	private static final long serialVersionUID = -624766818867950719L;

	public AtomicInteger() {
		super();
	}

	public AtomicInteger(int initialValue) {
		super(initialValue);
	}

	/**
	 * Атамарная операция по отниманию числа.
	 * 
	 * @param delta разница между текущим и новым значением.
	 * @return новое значение.
	 */
	public final int subAndGet(int delta) {

		while(true) {

			int current = get();
			int next = current - delta;

			if(compareAndSet(current, next)) {
				return next;
			}
		}
	}
}
