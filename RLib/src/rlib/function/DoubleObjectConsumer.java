package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function-consumer.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface DoubleObjectConsumer<T> {

    void accept(double first, @Nullable T second);
}
