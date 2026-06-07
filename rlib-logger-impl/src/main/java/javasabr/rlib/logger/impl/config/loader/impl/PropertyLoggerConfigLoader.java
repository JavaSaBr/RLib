package javasabr.rlib.logger.impl.config.loader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.DefaultLoggerService;
import javasabr.rlib.logger.impl.config.LoggerConfig;
import javasabr.rlib.logger.impl.config.consumer.impl.ConsoleMessageConsumer;
import javasabr.rlib.logger.impl.config.impl.LoggerConfigBuilder;
import javasabr.rlib.logger.impl.config.loader.LoggerConfigLoader;
import javasabr.rlib.logger.impl.config.render.LogMessageRender;
import javasabr.rlib.logger.impl.config.render.impl.SimpleLogMessageRender;
import javasabr.rlib.logger.impl.config.render.impl.pattern.PatternLogMessageRender;

public class PropertyLoggerConfigLoader implements LoggerConfigLoader {

  public static final String FILE_MAIN = "rlib.logger.properties";
  public static final String FILE_TEST = "rlib.logger-test.properties";

  @Override
  public Optional<LoggerConfig> tryToLoad() {
    ClassLoader classLoader = Thread
        .currentThread()
        .getContextClassLoader();
    InputStream propertiesStream = classLoader.getResourceAsStream(FILE_TEST);
    if (propertiesStream == null) {
      propertiesStream = classLoader.getResourceAsStream(FILE_MAIN);
    }
    if (propertiesStream == null) {
      return Optional.empty();
    }
    Properties properties = new Properties();
    try {
      properties.load(propertiesStream);
    } catch (IOException ex) {
      ex.printStackTrace();
      return Optional.empty();
    } finally {
      try {
        propertiesStream.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    LoggerConfig loggerConfig = loadFromProperties(properties);
    properties.clear();
    return Optional.of(loggerConfig);
  }
  
  LoggerConfig loadFromProperties(Properties properties) {
    var builder = new LoggerConfigBuilder();
    LogMessageRender messageRender = null;
    for (Object key : properties.keySet()) {
      String stringKey = (String) key;
      if (stringKey.startsWith("logger.level.")) {
        LoggerLevel level = LoggerLevel.valueOf(properties.getProperty(stringKey));
        String loggerName = stringKey.substring("logger.level.".length());
        builder.registerLoggerLevel(loggerName, level);
      } else if (stringKey.equals("logger.message.pattern")) {
        messageRender = new PatternLogMessageRender(properties.getProperty(stringKey));
      }
    }
    if (messageRender == null) {
      messageRender = new SimpleLogMessageRender();
    }
    builder.registerLoggerConsumer(
        DefaultLoggerService.ROOT_LOGGER_NAME,
        LoggerLevel.TRACE,
        new ConsoleMessageConsumer(messageRender));
    return builder.build();
  }
  
  @Override
  public int order() {
    return LoggerConfigLoader.ORDER_NORMAL;
  }
}
