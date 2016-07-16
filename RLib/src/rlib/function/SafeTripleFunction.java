package rlib.function;

/**
 * Функциональный интерфейс-функция на 3 аргумента.
 */
@FunctionalInterface
public interface SafeTripleFunction<F, S, T, R> {

    public R apply(F first, S second, T third) throws Exception;
}
