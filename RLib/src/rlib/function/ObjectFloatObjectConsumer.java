package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The consumer with 3 arguments.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectFloatObjectConsumer<F, T> {

    void accept(@Nullable F first, float second, @Nullable T third);
}
