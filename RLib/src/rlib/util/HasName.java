package rlib.util;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to mark an object that it has a name.
 *
 * @author JavaSaBr
 */
public interface HasName {

    /**
     * @return the name of this object.
     */
    @NotNull
    String getName();
}
