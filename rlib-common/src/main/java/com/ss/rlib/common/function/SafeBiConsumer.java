package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeBiConsumer<F, S> {

    void accept(@Nullable F first, @Nullable S second) throws Exception;
}
