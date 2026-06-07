package javasabr.rlib.logger.slf4j.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.dictionary.RefToRefDictionary;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.DefaultLoggerService;
import javasabr.rlib.logger.impl.config.consumer.LogMessageConsumer;
import javasabr.rlib.logger.impl.config.impl.DefaultLoggerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.slf4j.LoggerFactory;

@ResourceLock("LoggerListeners")
@ResourceLock(value = "RLibLoggerOverrides", mode = ResourceAccessMode.READ_WRITE)
class Slf4jLoggerImplTest {

  private final List<String> receivedLogs = new ArrayList<>();
  private Logger logger;

  @BeforeEach
  void prepare() {
    RefToRefDictionary<DefaultLoggerConfig.LoggerConsumersKey, Array<LogMessageConsumer>> loggerConsumers = RefToRefDictionary.of(
        DefaultLoggerConfig.ROOT_TRACE_CONSUMERS_KEY,
        Array.of((level, logger, message) -> receivedLogs.add(level + " " + logger.name() + " " + message)));
    var defaultLoggerService = new DefaultLoggerService(new DefaultLoggerConfig(
        DefaultLoggerConfig.ENABLE_ALL_LEVELS,
        loggerConsumers));
    logger = defaultLoggerService.getLogger(Slf4jLoggerImplTest.class);
  }
  
  @Test
  void shouldReturnSlf4jLoggerImplFromLoggerFactory() {
    // when:
    var logger = LoggerFactory.getLogger(Slf4jLoggerImplTest.class);

    // then:
    assertThat(logger)
        .isInstanceOf(Slf4jLoggerImpl.class);
  }

  @Test
  void shouldReturnLoggerWithMatchingName() {
    // when:
    var logger = LoggerFactory.getLogger("my.logger.name");

    // then:
    assertThat(logger.getName())
        .isEqualTo("my.logger.name");
  }

  @Test
  void shouldDelegateInfoMessageToRlibLogger() {
    // given:
    var slf4jLogger = new Slf4jLoggerImpl(logger);

    // when:
    slf4jLogger.info("hello from slf4j");

    // then:
    assertThat(receivedLogs.size())
        .isEqualTo(1);
    assertThat(receivedLogs.getFirst())
        .isEqualTo("INFO javasabr.rlib.logger.slf4j.impl.Slf4jLoggerImplTest hello from slf4j");
  }

  @Test
  void shouldDelegateErrorWithExceptionToRlibLogger() {
    // given:
    var slf4jLogger = new Slf4jLoggerImpl(logger);
    var exception = new RuntimeException("boom");

    // when:
    slf4jLogger.error("error occurred", exception);

    // then:
    assertThat(receivedLogs.size())
        .isEqualTo(1);
    assertThat(receivedLogs.getFirst())
        .startsWith("ERROR javasabr.rlib.logger.slf4j.impl.Slf4jLoggerImplTest error occurred: java.lang.RuntimeException: boom");
  }

  @Test
  void shouldNotDelegateDebugWhenDisabled() {
    // given:
    logger.overrideEnabled(LoggerLevel.DEBUG, false);
    var slf4jLogger = new Slf4jLoggerImpl(logger);

    // when:
    slf4jLogger.debug("should not appear");

    // then:
    assertThat(receivedLogs.size())
        .isEqualTo(0);
  }

  @Test
  void shouldDelegateFormattedMessageToRlibLogger() {
    // given:
    logger.overrideEnabled(LoggerLevel.INFO, true);
    var slf4jLogger = new Slf4jLoggerImpl(logger);

    // when:
    slf4jLogger.info("value is {}", 42);

    // then:
    assertThat(receivedLogs.size())
        .isEqualTo(1);
    assertThat(receivedLogs.getFirst())
        .endsWith("INFO javasabr.rlib.logger.slf4j.impl.Slf4jLoggerImplTest value is 42");
  }

  @Test
  void shouldDelegateFormattedMessageWithTrailingExceptionToRlibLogger() {
    // given:
    logger.overrideEnabled(LoggerLevel.ERROR, true);
    var slf4jLogger = new Slf4jLoggerImpl(logger);
    var exception = new RuntimeException("oops");

    // when:
    slf4jLogger.error("failed with code {}", 500, exception);

    // then:
    assertThat(receivedLogs.size())
        .isEqualTo(1);
    assertThat(receivedLogs.getFirst())
        .contains("ERROR javasabr.rlib.logger.slf4j.impl.Slf4jLoggerImplTest failed with code 500: java.lang.RuntimeException: oops")
        .contains("RuntimeException: oops");
  }
}
