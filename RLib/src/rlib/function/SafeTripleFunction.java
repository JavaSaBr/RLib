package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeTripleFunction<F, S, T, R> {

    @Nullable
    R apply(@Nullable F first, @Nullable S second, @Nullable T third) throws Exception;
}
