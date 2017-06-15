package com.ss.rlib.util.dictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface for implementing Unsafe part of {@link LongDictionary} API.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public interface UnsafeLongDictionary<V> extends LongDictionary<V> {

    /**
     * Content long entry [ ].
     *
     * @return the array of entries.
     */
    @NotNull
    LongEntry<V>[] content();

    /**
     * Remove an entry for the key.
     *
     * @param key the key of the entry.
     * @return removed entry.
     */
    @Nullable
    LongEntry<V> removeEntryForKey(long key);
}
