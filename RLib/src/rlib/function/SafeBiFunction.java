package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeBiFunction<F, S, R> {

    @Nullable
    R apply(@Nullable F first, @Nullable S second) throws Exception;
}
