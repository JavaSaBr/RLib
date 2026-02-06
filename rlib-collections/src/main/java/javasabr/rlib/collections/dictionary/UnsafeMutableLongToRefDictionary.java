package javasabr.rlib.collections.dictionary;

/**
 * An unsafe mutable view of a long-to-reference dictionary providing direct access to internal entries.
 *
 * @param <V> the type of values
 * @param <E> the type of entries
 * @since 10.0.0
 */
public interface UnsafeMutableLongToRefDictionary<V, E extends LongToRefEntry<V>>
    extends MutableLongToRefDictionary<V>, UnsafeLongToRefDictionary<V, E> {

}
