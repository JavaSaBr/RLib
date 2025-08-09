package javasabr.rlib.common.function;

import org.jspecify.annotations.NullUnmarked;

/**
 * @author JavaSaBr
 */
@NullUnmarked
@FunctionalInterface
public interface FloatBiObjectConsumer<S, T> {

  void accept(float first, S second, T third);
}
