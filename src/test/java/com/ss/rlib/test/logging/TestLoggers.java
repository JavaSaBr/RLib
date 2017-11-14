package com.ss.rlib.test.logging;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerLevel;
import com.ss.rlib.logging.LoggerManager;
import org.junit.jupiter.api.Test;

/**
 * The test ot test loggers.
 *
 * @author JavaSaBr
 */
public class TestLoggers {

    @Test
    public void testCreateLogger() {
        final Logger logger = LoggerManager.getLogger(getClass());
        assertNotNull(logger, "Logger wasn't created.");
    }

    @Test
    public void testDebugMessages() {
        final Logger logger = LoggerManager.getLogger(getClass());
        LoggerLevel.DEBUG.setEnabled(true);
        logger.debug("Simple message");
        logger.debug(5, (integer) -> "Lazy message with 5: " + integer);
        logger.debug(5, 10D, (integer, aDouble) -> "Lazy message with 5: " + integer + " and 10: " + aDouble);
        logger.debug("", "Message with a string owner.");
        logger.debug("", 5, (integer) -> "Lazy message with 5: " + integer);
        logger.debug("", 5, 10D, (integer, aDouble) -> "Lazy message with 5: " + integer + " and 10: " + aDouble);
    }
}
