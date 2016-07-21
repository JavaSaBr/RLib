package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 2 аргумента.
 */
@FunctionalInterface
public interface ObjectLongPredicate<T> {

    public boolean test(T first, long second);
}
