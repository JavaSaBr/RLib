package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectFloatFunction<T, R> {

    @Nullable
    R apply(@Nullable T first, float second);
}
