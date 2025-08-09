package javasabr.rlib.common.util.dictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The iterator to iterate {@link IntegerDictionary}.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
@NullMarked
public class IntegerDictionaryIterator<V> implements Iterator<V> {

  /**
   * The dictionary.
   */
  private final UnsafeIntegerDictionary<V> dictionary;

  /**
   * The next entry.
   */
  private @Nullable IntegerEntry<V> next;

  /**
   * The current entry.
   */
  private @Nullable IntegerEntry<V> current;

  /**
   * The current index.
   */
  private int index;

  public IntegerDictionaryIterator(UnsafeIntegerDictionary<V> dictionary) {
    this.dictionary = dictionary;

    if (!dictionary.isEmpty()) {
      IntegerEntry<V>[] entries = dictionary.entries();
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
  private IntegerEntry<V> nextEntry() {

    IntegerEntry<V>[] content = dictionary.entries();
    IntegerEntry<V> entry = next;

    if (entry == null) {
      throw new NoSuchElementException();
    }

    if ((next = entry.getNext()) == null) {
      while (index < content.length && (next = content[index++]) == null);
    }

    current = entry;

    return entry;
  }

  @Override
  public void remove() {

    if (current == null) {
      throw new IllegalStateException();
    }

    int key = current.getKey();

    current = null;

    dictionary.removeEntryForKey(key);
  }
}