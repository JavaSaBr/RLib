package javasabr.rlib.common.util;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;

/**
 * Utility methods for thread operations.
 *
 * @since 10.0.0
 */
public class ThreadUtils {

  private static final Logger LOGGER = LoggerManager.getLogger(ThreadUtils.class);

  /**
   * Sleeps the current thread for the specified time, ignoring interrupts.
   *
   * @param time the time to sleep in milliseconds
   */
  public static void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      LOGGER.warning(e);
    }
  }
}
