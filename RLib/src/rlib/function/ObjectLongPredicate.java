package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectLongPredicate<T> {

    boolean test(@Nullable T first, long second);
}
