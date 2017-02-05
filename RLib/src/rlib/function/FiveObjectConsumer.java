package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The consumer with 5 arguments.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface FiveObjectConsumer<F, S, T, FO, FI> {

    void accept(@Nullable F first, @Nullable S second, @Nullable T third, @Nullable FO fourth, @Nullable FI five);
}
