package com.ss.rlib.common.geom;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a buffer of vectors.
 *
 * @author JavaSaBr
 */
public interface Vector3fBuffer {

    Vector3fBuffer NO_REUSE = new Vector3fBuffer() {

        @Override
        public @NotNull Vector3f take() {
            return new Vector3f();
        }

        @Override
        public @NotNull Vector3f take(@NotNull Vector3f source) {
            return new Vector3f(source);
        }

        @Override
        public @NotNull Vector3f take(float x, float y, float z) {
            return new Vector3f(x, y, z);
        }
        
        @Override
        public void put(@NotNull Vector3f vector) {
            // NO_REUSE doesnt contains pooling implementation.
        }
    };
    
    /**
     * Take a next free vector.
     *
     * @return the next vector.
     */
    @NotNull Vector3f take();
    
    /**
     * Take a next free vector with copied values from the source vector.
     *
     * @param source the source vector.
     * @return the next free vector with copied values.
     */
    default @NotNull Vector3f take(@NotNull Vector3f source) {
        return take().set(source);
    }
    
    /**
     * Take a next free vector with copied values.
     *
     * @param x the X component.
     * @param y the Y component.
     * @param z the Z component.
     * @return the next free vector with copied values.
     */
    default @NotNull Vector3f take(float x, float y, float z) {
        return take().set(x, y, z);
    }
    
    /**
     * Put vector to the pool.
     * 
     * @param vector vector
     */
    void put(@NotNull Vector3f vector);
    
    /**
     * Put vectors to the pool.
     * 
     * @param vectors vectors
     */
    default void put(@NotNull Vector3f... vectors) {
        for(int i = 0; i < vectors.length; i++) {
            put(vectors[i]);
        }
    }

    /**
     * Take a next free vector.<br>
     * @deprecated use {@link Vector3fBuffer#take()}
     *
     * @return the next vector.
     */
    @Deprecated
    default @NotNull Vector3f nextVector() {
        return take();
    }

    /**
     * Take a next free vector with copied values from the source vector.<br>
     * @deprecated use {@link Vector3fBuffer#take(Vector3f)}
     *
     * @param source the source vector.
     * @return the next free vector with copied values.
     */
    @Deprecated
    default @NotNull Vector3f next(@NotNull Vector3f source) {
        return take(source);
    }

    /**
     * Take a next free vector with copied values.<br>
     * @deprecated use {@link Vector3fBuffer#take(float, float, float)}
     *
     * @param x the X component.
     * @param y the Y component.
     * @param z the Z component.
     * @return the next free vector with copied values.
     */
    @Deprecated
    default @NotNull Vector3f next(float x, float y, float z) {
        return take(x, y, z);
    }
}
