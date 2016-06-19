package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 3 аргумента.
 */
@FunctionalInterface
public interface ObjectLongObjectConsumer<F, T> {

    public void accept(F first, long second, T third);
}
