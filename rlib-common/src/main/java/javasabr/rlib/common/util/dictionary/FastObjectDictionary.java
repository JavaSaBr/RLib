package javasabr.rlib.common.util.dictionary;

import javasabr.rlib.common.util.ArrayUtils;
import org.jspecify.annotations.NullMarked;

/**
 * The fast implementation of {@link ObjectDictionary} without threadsafe supporting.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
@NullMarked
public class FastObjectDictionary<K, V> extends AbstractObjectDictionary<K, V> {

  private ObjectEntry<K, V>[] entries;

  private int threshold;
  private int size;

  protected FastObjectDictionary() {
    this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
  }

  protected FastObjectDictionary(float loadFactor) {
    this(loadFactor, DEFAULT_INITIAL_CAPACITY);
  }

  protected FastObjectDictionary(int initCapacity) {
    this(DEFAULT_LOAD_FACTOR, initCapacity);
  }

  protected FastObjectDictionary(float loadFactor, int initCapacity) {
    super(loadFactor, initCapacity);
    this.entries = ArrayUtils.create(getEntryType(), initCapacity);
  }

  @Override
  public void setSize(int size) {
    this.size = size;
  }

  @Override
  public void setEntries(ObjectEntry<K, V>[] entries) {
    this.entries = entries;
  }

  @Override
  public ObjectEntry<K, V> [] entries() {
    return entries;
  }

  @Override
  public void setThreshold(int threshold) {
    this.threshold = threshold;
  }

  @Override
  public int getThreshold() {
    return threshold;
  }

  @Override
  protected int decrementSizeAndGet() {
    return --size;
  }

  @Override
  protected int incrementSizeAndGet() {
    return ++size;
  }

  @Override
  public int size() {
    return size;
  }
}
