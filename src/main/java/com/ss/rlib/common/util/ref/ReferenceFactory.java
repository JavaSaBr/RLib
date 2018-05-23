package com.ss.rlib.common.util.ref;

import static com.ss.rlib.common.util.ClassUtils.unsafeCast;
import org.jetbrains.annotations.NotNull;

/**
 * THe factory to create new references.
 *
 * @author JavaSaBr
 */
public final class ReferenceFactory {

    /**
     * Create a new reference of the type.
     *
     * @param type the type of reference.
     * @param <T>  the reference's type.
     * @return the new reference.
     */
    public static <T extends Reference> @NotNull T newRef(@NotNull ReferenceType type) {
        return unsafeCast(type.create());
    }

    /**
     * Take or create a reference from the global pool.
     *
     * @param type the type of reference.
     * @param <T>  the reference's type.
     * @return the reference.
     */
    public static <T extends Reference> @NotNull T takeFromPool(@NotNull ReferenceType type) {
        return unsafeCast(type.take());
    }

    /**
     * Take or create a reference from the thread pool.
     *
     * @param type the type of reference.
     * @param <T>  the reference's type.
     * @return the reference.
     */
    public static <T extends Reference> @NotNull T takeFromTLPool(@NotNull ReferenceType type) {
        return unsafeCast(type.takeThreadLocal());
    }

    private ReferenceFactory() {
        throw new IllegalArgumentException();
    }
}
