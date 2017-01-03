package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface DoubleObjectConsumer<T> {

    void accept(double first, @Nullable T second);
}
