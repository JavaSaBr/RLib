package javasabr.rlib.logger.impl.config.loader;

import java.util.Comparator;
import java.util.Optional;
import java.util.ServiceLoader;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayCollectors;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.logger.impl.config.LoggerConfig;
import javasabr.rlib.logger.impl.config.loader.impl.DefaultLoggerConfigLoader;
import javasabr.rlib.logger.impl.config.loader.impl.PropertyLoggerConfigLoader;

/**
 * Resolver of logger configuration from available loaders.
 *
 * @since 10.0.0
 */
public class LoggerConfigResolver {
  
  private static final Array<LoggerConfigLoader> LOADERS;

  static {
    var registeredProviders = ArrayFactory.mutableArray(LoggerConfigLoader.class);
    registeredProviders.add(new DefaultLoggerConfigLoader());
    registeredProviders.add(new PropertyLoggerConfigLoader());
    for (var provider : ServiceLoader.load(LoggerConfigLoadersProvider.class)) {
      registeredProviders.addAll(provider.getLoggerConfigLoaders());
    }
    LOADERS = registeredProviders.stream()
        .sorted(Comparator.comparingInt(LoggerConfigLoader::order))
        .collect(ArrayCollectors.toArray(LoggerConfigLoader.class));
  }
  
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
