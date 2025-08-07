package javasabr.rlib.common.function;

import java.util.function.BiPredicate;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NotNullBiPredicate<T, U> extends BiPredicate<T, U> {

    @Override
    boolean test(@NotNull T first, @NotNull U second);
}
