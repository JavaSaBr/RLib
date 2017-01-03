package rlib.geom;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a buffer with vectors.
 *
 * @author JavaSaBr
 */
public interface VectorBuffer {

    /**
     * @return the next vector.
     */
    @NotNull
    Vector nextVector();
}
