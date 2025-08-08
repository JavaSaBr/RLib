package javasabr.rlib.logger.api.impl;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class NullLogger implements Logger {

  @Override
  public void print(LoggerLevel level, String message) {}

  @Override
  public void print(LoggerLevel level, Throwable exception) {}
}
