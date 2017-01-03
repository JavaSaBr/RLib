package rlib.util.dictionary;

/**
 * The interface with methods for supporting threadsafe for the {@link LongDictionary}.
 *
 * @author JavaSaBr
 */
public interface ConcurrentLongDictionary<V> extends LongDictionary<V>, ConcurrentDictionary<LongKey, V> {
}
