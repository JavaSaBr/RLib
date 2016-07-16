package rlib.function;

/**
 * Функциональный интерфейс-потребитель на 2 аргумента.
 */
@FunctionalInterface
public interface FloatFloatConsumer {

    public void accept(float first, float second);
}
