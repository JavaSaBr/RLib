package com.ss.rlib.geom;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a buffer of vectors.
 *
 * @author JavaSaBr
 */
public interface Vector3fBuffer {

    /**
     * Take the next free vector.
     *
     * @return the next vector.
     */
    @NotNull Vector3f nextVector();
}
