package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Entry<T, V> extends Reusable {

    /**
     * Get the next entry.
     *
     * @return the next entry.
     */
    @Nullable T getNext();

    /**
     * Set the next entry.
     *
     * @param next the next entry.
     */
    void setNext(@Nullable T next);

    /**
     * Get the value.
     *
     * @return the value.
     */
    @NotNull V getValue();

    /**
     * Set the value.
     *
     * @param value the new value of this entry.
     * @return the old value of null.
     */
    @NotNull V setValue(@NotNull V value);

    /**
     * Get the hash.
     *
     * @return the hash.
     */
    int getHash();
}
