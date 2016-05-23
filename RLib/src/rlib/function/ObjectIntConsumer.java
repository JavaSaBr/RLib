package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 2 аргумента.
 */
@FunctionalInterface
public interface ObjectIntConsumer<T> {

    public void accept(T first, int second);
}
