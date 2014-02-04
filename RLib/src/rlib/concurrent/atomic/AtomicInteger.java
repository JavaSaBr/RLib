package rlib.concurrent.atomic;

/**
 * Дополнение к стандартной реализации.
 * 
 * @author Ronn
 */
public class AtomicInteger extends java.util.concurrent.atomic.AtomicInteger {

	private static final long serialVersionUID = -624766818867950719L;

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
