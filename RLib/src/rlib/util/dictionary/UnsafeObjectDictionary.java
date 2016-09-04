package rlib.util.dictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface for implementing Unsafe part of {@link ObjectDictionary} API.
 *
 * @author JavaSaBr
 */
public interface UnsafeObjectDictionary<K, V> extends ObjectDictionary<K, V> {

    /**
     * @return the array of entries.
     */
    @NotNull
    ObjectEntry<K, V>[] content();

    /**
     * Remove an entry for the key.
     *
     * @param key the key of the entry.
     * @return removed entry.
     */
    @Nullable
    ObjectEntry<K, V> removeEntryForKey(@NotNull final K key);
}
