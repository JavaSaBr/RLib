package javasabr.rlib.common.function;

import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public class Functions {

  public static class Predicates {

    public static @NotNull Predicate<Boolean> isTrue() {

      return bool -> bool;
    }

    public static @NotNull Predicate<Boolean> ifTrue(@NotNull Runnable task) {

      return bool -> {

        if (bool) {
          task.run();
        }

        return true;
      };
    }

    public static @NotNull Predicate<Boolean> throwIfTrue(@NotNull Supplier<? extends RuntimeException> factory) {

      return bool -> {

        if (bool) {
          throw factory.get();
        }

        return true;
      };
    }

    public static @NotNull Predicate<Boolean> isFalse() {

      return bool -> !bool;
    }

    public static @NotNull Predicate<Boolean> ifFalse(@NotNull Runnable task) {

      return bool -> {

        if (!bool) {
          task.run();
        }

        return true;
      };
    }

    public static @NotNull Predicate<Boolean> throwIfFalse(@NotNull Supplier<? extends RuntimeException> factory) {

      return bool -> {

        if (!bool) {
          throw factory.get();
        }

        return true;
      };
    }
  }
}
