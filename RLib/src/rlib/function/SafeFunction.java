package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeFunction<F, R> {

    @Nullable
    R apply(@Nullable F first) throws Exception;
}
