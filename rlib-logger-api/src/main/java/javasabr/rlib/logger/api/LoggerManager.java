package javasabr.rlib.logger.api;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.ServiceLoader;
import javasabr.rlib.logger.api.impl.NoOpsLoggerFactory;

/**
 * Central manager for obtaining and configuring loggers.
 *
 * @since 10.0.0
 */
public class LoggerManager {

  private static final LoggerFactory LOGGER_FACTORY;

  static {

    String className = System.getProperty("com.ss.rlib.logger.factory", "");
    Class<? extends LoggerFactory> implementation = null;

    if (!className.isEmpty()) {
      try {
        implementation = (Class<? extends LoggerFactory>) Class.forName(className);
      } catch (ClassNotFoundException exception) {
        exception.printStackTrace();
      }
    }

    if (implementation == null) {
      Iterator<LoggerFactory> impls = ServiceLoader
          .load(LoggerFactory.class)
          .iterator();
      if (impls.hasNext()) {
        implementation = impls.next().getClass();
      }
    }

    if (implementation == null) {
      System.err.printf(
          "ERROR: No any exist implementation of [%s], will be used null logger%n",
          LoggerFactory.class);
      LOGGER_FACTORY = new NoOpsLoggerFactory();
    } else {
      try {
        LOGGER_FACTORY = implementation
            .getDeclaredConstructor()
            .newInstance();
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * Returns the default logger.
   *
   * @return the default logger
   * @since 10.0.0
   */
  public static Logger getDefaultLogger() {
    return LOGGER_FACTORY.getDefault();
  }

  /**
   * Returns a logger for the specified class.
   *
   * @param cs the class to create a logger for
   * @return the logger instance
   * @since 10.0.0
   */
  public static Logger getLogger(Class<?> cs) {
    return LOGGER_FACTORY.getLogger(cs);
  }

  /**
   * Returns a logger with the specified id.
   *
   * @param id the logger id
   * @return the logger instance
   * @since 10.0.0
   */
  public static Logger getLogger(String id) {
    return LOGGER_FACTORY.getLogger(id);
  }

  public static LoggerService getLoggerService() {
    return LOGGER_FACTORY.getLoggerService();
  }
  
  /**
   * Configures the default setting for a log level.
   *
   * @param level the log level
   * @param def true to enable by default, false to disable
   * @since 10.0.0
   */
  public static void configureDefault(LoggerLevel level, boolean def) {
    LOGGER_FACTORY
        .getLoggerService()
        .configureDefault(level, def);
  }

  /**
   * Removes the default configuration for a log level.
   *
   * @param level the log level
   * @since 10.0.0
   */
  public static void removeDefault(LoggerLevel level) {
    LOGGER_FACTORY
        .getLoggerService()
        .removeDefault(level);
  }

  /**
   * Enables logging at the specified level for the class.
   *
   * @param cs the class to configure
   * @param level the log level
   * @since 10.0.0
   */
  public static void enable(Class<?> cs, LoggerLevel level) {
    LOGGER_FACTORY
        .getLoggerService()
        .enable(cs, level);
  }

  /**
   * Disables logging at the specified level for the class.
   *
   * @param cs the class to configure
   * @param level the log level
   * @since 10.0.0
   */
  public static void disable(Class<?> cs, LoggerLevel level) {
    LOGGER_FACTORY
        .getLoggerService()
        .disable(cs, level);
  }
}
