package javasabr.rlib.common.concurrent.deadlock;

import java.lang.management.ThreadInfo;
import org.jspecify.annotations.NullMarked;

/**
 * The interface to implement a deadlock listener.
 *
 * @author JavaSaBr
 */
@NullMarked
public interface DeadLockListener {

  /**
   * Notify about deadlock detecting.
   *
   * @param info the information about thread.
   */
  void onDetected(ThreadInfo info);
}
