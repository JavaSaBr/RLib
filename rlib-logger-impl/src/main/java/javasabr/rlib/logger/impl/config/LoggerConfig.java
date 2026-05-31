package javasabr.rlib.logger.impl.config;

import javasabr.rlib.collections.array.Array;
import javasabr.rlib.logger.api.Logger;

public interface LoggerConfig {
  
  void configure(Logger logger);
  
  Array<LogConsumer> resolveConsumers(Logger logger);
}
