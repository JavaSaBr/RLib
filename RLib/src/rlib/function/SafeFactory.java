package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeFactory<R> {

    @Nullable
    R get() throws Exception;
}
