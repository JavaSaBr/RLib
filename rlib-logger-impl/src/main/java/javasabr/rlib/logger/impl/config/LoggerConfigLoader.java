package javasabr.rlib.logger.impl.config;

import java.util.Optional;

/**
 * Loader of logger configuration.
 *
 * @since 10.0.0
 */
public interface LoggerConfigLoader {
  
  /**
   * Tries to load logger configuration.
   *
   * @return loaded configuration if available
   * @since 10.0.0
   */
  Optional<LoggerConfig> tryToLoad();
  
  /**
   * Returns loader order.
   *
   * @return the loader order
   * @since 10.0.0
   */
  int order();
}
