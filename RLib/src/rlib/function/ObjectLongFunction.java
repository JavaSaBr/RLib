package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 2 аргумента.
 */
@FunctionalInterface
public interface ObjectLongFunction<T, R> {

    public R apply(T first, long second);
}
