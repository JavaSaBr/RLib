package javasabr.rlib.logger.impl;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerFactory;
import javasabr.rlib.logger.api.LoggerService;
import javasabr.rlib.logger.impl.config.LoggerConfigResolver;

public class DefaultLoggerFactory implements LoggerFactory {
  
  DefaultLoggerService defaultLoggerService;
  
  public DefaultLoggerFactory() {
    this.defaultLoggerService = new DefaultLoggerService(LoggerConfigResolver.load());
  }
  
  @Override
  public Logger getLogger(String name) {
    return defaultLoggerService.getLogger(name);
  }

  @Override
  public Logger getLogger(Class<?> type) {
    return defaultLoggerService.getLogger(type);
  }

  @Override
  public Logger getDefault() {
    return defaultLoggerService.getDefault();
  }

  @Override
  public LoggerService getLoggerService() {
    return defaultLoggerService;
  }
}
