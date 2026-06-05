package javasabr.rlib.logger.impl;

import java.util.Arrays;
import javasabr.rlib.collections.array.UnsafeArray;
import javasabr.rlib.common.util.StringUtils;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerService;
import javasabr.rlib.logger.impl.config.LogMessageConsumer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
public final class DefaultLogger implements Logger {

  final int[] override;
  final String name;
  final String shortName;
  final DefaultLoggerService loggerService;

  @Nullable
  UnsafeArray<LogMessageConsumer> traceConsumers;
  @Nullable
  UnsafeArray<LogMessageConsumer> debugConsumers;
  @Nullable
  UnsafeArray<LogMessageConsumer> infoConsumers;
  @Nullable
  UnsafeArray<LogMessageConsumer> warnConsumers;
  @Nullable
  UnsafeArray<LogMessageConsumer> errorConsumers;
  
  public DefaultLogger(String name, String shortName, DefaultLoggerService loggerService) {
    this.name = name;
    this.shortName = shortName;
    this.loggerService = loggerService;
    this.override = new int[DefaultLoggerService.LOGGER_LEVELS.length];
    Arrays.fill(override, LoggerService.NOT_CONFIGURE);
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public String shortName() {
    return shortName;
  }

  @Override
  public boolean enabled(LoggerLevel level) {
    int value = override[level.ordinal()];
    if (value != LoggerService.NOT_CONFIGURE) {
      return value == LoggerService.ENABLED;
    }
    value = loggerService.enabled(level);
    if (value != LoggerService.NOT_CONFIGURE) {
      return value == LoggerService.ENABLED;
    }
    return level.enabled();
  }

  @Override
  public void overrideEnabled(LoggerLevel level, boolean enabled) {
    override[level.ordinal()] = enabled ? LoggerService.ENABLED : LoggerService.DISABLED;
  }

  @Override
  public void resetToDefault(LoggerLevel level) {
    override[level.ordinal()] = LoggerService.NOT_CONFIGURE;
  }

  @Override
  public void print(LoggerLevel level, String logMessage) {
    if (enabled(level)) {
      loggerService.write(this, level, logMessage);
    }
  }

  @Override
  public void print(LoggerLevel level, Throwable exception) {
    if (enabled(level)) {
      loggerService.write(this, level, StringUtils.toString(exception));
    }
  }

  @Override
  public void print(LoggerLevel level, String message, Throwable exception) {
    if (enabled(level)) {
      String exceptionInfo = StringUtils.toString(exception);
      loggerService.write(this, level, message + ": " + exceptionInfo);
    }
  }

  @Nullable 
  UnsafeArray<LogMessageConsumer> resolvedConsumers(LoggerLevel level) {
    return switch (level) {
      case TRACE -> traceConsumers;
      case DEBUG -> debugConsumers;
      case INFO -> infoConsumers;
      case WARNING -> warnConsumers;
      case ERROR -> errorConsumers;
    };
  }

  void saveResolvedConsumers(LoggerLevel level, UnsafeArray<LogMessageConsumer> consumers) {
    switch (level) {
      case TRACE -> traceConsumers = consumers;
      case DEBUG -> debugConsumers = consumers;
      case INFO -> infoConsumers = consumers;
      case WARNING -> warnConsumers = consumers;
      case ERROR -> errorConsumers = consumers;
    }
  }
}
