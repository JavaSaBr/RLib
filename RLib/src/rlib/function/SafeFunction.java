package rlib.function;

/**
 * Функциональный интерфейс-функция на 1 аргумент.
 */
@FunctionalInterface
public interface SafeFunction<F, R> {

    public R apply(F first) throws Exception;
}
