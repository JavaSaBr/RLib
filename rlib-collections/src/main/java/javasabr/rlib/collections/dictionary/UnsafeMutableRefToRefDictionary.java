package javasabr.rlib.collections.dictionary;

/**
 * An unsafe mutable view of a reference-to-reference dictionary providing direct access to internal entries.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 * @param <E> the type of entries
 * @since 10.0.0
 */
public interface UnsafeMutableRefToRefDictionary<K, V, E extends RefToRefEntry<K, V>>
    extends MutableRefToRefDictionary<K, V>, UnsafeRefToRefDictionary<K, V, E> {

}
