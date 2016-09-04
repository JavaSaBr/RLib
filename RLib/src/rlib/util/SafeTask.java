package rlib.util;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * The interface for implementing safe task.
 *
 * @author JavaSaBr
 */
public interface SafeTask extends Runnable {

    @Override
    default void run() {
        try {
            runImpl();
        } catch (final Exception e) {
            final Logger logger = LoggerManager.getDefaultLogger();
            logger.warning(this, e);
        }
    }

    void runImpl();
}
