package rlib.util;

/**
 * The interface for implementing the method for reloading the object to new version of the object.
 *
 * @author JavaSaBr
 */
public interface Reloadable<E> {

    /**
     * Reload this object to version of the object.
     *
     * @param updated the updated object.
     */
    void reload(E updated);
}
