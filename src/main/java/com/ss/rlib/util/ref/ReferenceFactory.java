package com.ss.rlib.util.ref;

import org.jetbrains.annotations.NotNull;

/**
 * THe factory for creating new references.
 *
 * @author JavaSaBr
 */
public final class ReferenceFactory {

    /**
     * Create new reference of the type.
     *
     * @param type the type of reference.
     * @return the new reference.
     */
    @NotNull
    public static Reference newRef(@NotNull final ReferenceType type) {
        return type.create();
    }

    /**
     * Take or create a reference from the global pool.
     *
     * @param type the type of reference.
     * @return the reference.
     */
    @NotNull
    public static Reference takeFromPool(@NotNull final ReferenceType type) {
        return type.take();
    }

    /**
     * Take or create a reference from the thread pool.
     *
     * @param type the type of reference.
     * @return the reference.
     */
    @NotNull
    public static Reference takeFromTLPool(@NotNull final ReferenceType type) {
        return type.takeThreadLocal();
    }

    private ReferenceFactory() {
        throw new IllegalArgumentException();
    }
}
