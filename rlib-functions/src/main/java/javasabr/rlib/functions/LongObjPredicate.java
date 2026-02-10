package javasabr.rlib.functions;

/**
 * Represents a predicate that accepts a long and an object argument.
 *
 * @param <B> the type of the object argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface LongObjPredicate<B> {

  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param arg1 the long argument
   * @param arg2 the object argument
   * @return true if the arguments match the predicate
   */
  boolean test(long arg1, B arg2);
}
