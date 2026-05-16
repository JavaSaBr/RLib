package javasabr.rlib.common.util;

import lombok.CustomLog;

/**
 * Utility methods for thread operations.
 *
 * @since 10.0.0
 */
@CustomLog
public class ThreadUtils {
  
  /**
   * Sleeps the current thread for the specified time, ignoring interrupts.
   *
   * @param time the time to sleep in milliseconds
   * @return true if it was interrupted
   * @since 10.0.0
   */
  public static boolean sleep(long time) {
    try {
      Thread.sleep(time);
      return false;
    } catch (InterruptedException e) {
      log.warn(e);
      return true;
    }
  }
}
