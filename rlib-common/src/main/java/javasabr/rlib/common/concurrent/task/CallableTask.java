package javasabr.rlib.common.concurrent.task;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * The interface to implement a callable tasks.
 *
 * @param <R> the type parameter
 * @param <L> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface CallableTask<R, L> {

  /**
   * Execute this task.
   *
   * @param local the thread local container.
   * @param currentTime the current time.
   * @return the result.
   */
  @Nullable
  R call(@NonNull L local, long currentTime);
}
