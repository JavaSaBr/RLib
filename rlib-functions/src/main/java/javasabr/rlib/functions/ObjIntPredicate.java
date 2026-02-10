package javasabr.rlib.functions;

/**
 * Represents a predicate that accepts an object and an int argument.
 *
 * @param <A> the object argument type
 * @since 10.0.0
 */
@FunctionalInterface
public interface ObjIntPredicate<A> {

  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @return true if the arguments match the predicate, false otherwise
   * @since 10.0.0
   */
  boolean test(A arg1, int arg2);
}
