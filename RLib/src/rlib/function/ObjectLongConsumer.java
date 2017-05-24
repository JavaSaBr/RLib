package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectLongConsumer<T> {

    void accept(@Nullable T first, long second);
}
