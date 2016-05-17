package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 2 аргумента.
 */
@FunctionalInterface
public interface LongObjectPredicate<T> {

    public boolean test(long first, T second);
}
