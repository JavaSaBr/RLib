package javasabr.rlib.logger.impl.config;

import javasabr.rlib.collections.array.UnsafeArray;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;

public interface LoggerConfig {

  /**
   * Should configure enable state of all levels.
   */
  void configureLevels(Logger logger);

  /**
   * Should return trimmed unsafe array for efficient access.
   */
  UnsafeArray<LogMessageConsumer> resolveConsumers(Logger logger, LoggerLevel level);
}
