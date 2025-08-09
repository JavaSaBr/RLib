package javasabr.rlib.common.concurrent.atomic;

import java.io.Serial;
import java.util.concurrent.atomic.AtomicInteger;
import javasabr.rlib.common.util.pools.Reusable;

/**
 * The atomic integer with additional methods.
 *
 * @author JavaSaBr
 */
public final class ReusableAtomicInteger extends AtomicInteger implements Reusable {

  @Serial
  private static final long serialVersionUID = -624766818867950719L;

  public ReusableAtomicInteger() {}

  public ReusableAtomicInteger(int initialValue) {
    super(initialValue);
  }

  /**
   * Atomically decrements by delta the current value.
   *
   * @param delta the delta.
   * @return the result value.
   */
  public int subAndGet(int delta) {

    int current;
    int next;

    do {
      current = get();
      next = current - delta;
    } while (!compareAndSet(current, next));

    return next;
  }
}
