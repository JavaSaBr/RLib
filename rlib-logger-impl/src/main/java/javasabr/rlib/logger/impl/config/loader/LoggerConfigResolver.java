package javasabr.rlib.logger.impl.config.loader;

import java.util.Comparator;
import java.util.Optional;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayCollectors;
import javasabr.rlib.logger.impl.config.LoggerConfig;
import javasabr.rlib.logger.impl.config.loader.impl.DefaultLoggerConfigLoader;

/**
 * Resolver of logger configuration from available loaders.
 *
 * @since 10.0.0
 */
public class LoggerConfigResolver {
  
  private static final Array<LoggerConfigLoader> LOADERS = Array
      .<LoggerConfigLoader>of(new DefaultLoggerConfigLoader())
      .stream()
      .sorted(Comparator.comparingInt(LoggerConfigLoader::order))
      .collect(ArrayCollectors.toArray(LoggerConfigLoader.class));

  /**
   * Loads logger configuration.
   *
   * @return the loaded logger configuration
   * @since 10.0.0
   */
  public static LoggerConfig load() {
    return LOADERS
        .stream()
        .map(LoggerConfigLoader::tryToLoad)
        .flatMap(Optional::stream)
        .findFirst()
        .orElseThrow();
  }
}
