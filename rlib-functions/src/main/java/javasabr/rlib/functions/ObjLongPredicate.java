package javasabr.rlib.functions;

/**
 * Represents a predicate that accepts an object and a long argument.
 *
 * @param <A> the type of the object argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface ObjLongPredicate<A> {

  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param arg1 the object argument
   * @param arg2 the long argument
   * @return true if the arguments match the predicate
   * @since 10.0.0
   */
  boolean test(A arg1, long arg2);
}
