package javasabr.rlib.logger.impl.config;

import javasabr.rlib.logger.api.LoggerLevel;

public interface LogConsumer {
  
  void consume(LoggerLevel level, String loggerName, String message);
}
