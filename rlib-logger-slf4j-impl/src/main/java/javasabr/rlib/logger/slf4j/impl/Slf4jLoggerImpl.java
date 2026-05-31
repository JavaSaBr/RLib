package javasabr.rlib.logger.slf4j.impl;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;

@RequiredArgsConstructor
public class Slf4jLoggerImpl implements org.slf4j.Logger {
  
  private final Logger logger;
  
  @Override
  public String getName() {
    return logger.name();
  }

  @Override
  public boolean isTraceEnabled() {
    return logger.enabled(LoggerLevel.TRACE);
  }

  @Override
  public void trace(String msg) {
    logger.trace(msg);
  }

  @Override
  public void trace(String format, Object arg) {
    if (isTraceEnabled()) {
      logger.trace(MessageFormatter.format(format, arg).getMessage());
    }
  }

  @Override
  public void trace(String format, Object arg1, Object arg2) {
    if (isTraceEnabled()) {
      logger.trace(MessageFormatter.format(format, arg1, arg2).getMessage());
    }
  }

  @Override
  public void trace(String format, Object... arguments) {
    if (isTraceEnabled()) {
      logger.trace(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }
  }

  @Override
  public void trace(String msg, Throwable exception) {
    logger.trace(msg, exception);
  }

  @Override
  public boolean isTraceEnabled(Marker marker) {
    return logger.enabled(LoggerLevel.TRACE);
  }

  @Override
  public void trace(Marker marker, String msg) {
    logger.trace(msg);
  }

  @Override
  public void trace(Marker marker, String format, Object arg) {
    if (isTraceEnabled()) {
      logger.trace(MessageFormatter.format(format, arg).getMessage());
    }
  }

  @Override
  public void trace(Marker marker, String format, Object arg1, Object arg2) {
    if (isTraceEnabled()) {
      logger.trace(MessageFormatter.format(format, arg1, arg2).getMessage());
    }
  }

  @Override
  public void trace(Marker marker, String format, Object... arguments) {
    if (isTraceEnabled()) {
      logger.trace(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }
  }

  @Override
  public void trace(Marker marker, String msg, Throwable exception) {
    logger.trace(msg, exception);
  }

  @Override
  public boolean isDebugEnabled() {
    return logger.enabled(LoggerLevel.DEBUG);
  }

  @Override
  public void debug(String msg) {
    logger.debug(msg);
  }

  @Override
  public void debug(String format, Object arg) {
    if (isDebugEnabled()) {
      logger.debug(MessageFormatter.format(format, arg).getMessage());
    }
  }

  @Override
  public void debug(String format, Object arg1, Object arg2) {
    if (isDebugEnabled()) {
      logger.debug(MessageFormatter.format(format, arg1, arg2).getMessage());
    }
  }

  @Override
  public void debug(String format, Object... arguments) {
    if (isDebugEnabled()) {
      logger.debug(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }
  }

  @Override
  public void debug(String msg, Throwable exception) {
    logger.debug(msg, exception);
  }

  @Override
  public boolean isDebugEnabled(Marker marker) {
    return logger.enabled(LoggerLevel.DEBUG);
  }

  @Override
  public void debug(Marker marker, String msg) {
    logger.debug(msg);
  }

  @Override
  public void debug(Marker marker, String format, Object arg) {
    if (isDebugEnabled()) {
      logger.debug(MessageFormatter.format(format, arg).getMessage());
    }
  }

  @Override
  public void debug(Marker marker, String format, Object arg1, Object arg2) {
    if (isDebugEnabled()) {
      logger.debug(MessageFormatter.format(format, arg1, arg2).getMessage());
    }
  }

  @Override
  public void debug(Marker marker, String format, Object... arguments) {
    if (isDebugEnabled()) {
      logger.debug(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }
  }

  @Override
  public void debug(Marker marker, String msg, Throwable exception) {
    logger.debug(msg, exception);
  }

  @Override
  public boolean isInfoEnabled() {
    return logger.enabled(LoggerLevel.INFO);
  }

  @Override
  public void info(String msg) {
    logger.info(msg);
  }

  @Override
  public void info(String format, Object arg) {
    if (isInfoEnabled()) {
      logger.info(MessageFormatter.format(format, arg).getMessage());
    }
  }

  @Override
  public void info(String format, Object arg1, Object arg2) {
    if (isInfoEnabled()) {
      logger.info(MessageFormatter.format(format, arg1, arg2).getMessage());
    }
  }

  @Override
  public void info(String format, Object... arguments) {
    if (isInfoEnabled()) {
      logger.info(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }
  }

  @Override
  public void info(String msg, Throwable exception) {
    logger.info(msg, exception);
  }

  @Override
  public boolean isInfoEnabled(Marker marker) {
    return logger.enabled(LoggerLevel.INFO);
  }

  @Override
  public void info(Marker marker, String msg) {
    logger.info(msg);
  }

  @Override
  public void info(Marker marker, String format, Object arg) {
    if (isInfoEnabled()) {
      logger.info(MessageFormatter.format(format, arg).getMessage());
    }
  }

  @Override
  public void info(Marker marker, String format, Object arg1, Object arg2) {
    if (isInfoEnabled()) {
      logger.info(MessageFormatter.format(format, arg1, arg2).getMessage());
    }
  }

  @Override
  public void info(Marker marker, String format, Object... arguments) {
    if (isInfoEnabled()) {
      logger.info(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }
  }

  @Override
  public void info(Marker marker, String msg, Throwable exception) {
    logger.info(msg, exception);
  }

  @Override
  public boolean isWarnEnabled() {
    return logger.enabled(LoggerLevel.WARNING);
  }

  @Override
  public void warn(String msg) {
    logger.warn(msg);
  }

  @Override
  public void warn(String format, Object arg) {
    if (isWarnEnabled()) {
      logger.warn(MessageFormatter.format(format, arg).getMessage());
    }
  }

  @Override
  public void warn(String format, Object arg1, Object arg2) {
    if (isWarnEnabled()) {
      logger.warn(MessageFormatter.format(format, arg1, arg2).getMessage());
    }
  }

  @Override
  public void warn(String format, Object... arguments) {
    if (isWarnEnabled()) {
      logger.warn(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }
  }

  @Override
  public void warn(String msg, Throwable exception) {
    logger.warn(msg, exception);
  }

  @Override
  public boolean isWarnEnabled(Marker marker) {
    return logger.enabled(LoggerLevel.WARNING);
  }

  @Override
  public void warn(Marker marker, String msg) {
    logger.warn(msg);
  }

  @Override
  public void warn(Marker marker, String format, Object arg) {
    if (isWarnEnabled()) {
      logger.warn(MessageFormatter.format(format, arg).getMessage());
    }
  }

  @Override
  public void warn(Marker marker, String format, Object arg1, Object arg2) {
    if (isWarnEnabled()) {
      logger.warn(MessageFormatter.format(format, arg1, arg2).getMessage());
    }
  }

  @Override
  public void warn(Marker marker, String format, Object... arguments) {
    if (isWarnEnabled()) {
      logger.warn(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }
  }

  @Override
  public void warn(Marker marker, String msg, Throwable exception) {
    logger.warn(msg, exception);
  }

  @Override
  public boolean isErrorEnabled() {
    return logger.enabled(LoggerLevel.ERROR);
  }

  @Override
  public void error(String msg) {
    logger.error(msg);
  }

  @Override
  public void error(String format, Object arg) {
    if (isErrorEnabled()) {
      logger.error(MessageFormatter.format(format, arg).getMessage());
    }
  }

  @Override
  public void error(String format, Object arg1, Object arg2) {
    if (isErrorEnabled()) {
      logger.error(MessageFormatter.format(format, arg1, arg2).getMessage());
    }
  }

  @Override
  public void error(String format, Object... arguments) {
    if (isErrorEnabled()) {
      logger.error(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }
  }

  @Override
  public void error(String msg, Throwable exception) {
    logger.error(msg, exception);
  }

  @Override
  public boolean isErrorEnabled(Marker marker) {
    return logger.enabled(LoggerLevel.ERROR);
  }

  @Override
  public void error(Marker marker, String msg) {
    logger.error(msg);
  }

  @Override
  public void error(Marker marker, String format, Object arg) {
    if (isErrorEnabled()) {
      logger.error(MessageFormatter.format(format, arg).getMessage());
    }
  }

  @Override
  public void error(Marker marker, String format, Object arg1, Object arg2) {
    if (isErrorEnabled()) {
      logger.error(MessageFormatter.format(format, arg1, arg2).getMessage());
    }
  }

  @Override
  public void error(Marker marker, String format, Object... arguments) {
    if (isErrorEnabled()) {
      logger.error(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }
  }

  @Override
  public void error(Marker marker, String msg, Throwable exception) {
    logger.error(msg, exception);
  }
}
