package javasabr.rlib.concurrent.util;

import java.util.function.Function;
import javasabr.rlib.concurrent.lock.Lockable;
import javasabr.rlib.functions.ObjIntFunction;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@UtilityClass
public final class ConcurrentUtils {

  private static final Logger LOGGER = LoggerManager.getLogger(ConcurrentUtils.class);

  public static void notifyAll(Object object) {
    synchronized (object) {
      object.notifyAll();
    }
  }

  public static void notifyAllInSynchronize(Object object) {
    object.notifyAll();
  }

  public static void notifyAndWait(Object object) {
    synchronized (object) {
      notifyAllInSynchronize(object);
      waitInSynchronize(object);
    }
  }

  public static void wait(Object object) {
    synchronized (object) {
      try {
        object.wait();
      } catch (final InterruptedException e) {
        LOGGER.warning(e);
      }
    }
  }

  public static void wait(Object object, long time) {
    synchronized (object) {
      try {
        object.wait(time);
      } catch (final InterruptedException e) {
        LOGGER.warning(e);
      }
    }
  }

  public static void waitInSynchronize(Object object) {
    try {
      object.wait();
    } catch (final InterruptedException e) {
      LOGGER.warning(e);
    }
  }

  public static void waitInSynchronize(Object object, long time) {
    try {
      object.wait(time);
    } catch (final InterruptedException e) {
      LOGGER.warning(e);
    }
  }

  @Nullable
  public static <T extends Lockable, R> R get(T sync, Function<T, @Nullable R> function) {
    sync.lock();
    try {
      return function.apply(sync);
    } finally {
      sync.unlock();
    }
  }

  @Nullable
  public static <T extends Lockable, R> R get(T sync, int argument, ObjIntFunction<T, @Nullable R> function) {
    sync.lock();
    try {
      return function.apply(sync, argument);
    } finally {
      sync.unlock();
    }
  }
}
