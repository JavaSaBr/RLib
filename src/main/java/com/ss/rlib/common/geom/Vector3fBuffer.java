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
        public @NotNull Vector3f nextVector() {
            return new Vector3f();
        }

        @Override
        public @NotNull Vector3f next(@NotNull Vector3f source) {
            return new Vector3f(source);
        }

        @Override
        public @NotNull Vector3f next(float x, float y, float z) {
            return new Vector3f(x, y, z);
        }
    };

    /**
     * Take a next free vector.
     *
     * @return the next vector.
     */
    @NotNull Vector3f nextVector();

    /**
     * Take a next free vector with copied values from the source vector.
     *
     * @param source the source vector.
     * @return the next free vector with copied values.
     */
    default @NotNull Vector3f next(@NotNull Vector3f source) {
        return nextVector().set(source);
    }

    /**
     * Take a next free vector with copied values.
     *
     * @param x the X component.
     * @param y the Y component.
     * @param z the Z component.
     * @return the next free vector with copied values.
     */
    default @NotNull Vector3f next(float x, float y, float z) {
        return nextVector().set(x, y, z);
    }
}
