package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeConsumer<T> {

    void accept(@Nullable T argument) throws Exception;
}
