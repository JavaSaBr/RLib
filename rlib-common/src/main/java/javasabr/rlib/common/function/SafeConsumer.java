package javasabr.rlib.common.function;

import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeConsumer<T> {

  void accept(@Nullable T argument) throws Exception;
}
