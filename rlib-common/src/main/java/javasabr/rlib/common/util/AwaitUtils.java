package javasabr.rlib.common.util;

import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;
import org.jspecify.annotations.NullMarked;

/**
 * The utility class to await some conditions.
 *
 * @author crazyrokr
 */
@NullMarked
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
  public static boolean await(long amount, ChronoUnit unit, Supplier<Boolean> condition) throws InterruptedException {
    if (condition.get()) {
      return true;
    }
    var timeoutMillis = unit.getDuration().toMillis() * amount;
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
