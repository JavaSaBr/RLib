package com.ss.rlib.compiler;

import org.jetbrains.annotations.NotNull;

/**
 * The interface for implementing byte source container.
 *
 * @author JavaSaBr
 */
public interface ByteSource {

    /**
     * Get byte source.
     *
     * @return the byte code of the class.
     */
    @NotNull
    byte[] getByteSource();
}
