package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectIntObjectConsumer<F, T> {

    void accept(@Nullable F first, int second, @Nullable T third);
}
