package javasabr.rlib.logger.slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerFactory;
import javasabr.rlib.logger.api.LoggerService;
import javasabr.rlib.logger.api.impl.NoOpsLoggerService;

public class Slf4jLoggerFactory implements LoggerFactory {

  private static final LoggerService NO_OPS_LOGGER_SERVICE = new NoOpsLoggerService();
  
  private final ConcurrentMap<Object, Logger> loggers;
  private final Logger logger;

  public Slf4jLoggerFactory() {
    this.logger = new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(""));
    this.loggers = new ConcurrentHashMap<>();
  }

  @Override
  public Logger getLogger(String name) {
    return loggers.computeIfAbsent(
        name, 
        key -> new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(key.toString())));
  }

  @Override
  public Logger getLogger(Class<?> type) {
    return loggers.computeIfAbsent(
        type, 
        key -> {
          var clazz = (Class<?>) key;
          return new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(clazz));
        });
  }

  @Override
  public Logger getRootLogger() {
    return logger;
  }

  @Override
  public LoggerService getLoggerService() {
    return NO_OPS_LOGGER_SERVICE;
  }
}
