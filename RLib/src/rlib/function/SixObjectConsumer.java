package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The consumer with 5 arguments.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SixObjectConsumer<F, S, T, FO, FI, SX> {

    void accept(@Nullable F first, @Nullable S second, @Nullable T third, @Nullable FO fourth,
                @Nullable FI five, @Nullable SX six);
}
