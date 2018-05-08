package com.ss.rlib.common.util;

import org.jetbrains.annotations.NotNull;

/**
 * The interface for implementing the method for reloading the object to new version of the object.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public interface Reloadable<E> {

    /**
     * Reload this object to version of the object.
     *
     * @param updated the updated object.
     */
    void reload(@NotNull E updated);
}
