package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 2 аргумента.
 */
@FunctionalInterface
public interface BooleanFloatConsumer {

    public void accept(boolean first, float second);
}
