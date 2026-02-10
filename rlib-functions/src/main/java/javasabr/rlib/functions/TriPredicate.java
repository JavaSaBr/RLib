package javasabr.rlib.functions;

/**
 * Represents a predicate that accepts three arguments.
 *
 * @param <A> the type of the first argument
 * @param <B> the type of the second argument
 * @param <C> the type of the third argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface TriPredicate<A, B, C> {

  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @param arg3 the third argument
   * @return true if the arguments match the predicate
   */
  boolean test(A arg1, B arg2, C arg3);
}
