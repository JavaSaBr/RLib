package com.ss.rlib.common.compiler;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement byte code container.
 *
 * @author JavaSaBr
 */
public interface ByteCode {

    /**
     * Get the byte code.
     *
     * @return the byte code.
     */
    @NotNull byte[] getByteCode();
}
