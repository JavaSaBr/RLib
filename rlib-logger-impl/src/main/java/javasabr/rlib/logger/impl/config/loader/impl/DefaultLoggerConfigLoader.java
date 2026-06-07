package javasabr.rlib.logger.impl.config.loader.impl;

import java.util.Optional;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.dictionary.RefToRefDictionary;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.DefaultLoggerService;
import javasabr.rlib.logger.impl.config.LoggerConfig;
import javasabr.rlib.logger.impl.config.consumer.LogMessageConsumer;
import javasabr.rlib.logger.impl.config.consumer.impl.ConsoleMessageConsumer;
import javasabr.rlib.logger.impl.config.impl.DefaultLoggerConfig;
import javasabr.rlib.logger.impl.config.impl.DefaultLoggerConfig.LoggerConsumersKey;
import javasabr.rlib.logger.impl.config.loader.LoggerConfigLoader;
import javasabr.rlib.logger.impl.config.render.impl.SimpleLogMessageRender;

public class DefaultLoggerConfigLoader implements LoggerConfigLoader {
 
  @Override
  public Optional<LoggerConfig> tryToLoad() {
    RefToRefDictionary<String, LoggerLevel> loggerLevels = RefToRefDictionary.of(
        DefaultLoggerService.ROOT_LOGGER_NAME,
        LoggerLevel.INFO);
    RefToRefDictionary<LoggerConsumersKey, Array<LogMessageConsumer>> loggerConsumers = RefToRefDictionary.of(
        DefaultLoggerConfig.ROOT_INFO_CONSUMERS_KEY,
        Array.of(new ConsoleMessageConsumer(new SimpleLogMessageRender())));
    return Optional.of(new DefaultLoggerConfig(loggerLevels, loggerConsumers));
  }

  @Override
  public int order() {
    return Integer.MAX_VALUE;
  }
}
