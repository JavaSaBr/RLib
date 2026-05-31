package javasabr.rlib.logger.impl;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.ArrayIterationFunctions;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerListener;
import javasabr.rlib.logger.api.LoggerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * The class for managing loggers.
 *
 * @author JavaSaBr
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class DefaultLoggerService implements LoggerService {

  static final LoggerLevel[] LOGGER_LEVELS = LoggerLevel.values();

  ConcurrentMap<Object, DefaultLogger> loggers;
  MutableArray<LoggerListener> listeners;
  ArrayIterationFunctions<LoggerListener> listenerIterations;
  MutableArray<Writer> writers;
  ArrayIterationFunctions<Writer> writerIterations;

  Logger logger;
  DateTimeFormatter timeFormatter;
  int[] override;

  public DefaultLoggerService() {
    this.loggers = new ConcurrentHashMap<>();
    this.logger = getLogger("");
    this.timeFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy HH:mm:ss:SSS");
    this.listeners = ArrayFactory.copyOnModifyArray(LoggerListener.class);
    this.listenerIterations = listeners.iterations();
    this.writers = ArrayFactory.copyOnModifyArray(Writer.class);
    this.writerIterations = writers.iterations();
    this.override = new int[LOGGER_LEVELS.length];
    Arrays.fill(override, NOT_CONFIGURE);
  }

  @Override
  public void addListener(LoggerListener listener) {
    listeners.add(listener);
  }

  @Override
  public void addWriter(Writer writer) {
    writers.add(writer);
  }

  public Logger getDefault() {
    return logger;
  }

  public DefaultLogger getLogger(Class<?> type) {
    return loggers.computeIfAbsent(
        type, 
        key -> {
          var clazz = (Class<?>) key;
          return new DefaultLogger(clazz.getSimpleName(), this);
        });
  }

  public DefaultLogger getLogger(String name) {
    return loggers.computeIfAbsent(
        name, 
        key -> new DefaultLogger(key.toString(), this));
  }

  @Override
  public void removeListener(LoggerListener listener) {
    listeners.remove(listener);
  }

  @Override
  public void removeWriter(Writer writer) {
    writers.remove(writer);
  }

  @Override
  public void enable(Class<?> cs, LoggerLevel level) {
    getLogger(cs).overrideEnabled(level, true);
  }

  @Override
  public void disable(Class<?> cs, LoggerLevel level) {
    getLogger(cs).overrideEnabled(level, false);
  }

  @Override
  public void configureDefault(LoggerLevel level, boolean def) {
    override[level.ordinal()] = def ? ENABLED : DISABLED;
  }

  @Override
  public void removeDefault(LoggerLevel level) {
    override[level.ordinal()] = NOT_CONFIGURE;
  }

  @Override
  public int enabled(LoggerLevel level) {
    return override[level.ordinal()];
  }

  @Override
  public void write(Logger logger, LoggerLevel level, String message) {
    if (logger instanceof DefaultLogger defaultLogger) {
      write(defaultLogger, level, message);
    } else {
      throw new UnsupportedOperationException("Unsupported logger type: " + logger.getClass());
    }
  }

  void write(DefaultLogger logger, LoggerLevel level, String message) {
    String name = logger.name();
    var timestamp = timeFormatter.format(LocalDateTime.now());
    var resultMessage = level.title()
        + level.offset() + ' '
        + timestamp + ' '
        + name + ": "
        + message;
    write(level, resultMessage);
  }

  private void write(LoggerLevel level, String resultMessage) {
    listenerIterations.forEach(resultMessage, LoggerListener::println);
    writerIterations.forEach(resultMessage, DefaultLoggerService::append);
    switch (level) {
      case INFO, DEBUG -> System.out.println(resultMessage);
      case ERROR, WARNING -> System.err.println(resultMessage);
    }
    if (!level.forceFlush()) {
      return;
    }
    listeners.forEach(LoggerListener::flush);
    writers.forEach(DefaultLoggerService::flush);
  }

  private static void append(Writer writer, String toWrite) {
    try {
      writer.append(toWrite);
      writer.append('\n');
    } catch (IOException exception) {
      //noinspection CallToPrintStackTrace
      exception.printStackTrace();
    }
  }

  private static void flush(Writer writer) {
    try {
      writer.flush();
    } catch (IOException exception) {
      //noinspection CallToPrintStackTrace
      exception.printStackTrace();
    }
  }
}
