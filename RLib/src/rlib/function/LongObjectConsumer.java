package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 2 аргумента.
 */
@FunctionalInterface
public interface LongObjectConsumer<T> {

    public void accept(long first, T second);
}
