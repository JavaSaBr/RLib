package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 2 аргумента.
 */
@FunctionalInterface
public interface ObjectIntPredicate<T> {

    public boolean test(T fisrt, int second);
}
