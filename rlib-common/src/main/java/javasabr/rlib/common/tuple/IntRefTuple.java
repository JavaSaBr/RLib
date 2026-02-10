package javasabr.rlib.common.tuple;

/**
 * A tuple containing an int value and an object reference.
 *
 * @param left the int value
 * @param right the object reference
 * @param <R> the type of the object reference
 * @since 10.0.0
 */
public record IntRefTuple<R>(int left, R right) {}
