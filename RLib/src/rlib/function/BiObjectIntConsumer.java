package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 3 аргумента.
 */
@FunctionalInterface
public interface BiObjectIntConsumer<F, S> {

    public void accept(F first, S second, int third);
}
