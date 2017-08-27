package com.ss.rlib.util.ref;

import static com.ss.rlib.util.ClassUtils.unsafeCast;
import org.jetbrains.annotations.NotNull;

/**
 * THe factory to create new references.
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
    public static <T extends Reference> @NotNull T newRef(@NotNull final ReferenceType type) {
        return unsafeCast(type.create());
    }

    /**
     * Take or create a reference from the global pool.
     *
     * @param type the type of reference.
     * @return the reference.
     */
    public static <T extends Reference> @NotNull T takeFromPool(@NotNull final ReferenceType type) {
        return unsafeCast(type.create());
    }

    /**
     * Take or create a reference from the thread pool.
     *
     * @param type the type of reference.
     * @return the reference.
     */
    public static <T extends Reference> @NotNull T takeFromTLPool(@NotNull final ReferenceType type) {
        return unsafeCast(type.create());
    }

    private ReferenceFactory() {
        throw new IllegalArgumentException();
    }
}
