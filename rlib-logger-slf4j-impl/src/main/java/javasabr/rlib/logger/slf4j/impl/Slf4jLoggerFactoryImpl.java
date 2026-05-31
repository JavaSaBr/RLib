package javasabr.rlib.logger.slf4j.impl;

import javasabr.rlib.logger.api.LoggerManager;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class Slf4jLoggerFactoryImpl implements ILoggerFactory {
  
  @Override
  public Logger getLogger(String name) {
    return new Slf4jLoggerImpl(LoggerManager.getLogger(name));
  }
}
