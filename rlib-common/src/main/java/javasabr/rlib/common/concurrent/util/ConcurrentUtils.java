package javasabr.rlib.common.concurrent.util;

import java.util.function.Function;
import javasabr.rlib.common.concurrent.lock.Lockable;
import javasabr.rlib.common.function.ObjectIntFunction;
import javasabr.rlib.common.function.ObjectLongFunction;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * THe utility class with methods to work in concurrent cases.
 *
 * @author JavaSaBr
 */
@NullMarked
public final class ConcurrentUtils {

  private static final Logger LOGGER = LoggerManager.getLogger(ConcurrentUtils.class);

  /**
   * Notify all threads.
   *
   * @param object the object
   */
  public static void notifyAll(Object object) {
    synchronized (object) {
      object.notifyAll();
    }
  }

  /**
   * Notify all threads from a synchronized block.
   *
   * @param object the object
   */
  public static void notifyAllInSynchronize(Object object) {
    object.notifyAll();
  }

  /**
   * Notify all threads and wait.
   *
   * @param object the object
   */
  public static void notifyAndWait(Object object) {
    synchronized (object) {
      notifyAllInSynchronize(object);
      waitInSynchronize(object);
    }
  }

  /**
   * Wait.
   *
   * @param object the object
   */
  public static void wait(Object object) {
    synchronized (object) {
      try {
        object.wait();
      } catch (final InterruptedException e) {
        LOGGER.warning(e);
      }
    }
  }

  /**
   * Wait.
   *
   * @param object the object.
   * @param time the time in ms.
   */
  public static void wait(Object object, long time) {
    synchronized (object) {
      try {
        object.wait(time);
      } catch (final InterruptedException e) {
        LOGGER.warning(e);
      }
    }
  }

  /**
   * Wait from a synchronized block.
   *
   * @param object the object
   */
  public static void waitInSynchronize(Object object) {
    try {
      object.wait();
    } catch (final InterruptedException e) {
      LOGGER.warning(e);
    }
  }

  /**
   * Wait from a synchronized block.
   *
   * @param object the object.
   * @param time the time in ms.
   */
  public static void waitInSynchronize(Object object, long time) {
    try {
      object.wait(time);
    } catch (final InterruptedException e) {
      LOGGER.warning(e);
    }
  }

  /**
   * Apply a function in locked block.
   *
   * @param <T> the type parameter
   * @param <R> the type parameter
   * @param sync the synchronizer.
   * @param function the function.
   * @return the result from the function.
   */
  @Nullable
  public static <T extends Lockable, R> R get(T sync, Function<T, @Nullable R> function) {
    sync.lock();
    try {
      return function.apply(sync);
    } finally {
      sync.unlock();
    }
  }

  /**
   * Apply a function in locked block.
   *
   * @param <T> the type parameter
   * @param <R> the type parameter
   * @param sync the synchronizer.
   * @param argument the argument.
   * @param function the function.
   * @return the result from the function.
   */
  @Nullable
  public static <T extends Lockable, R> R get(T sync, int argument, ObjectIntFunction<T, R> function) {
    sync.lock();
    try {
      return function.apply(sync, argument);
    } finally {
      sync.unlock();
    }
  }

  /**
   * Apply a function in locked block.
   *
   * @param <T> the type parameter
   * @param <R> the type parameter
   * @param sync the synchronizer.
   * @param argument the argument.
   * @param function the function.
   * @return the result from the function.
   */
  @Nullable
  public static <T extends Lockable, @Nullable R> R getInL(T sync, long argument, ObjectLongFunction<T, R> function) {
    sync.lock();
    try {
      return function.apply(sync, argument);
    } finally {
      sync.unlock();
    }
  }

  private ConcurrentUtils() {
    throw new RuntimeException();
  }
}
