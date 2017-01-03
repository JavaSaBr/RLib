package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeBiConsumer<F, S> {

    void accept(@Nullable F first, @Nullable S second) throws Exception;
}
