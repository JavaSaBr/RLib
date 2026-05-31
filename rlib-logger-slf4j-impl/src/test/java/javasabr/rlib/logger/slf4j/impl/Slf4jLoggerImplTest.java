package javasabr.rlib.logger.slf4j.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.LockableArray;
import javasabr.rlib.collections.operation.LockableOperations;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerListener;
import javasabr.rlib.logger.api.LoggerManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.slf4j.LoggerFactory;

@ResourceLock("LoggerListeners")
@ResourceLock(value = "RLibLoggerOverrides", mode = ResourceAccessMode.READ_WRITE)
class Slf4jLoggerImplTest {

  private static final LockableArray<String> LOGS_DATA = ArrayFactory
      .stampedLockBasedArray(String.class);
  private static final LockableOperations<LockableArray<String>> LOGS_DATA_OPERATIONS =
      LOGS_DATA.operations();
  private static final LoggerListener LOGGER_LISTENER = text -> LOGS_DATA_OPERATIONS
      .inWriteLock(text, Collection::add);

  private final Logger rlibLogger = LoggerManager.getLogger(Slf4jLoggerImplTest.class);

  @BeforeEach
  void prepare() {
    LoggerManager.addListener(LOGGER_LISTENER);
    LOGS_DATA_OPERATIONS.inWriteLock(Collection::clear);
  }

  @AfterEach
  void cleanup() {
    for (var level : LoggerLevel.values()) {
      rlibLogger.resetToDefault(level);
    }
    LOGS_DATA_OPERATIONS.inWriteLock(Collection::clear);
    LoggerManager.removeListener(LOGGER_LISTENER);
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
    rlibLogger.overrideEnabled(LoggerLevel.INFO, true);
    var slf4jLogger = LoggerFactory.getLogger(Slf4jLoggerImplTest.class);

    // when:
    slf4jLogger.info("hello from slf4j");

    // then:
    assertThat(LOGS_DATA.size())
        .isEqualTo(1);
    assertThat(LOGS_DATA.get(0))
        .endsWith("Slf4jLoggerImplTest: hello from slf4j");
  }

  @Test
  void shouldDelegateErrorWithExceptionToRlibLogger() {
    // given:
    rlibLogger.overrideEnabled(LoggerLevel.ERROR, true);
    var slf4jLogger = LoggerFactory.getLogger(Slf4jLoggerImplTest.class);
    var exception = new RuntimeException("boom");

    // when:
    slf4jLogger.error("error occurred", exception);

    // then:
    assertThat(LOGS_DATA.size())
        .isEqualTo(1);
    assertThat(LOGS_DATA.get(0))
        .contains("Slf4jLoggerImplTest: error occurred")
        .contains("RuntimeException: boom");
  }

  @Test
  void shouldNotDelegateDebugWhenDisabledByDefault() {
    // given:
    var slf4jLogger = LoggerFactory.getLogger(Slf4jLoggerImplTest.class);

    // when:
    slf4jLogger.debug("should not appear");

    // then:
    assertThat(LOGS_DATA.size())
        .isEqualTo(0);
  }

  @Test
  void shouldDelegateFormattedMessageToRlibLogger() {
    // given:
    rlibLogger.overrideEnabled(LoggerLevel.INFO, true);
    var slf4jLogger = LoggerFactory.getLogger(Slf4jLoggerImplTest.class);

    // when:
    slf4jLogger.info("value is {}", 42);

    // then:
    assertThat(LOGS_DATA.size())
        .isEqualTo(1);
    assertThat(LOGS_DATA.get(0))
        .endsWith("Slf4jLoggerImplTest: value is 42");
  }

  @Test
  void shouldDelegateFormattedMessageWithTrailingExceptionToRlibLogger() {
    // given:
    rlibLogger.overrideEnabled(LoggerLevel.ERROR, true);
    
    var slf4jLogger = LoggerFactory.getLogger(Slf4jLoggerImplTest.class);
    var exception = new RuntimeException("oops");

    // when:
    slf4jLogger.error("failed with code {}", 500, exception);

    // then:
    assertThat(LOGS_DATA.size())
        .isEqualTo(1);
    assertThat(LOGS_DATA.get(0))
        .contains("Slf4jLoggerImplTest: failed with code 500")
        .contains("RuntimeException: oops");
  }
}
