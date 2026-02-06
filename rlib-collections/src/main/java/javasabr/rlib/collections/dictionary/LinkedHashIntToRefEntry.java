package javasabr.rlib.collections.dictionary;

/**
 * A linked hash entry for int-to-reference dictionaries.
 *
 * @param <V> the type of values
 * @param <N> the type of the next entry
 * @since 10.0.0
 */
public interface LinkedHashIntToRefEntry<V, N extends LinkedHashIntToRefEntry<V, N>>
    extends LinkedEntry<N>, HashEntry, IntToRefEntry<V> {
}
