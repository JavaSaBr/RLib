package javasabr.rlib.logger.impl.config;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;

/**
 * Consumer for processed log messages.
 *
 * @since 10.0.0
 */
public interface LogMessageConsumer {
  
  /**
   * Consumes a log message.
   *
   * @param level the log level
   * @param logger the logger
   * @param message the message
   * @since 10.0.0
   */
  void consume(LoggerLevel level, Logger logger, String message);
}
