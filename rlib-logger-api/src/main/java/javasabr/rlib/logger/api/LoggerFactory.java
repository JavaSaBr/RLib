package javasabr.rlib.logger.api;

import java.io.Writer;

public interface LoggerFactory {

  /**
   * Make a new logger with the name.
   *
   * @param name the logger's name.
   * @return the new logger.
   */
  Logger make(String name);

  /**
   * Make a new logger for the type.
   *
   * @param type the logger's type.
   * @return the new logger.
   */
  Logger make(Class<?> type);

  /**
   * Get a default logger.
   *
   * @return he default logger.
   */
  Logger getDefault();

  /**
   * Add the new listener.
   *
   * @param listener the new listener.
   */
  void addListener(LoggerListener listener);

  /**
   * Add the new writer.
   *
   * @param writer the new writer.
   */
  void addWriter(Writer writer);

  /**
   * Remove the listener.
   *
   * @param listener the listener.
   */
  void removeListener(LoggerListener listener);

  /**
   * Remove the writer.
   *
   * @param writer the writer.
   */
  void removeWriter(Writer writer);
}
