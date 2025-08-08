package javasabr.rlib.common.function;

import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeSupplier<T> {

  @Nullable T get() throws Exception;
}
