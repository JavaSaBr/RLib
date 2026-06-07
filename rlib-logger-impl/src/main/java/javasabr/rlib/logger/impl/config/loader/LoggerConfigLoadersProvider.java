package javasabr.rlib.logger.impl.config.loader;

import javasabr.rlib.collections.array.Array;

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
