package javasabr.rlib.logger.slf4j;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Slf4jLogger implements Logger {

  private final org.slf4j.Logger logger;

  @Override
  public String name() {
    return logger.getName();
  }

  @Override
  public boolean enabled(LoggerLevel level) {
    return switch (level) {
      case TRACE -> logger.isTraceEnabled();
      case DEBUG -> logger.isDebugEnabled();
      case INFO -> logger.isInfoEnabled();
      case WARNING -> logger.isWarnEnabled();
      case ERROR -> logger.isErrorEnabled();
    };
  }

  @Override
  public void print(LoggerLevel level, String message) {
    switch (level) {
      case TRACE -> logger.trace(message);
      case DEBUG -> logger.debug(message);
      case INFO -> logger.info(message);
      case WARNING -> logger.warn(message);
      case ERROR -> logger.error(message);
    }
  }

  @Override
  public void print(LoggerLevel level, Throwable exception) {
    switch (level) {
      case TRACE -> logger.trace(exception.getMessage(), exception);
      case DEBUG -> logger.debug(exception.getMessage(), exception);
      case INFO -> logger.info(exception.getMessage(), exception);
      case WARNING -> logger.warn(exception.getMessage(), exception);
      case ERROR -> logger.error(exception.getMessage(), exception);
    }
  }

  @Override
  public void print(LoggerLevel level, String message, Throwable exception) {
    switch (level) {
      case TRACE -> logger.trace(message, exception);
      case DEBUG -> logger.debug(message, exception);
      case INFO -> logger.info(message, exception);
      case WARNING -> logger.warn(message, exception);
      case ERROR -> logger.error(message, exception);
    }
  }
}
