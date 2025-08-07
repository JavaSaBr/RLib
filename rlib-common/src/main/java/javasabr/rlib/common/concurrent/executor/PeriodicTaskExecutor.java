package javasabr.rlib.common.concurrent.executor;

import javasabr.rlib.common.concurrent.task.PeriodicTask;
import org.jspecify.annotations.NullMarked;

/**
 * The interface to implement a periodic task executor.
 *
 * @param <T> the type parameter
 * @param <L> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public interface PeriodicTaskExecutor<T extends PeriodicTask<L>, L> {

  /**
   * Add a periodic task to execute.
   *
   * @param task the periodic task.
   */
  void addTask(T task);

  /**
   * Remove a periodic task from executing.
   *
   * @param task the periodic task.
   */
  void removeTask(T task);
}
