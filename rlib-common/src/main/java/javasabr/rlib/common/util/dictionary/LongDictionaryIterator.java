package javasabr.rlib.common.util.dictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The iterator to iterate {@link LongDictionary}.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
@NullMarked
public class LongDictionaryIterator<V> implements Iterator<V> {

  /**
   * The dictionary.
   */
  private final UnsafeLongDictionary<V> dictionary;

  /**
   * The next entry.
   */
  private @Nullable LongEntry<V> next;

  /**
   * The current entry.
   */
  private @Nullable LongEntry<V> current;

  /**
   * The current index.
   */
  private int index;

  public LongDictionaryIterator(UnsafeLongDictionary<V> dictionary) {
    this.dictionary = dictionary;

    if (!dictionary.isEmpty()) {
      LongEntry<V>[] entries = dictionary.entries();
      while (index < entries.length && (next = entries[index++]) == null);
    }
  }

  @Override
  public boolean hasNext() {
    return next != null;
  }

  @Override
  public V next() {
    return nextEntry().getValue();
  }

  /**
   * Get the next entry.
   *
   * @return the next entry.
   */
  private LongEntry<V> nextEntry() {

    LongEntry<V>[] entries = dictionary.entries();
    LongEntry<V> entry = next;

    if (entry == null) {
      throw new NoSuchElementException();
    }

    if ((next = entry.getNext()) == null) {
      while (index < entries.length && (next = entries[index++]) == null);
    }

    current = entry;

    return entry;
  }

  @Override
  public void remove() {

    if (current == null) {
      throw new IllegalStateException();
    }

    long key = current.getKey();

    current = null;

    dictionary.removeEntryForKey(key);
  }
}
