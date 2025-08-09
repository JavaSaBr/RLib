package javasabr.rlib.common.util.dictionary;

import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The fast read-only implementation of {@link ObjectDictionary} without threadsafe supporting.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
@NullMarked
@NoArgsConstructor
public class ReadOnlyFastObjectDictionary<K, V> extends FastObjectDictionary<K, V> {

  public ReadOnlyFastObjectDictionary(Object... values) {

    if (values.length < 2 || values.length % 2 != 0) {
      throw new IllegalArgumentException("Incorrect argument's count.");
    }

    for (int i = 0, length = values.length - 2; i <= length; i += 2) {
      //noinspection unchecked
      super.put((K) values[i], (V) values[i + 1]);
    }
  }

  @Override
  public void put(Dictionary<K, V> dictionary) {
    throw new IllegalStateException("Dictionary is readonly.");
  }

  @Override
  public @Nullable V put(K key, @Nullable V value) {
    throw new IllegalStateException("Dictionary is readonly.");
  }

  @Override
  public @Nullable V remove(K key) {
    throw new IllegalStateException("Dictionary is readonly.");
  }

  @Override
  public @Nullable ObjectEntry<K, V> removeEntryForKey(K key) {
    throw new IllegalStateException("Dictionary is readonly.");
  }

  @Override
  public void clear() {
    throw new IllegalStateException("Dictionary is readonly.");
  }
}
