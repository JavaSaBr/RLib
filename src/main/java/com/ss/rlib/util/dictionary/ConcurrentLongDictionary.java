package com.ss.rlib.util.dictionary;

/**
 * The interface with methods for supporting threadsafe for the {@link LongDictionary}.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public interface ConcurrentLongDictionary<V> extends LongDictionary<V>, ConcurrentDictionary<LongKey, V> {
}
