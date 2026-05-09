package javasabr.rlib.common.util;

import java.util.Objects;
import java.util.concurrent.CompletionException;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.Nullable;

/**
 * Utility methods for asynchronous programming and CompletionStage handling.
 *
 * @since 10.0.0
 */
@UtilityClass
public class AsyncUtils {

  /**
   * A utility method that ignores the throwable and returns null.
   * <p>
   * Useful as an exception handler in CompletionStage chains.
   *
   * @param <T> the expected result type
   * @param throwable the throwable to skip
   * @return always null
   */
  @Nullable
  public static <T> T skip(Throwable throwable) {
    return null;
  }

  /**
   * Continues a CompletionStage by either returning the result or throwing an exception.
   *
   * @param <T> the result type
   * @param result the result value, required if throwable is null
   * @param throwable the exception, if any
   * @return the result if no exception occurred
   * @throws RuntimeException if throwable is a RuntimeException
   * @throws CompletionException if throwable is any other exception
   */
  public static <T> T continueCompletableStage(@Nullable T result, @Nullable Throwable throwable) {
    if (throwable instanceof RuntimeException) {
      throw (RuntimeException) throwable;
    } else if (throwable != null) {
      throw new CompletionException(throwable);
    } else {
      return Objects.requireNonNull(result);
    }
  }
}
