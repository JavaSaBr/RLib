package javasabr.rlib.common.util;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;

/**
 * The utility class to await some conditions.
 *
 * @author crazyrokr
 */
@UtilityClass
public final class AwaitUtils {

  /**
   * Await for the condition during the amount of time units.
   *
   * @param amount the amount of time units.
   * @param unit the time unit.
   * @param condition the condition.
   * @return true if the condition was met.
   * @throws InterruptedException if the current thread was interrupted.
   */
  public static boolean await(long amount, TimeUnit unit, Supplier<Boolean> condition) throws InterruptedException {
    if (condition.get()) {
      return true;
    }
    var timeoutMillis = unit.toMillis(amount);
    var endTime = System.currentTimeMillis() + timeoutMillis;
    while (System.currentTimeMillis() < endTime) {
      if (condition.get()) {
        return true;
      }
      Thread.sleep(Math.clamp(endTime - System.currentTimeMillis(), 1, 10));
    }
    return condition.get();
  }
}
