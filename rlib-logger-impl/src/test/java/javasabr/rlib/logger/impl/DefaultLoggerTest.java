package javasabr.rlib.logger.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.LockableArray;
import javasabr.rlib.collections.operation.LockableOperations;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerListener;
import javasabr.rlib.logger.api.LoggerManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

@ResourceLock("LoggerListeners")
class DefaultLoggerTest {

  private static final LockableArray<String> LOGS_DATA = ArrayFactory
      .stampedLockBasedArray(String.class);
  private static final LockableOperations<LockableArray<String>> LOGS_DATA_OPERATIONS = 
      LOGS_DATA.operations();
  
  private static final LoggerListener LOGGER_LISTENER = text -> LOGS_DATA_OPERATIONS
      .inWriteLock(text, Collection::add);

  @BeforeEach
  void prepare() {
    LoggerManager.addListener(LOGGER_LISTENER);
    LOGS_DATA_OPERATIONS
        .inWriteLock(Collection::clear);
  }
  
  @AfterEach
  void cleanup() {
    LOGS_DATA_OPERATIONS.inWriteLock(Collection::clear);
    LoggerManager.removeListener(LOGGER_LISTENER);
  }

  @Test
  void shouldCreateDefaultLoggerImplementation() {
    assertThat(LoggerManager.getLogger(DefaultLoggerTest.class))
        .isInstanceOf(DefaultLogger.class);
  }

  @Test
  void shouldWriteDataToDefaultLoggerImplementation() {
    // given:
    var logger = LoggerManager.getLogger(DefaultLoggerTest.class);
    logger.overrideEnabled(LoggerLevel.DEBUG, true);
    logger.overrideEnabled(LoggerLevel.WARNING, true);
    logger.overrideEnabled(LoggerLevel.ERROR, true);
    logger.overrideEnabled(LoggerLevel.INFO, true);
    
    // when:
    logger.print(LoggerLevel.ERROR, "test error data");

    // then:
    assertThat(LOGS_DATA.size()).isEqualTo(1);
    assertThat(LOGS_DATA.get(0)).startsWith("ERROR  ");
    assertThat(LOGS_DATA.get(0)).endsWith("DefaultLoggerTest: test error data");

    // when:
    logger.print(LoggerLevel.WARNING, "test warn data 2");

    // then:
    assertThat(LOGS_DATA.size()).isEqualTo(2);
    assertThat(LOGS_DATA.get(1)).startsWith("WARN   ");
    assertThat(LOGS_DATA.get(1)).endsWith("DefaultLoggerTest: test warn data 2");

    // when:
    logger.print(LoggerLevel.DEBUG, "test debug data 3");

    // then:
    assertThat(LOGS_DATA.size()).isEqualTo(3);
    assertThat(LOGS_DATA.get(2)).startsWith("DEBUG  ");
    assertThat(LOGS_DATA.get(2)).endsWith("DefaultLoggerTest: test debug data 3");

    // when:
    logger.print(LoggerLevel.INFO, "test info data 4");

    // then:
    assertThat(LOGS_DATA.size()).isEqualTo(4);
    assertThat(LOGS_DATA.get(3)).startsWith("INFO   ");
    assertThat(LOGS_DATA.get(3)).endsWith("DefaultLoggerTest: test info data 4");
  }
}
