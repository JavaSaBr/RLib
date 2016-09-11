package rlib.function;

/**
 * The consumer with 4 arguments.
 */
@FunctionalInterface
public interface FourObjectConsumer<F, S, T, FO> {
    void accept(F first, S second, T third, FO fourth);
}
