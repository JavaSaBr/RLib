package javasabr.rlib.logger.impl.config;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;

public interface LogMessageConsumer {
  
  void consume(LoggerLevel level, Logger logger, String message);
}
