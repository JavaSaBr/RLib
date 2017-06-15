package com.ss.rlib.concurrent.util;

import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerManager;

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
