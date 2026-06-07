package javasabr.rlib.logger.impl.config.loader;

import javasabr.rlib.collections.array.Array;

public interface LoggerConfigLoadersProvider {
  
  Array<LoggerConfigLoader> getLoggerConfigLoaders();
}
