package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;

/**
 * The interface with methods for supporting threadsafe for the {@link LongDictionary}.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public interface ConcurrentLongDictionary<V> extends LongDictionary<V>, ConcurrentDictionary<LongKey, V> {

    /**
     * Create a new concurrent long dictionary for the value's type.
     *
     * @param valueType the value's type.
     * @param <T>       the value's type.
     * @return the new concurrent long dictionary.
     */
    static <T> @NotNull ConcurrentLongDictionary<T> ofType(@NotNull Class<? super T> valueType) {
        return DictionaryFactory.newConcurrentAtomicLongDictionary();
    }
}
