package javasabr.rlib.logger.slf4j.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javasabr.rlib.logger.api.LoggerManager;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class Slf4jLoggerFactoryImpl implements ILoggerFactory {
  
  private final ConcurrentMap<String, Logger> loggers = new ConcurrentHashMap<>();
 
  @Override
  public Logger getLogger(String name) {
    return loggers
        .computeIfAbsent(name, requested -> new Slf4jLoggerImpl(LoggerManager.getLogger(requested)));
  }
}
