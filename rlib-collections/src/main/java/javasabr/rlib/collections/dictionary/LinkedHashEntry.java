package javasabr.rlib.collections.dictionary;

/**
 * A linked hash entry for reference-to-reference dictionaries.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 * @param <N> the type of the next entry
 * @since 10.0.0
 */
public interface LinkedHashEntry<K, V, N extends LinkedHashEntry<K, V, N>>
    extends LinkedEntry<N>, HashEntry, RefToRefEntry<K, V> {
}
