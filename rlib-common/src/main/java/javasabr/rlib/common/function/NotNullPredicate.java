package javasabr.rlib.common.function;

import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NotNullPredicate<T> extends Predicate<T> {

    @Override
    boolean test(@NotNull T object);
}
