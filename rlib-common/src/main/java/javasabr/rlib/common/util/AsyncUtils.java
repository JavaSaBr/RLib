package javasabr.rlib.common.util;

import java.util.concurrent.CompletionException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@NullMarked
public class AsyncUtils {

  public static <T> @Nullable T skip(Throwable throwable) {
    return null;
  }

  public static <T> @Nullable T continueCompletableStage(@Nullable T result, @Nullable Throwable throwable) {
    if (throwable instanceof RuntimeException) {
      throw (RuntimeException) throwable;
    } else if (throwable != null) {
      throw new CompletionException(throwable);
    } else {
      return result;
    }
  }
}
