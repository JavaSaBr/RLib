package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 2 аргумента.
 */
@FunctionalInterface
public interface LongBiObjectConsumer<S, T> {

    public void accept(long first, S second, T third);
}
