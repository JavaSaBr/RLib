package javasabr.rlib.logger.api;

import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ServiceLoader;
import javasabr.rlib.logger.api.impl.NullLoggerFactory;

/**
 * The class to manage loggers.
 *
 * @author JavaSaBr
 */
public class LoggerManager {

  private static final LoggerFactory LOGGER_FACTORY;

  static {

    String className = System.getProperty("com.ss.rlib.logger.factory", "");

    Class<? extends LoggerFactory> implementation = null;

    if (!className.isEmpty()) {
      try {
        implementation = (Class<? extends LoggerFactory>) Class.forName(className);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }

    if (implementation == null) {

      var impls = ServiceLoader
          .load(LoggerFactory.class)
          .iterator();

      if (impls.hasNext()) {
        implementation = impls
            .next()
            .getClass();
      }
    }

    if (implementation == null) {
      System.err.println("ERROR: No any exist implementation of Rlib Logger Factory, will use null logger");
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
   * Add the new listener.
   *
   * @param listener the new listener.
   */
  public static void addListener(LoggerListener listener) {
    LOGGER_FACTORY.addListener(listener);
  }

  /**
   * Add the new writer.
   *
   * @param writer the new writer.
   */
  public static void addWriter(Writer writer) {
    LOGGER_FACTORY.addWriter(writer);
  }

  /**
   * Get the main logger.
   *
   * @return the main logger.
   */
  public static Logger getDefaultLogger() {
    return LOGGER_FACTORY.getDefault();
  }

  /**
   * Get or create a logger for the class.
   *
   * @param cs the class.
   * @return the logger for the class.
   */
  public static Logger getLogger(Class<?> cs) {
    return LOGGER_FACTORY.make(cs);
  }

  /**
   * Get or create a logger for the id.
   *
   * @param id the id.
   * @return the logger for the class.
   */
  public static Logger getLogger(String id) {
    return LOGGER_FACTORY.make(id);
  }

  /**
   * Remove the listener.
   *
   * @param listener the listener.
   */
  public static void removeListener(LoggerListener listener) {
    LOGGER_FACTORY.removeListener(listener);
  }

  /**
   * Remove the writer.
   *
   * @param writer the writer.
   */
  public static void removeWriter(Writer writer) {
    LOGGER_FACTORY.removeWriter(writer);
  }

  /**
   * Enable passed logger level for some logger.
   *
   * @param cs the class which use its own logger.
   * @param level the logger level to enable.
   */
  public static void enable(Class<?> cs, LoggerLevel level) {
    getLogger(cs).setEnabled(level, true);
  }

  /**
   * Disable passed logger level for some logger.
   *
   * @param cs the class which use its own logger.
   * @param level the logger level to disable.
   */
  public static void disable(Class<?> cs, LoggerLevel level) {
    getLogger(cs).setEnabled(level, false);
  }
}
