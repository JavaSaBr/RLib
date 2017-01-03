package rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * ФThe function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface LongObjectPredicate<T> {

    boolean test(long first, @Nullable T second);
}
