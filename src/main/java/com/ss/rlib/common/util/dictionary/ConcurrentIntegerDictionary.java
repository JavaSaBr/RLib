package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;

/**
 * The interface with methods for supporting threadsafe for the {@link IntegerDictionary}.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public interface ConcurrentIntegerDictionary<V> extends IntegerDictionary<V>, ConcurrentDictionary<IntKey, V> {

    /**
     * Create a new concurrent integer dictionary for the value's type.
     *
     * @param valueType the value's type.
     * @param <T>       the value's type.
     * @return the new concurrent integer dictionary.
     */
    static <T> @NotNull ConcurrentIntegerDictionary<T> ofType(@NotNull Class<? super T> valueType) {
        return DictionaryFactory.newConcurrentAtomicIntegerDictionary();
    }
}
