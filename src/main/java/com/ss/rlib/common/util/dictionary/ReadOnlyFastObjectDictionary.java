package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The fast read-only implementation of {@link ObjectDictionary} without threadsafe supporting.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public class ReadOnlyFastObjectDictionary<K, V> extends FastObjectDictionary<K, V> {

    public ReadOnlyFastObjectDictionary() {

    }

    public ReadOnlyFastObjectDictionary(Object... values) {

        if (values.length < 2 || values.length % 2 != 0) {
            throw new IllegalArgumentException("Incorrect argument's count.");
        }

        for (int i = 0, length = values.length - 2; i <= length; i += 2) {
            super.put((K) values[i], (V) values[i + 1]);
        }
    }

    @Override
    public void put(@NotNull Dictionary<K, V> dictionary) {
        throw new IllegalStateException("This dictionary is only read.");
    }

    @Override
    public @Nullable V put(@NotNull K key, @Nullable V value) {
        throw new IllegalStateException("This dictionary is only read.");
    }

    @Override
    public @Nullable V remove(@NotNull K key) {
        throw new IllegalStateException("This dictionary is only read.");
    }

    @Override
    public void clear() {
        throw new IllegalStateException("This dictionary is only read.");
    }
}
