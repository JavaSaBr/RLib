package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface IntObjectPredicate<T> {

    boolean test(int first, @Nullable T second);
}
