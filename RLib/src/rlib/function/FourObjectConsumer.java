package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The consumer with 4 arguments.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface FourObjectConsumer<F, S, T, FO> {

    void accept(@Nullable F first, @Nullable S second, @Nullable T third, @Nullable FO fourth);
}
