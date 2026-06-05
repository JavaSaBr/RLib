package javasabr.rlib.logger.api.impl;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerFactory;
import javasabr.rlib.logger.api.LoggerService;

public class NoOpsLoggerFactory implements LoggerFactory {

  private static final NoOpsLogger NO_OPS_LOGGER = new NoOpsLogger();
  private static final LoggerService NO_OPS_LOGGER_SERVICE = new NoOpsLoggerService();

  @Override
  public Logger getLogger(String name) {
    return NO_OPS_LOGGER;
  }

  @Override
  public Logger getLogger(Class<?> type) {
    return NO_OPS_LOGGER;
  }

  @Override
  public Logger getDefault() {
    return NO_OPS_LOGGER;
  }

  @Override
  public LoggerService getLoggerService() {
    return NO_OPS_LOGGER_SERVICE;
  }
}
