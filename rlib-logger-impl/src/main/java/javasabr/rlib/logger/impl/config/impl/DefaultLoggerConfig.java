package javasabr.rlib.logger.impl.config.impl;

import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.UnsafeArray;
import javasabr.rlib.collections.dictionary.DictionaryFactory;
import javasabr.rlib.collections.dictionary.RefToRefDictionary;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.DefaultLoggerService;
import javasabr.rlib.logger.impl.config.LogMessageConsumer;
import javasabr.rlib.logger.impl.config.LoggerConfig;

public class DefaultLoggerConfig implements LoggerConfig {

  static final UnsafeArray<LogMessageConsumer> EMPTY_CONSUMERS = Array
      .empty(LogMessageConsumer.class)
      .asUnsafe();

  static final UnsafeArray<LoggerLevel> TRACE_AND_LOWER_LEVELS = Array
      .of(LoggerLevel.TRACE)
      .asUnsafe();
  static final UnsafeArray<LoggerLevel> DEBUG_AND_LOWER_LEVELS = Array
      .of(LoggerLevel.DEBUG, LoggerLevel.TRACE)
      .asUnsafe();
  static final UnsafeArray<LoggerLevel> INFO_AND_LOWER_LEVELS = Array
      .of(LoggerLevel.INFO, LoggerLevel.DEBUG, LoggerLevel.TRACE)
      .asUnsafe();
  static final UnsafeArray<LoggerLevel> WARN_AND_LOWER_LEVELS = Array
      .of(LoggerLevel.WARNING, LoggerLevel.INFO, LoggerLevel.DEBUG, LoggerLevel.TRACE)
      .asUnsafe();
  static final UnsafeArray<LoggerLevel> ERROR_AND_LOWER_LEVELS = Array
      .of(LoggerLevel.ERROR, LoggerLevel.WARNING, LoggerLevel.INFO, LoggerLevel.DEBUG, LoggerLevel.TRACE)
      .asUnsafe();

  static final UnsafeArray<LoggerLevel> TRACE_AND_HIGHER_LEVELS = Array
      .of(LoggerLevel.TRACE, LoggerLevel.DEBUG, LoggerLevel.INFO, LoggerLevel.WARNING, LoggerLevel.ERROR)
      .asUnsafe();
  static final UnsafeArray<LoggerLevel> DEBUG_AND_HIGHER_LEVELS = Array
      .of(LoggerLevel.DEBUG, LoggerLevel.INFO, LoggerLevel.WARNING, LoggerLevel.ERROR)
      .asUnsafe();
  static final UnsafeArray<LoggerLevel> INFO_AND_HIGHER_LEVELS = Array
      .of(LoggerLevel.INFO, LoggerLevel.WARNING, LoggerLevel.ERROR)
      .asUnsafe();
  static final UnsafeArray<LoggerLevel> WARN_AND_HIGHER_LEVELS = Array
      .of(LoggerLevel.WARNING, LoggerLevel.ERROR)
      .asUnsafe();
  static final UnsafeArray<LoggerLevel> ERROR_AND_HIGHER_LEVELS = Array
      .of(LoggerLevel.ERROR)
      .asUnsafe();
  
  public static final LoggerConsumersKey ROOT_TRACE_CONSUMERS_KEY = 
      new LoggerConsumersKey(DefaultLoggerService.ROOT_LOGGER_NAME, LoggerLevel.TRACE);
  public static final LoggerConsumersKey ROOT_DEBUG_CONSUMERS_KEY =
      new LoggerConsumersKey(DefaultLoggerService.ROOT_LOGGER_NAME, LoggerLevel.DEBUG);
  public static final LoggerConsumersKey ROOT_INFO_CONSUMERS_KEY =
      new LoggerConsumersKey(DefaultLoggerService.ROOT_LOGGER_NAME, LoggerLevel.INFO);
  public static final LoggerConsumersKey ROOT_WARN_CONSUMERS_KEY =
      new LoggerConsumersKey(DefaultLoggerService.ROOT_LOGGER_NAME, LoggerLevel.WARNING);
  public static final LoggerConsumersKey ROOT_ERROR_CONSUMERS_KEY =
      new LoggerConsumersKey(DefaultLoggerService.ROOT_LOGGER_NAME, LoggerLevel.ERROR);
  
  public static final RefToRefDictionary<String, LoggerLevel> ENABLE_ALL_LEVELS = RefToRefDictionary.of(
      DefaultLoggerService.ROOT_LOGGER_NAME,
      LoggerLevel.TRACE);
  
  final RefToRefDictionary<String, LoggerLevel> loggerLevels;
  final RefToRefDictionary<LoggerConsumersKey, UnsafeArray<LogMessageConsumer>> loggerConsumers;

  public DefaultLoggerConfig(
      RefToRefDictionary<String, LoggerLevel> loggerLevels,
      RefToRefDictionary<LoggerConsumersKey, Array<LogMessageConsumer>> loggerConsumers) {
    this.loggerLevels = loggerLevels;
    this.loggerConsumers = prepareLoggersConsumers(loggerConsumers);
  }

  @Override
  public void configureLevels(Logger logger) {
    LoggerLevel targetLevel = loggerLevels.get(logger.name());
    if (targetLevel == null) {
      targetLevel = loggerLevels.get(DefaultLoggerService.ROOT_LOGGER_NAME);
    }
    if (targetLevel == null) {
      return;
    }
    for (LoggerLevel level : DefaultLoggerService.LOGGER_LEVELS) {
      logger.overrideEnabled(level, false);
    }
    for (LoggerLevel level : resolveConfiguringLevels(targetLevel)) {
      logger.overrideEnabled(level, true);
    }
  }

  @Override
  public UnsafeArray<LogMessageConsumer> resolveConsumers(Logger logger, LoggerLevel level) {
    String loggerName = logger.name();
    UnsafeArray<LoggerLevel> applicableLevels = resolveConsumerLevels(level);
    for (LoggerLevel lookupLevel : applicableLevels.wrapped()) {
      @SuppressWarnings("DataFlowIssue")
      UnsafeArray<LogMessageConsumer> consumers = loggerConsumers
          .get(new LoggerConsumersKey(loggerName, lookupLevel));
      if (consumers != null) {
        return consumers;
      }
    }
    for (LoggerLevel lookupLevel : applicableLevels.wrapped()) {
      @SuppressWarnings("DataFlowIssue") 
      UnsafeArray<LogMessageConsumer> consumers = loggerConsumers
          .get(resolveRootConsumersKey(lookupLevel));
      if (consumers != null) {
        return consumers;
      }
    }
    return EMPTY_CONSUMERS;
  }

  public record LoggerConsumersKey(String loggerName, LoggerLevel level) {}

  private static UnsafeArray<LoggerLevel> resolveConsumerLevels(LoggerLevel level) {
    return switch (level) {
      case TRACE -> TRACE_AND_LOWER_LEVELS;
      case DEBUG -> DEBUG_AND_LOWER_LEVELS;
      case INFO -> INFO_AND_LOWER_LEVELS;
      case WARNING -> WARN_AND_LOWER_LEVELS;
      case ERROR -> ERROR_AND_LOWER_LEVELS;
    };
  }

  private static UnsafeArray<LoggerLevel> resolveConfiguringLevels(LoggerLevel level) {
    return switch (level) {
      case TRACE -> TRACE_AND_HIGHER_LEVELS;
      case DEBUG -> DEBUG_AND_HIGHER_LEVELS;
      case INFO -> INFO_AND_HIGHER_LEVELS;
      case WARNING -> WARN_AND_HIGHER_LEVELS;
      case ERROR -> ERROR_AND_HIGHER_LEVELS;
    };
  }
  
  private static LoggerConsumersKey resolveRootConsumersKey(LoggerLevel level) {
    return switch (level) {
      case TRACE -> ROOT_TRACE_CONSUMERS_KEY;
      case DEBUG -> ROOT_DEBUG_CONSUMERS_KEY;
      case INFO -> ROOT_INFO_CONSUMERS_KEY;
      case WARNING -> ROOT_WARN_CONSUMERS_KEY;
      case ERROR -> ROOT_ERROR_CONSUMERS_KEY;
    };
  }

  private static RefToRefDictionary<LoggerConsumersKey, UnsafeArray<LogMessageConsumer>> prepareLoggersConsumers(
      RefToRefDictionary<LoggerConsumersKey, Array<LogMessageConsumer>> loggerConsumers) {
    var tempDictionary = DictionaryFactory
        .<LoggerConsumersKey, UnsafeArray<LogMessageConsumer>>mutableRefToRefDictionary();
    loggerConsumers.forEach((key, consumers) -> {
      UnsafeArray<LogMessageConsumer> trimmedCopy = Array
          .copyOf(consumers)
          .asUnsafe();
      tempDictionary.put(key, trimmedCopy);
    });
    return tempDictionary.toReadOnly();
  }
}
