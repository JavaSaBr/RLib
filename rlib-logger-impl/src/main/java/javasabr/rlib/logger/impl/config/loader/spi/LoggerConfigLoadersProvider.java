package javasabr.rlib.logger.impl.config.loader.spi;

import javasabr.rlib.collections.array.Array;
import javasabr.rlib.logger.impl.config.loader.LoggerConfigLoader;

/**
 * Provider of additional logger configuration loaders.
 *
 * @since 10.0.0
 */
public interface LoggerConfigLoadersProvider {
  
  /**
   * Returns additional logger configuration loaders.
   *
   * @return the logger configuration loaders
   * @since 10.0.0
   */
  Array<LoggerConfigLoader> getLoggerConfigLoaders();
}
