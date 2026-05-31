package javasabr.rlib.logger.api;

import java.io.Writer;

/**
 * Service interface for configuring logger behavior.
 *
 * @since 10.0.0
 */
public interface LoggerService {

  /**
   * Indicates that the level is not configured.
   */
  int NOT_CONFIGURE = -1;

  /**
   * Indicates that the level is disabled.
   */
  int DISABLED = 0;

  /**
   * Indicates that the level is enabled.
   */
  int ENABLED = 1;

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

  /**
   * Enables logging at the specified level for the class.
   *
   * @param cs the class to configure
   * @param level the log level
   * @since 10.0.0
   */
  void enable(Class<?> cs, LoggerLevel level);

  /**
   * Disables logging at the specified level for the class.
   *
   * @param cs the class to configure
   * @param level the log level
   * @since 10.0.0
   */
  void disable(Class<?> cs, LoggerLevel level);

  /**
   * Configures the default setting for a log level.
   *
   * @param level the log level
   * @param def true to enable by default, false to disable
   * @since 10.0.0
   */
  void configureDefault(LoggerLevel level, boolean def);

  /**
   * Removes the default configuration for a log level.
   *
   * @param level the log level
   * @since 10.0.0
   */
  void removeDefault(LoggerLevel level);

  /**
   * Returns the enabled state for the specified level.
   *
   * @param level the log level
   * @return {@link #NOT_CONFIGURE}, {@link #ENABLED}, or {@link #DISABLED}
   * @since 10.0.0
   */
  int enabled(LoggerLevel level);

  /**
   * Writes a log message.
   *
   * @param logger the logger
   * @param level the log level
   * @param message the message to write
   * @since 10.0.0
   */
  void write(Logger logger, LoggerLevel level, String message);
}
