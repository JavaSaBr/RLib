package javasabr.rlib.logger.impl.config;

import java.util.Comparator;
import java.util.Optional;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayCollectors;
import javasabr.rlib.logger.impl.config.impl.DefaultLoggerConfigLoader;

public class LoggerConfigResolver {
  
  private static final Array<LoggerConfigLoader> LOADERS = Array
      .<LoggerConfigLoader>of(new DefaultLoggerConfigLoader())
      .stream()
      .sorted(Comparator.comparingInt(LoggerConfigLoader::order))
      .collect(ArrayCollectors.toArray(LoggerConfigLoader.class));

  public static LoggerConfig load() {
    return LOADERS
        .stream()
        .map(LoggerConfigLoader::tryToLoad)
        .flatMap(Optional::stream)
        .findFirst()
        .orElseThrow();
  }
}
