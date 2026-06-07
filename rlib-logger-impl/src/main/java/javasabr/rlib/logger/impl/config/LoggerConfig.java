package javasabr.rlib.logger.impl.config;

import javasabr.rlib.collections.array.UnsafeArray;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.config.consumer.LogMessageConsumer;

/**
 * Configuration contract for logger levels and message consumers.
 *
 * @since 10.0.0
 */
public interface LoggerConfig {

  /**
   * Configures enabled state for logger levels.
   *
   * @param logger the logger
   * @since 10.0.0
   */
  void configureLevels(Logger logger);

  /**
   * Resolves consumers for the logger and level.
   *
   * @param logger the logger
   * @param level the log level
   * @return the resolved consumers
   * @since 10.0.0
   */
  UnsafeArray<LogMessageConsumer> resolveConsumers(Logger logger, LoggerLevel level);
}
