package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 1 аргумент.
 */
@FunctionalInterface
public interface FunctionInt<T> {

    public int apply(T first);
}
