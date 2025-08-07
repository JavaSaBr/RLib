package javasabr.rlib.common.concurrent.util;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;

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
  public static void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      LOGGER.warning(e);
    }
  }
}
