package rlib.function;

/**
 * The consumer with 3 arguments.
 */
@FunctionalInterface
public interface ObjectFloatObjectConsumer<F, T> {

    void accept(F first, float second, T third);
}
