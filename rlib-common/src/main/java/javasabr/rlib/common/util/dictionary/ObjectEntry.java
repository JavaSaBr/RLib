package javasabr.rlib.common.util.dictionary;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The entry of {@link ObjectDictionary}.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
@NullMarked
public class ObjectEntry<K, V> implements Entry<ObjectEntry<K, V>, V> {

  /**
   * The next entry.
   */
  @Setter
  @Getter
  private @Nullable ObjectEntry<K, V> next;

  /**
   * The key of this entry.
   */
  @Getter
  private @Nullable K key;

  /**
   * The value of this entry.
   */
  @Getter
  private @Nullable V value;

  /**
   * The hash of the key.
   */
  @Getter
  private int hash;

  @Override
  public boolean equals(@Nullable Object object) {

    if (object == null || object.getClass() != ObjectEntry.class) {
      return false;
    }

    var entry = (ObjectEntry<?, ?>) object;
    var firstKey = getKey();
    var secondKey = entry.getKey();

    if (!Objects.equals(firstKey, secondKey)) {
      return false;
    }

    var firstValue = getValue();
    var secondValue = entry.getValue();

    return Objects.equals(firstValue, secondValue);
  }

  @Override
  public void free() {
    key = null;
    value = null;
    next = null;
    hash = 0;
  }

  @Override
  public final int hashCode() {
    return Objects.hashCode(key) ^ Objects.hashCode(value);
  }

  /**
   * Set all fields of this entry.
   *
   * @param hash the hash.
   * @param key the key.
   * @param value the value.
   * @param next the next entry.
   */
  public void set(int hash, K key, V value, @Nullable ObjectEntry<K, V> next) {
    this.value = value;
    this.next = next;
    this.key = key;
    this.hash = hash;
  }

  @Override
  @SuppressWarnings("ConstantConditions")
  public V setValue(V value) {
    var old = getValue();
    this.value = value;
    return old;
  }

  @Override
  public final String toString() {
    return "Entry : " + key + " = " + value;
  }
}
