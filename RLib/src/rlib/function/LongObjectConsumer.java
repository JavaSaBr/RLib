package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface LongObjectConsumer<T> {

    void accept(long first, @Nullable T second);
}
