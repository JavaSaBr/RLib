package rlib.util.dictionary;

/**
 * The interface with methods for supporting threadsafe for the {@link IntegerDictionary}.
 *
 * @author JavaSaBr
 */
public interface ConcurrentIntegerDictionary<V> extends IntegerDictionary<V>, ConcurrentDictionary<IntKey, V> {
}
