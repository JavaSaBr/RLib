package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 3 аргумента.
 */
@FunctionalInterface
public interface ObjectIntObjectConsumer<F, T> {

    public void accept(F first, int second, T third);
}
