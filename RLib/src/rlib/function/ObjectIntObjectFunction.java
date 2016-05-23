package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 3 аргумента.
 */
@FunctionalInterface
public interface ObjectIntObjectFunction<F, T, R> {

    public R apply(F first, int second, T third);
}
