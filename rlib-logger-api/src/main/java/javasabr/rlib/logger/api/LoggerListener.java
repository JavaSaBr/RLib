package javasabr.rlib.logger.api;

/**
 * The interface to implement a listener of logger events.
 *
 * @author JavaSaBr
 */
public interface LoggerListener {

  /**
   * Print the result logger message.
   *
   * @param text the text.
   */
  void println(String text);

  /**
   * Flush last data.
   */
  default void flush() {
  }
}
