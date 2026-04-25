package javasabr.rlib.common.tuple;

/**
 * A tuple containing a long value and an object reference.
 *
 * @param left the long value
 * @param right the object reference
 * @param <R> the type of the object reference
 * @since 10.0.0
 */
public record LongRefTuple<R>(long left, R right) {}
