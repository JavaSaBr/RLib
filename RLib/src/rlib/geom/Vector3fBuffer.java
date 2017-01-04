package rlib.geom;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a buffer with vectors.
 *
 * @author JavaSaBr
 */
public interface Vector3fBuffer {

    /**
     * @return the next vector.
     */
    @NotNull
    Vector3f nextVector();
}
