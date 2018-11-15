package com.ss.rlib.common.concurrent.util;

import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;
import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;

/**
 * The class with utilities methods to work with threads.
 *
 * @author JavaSaBr
 */
public class ThreadUtils {

    private static final Logger LOGGER = LoggerManager.getLogger(ThreadUtils.class);

    /**
     * Sleep the current thread.
     *
     * @param time the time in ms.
     */
    public static void sleep(final long time) {
        try {
            Thread.sleep(time);
        } catch (final InterruptedException e) {
            LOGGER.warning(e);
        }
    }
}
