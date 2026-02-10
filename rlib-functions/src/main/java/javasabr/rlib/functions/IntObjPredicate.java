package javasabr.rlib.functions;

/**
 * Represents a predicate that accepts an int and an object argument.
 *
 * @param <B> the type of the object argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface IntObjPredicate<B> {

  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param arg1 the int argument
   * @param arg2 the object argument
   * @return true if the arguments match the predicate
   */
  boolean test(int arg1, B arg2);
}
