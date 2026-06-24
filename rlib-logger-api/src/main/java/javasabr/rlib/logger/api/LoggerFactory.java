package javasabr.rlib.logger.api;

/**
 * Factory for creating {@link Logger} instances.
 *
 * @since 10.0.0
 */
public interface LoggerFactory {

  String ROOT_LOGGER_NAME = "ROOT";
  
  /**
   * Creates or gets a logger with the specified name.
   *
   * @param name the logger name
   * @return the logger instance
   * @since 10.0.0
   */
  Logger getLogger(String name);

  /**
   * Creates or gets a logger for the specified class.
   *
   * @param type the class to create a logger for
   * @return the logger instance
   * @since 10.0.0
   */
  Logger getLogger(Class<?> type);

  /**
   * Returns the root logger.
   *
   * @return the root logger
   * @since 10.0.0
   */
  Logger getRootLogger();
  
  /**
   * Returns the logger service.
   *
   * @return the logger service
   * @since 10.0.0
   */
  LoggerService getLoggerService();
}
