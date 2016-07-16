package rlib.function;

/**
 * Функциональный интерфейс-фабрика.
 */
@FunctionalInterface
public interface SafeFactory<R> {

    public R get() throws Exception;
}
