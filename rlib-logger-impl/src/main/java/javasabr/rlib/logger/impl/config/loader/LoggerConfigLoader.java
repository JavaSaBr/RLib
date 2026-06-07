package javasabr.rlib.logger.impl.config.loader;

import java.util.Optional;
import javasabr.rlib.logger.impl.config.LoggerConfig;

/**
 * Loader of logger configuration.
 *
 * @since 10.0.0
 */
public interface LoggerConfigLoader {
  
  int ORDER_NORMAL = 100;
  int ORDER_LOW = 1000;
  int ORDER_HIGH = 0;
  
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
