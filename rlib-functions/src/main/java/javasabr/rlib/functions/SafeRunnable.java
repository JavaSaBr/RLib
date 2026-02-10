package javasabr.rlib.functions;

/**
 * Represents a task that may throw an exception.
 *
 * @since 10.0.0
 */
@FunctionalInterface
public interface SafeRunnable {

  /**
   * Runs this task.
   *
   * @throws Exception if an error occurs
   * @since 10.0.0
   */
  void run() throws Exception;
}
