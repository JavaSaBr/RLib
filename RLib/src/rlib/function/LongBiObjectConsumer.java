package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface LongBiObjectConsumer<S, T> {

    void accept(long first, @Nullable S second, @Nullable T third);
}
