package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 2 аргумента.
 */
@FunctionalInterface
public interface IntObjectPredicate<T> {

    public boolean test(int first, T second);
}
