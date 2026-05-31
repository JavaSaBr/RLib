package javasabr.rlib.logger.impl;

import java.util.Arrays;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.common.util.StringUtils;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerService;
import javasabr.rlib.logger.impl.config.LogConsumer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * @author JavaSaBr
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
public final class DefaultLogger implements Logger {

  final int[] override;
  final String name;
  final DefaultLoggerService loggerService;

  Array<LogConsumer> resolvedConsumers;
  
  public DefaultLogger(String name, DefaultLoggerService loggerService) {
    this.name = name;
    this.loggerService = loggerService;
    this.override = new int[DefaultLoggerService.LOGGER_LEVELS.length];
    Arrays.fill(override, LoggerService.NOT_CONFIGURE);
  }

  @Override
  public String name() {
    return name;
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
}
