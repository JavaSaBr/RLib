package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The fast read-only implementation of {@link LongDictionary} without threadsafe supporting.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public class ReadOnlyFastLongDictionary<V> extends FastLongDictionary<V> {

    public ReadOnlyFastLongDictionary() {
    }

    @Override
    public void put(@NotNull Dictionary<LongKey, V> dictionary) {
        throw new IllegalStateException("This dictionary is only read.");
    }

    @Override
    public @Nullable V put(long key, @Nullable V value) {
        throw new IllegalStateException("This dictionary is only read.");
    }

    @Override
    public @Nullable V remove(long key) {
        throw new IllegalStateException("This dictionary is only read.");
    }

    @Override
    public void clear() {
        throw new IllegalStateException("This dictionary is only read.");
    }
}
