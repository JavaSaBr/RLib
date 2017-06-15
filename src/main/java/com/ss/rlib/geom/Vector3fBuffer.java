package com.ss.rlib.geom;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a buffer with vectors.
 *
 * @author JavaSaBr
 */
public interface Vector3fBuffer {

    /**
     * Next vector vector 3 f.
     *
     * @return the next vector.
     */
    @NotNull
    Vector3f nextVector();
}
