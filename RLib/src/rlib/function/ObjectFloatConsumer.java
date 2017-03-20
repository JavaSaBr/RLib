package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectFloatConsumer<T> {

    void apply(@Nullable T first, float second);
}
