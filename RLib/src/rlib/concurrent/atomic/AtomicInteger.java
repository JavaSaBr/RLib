package rlib.concurrent.atomic;

import rlib.util.pools.Reusable;

/**
 * Дополнение к стандартной реализации.
 *
 * @author Ronn
 */
public class AtomicInteger extends java.util.concurrent.atomic.AtomicInteger implements Reusable {

    private static final long serialVersionUID = -624766818867950719L;

    public AtomicInteger() {
    }

    public AtomicInteger(final int initialValue) {
        super(initialValue);
    }

    /**
     * Атамарная операция по отниманию числа.
     *
     * @param delta разница между текущим и новым значением.
     * @return новое значение.
     */
    public final int subAndGet(final int delta) {

        while (true) {

            final int current = get();
            final int next = current - delta;

            if (compareAndSet(current, next)) {
                return next;
            }
        }
    }
}
