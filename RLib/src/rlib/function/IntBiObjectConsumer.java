package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 2 аргумента.
 */
@FunctionalInterface
public interface IntBiObjectConsumer<S, T> {

    public void accept(int first, S second, T third);
}
