package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 2 аргумента.
 */
@FunctionalInterface
public interface ObjectIntFunction<T, R> {

    public R apply(T first, int second);
}
