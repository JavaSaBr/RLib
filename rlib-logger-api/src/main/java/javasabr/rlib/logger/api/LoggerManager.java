package javasabr.rlib.logger.api;

import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.ServiceLoader;
import javasabr.rlib.logger.api.impl.NullLoggerFactory;

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
      System.err.printf("ERROR: No any exist implementation of [%s], will be used null logger%n", LoggerFactory.class);
      LOGGER_FACTORY = new NullLoggerFactory();
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
    return LOGGER_FACTORY.make(cs);
  }

  /**
   * Returns a logger with the specified id.
   *
   * @param id the logger id
   * @return the logger instance
   * @since 10.0.0
   */
  public static Logger getLogger(String id) {
    return LOGGER_FACTORY.make(id);
  }

  /**
   * Adds a listener to receive log output.
   *
   * @param listener the listener to add
   * @since 10.0.0
   */
  public static void addListener(LoggerListener listener) {
    if (LOGGER_FACTORY instanceof LoggerService ls) {
      ls.addListener(listener);
    }
  }

  /**
   * Removes a previously added listener.
   *
   * @param listener the listener to remove
   * @since 10.0.0
   */
  public static void removeListener(LoggerListener listener) {
    if (LOGGER_FACTORY instanceof LoggerService ls) {
      ls.removeListener(listener);
    }
  }

  /**
   * Adds a writer to receive log output.
   *
   * @param writer the writer to add
   * @since 10.0.0
   */
  public static void addWriter(Writer writer) {
    if (LOGGER_FACTORY instanceof LoggerService ls) {
      ls.addWriter(writer);
    }
  }

  /**
   * Removes a previously added writer.
   *
   * @param writer the writer to remove
   * @since 10.0.0
   */
  public static void removeWriter(Writer writer) {
    if (LOGGER_FACTORY instanceof LoggerService ls) {
      ls.removeWriter(writer);
    }
  }

  /**
   * Configures the default setting for a log level.
   *
   * @param level the log level
   * @param def true to enable by default, false to disable
   * @since 10.0.0
   */
  public static void configureDefault(LoggerLevel level, boolean def) {
    if (LOGGER_FACTORY instanceof LoggerService ls) {
      ls.configureDefault(level, def);
    }
  }

  /**
   * Removes the default configuration for a log level.
   *
   * @param level the log level
   * @since 10.0.0
   */
  public static void removeDefault(LoggerLevel level) {
    if (LOGGER_FACTORY instanceof LoggerService ls) {
      ls.removeDefault(level);
    }
  }

  /**
   * Enables logging at the specified level for the class.
   *
   * @param cs the class to configure
   * @param level the log level
   * @since 10.0.0
   */
  public static void enable(Class<?> cs, LoggerLevel level) {
    if (LOGGER_FACTORY instanceof LoggerService ls) {
      ls.enable(cs, level);
    }
  }

  /**
   * Disables logging at the specified level for the class.
   *
   * @param cs the class to configure
   * @param level the log level
   * @since 10.0.0
   */
  public static void disable(Class<?> cs, LoggerLevel level) {
    if (LOGGER_FACTORY instanceof LoggerService ls) {
      ls.disable(cs, level);
    }
  }
}
