package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectLongObjectConsumer<F, T> {

    void accept(@Nullable F first, long second, @Nullable T third);
}
