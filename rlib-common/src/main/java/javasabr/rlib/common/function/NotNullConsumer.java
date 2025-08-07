package javasabr.rlib.common.function;

import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NotNullConsumer<T> extends Consumer<T> {

    @Override
    void accept(@NotNull T object);
}
