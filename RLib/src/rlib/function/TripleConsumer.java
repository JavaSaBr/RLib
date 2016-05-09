package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 3 аргумента.
 */
@FunctionalInterface
public interface TripleConsumer<F, S, T> {

    public void accept(F first, S second, T third);
}
