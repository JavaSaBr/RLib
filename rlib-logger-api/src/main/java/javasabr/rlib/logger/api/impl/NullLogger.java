package javasabr.rlib.logger.api.impl;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import org.jspecify.annotations.NonNull;

public final class NullLogger implements Logger {
  
  @Override
  public String name() {
    return "null";
  }

  @Override
  public void print(LoggerLevel level, String message) {}

  @Override
  public void print(LoggerLevel level, Throwable exception) {}

  @Override
  public void print(LoggerLevel level, String message, Throwable exception) {}
}
