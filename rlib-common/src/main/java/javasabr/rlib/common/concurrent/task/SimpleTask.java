package javasabr.rlib.common.concurrent.task;

/**
 * The interface to implement a simple task.
 *
 * @param <L> the type parameter
 * @author JavaSaBr
 */
public interface SimpleTask<L> extends CallableTask<Void, L> {

  @Override
  default Void call(final L local, final long currentTime) {
    execute(local, currentTime);
    return null;
  }

  /**
   * Execute this task.
   *
   * @param local the thread local container.
   * @param currentTime the current time.
   */
  void execute(L local, long currentTime);
}
