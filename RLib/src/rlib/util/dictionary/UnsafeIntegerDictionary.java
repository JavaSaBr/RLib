package rlib.util.dictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface for implementing Unsafe part of {@link IntegerDictionary} API.
 *
 * @author JavaSaBr
 */
public interface UnsafeIntegerDictionary<V> extends IntegerDictionary<V> {

    /**
     * @return the array of entries.
     */
    @NotNull
    IntegerEntry<V>[] content();

    /**
     * Remove an entry for the key.
     *
     * @param key the key of the entry.
     * @return removed entry.
     */
    @Nullable
    IntegerEntry<V> removeEntryForKey(final int key);
}
