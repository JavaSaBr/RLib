package javasabr.rlib.common.tuple;

/**
 * A tuple containing two object references.
 *
 * @param left the left value
 * @param right the right value
 * @param <L> the type of the left value
 * @param <R> the type of the right value
 * @since 10.0.0
 */
public record Tuple<L, R>(L left, R right) {}
