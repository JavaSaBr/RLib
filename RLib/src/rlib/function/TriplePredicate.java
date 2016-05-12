package rlib.function;

/**
 * Функциональный интерфейс-условие на 3 аргумента.
 */
@FunctionalInterface
public interface TriplePredicate<F, S, T> {

    public boolean test(F first, S second, T third);
}
