package com.ss.rlib.logger.slf4j.test;

import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.logger.slf4j.Slf4jLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Slf4jLoggerTest {

    @Test
    void shouldCreateDefaultLoggerImplementation() {

        var logger = LoggerManager.getLogger(Slf4jLoggerTest.class);

        Assertions.assertTrue(logger instanceof Slf4jLogger);
    }
}
