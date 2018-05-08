package com.ss.rlib.common.util.dictionary;

/**
 * The interface with methods for supporting threadsafe for the {@link ObjectDictionary}.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public interface ConcurrentObjectDictionary<K, V> extends ObjectDictionary<K, V>, ConcurrentDictionary<K, V> {

}
