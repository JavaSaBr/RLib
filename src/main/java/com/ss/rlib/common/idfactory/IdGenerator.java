package com.ss.rlib.common.idfactory;

/**
 * The interface to implement an id factory.
 *
 * @author JavaSaBr
 */
public interface IdGenerator {

    /**
     * Get a next free ID.
     *
     * @return the next free ID.
     */
    default int getNextId() {
        throw new UnsupportedOperationException();
    }

    /**
     * Prepare.
     */
    default void prepare() {
    }

    /**
     * Free an used ID.
     *
     * @param id the released ID.
     */
    default void releaseId(final int id) {
    }

    /**
     * Used ids int.
     *
     * @return the count of used IDs.
     */
    default int usedIds() {
        return 0;
    }
}
