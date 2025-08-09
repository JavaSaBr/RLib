package javasabr.rlib.logger.impl;

import java.util.Objects;
import javasabr.rlib.common.util.StringUtils;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;

/**
 * The base implementation of the logger.
 *
 * @author JavaSaBr
 */
public final class DefaultLogger implements Logger {

  private static final LoggerLevel[] VALUES = LoggerLevel.values();

  /**
   * The table of override enabled statuses.
   */
  private final Boolean[] override;

  /**
   * The logger name.
   */
  private final String name;

  /**
   * The default logger factory.
   */
  private final DefaultLoggerFactory loggerFactory;

  public DefaultLogger(String name, DefaultLoggerFactory loggerFactory) {
    this.name = name;
    this.loggerFactory = loggerFactory;
    this.override = new Boolean[VALUES.length];
  }

  @Override
  public boolean isEnabled(LoggerLevel level) {
    var value = override[level.ordinal()];
    return Objects.requireNonNullElse(value, level.isEnabled());
  }

  @Override
  public boolean setEnabled(LoggerLevel level, boolean enabled) {
    override[level.ordinal()] = enabled;
    return true;
  }

  @Override
  public boolean applyDefault(LoggerLevel level) {
    override[level.ordinal()] = null;
    return true;
  }

  @Override
  public void print(LoggerLevel level, String message) {
    if (isEnabled(level)) {
      loggerFactory.write(level, name, message);
    }
  }

  @Override
  public void print(LoggerLevel level, Throwable exception) {
    if (isEnabled(level)) {
      loggerFactory.write(level, name, StringUtils.toString(exception));
    }
  }
}
