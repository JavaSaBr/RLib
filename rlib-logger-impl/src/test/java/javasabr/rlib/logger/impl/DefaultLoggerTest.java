package javasabr.rlib.logger.impl;

import static org.assertj.core.api.Assertions.assertThat;

import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.LockableArray;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerListener;
import javasabr.rlib.logger.api.LoggerManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DefaultLoggerTest {

  private static final LockableArray<String> WROTE_DATA = ArrayFactory
      .stampedLockBasedArray(String.class);

  private static final LoggerListener LOGGER_LISTENER = text -> {
    long stamp = WROTE_DATA.writeLock();
    try {
      WROTE_DATA.add(text);
    } finally {
      WROTE_DATA.writeUnlock(stamp);
    }
  };

  @BeforeAll
  static void registerListener() {
    LoggerManager.addListener(LOGGER_LISTENER);
  }

  @AfterAll
  static void unregisterListener() {
    LoggerManager.addListener(LOGGER_LISTENER);
  }

  @BeforeEach
  void clearWroteData() {
    long stamp = WROTE_DATA.writeLock();
    try {
      WROTE_DATA.clear();
    } finally {
      WROTE_DATA.writeUnlock(stamp);
    }
  }

  @Test
  void shouldCreateDefaultLoggerImplementation() {
    var logger = LoggerManager.getLogger(DefaultLoggerTest.class);
    assertThat(logger)
        .isInstanceOf(DefaultLogger.class);
  }

  @Test
  void shouldWriteDataToDefaultLoggerImplementation() {

    var logger = LoggerManager.getLogger(DefaultLoggerTest.class);
    logger.print(LoggerLevel.ERROR, "test data");

    assertThat(WROTE_DATA.size()).isEqualTo(1);

    logger.print(LoggerLevel.ERROR, "test data 2");

    assertThat(WROTE_DATA.size()).isEqualTo(2);
  }
}
