package javasabr.rlib.logger.slf4j;

import java.io.Writer;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerFactory;
import javasabr.rlib.logger.api.LoggerListener;

public class Slf4jLoggerFactory implements LoggerFactory {

  private final Logger logger;

  public Slf4jLoggerFactory() {
    this.logger = new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(""));
  }

  @Override
  public Logger make(String name) {
    return new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(name));
  }

  @Override
  public Logger make(Class<?> type) {
    return new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(type));
  }

  @Override
  public Logger getDefault() {
    return logger;
  }

  @Override
  public void addListener(LoggerListener listener) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addWriter(Writer writer) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeListener(LoggerListener listener) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeWriter(Writer writer) {
    throw new UnsupportedOperationException();
  }
}
