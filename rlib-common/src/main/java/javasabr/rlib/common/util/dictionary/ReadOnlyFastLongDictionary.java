package javasabr.rlib.common.util.dictionary;

import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The fast read-only implementation of {@link LongDictionary} without threadsafe supporting.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
@NullMarked
@NoArgsConstructor
public class ReadOnlyFastLongDictionary<V> extends FastLongDictionary<V> {

  @Override
  public void put(Dictionary<LongKey, V> dictionary) {
    throw new IllegalStateException("Dictionary is only read.");
  }

  @Override
  public @Nullable V put(long key, @Nullable V value) {
    throw new IllegalStateException("Dictionary is only read.");
  }

  @Override
  public @Nullable V remove(long key) {
    throw new IllegalStateException("Dictionary is only read.");
  }

  @Override
  public @Nullable LongEntry<V> removeEntryForKey(long key) {
    throw new IllegalStateException("Dictionary is only read.");
  }

  @Override
  public void clear() {
    throw new IllegalStateException("Dictionary is only read.");
  }
}
