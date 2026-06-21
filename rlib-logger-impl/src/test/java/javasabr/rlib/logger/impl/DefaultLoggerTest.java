package javasabr.rlib.logger.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.dictionary.RefToRefDictionary;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerFactory;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerManager;
import javasabr.rlib.logger.impl.config.consumer.LogMessageConsumer;
import javasabr.rlib.logger.impl.config.impl.DefaultLoggerConfig;
import javasabr.rlib.logger.impl.config.impl.DefaultLoggerConfig.LoggerConsumersKey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class DefaultLoggerTest {
  
  private final List<String> receivedLogs = new ArrayList<>();
  
  @AfterEach
  void cleanup() {
    receivedLogs.clear();
  }

  @Test
  void shouldCreateDefaultLoggerImplementation() {
    assertThat(LoggerManager.getLogger(DefaultLoggerTest.class))
        .isInstanceOf(DefaultLogger.class);
  }

  @Test
  void shouldCreateLoggerWithCorrectFullNameAndShortName() {
    // when:
    Logger logger1 = LoggerManager.getLogger(DefaultLoggerTest.class);
    Logger logger2 = LoggerManager.getLogger("javasabr.rlib.logger.impl.DefaultLoggerTest2");
    
    // then
    assertThat(logger1.name()).isEqualTo("javasabr.rlib.logger.impl.DefaultLoggerTest");
    assertThat(logger1.shortName()).isEqualTo("DefaultLoggerTest");
    assertThat(logger2.name()).isEqualTo("javasabr.rlib.logger.impl.DefaultLoggerTest2");
    assertThat(logger2.shortName()).isEqualTo("DefaultLoggerTest2");

    // when:
    Logger logger3 = LoggerManager.getLogger("javasabr.rlib.logger.impl.DefaultLoggerTest3.");
    
    // then:
    assertThat(logger3.name()).isEqualTo("javasabr.rlib.logger.impl.DefaultLoggerTest3.");
    assertThat(logger3.shortName()).isEqualTo("DefaultLoggerTest3");
  }

  @Test
  void shouldSendLogMessagesForAllLevels() {
    // given:
    RefToRefDictionary<LoggerConsumersKey, Array<LogMessageConsumer>> loggerConsumers = RefToRefDictionary.of(
        DefaultLoggerConfig.ROOT_TRACE_CONSUMERS_KEY,
        Array.of((level, logger, message) -> receivedLogs.add(level + " " + logger.name() + " " + message)));
    var loggerService = new DefaultLoggerService(new DefaultLoggerConfig(
        DefaultLoggerConfig.ENABLE_ALL_LEVELS,
        loggerConsumers));
    var logger = loggerService.getLogger(DefaultLoggerTest.class);
    
    // when:
    logger.print(LoggerLevel.ERROR, "test error data");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(1);
    assertThat(receivedLogs.get(0)).isEqualTo("ERROR javasabr.rlib.logger.impl.DefaultLoggerTest test error data");

    // when:
    logger.print(LoggerLevel.WARNING, "test warn data 2");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(2);
    assertThat(receivedLogs.get(1)).isEqualTo("WARN javasabr.rlib.logger.impl.DefaultLoggerTest test warn data 2");

    // when:
    logger.print(LoggerLevel.DEBUG, "test debug data 3");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(3);
    assertThat(receivedLogs.get(2)).isEqualTo("DEBUG javasabr.rlib.logger.impl.DefaultLoggerTest test debug data 3");

    // when:
    logger.print(LoggerLevel.INFO, "test info data 4");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(4);
    assertThat(receivedLogs.get(3)).isEqualTo("INFO javasabr.rlib.logger.impl.DefaultLoggerTest test info data 4");

    // when:
    logger.print(LoggerLevel.TRACE, "test info data 5");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(5);
    assertThat(receivedLogs.get(4)).isEqualTo("TRACE javasabr.rlib.logger.impl.DefaultLoggerTest test info data 5");
  }

  @Test
  void shouldSendOnlyErrorLogMessages() {
    // given:
    var enabledLevels = RefToRefDictionary.of(
        LoggerFactory.ROOT_LOGGER_NAME,
        LoggerLevel.ERROR);
    RefToRefDictionary<LoggerConsumersKey, Array<LogMessageConsumer>> loggerConsumers = RefToRefDictionary.of(
        DefaultLoggerConfig.ROOT_TRACE_CONSUMERS_KEY,
        Array.of((level, logger, message) -> receivedLogs.add(level + " " + logger.name() + " " + message)));
    var loggerService = new DefaultLoggerService(new DefaultLoggerConfig(enabledLevels, loggerConsumers));
    var logger = loggerService.getLogger(DefaultLoggerTest.class);

    // when:
    logger.print(LoggerLevel.ERROR, "test error data");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(1);
    assertThat(receivedLogs.get(0)).isEqualTo("ERROR javasabr.rlib.logger.impl.DefaultLoggerTest test error data");

    // when:
    logger.print(LoggerLevel.WARNING, "test warn data 2");
    logger.print(LoggerLevel.INFO, "test info data 3");
    logger.print(LoggerLevel.DEBUG, "test debug data 4");
    logger.print(LoggerLevel.TRACE, "test info data 5");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(1);
  }

  @Test
  void shouldSendOnlyWarnAndHigherLogMessages() {
    // given:
    var enabledLevels = RefToRefDictionary.of(
        LoggerFactory.ROOT_LOGGER_NAME,
        LoggerLevel.WARNING);
    RefToRefDictionary<LoggerConsumersKey, Array<LogMessageConsumer>> loggerConsumers = RefToRefDictionary.of(
        DefaultLoggerConfig.ROOT_TRACE_CONSUMERS_KEY,
        Array.of((level, logger, message) -> receivedLogs.add(level + " " + logger.name() + " " + message)));
    var loggerService = new DefaultLoggerService(new DefaultLoggerConfig(enabledLevels, loggerConsumers));
    var logger = loggerService.getLogger(DefaultLoggerTest.class);

    // when:
    logger.print(LoggerLevel.ERROR, "test error data");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(1);
    assertThat(receivedLogs.get(0)).isEqualTo("ERROR javasabr.rlib.logger.impl.DefaultLoggerTest test error data");

    // when:
    logger.print(LoggerLevel.WARNING, "test warn data 2");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(2);
    assertThat(receivedLogs.get(1)).isEqualTo("WARN javasabr.rlib.logger.impl.DefaultLoggerTest test warn data 2");

    // when:
    logger.print(LoggerLevel.INFO, "test info data 3");
    logger.print(LoggerLevel.DEBUG, "test debug data 4");
    logger.print(LoggerLevel.TRACE, "test info data 5");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(2);
  }

  @Test
  void shouldSendOnlyInfoAndHigherLogMessages() {
    // given:
    var enabledLevels = RefToRefDictionary.of(
        LoggerFactory.ROOT_LOGGER_NAME,
        LoggerLevel.INFO);
    RefToRefDictionary<LoggerConsumersKey, Array<LogMessageConsumer>> loggerConsumers = RefToRefDictionary.of(
        DefaultLoggerConfig.ROOT_TRACE_CONSUMERS_KEY,
        Array.of((level, logger, message) -> receivedLogs.add(level + " " + logger.name() + " " + message)));
    var loggerService = new DefaultLoggerService(new DefaultLoggerConfig(enabledLevels, loggerConsumers));
    var logger = loggerService.getLogger(DefaultLoggerTest.class);

    // when:
    logger.print(LoggerLevel.ERROR, "test error data");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(1);
    assertThat(receivedLogs.get(0)).isEqualTo("ERROR javasabr.rlib.logger.impl.DefaultLoggerTest test error data");

    // when:
    logger.print(LoggerLevel.WARNING, "test warn data 2");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(2);
    assertThat(receivedLogs.get(1)).isEqualTo("WARN javasabr.rlib.logger.impl.DefaultLoggerTest test warn data 2");
    
    // when:
    logger.print(LoggerLevel.INFO, "test info data 3");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(3);
    assertThat(receivedLogs.get(2)).isEqualTo("INFO javasabr.rlib.logger.impl.DefaultLoggerTest test info data 3");
   
    // when:
    logger.print(LoggerLevel.DEBUG, "test debug data 4");
    logger.print(LoggerLevel.TRACE, "test info data 5");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(3);
  }

  @Test
  void shouldSendOnlyDebugAndHigherLogMessages() {
    // given:
    var enabledLevels = RefToRefDictionary.of(
        LoggerFactory.ROOT_LOGGER_NAME,
        LoggerLevel.DEBUG);
    RefToRefDictionary<LoggerConsumersKey, Array<LogMessageConsumer>> loggerConsumers = RefToRefDictionary.of(
        DefaultLoggerConfig.ROOT_TRACE_CONSUMERS_KEY,
        Array.of((level, logger, message) -> receivedLogs.add(level + " " + logger.name() + " " + message)));
    var loggerService = new DefaultLoggerService(new DefaultLoggerConfig(enabledLevels, loggerConsumers));
    var logger = loggerService.getLogger(DefaultLoggerTest.class);

    // when:
    logger.print(LoggerLevel.ERROR, "test error data");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(1);
    assertThat(receivedLogs.get(0)).isEqualTo("ERROR javasabr.rlib.logger.impl.DefaultLoggerTest test error data");

    // when:
    logger.print(LoggerLevel.WARNING, "test warn data 2");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(2);
    assertThat(receivedLogs.get(1)).isEqualTo("WARN javasabr.rlib.logger.impl.DefaultLoggerTest test warn data 2");

    // when:
    logger.print(LoggerLevel.INFO, "test info data 3");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(3);
    assertThat(receivedLogs.get(2)).isEqualTo("INFO javasabr.rlib.logger.impl.DefaultLoggerTest test info data 3");

    // when:
    logger.print(LoggerLevel.DEBUG, "test debug data 4");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(4);
    assertThat(receivedLogs.get(3)).isEqualTo("DEBUG javasabr.rlib.logger.impl.DefaultLoggerTest test debug data 4");

    // when:
    logger.print(LoggerLevel.TRACE, "test info data 5");

    // then:
    assertThat(receivedLogs.size()).isEqualTo(4);
  }

  @Test
  void shouldSendLogMessageToCorrectConsumer1() {
    // given:
    List<String> traceReceivedLogs = new ArrayList<>();
    List<String> warnReceivedLogs = new ArrayList<>();
    RefToRefDictionary<LoggerConsumersKey, Array<LogMessageConsumer>> loggerConsumers = RefToRefDictionary.of(
        DefaultLoggerConfig.ROOT_TRACE_CONSUMERS_KEY,
        Array.of((level, logger, message) -> traceReceivedLogs.add(level + " " + logger.name() + " " + message)),
        DefaultLoggerConfig.ROOT_WARN_CONSUMERS_KEY,
        Array.of((level, logger, message) -> warnReceivedLogs.add(level + " " + logger.name() + " " + message)));
    var loggerService = new DefaultLoggerService(new DefaultLoggerConfig(
        DefaultLoggerConfig.ENABLE_ALL_LEVELS,
        loggerConsumers));
    var logger = loggerService.getLogger(DefaultLoggerTest.class);

    // when:
    logger.print(LoggerLevel.ERROR, "test error data");
    logger.print(LoggerLevel.WARNING, "test warn data 2");
    logger.print(LoggerLevel.INFO, "test info data 3");
    logger.print(LoggerLevel.DEBUG, "test debug data 4");
    logger.print(LoggerLevel.TRACE, "test info data 5");

    // then:
    assertThat(warnReceivedLogs).hasSize(2);
    assertThat(traceReceivedLogs).hasSize(3);
    assertThat(warnReceivedLogs.get(0)).isEqualTo("ERROR javasabr.rlib.logger.impl.DefaultLoggerTest test error data");
    assertThat(warnReceivedLogs.get(1)).isEqualTo("WARN javasabr.rlib.logger.impl.DefaultLoggerTest test warn data 2");
    assertThat(traceReceivedLogs.get(0)).isEqualTo("INFO javasabr.rlib.logger.impl.DefaultLoggerTest test info data 3");
    assertThat(traceReceivedLogs.get(1)).isEqualTo("DEBUG javasabr.rlib.logger.impl.DefaultLoggerTest test debug data 4");
    assertThat(traceReceivedLogs.get(2)).isEqualTo("TRACE javasabr.rlib.logger.impl.DefaultLoggerTest test info data 5");
  }

  @Test
  void shouldSendLogMessageToCorrectConsumer2() {
    // given:
    List<String> debugReceivedLogs = new ArrayList<>();
    List<String> errorReceivedLogs = new ArrayList<>();
    RefToRefDictionary<LoggerConsumersKey, Array<LogMessageConsumer>> loggerConsumers = RefToRefDictionary.of(
        DefaultLoggerConfig.ROOT_DEBUG_CONSUMERS_KEY,
        Array.of((level, logger, message) -> debugReceivedLogs.add(level + " " + logger.name() + " " + message)),
        DefaultLoggerConfig.ROOT_ERROR_CONSUMERS_KEY,
        Array.of((level, logger, message) -> errorReceivedLogs.add(level + " " + logger.name() + " " + message)));
    var loggerService = new DefaultLoggerService(new DefaultLoggerConfig(
        DefaultLoggerConfig.ENABLE_ALL_LEVELS,
        loggerConsumers));
    var logger = loggerService.getLogger(DefaultLoggerTest.class);

    // when:
    logger.print(LoggerLevel.ERROR, "test error data");
    logger.print(LoggerLevel.WARNING, "test warn data 2");
    logger.print(LoggerLevel.INFO, "test info data 3");
    logger.print(LoggerLevel.DEBUG, "test debug data 4");
    logger.print(LoggerLevel.TRACE, "test info data 5");

    // then:
    assertThat(errorReceivedLogs).hasSize(1);
    assertThat(debugReceivedLogs).hasSize(3);
    assertThat(errorReceivedLogs.get(0)).isEqualTo("ERROR javasabr.rlib.logger.impl.DefaultLoggerTest test error data");
    assertThat(debugReceivedLogs.get(0)).isEqualTo("WARN javasabr.rlib.logger.impl.DefaultLoggerTest test warn data 2");
    assertThat(debugReceivedLogs.get(1)).isEqualTo("INFO javasabr.rlib.logger.impl.DefaultLoggerTest test info data 3");
    assertThat(debugReceivedLogs.get(2)).isEqualTo("DEBUG javasabr.rlib.logger.impl.DefaultLoggerTest test debug data 4");
  }

  @Test
  void shouldSendLogMessageToCorrectConsumer3() {
    // given:
    List<String> traceReceivedLogs = new ArrayList<>();
    List<String> infoReceivedLogs = new ArrayList<>();
    RefToRefDictionary<LoggerConsumersKey, Array<LogMessageConsumer>> loggerConsumers = RefToRefDictionary.of(
        DefaultLoggerConfig.ROOT_TRACE_CONSUMERS_KEY,
        Array.of((level, logger, message) -> traceReceivedLogs.add(level + " " + logger.name() + " " + message)),
        DefaultLoggerConfig.ROOT_INFO_CONSUMERS_KEY,
        Array.of((level, logger, message) -> infoReceivedLogs.add(level + " " + logger.name() + " " + message)));
    var loggerService = new DefaultLoggerService(new DefaultLoggerConfig(
        DefaultLoggerConfig.ENABLE_ALL_LEVELS,
        loggerConsumers));
    var logger = loggerService.getLogger(DefaultLoggerTest.class);

    // when:
    logger.print(LoggerLevel.ERROR, "test error data");
    logger.print(LoggerLevel.WARNING, "test warn data 2");
    logger.print(LoggerLevel.INFO, "test info data 3");
    logger.print(LoggerLevel.DEBUG, "test debug data 4");
    logger.print(LoggerLevel.TRACE, "test info data 5");

    // then:
    assertThat(infoReceivedLogs).hasSize(3);
    assertThat(traceReceivedLogs).hasSize(2);
    assertThat(infoReceivedLogs.get(0)).isEqualTo("ERROR javasabr.rlib.logger.impl.DefaultLoggerTest test error data");
    assertThat(infoReceivedLogs.get(1)).isEqualTo("WARN javasabr.rlib.logger.impl.DefaultLoggerTest test warn data 2");
    assertThat(infoReceivedLogs.get(2)).isEqualTo("INFO javasabr.rlib.logger.impl.DefaultLoggerTest test info data 3");
    assertThat(traceReceivedLogs.get(0)).isEqualTo("DEBUG javasabr.rlib.logger.impl.DefaultLoggerTest test debug data 4");
    assertThat(traceReceivedLogs.get(1)).isEqualTo("TRACE javasabr.rlib.logger.impl.DefaultLoggerTest test info data 5");
  }
}
