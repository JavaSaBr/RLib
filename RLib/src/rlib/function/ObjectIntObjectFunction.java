package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectIntObjectFunction<F, T, R> {

    @Nullable
    R apply(@Nullable F first, int second, @Nullable T third);
}
