package com.ss.rlib.util.dictionary;

/**
 * The interface with methods for supporting threadsafe for the {@link IntegerDictionary}.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public interface ConcurrentIntegerDictionary<V> extends IntegerDictionary<V>, ConcurrentDictionary<IntKey, V> {
}
