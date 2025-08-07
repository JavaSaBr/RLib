package com.ss.rlib.logger.impl.test;

import javasabr.rlib.common.util.array.ConcurrentArray;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerListener;
import javasabr.rlib.logger.api.LoggerManager;
import com.ss.rlib.logger.impl.DefaultLogger;
import org.junit.jupiter.api.*;

import java.util.Collection;

public class DefaultLoggerTest {

    private static final ConcurrentArray<String> WROTE_DATA = ConcurrentArray.ofType(String.class);

    private static final LoggerListener LOGGER_LISTENER =
        text -> WROTE_DATA.runInWriteLock(strings -> strings.add(text));

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
        WROTE_DATA.runInWriteLock(Collection::clear);
    }

    @Test
    void shouldCreateDefaultLoggerImplementation() {

        var logger = LoggerManager.getLogger(DefaultLoggerTest.class);

        Assertions.assertTrue(logger instanceof DefaultLogger);
    }

    @Test
    void shouldWriteDataToDefaultLoggerImplementation() {

        var logger = LoggerManager.getLogger(DefaultLoggerTest.class);
        logger.print(LoggerLevel.ERROR, "test data");

        Assertions.assertEquals(1, WROTE_DATA.size());

        logger.print(LoggerLevel.ERROR, "test data 2");

        Assertions.assertEquals(2, WROTE_DATA.size());
    }
}
