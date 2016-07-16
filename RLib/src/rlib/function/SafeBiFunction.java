package rlib.function;

/**
 * Функциональный интерфейс-функция на 2 аргумента.
 */
@FunctionalInterface
public interface SafeBiFunction<F, S, R> {

    public R apply(F first, S second) throws Exception;
}
