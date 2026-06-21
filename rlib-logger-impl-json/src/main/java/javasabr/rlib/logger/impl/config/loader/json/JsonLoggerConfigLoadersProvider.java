package javasabr.rlib.logger.impl.config.loader.json;

import javasabr.rlib.collections.array.Array;
import javasabr.rlib.logger.impl.config.loader.LoggerConfigLoader;
import javasabr.rlib.logger.impl.config.loader.spi.LoggerConfigLoadersProvider;

public class JsonLoggerConfigLoadersProvider implements LoggerConfigLoadersProvider {

  private static final Array<LoggerConfigLoader> LOADERS = Array.of(new JsonLoggerConfigLoader());

  @Override
  public Array<LoggerConfigLoader> getLoggerConfigLoaders() {
    return LOADERS;
  }
}
