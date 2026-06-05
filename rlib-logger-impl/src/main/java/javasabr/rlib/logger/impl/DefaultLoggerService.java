package javasabr.rlib.logger.impl;

import java.util.Arrays;
import java.util.function.Function;
import javasabr.rlib.collections.array.UnsafeArray;
import javasabr.rlib.collections.dictionary.DictionaryFactory;
import javasabr.rlib.collections.dictionary.LockableRefToRefDictionary;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerService;
import javasabr.rlib.logger.impl.config.LogMessageConsumer;
import javasabr.rlib.logger.impl.config.LoggerConfig;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * The class for managing loggers.
 *
 * @author JavaSaBr
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
public class DefaultLoggerService implements LoggerService {

  public static final LoggerLevel[] LOGGER_LEVELS = LoggerLevel.values();
  public static final String ROOT_LOGGER_NAME = "ROOT";

  final LockableRefToRefDictionary<String, DefaultLogger> loggers;
  final Function<String, DefaultLogger> loggerFactory = this::createNew;

  final Logger logger;
  final int[] override;
  
  volatile LoggerConfig config;

  public DefaultLoggerService(LoggerConfig config) {
    this.config = config;
    this.loggers = DictionaryFactory.stampedLockBasedRefToRefDictionary();
    this.logger = getLogger(ROOT_LOGGER_NAME);
    this.override = new int[LOGGER_LEVELS.length];
    Arrays.fill(override, NOT_CONFIGURE);
  }

  public Logger getDefault() {
    return logger;
  }

  public DefaultLogger getLogger(Class<?> type) {
    long lock = loggers.writeLock();
    try {
      return loggers.getOrCompute(type.getName(), loggerFactory);
    } finally {
      loggers.writeUnlock(lock);
    }
  }

  public DefaultLogger getLogger(String name) {
    long lock = loggers.writeLock();
    try {
      return loggers.getOrCompute(name, loggerFactory);
    } finally {
      loggers.writeUnlock(lock);
    }
  }

  private DefaultLogger createNew(String name) {
    String shortName = calculateShortName(name);
    var created = new DefaultLogger(name, shortName, this);
    config.configureLevels(created);
    return created;
  }
  
  @Override
  public void enable(Class<?> cs, LoggerLevel level) {
    getLogger(cs).overrideEnabled(level, true);
  }

  @Override
  public void disable(Class<?> cs, LoggerLevel level) {
    getLogger(cs).overrideEnabled(level, false);
  }

  @Override
  public void configureDefault(LoggerLevel level, boolean def) {
    override[level.ordinal()] = def ? ENABLED : DISABLED;
  }

  @Override
  public void removeDefault(LoggerLevel level) {
    override[level.ordinal()] = NOT_CONFIGURE;
  }

  @Override
  public int enabled(LoggerLevel level) {
    return override[level.ordinal()];
  }

  @Override
  public void write(Logger logger, LoggerLevel level, String message) {
    if (logger instanceof DefaultLogger defaultLogger) {
      write(defaultLogger, level, message);
    } else {
      UnsafeArray<LogMessageConsumer> consumers = config
          .resolveConsumers(logger, level)
          .asUnsafe();
      for (LogMessageConsumer consumer : consumers.wrapped()) {
        //noinspection DataFlowIssue it's safe
        consumer.consume(level, logger, message);
      }
    }
  }

  void write(DefaultLogger logger, LoggerLevel level, String message) {
    UnsafeArray<LogMessageConsumer> consumers = resolveConsumers(logger, level);
    for (LogMessageConsumer consumer : consumers.wrapped()) {
      //noinspection DataFlowIssue it's safe
      consumer.consume(level, logger, message);
    }
  }

  private UnsafeArray<LogMessageConsumer> resolveConsumers(DefaultLogger logger, LoggerLevel level) {
    UnsafeArray<LogMessageConsumer> consumers = logger.resolvedConsumers(level);
    if (consumers == null) {
      consumers = config
          .resolveConsumers(logger, level)
          .asUnsafe();
      logger.saveResolvedConsumers(level, consumers);
    }
    return consumers;
  }
  
  private static String calculateShortName(String name) {
    String shortName = name;
    int cutUntil = name.lastIndexOf('.');
    boolean isDotLastChar = cutUntil != -1 && cutUntil == shortName.length() - 1;
    if (isDotLastChar && shortName.length() > 1) {
      shortName = shortName.substring(0, shortName.length() - 1);
      cutUntil = shortName.lastIndexOf('.');
    }
    if (cutUntil != -1) {
      shortName = shortName.substring(cutUntil + 1);
    }
    if (shortName.isEmpty()) {
      shortName = name;
    }
    return shortName;
  }
}
