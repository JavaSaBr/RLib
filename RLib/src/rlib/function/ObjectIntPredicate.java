package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * THe function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectIntPredicate<T> {

    boolean test(@Nullable T fisrt, int second);
}
