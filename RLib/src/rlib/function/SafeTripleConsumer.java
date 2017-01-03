package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeTripleConsumer<F, S, T> {

    void accept(@Nullable F first, @Nullable S second, @Nullable T third) throws Exception;
}
