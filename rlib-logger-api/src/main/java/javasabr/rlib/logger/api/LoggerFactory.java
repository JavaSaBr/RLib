package javasabr.rlib.logger.api;

import java.io.Writer;

/**
 * Factory for creating {@link Logger} instances.
 *
 * @since 10.0.0
 */
public interface LoggerFactory {

  /**
   * Creates a logger with the specified name.
   *
   * @param name the logger name
   * @return the logger instance
   * @since 10.0.0
   */
  Logger make(String name);

  /**
   * Creates a logger for the specified class.
   *
   * @param type the class to create a logger for
   * @return the logger instance
   * @since 10.0.0
   */
  Logger make(Class<?> type);

  /**
   * Returns the default logger.
   *
   * @return the default logger
   * @since 10.0.0
   */
  Logger getDefault();

  /**
   * Adds a listener to receive log output.
   *
   * @param listener the listener to add
   * @since 10.0.0
   */
  void addListener(LoggerListener listener);

  /**
   * Removes a previously added listener.
   *
   * @param listener the listener to remove
   * @since 10.0.0
   */
  void removeListener(LoggerListener listener);

  /**
   * Adds a writer to receive log output.
   *
   * @param writer the writer to add
   * @since 10.0.0
   */
  void addWriter(Writer writer);

  /**
   * Removes a previously added writer.
   *
   * @param writer the writer to remove
   * @since 10.0.0
   */
  void removeWriter(Writer writer);
}
