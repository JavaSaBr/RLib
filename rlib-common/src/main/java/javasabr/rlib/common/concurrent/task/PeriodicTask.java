package javasabr.rlib.common.concurrent.task;

/**
 * The interface to implement a periodic tasks.
 *
 * @param <L> the type parameter
 * @author JavaSaBr
 */
public interface PeriodicTask<L> extends CallableTask<Boolean, L> {

  @Override
  default Boolean call(final L local, final long currentTime) {
    if (update(local, currentTime)) {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

  /**
   * Notify about finishing this task.
   *
   * @param local the thread local container.
   */
  default void onFinish(final L local) {
  }

  /**
   * Execute and update this task.
   *
   * @param local the thread local container.
   * @param currentTime the current time.
   * @return true if this task is finished.
   */
  boolean update(L local, long currentTime);
}
