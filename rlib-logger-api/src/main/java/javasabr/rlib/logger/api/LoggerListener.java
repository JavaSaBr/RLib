package javasabr.rlib.logger.api;

/**
 * Listener for receiving log output.
 *
 * @since 10.0.0
 */
public interface LoggerListener {

  /**
   * Called when a log line is printed.
   *
   * @param text the log text
   * @since 10.0.0
   */
  void println(String text);

  /**
   * Called to flush any buffered output.
   *
   * @since 10.0.0
   */
  default void flush() {}
}
