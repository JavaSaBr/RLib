package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 2 аргумента.
 */
@FunctionalInterface
public interface SafeBiConsumer<F, S> {

    public void accept(F first, S second) throws Exception;
}
