package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 1 аргумент.
 */
@FunctionalInterface
public interface SafeConsumer<T> {

    public void accept(T argument) throws Exception;
}
