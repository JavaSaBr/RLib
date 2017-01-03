package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface TriplePredicate<F, S, T> {

    boolean test(@Nullable F first, @Nullable S second, @Nullable T third);
}
