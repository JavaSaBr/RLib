package javasabr.rlib.logger.api;

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
