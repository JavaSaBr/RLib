package rlib.function;

/**
 * Функциональный интерфейс-функция на 3 аргумента.
 */
@FunctionalInterface
public interface TripleFunction<F, S, T, R> {

    public R apply(F first, S second, T third);
}
