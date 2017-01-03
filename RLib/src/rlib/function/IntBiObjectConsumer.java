package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface IntBiObjectConsumer<S, T> {

    void accept(int first, @Nullable S second, @Nullable T third);
}
