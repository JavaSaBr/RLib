package com.ss.rlib.common.util;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to mark an object that it has a name.
 *
 * @author JavaSaBr
 */
public interface HasName {

    /**
     * Gets name.
     *
     * @return the name of this object.
     */
    @NotNull
    String getName();
}
