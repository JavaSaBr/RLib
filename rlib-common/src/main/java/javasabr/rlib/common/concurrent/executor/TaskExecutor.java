package javasabr.rlib.common.concurrent.executor;

import java.util.concurrent.Future;
import javasabr.rlib.common.concurrent.task.CallableTask;
import javasabr.rlib.common.concurrent.task.SimpleTask;
import org.jspecify.annotations.NullMarked;

/**
 * The interface to implement a task executor.
 *
 * @param <L> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public interface TaskExecutor<L> {

  /**
   * Execute a simple task.
   *
   * @param task the simple task.
   */
  void execute(SimpleTask<L> task);

  /**
   * Submit a callable task.
   *
   * @param <R> the type parameter
   * @param task the callable task.
   * @return the reference to the task.
   */
  <R> Future<R> submit(CallableTask<R, L> task);
}
