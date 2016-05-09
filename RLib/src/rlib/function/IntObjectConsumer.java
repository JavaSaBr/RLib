package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 2 аргумента.
 */
@FunctionalInterface
public interface IntObjectConsumer<T> {

    public void accept(int first, T second);
}
