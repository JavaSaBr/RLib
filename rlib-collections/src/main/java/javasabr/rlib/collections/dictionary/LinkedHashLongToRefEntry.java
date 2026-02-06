package javasabr.rlib.collections.dictionary;

/**
 * A linked hash entry for long-to-reference dictionaries.
 *
 * @param <V> the type of values
 * @param <N> the type of the next entry
 * @since 10.0.0
 */
public interface LinkedHashLongToRefEntry<V, N extends LinkedHashLongToRefEntry<V, N>>
    extends LinkedEntry<N>, HashEntry, LongToRefEntry<V> {
}
