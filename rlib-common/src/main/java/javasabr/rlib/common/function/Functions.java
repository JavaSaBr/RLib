package javasabr.rlib.common.function;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Utility class providing factory methods for common functional interfaces.
 *
 * @since 10.0.0
 */
public class Functions {

  /**
   * Utility class providing predicate factory methods for boolean values.
   *
   * @since 10.0.0
   */
  public static class Predicates {

    /**
     * Returns a predicate that evaluates to true if the input is true.
     *
     * @return a predicate that checks for true
     */
    public static Predicate<Boolean> isTrue() {

      return bool -> bool;
    }

    /**
     * Returns a predicate that runs the task if the input is true.
     *
     * @param task the task to run if true
     * @return a predicate that runs the task on true input
     */
    public static Predicate<Boolean> ifTrue(Runnable task) {

      return bool -> {

        if (bool) {
          task.run();
        }

        return true;
      };
    }

    /**
     * Returns a predicate that throws an exception if the input is true.
     *
     * @param factory the exception factory
     * @return a predicate that throws on true input
     */
    public static Predicate<Boolean> throwIfTrue(Supplier<? extends RuntimeException> factory) {

      return bool -> {

        if (bool) {
          throw factory.get();
        }

        return true;
      };
    }

    /**
     * Returns a predicate that evaluates to true if the input is false.
     *
     * @return a predicate that checks for false
     */
    public static Predicate<Boolean> isFalse() {

      return bool -> !bool;
    }

    /**
     * Returns a predicate that runs the task if the input is false.
     *
     * @param task the task to run if false
     * @return a predicate that runs the task on false input
     */
    public static Predicate<Boolean> ifFalse(Runnable task) {

      return bool -> {

        if (!bool) {
          task.run();
        }

        return true;
      };
    }

    /**
     * Returns a predicate that throws an exception if the input is false.
     *
     * @param factory the exception factory
     * @return a predicate that throws on false input
     */
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
