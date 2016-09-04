package rlib.util.dictionary;

/**
 * The interface with methods for supporting threadsafe for the {@link ObjectDictionary}.
 *
 * @author JavaSaBr
 */
public interface ConcurrentObjectDictionary<K, V> extends ObjectDictionary<K, V>, ConcurrentDictionary<K, V> {

}
