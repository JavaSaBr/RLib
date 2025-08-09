package javasabr.rlib.logger.api.impl;

import java.io.Writer;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerFactory;
import javasabr.rlib.logger.api.LoggerListener;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class NullLoggerFactory implements LoggerFactory {

  private static final NullLogger NULL_LOGGER = new NullLogger();

  @Override
  public Logger make(String name) {
    return NULL_LOGGER;
  }

  @Override
  public Logger make(Class<?> type) {
    return NULL_LOGGER;
  }

  @Override
  public Logger getDefault() {
    return NULL_LOGGER;
  }

  @Override
  public void addListener(LoggerListener listener) {}

  @Override
  public void addWriter(Writer writer) {}

  @Override
  public void removeListener(LoggerListener listener) {}

  @Override
  public void removeWriter(Writer writer) {}
}
