package javasabr.rlib.common.function;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class Functions {

  public static class Predicates {

    public static Predicate<Boolean> isTrue() {

      return bool -> bool;
    }

    public static Predicate<Boolean> ifTrue(Runnable task) {

      return bool -> {

        if (bool) {
          task.run();
        }

        return true;
      };
    }

    public static Predicate<Boolean> throwIfTrue(Supplier<? extends RuntimeException> factory) {

      return bool -> {

        if (bool) {
          throw factory.get();
        }

        return true;
      };
    }

    public static Predicate<Boolean> isFalse() {

      return bool -> !bool;
    }

    public static Predicate<Boolean> ifFalse(Runnable task) {

      return bool -> {

        if (!bool) {
          task.run();
        }

        return true;
      };
    }

    public static Predicate<Boolean> throwIfFalse(Supplier<? extends RuntimeException> factory) {

      return bool -> {

        if (!bool) {
          throw factory.get();
        }

        return true;
      };
    }
  }
}
