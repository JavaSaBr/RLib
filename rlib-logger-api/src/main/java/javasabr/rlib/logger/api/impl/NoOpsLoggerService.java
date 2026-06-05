package javasabr.rlib.logger.api.impl;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerService;

public class NoOpsLoggerService implements LoggerService {

  @Override
  public void enable(Class<?> cs, LoggerLevel level) {}

  @Override
  public void disable(Class<?> cs, LoggerLevel level) {}

  @Override
  public void configureDefault(LoggerLevel level, boolean def) {}

  @Override
  public void removeDefault(LoggerLevel level) {}

  @Override
  public int enabled(LoggerLevel level) {
    return 0;
  }

  @Override
  public void write(Logger logger, LoggerLevel level, String message) {}
}
