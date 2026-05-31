package javasabr.rlib.logger.slf4j.impl;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
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
    logger.print(LoggerLevel.TRACE, msg);
  }

  @Override
  public void trace(String format, Object arg) {
    if (isTraceEnabled()) {
      traceImpl(MessageFormatter.format(format, arg));
    }
  }

  @Override
  public void trace(String format, Object arg1, Object arg2) {
    if (isTraceEnabled()) {
      traceImpl(MessageFormatter.format(format, arg1, arg2));
    }
  }

  @Override
  public void trace(String format, Object... arguments) {
    if (isTraceEnabled()) {
      traceImpl(MessageFormatter.arrayFormat(format, arguments));
    }
  }

  @Override
  public void trace(String msg, Throwable exception) {
    logger.print(LoggerLevel.TRACE, msg, exception);
  }

  @Override
  public boolean isTraceEnabled(Marker marker) {
    return logger.enabled(LoggerLevel.TRACE);
  }

  @Override
  public void trace(Marker marker, String msg) {
    logger.print(LoggerLevel.TRACE, msg);
  }

  @Override
  public void trace(Marker marker, String format, Object arg) {
    if (isTraceEnabled()) {
      traceImpl(MessageFormatter.format(format, arg));
    }
  }

  @Override
  public void trace(Marker marker, String format, Object arg1, Object arg2) {
    if (isTraceEnabled()) {
      traceImpl(MessageFormatter.format(format, arg1, arg2));
    }
  }

  @Override
  public void trace(Marker marker, String format, Object... arguments) {
    if (isTraceEnabled()) {
      traceImpl(MessageFormatter.arrayFormat(format, arguments));
    }
  }

  @Override
  public void trace(Marker marker, String msg, Throwable exception) {
    logger.trace(msg, exception);
  }
  
  private void traceImpl(FormattingTuple formatted) {
    if (formatted.getThrowable() != null) {
      logger.print(LoggerLevel.TRACE, formatted.getMessage(), formatted.getThrowable());
    } else {
      logger.print(LoggerLevel.TRACE, formatted.getMessage());
    }
  }
  
  @Override
  public boolean isDebugEnabled() {
    return logger.enabled(LoggerLevel.DEBUG);
  }

  @Override
  public void debug(String msg) {
    logger.print(LoggerLevel.DEBUG, msg);
  }

  @Override
  public void debug(String format, Object arg) {
    if (isDebugEnabled()) {
      debugImpl(MessageFormatter.format(format, arg));
    }
  }

  @Override
  public void debug(String format, Object arg1, Object arg2) {
    if (isDebugEnabled()) {
      debugImpl(MessageFormatter.format(format, arg1, arg2));
    }
  }

  @Override
  public void debug(String format, Object... arguments) {
    if (isDebugEnabled()) {
      debugImpl(MessageFormatter.arrayFormat(format, arguments));
    }
  }

  @Override
  public void debug(String msg, Throwable exception) {
    logger.print(LoggerLevel.DEBUG, msg, exception);
  }

  @Override
  public boolean isDebugEnabled(Marker marker) {
    return logger.enabled(LoggerLevel.DEBUG);
  }

  @Override
  public void debug(Marker marker, String msg) {
    logger.print(LoggerLevel.DEBUG, msg);
  }

  @Override
  public void debug(Marker marker, String format, Object arg) {
    if (isDebugEnabled()) {
      debugImpl(MessageFormatter.format(format, arg));
    }
  }

  @Override
  public void debug(Marker marker, String format, Object arg1, Object arg2) {
    if (isDebugEnabled()) {
      debugImpl(MessageFormatter.format(format, arg1, arg2));
    }
  }

  @Override
  public void debug(Marker marker, String format, Object... arguments) {
    if (isDebugEnabled()) {
      debugImpl(MessageFormatter.arrayFormat(format, arguments));
    }
  }

  @Override
  public void debug(Marker marker, String msg, Throwable exception) {
    logger.print(LoggerLevel.DEBUG, msg, exception);
  }
  
  private void debugImpl(FormattingTuple formatted) {
    if (formatted.getThrowable() != null) {
      logger.print(LoggerLevel.DEBUG, formatted.getMessage(), formatted.getThrowable());
    } else {
      logger.print(LoggerLevel.DEBUG, formatted.getMessage());
    }
  }
  
  @Override
  public boolean isInfoEnabled() {
    return logger.enabled(LoggerLevel.INFO);
  }

  @Override
  public void info(String msg) {
    logger.print(LoggerLevel.INFO, msg);
  }

  @Override
  public void info(String format, Object arg) {
    if (isInfoEnabled()) {
      infoImpl(MessageFormatter.format(format, arg));
    }
  }

  @Override
  public void info(String format, Object arg1, Object arg2) {
    if (isInfoEnabled()) {
      infoImpl(MessageFormatter.format(format, arg1, arg2));
    }
  }

  @Override
  public void info(String format, Object... arguments) {
    if (isInfoEnabled()) {
      infoImpl(MessageFormatter.arrayFormat(format, arguments));
    }
  }

  @Override
  public void info(String msg, Throwable exception) {
    logger.print(LoggerLevel.INFO, msg, exception);
  }

  @Override
  public boolean isInfoEnabled(Marker marker) {
    return logger.enabled(LoggerLevel.INFO);
  }

  @Override
  public void info(Marker marker, String msg) {
    logger.print(LoggerLevel.INFO, msg);
  }

  @Override
  public void info(Marker marker, String format, Object arg) {
    if (isInfoEnabled()) {
      infoImpl(MessageFormatter.format(format, arg));
    }
  }

  @Override
  public void info(Marker marker, String format, Object arg1, Object arg2) {
    if (isInfoEnabled()) {
      infoImpl(MessageFormatter.format(format, arg1, arg2));
    }
  }

  @Override
  public void info(Marker marker, String format, Object... arguments) {
    if (isInfoEnabled()) {
      infoImpl(MessageFormatter.arrayFormat(format, arguments));
    }
  }

  @Override
  public void info(Marker marker, String msg, Throwable exception) {
    logger.print(LoggerLevel.INFO, msg, exception);
  }

  private void infoImpl(FormattingTuple formatted) {
    if (formatted.getThrowable() != null) {
      logger.print(LoggerLevel.INFO, formatted.getMessage(), formatted.getThrowable());
    } else {
      logger.print(LoggerLevel.INFO, formatted.getMessage());
    }
  }

  @Override
  public boolean isWarnEnabled() {
    return logger.enabled(LoggerLevel.WARNING);
  }

  @Override
  public void warn(String msg) {
    logger.print(LoggerLevel.WARNING, msg);
  }

  @Override
  public void warn(String format, Object arg) {
    if (isWarnEnabled()) {
      warnImpl(MessageFormatter.format(format, arg));
    }
  }

  @Override
  public void warn(String format, Object arg1, Object arg2) {
    if (isWarnEnabled()) {
      warnImpl(MessageFormatter.format(format, arg1, arg2));
    }
  }

  @Override
  public void warn(String format, Object... arguments) {
    if (isWarnEnabled()) {
      warnImpl(MessageFormatter.arrayFormat(format, arguments));
    }
  }

  @Override
  public void warn(String msg, Throwable exception) {
    logger.print(LoggerLevel.WARNING, msg, exception);
  }

  @Override
  public boolean isWarnEnabled(Marker marker) {
    return logger.enabled(LoggerLevel.WARNING);
  }

  @Override
  public void warn(Marker marker, String msg) {
    logger.print(LoggerLevel.WARNING, msg);
  }

  @Override
  public void warn(Marker marker, String format, Object arg) {
    if (isWarnEnabled()) {
      warnImpl(MessageFormatter.format(format, arg));
    }
  }

  @Override
  public void warn(Marker marker, String format, Object arg1, Object arg2) {
    if (isWarnEnabled()) {
      warnImpl(MessageFormatter.format(format, arg1, arg2));
    }
  }

  @Override
  public void warn(Marker marker, String format, Object... arguments) {
    if (isWarnEnabled()) {
      warnImpl(MessageFormatter.arrayFormat(format, arguments));
    }
  }
  
  @Override
  public void warn(Marker marker, String msg, Throwable exception) {
    logger.print(LoggerLevel.WARNING, msg, exception);
  }

  private void warnImpl(FormattingTuple formatted) {
    if (formatted.getThrowable() != null) {
      logger.print(LoggerLevel.WARNING, formatted.getMessage(), formatted.getThrowable());
    } else {
      logger.print(LoggerLevel.WARNING, formatted.getMessage());
    }
  }
  
  @Override
  public boolean isErrorEnabled() {
    return logger.enabled(LoggerLevel.ERROR);
  }

  @Override
  public void error(String msg) {
    logger.print(LoggerLevel.ERROR, msg);
  }

  @Override
  public void error(String format, Object arg) {
    if (isErrorEnabled()) {
      errorImpl(MessageFormatter.format(format, arg));
    }
  }

  @Override
  public void error(String format, Object arg1, Object arg2) {
    if (isErrorEnabled()) {
      errorImpl(MessageFormatter.format(format, arg1, arg2));
    }
  }

  @Override
  public void error(String format, Object... arguments) {
    if (isErrorEnabled()) {
      errorImpl(MessageFormatter.arrayFormat(format, arguments));
    }
  }

  @Override
  public void error(String msg, Throwable exception) {
    logger.print(LoggerLevel.ERROR, msg, exception);
  }

  @Override
  public boolean isErrorEnabled(Marker marker) {
    return logger.enabled(LoggerLevel.ERROR);
  }

  @Override
  public void error(Marker marker, String msg) {
    logger.print(LoggerLevel.ERROR, msg);
  }

  @Override
  public void error(Marker marker, String format, Object arg) {
    if (isErrorEnabled()) {
      errorImpl(MessageFormatter.format(format, arg));
    }
  }

  @Override
  public void error(Marker marker, String format, Object arg1, Object arg2) {
    if (isErrorEnabled()) {
      errorImpl(MessageFormatter.format(format, arg1, arg2));
    }
  }

  @Override
  public void error(Marker marker, String format, Object... arguments) {
    if (isErrorEnabled()) {
      errorImpl(MessageFormatter.arrayFormat(format, arguments));
    }
  }

  @Override
  public void error(Marker marker, String msg, Throwable exception) {
    logger.print(LoggerLevel.ERROR, msg, exception);
  }
  
  private void errorImpl(FormattingTuple formatted) {
    if (formatted.getThrowable() != null) {
      logger.print(LoggerLevel.ERROR, formatted.getMessage(), formatted.getThrowable());
    } else {
      logger.print(LoggerLevel.ERROR, formatted.getMessage());
    }
  }
}
