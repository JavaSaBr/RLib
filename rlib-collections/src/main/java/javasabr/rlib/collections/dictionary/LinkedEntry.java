package javasabr.rlib.collections.dictionary;

import org.jspecify.annotations.Nullable;

/**
 * An entry that maintains a link to the next entry in a chain.
 *
 * @param <N> the type of the next entry
 * @since 10.0.0
 */
public interface LinkedEntry<N>  {

  /**
   * Returns the next entry in the chain.
   *
   * @return the next entry or null
   * @since 10.0.0
   */
  @Nullable
  N next();

  /**
   * Sets the next entry in the chain.
   *
   * @param next the next entry or null
   * @since 10.0.0
   */
  void next(@Nullable N next);
}
