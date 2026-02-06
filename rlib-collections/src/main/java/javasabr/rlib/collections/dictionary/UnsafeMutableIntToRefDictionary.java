package javasabr.rlib.collections.dictionary;

/**
 * An unsafe mutable view of an int-to-reference dictionary providing direct access to internal entries.
 *
 * @param <V> the type of values
 * @param <E> the type of entries
 * @since 10.0.0
 */
public interface UnsafeMutableIntToRefDictionary<V, E extends IntToRefEntry<V>>
    extends MutableIntToRefDictionary<V>, UnsafeIntToRefDictionary<V, E> {

}
