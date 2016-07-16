package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 3 аргумента.
 */
@FunctionalInterface
public interface SafeTripleConsumer<F, S, T> {

    public void accept(F first, S second, T third) throws Exception;
}
