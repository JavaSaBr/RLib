package javasabr.rlib.common.util.dictionary;

import javasabr.rlib.common.concurrent.atomic.ReusableAtomicInteger;
import javasabr.rlib.common.util.ArrayUtils;
import org.jspecify.annotations.NullMarked;

/**
 * The base implementation of the {@link ConcurrentObjectDictionary}.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
@NullMarked
public abstract class AbstractConcurrentObjectDictionary<K, V> extends AbstractObjectDictionary<K, V> implements
    ConcurrentObjectDictionary<K, V> {

  private final ReusableAtomicInteger size;

  private volatile ObjectEntry<K, V>[] entries;

  private volatile int threshold;

  protected AbstractConcurrentObjectDictionary() {
    this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
  }

  protected AbstractConcurrentObjectDictionary(float loadFactor) {
    this(loadFactor, DEFAULT_INITIAL_CAPACITY);
  }

  protected AbstractConcurrentObjectDictionary(int initCapacity) {
    this(DEFAULT_LOAD_FACTOR, initCapacity);
  }

  protected AbstractConcurrentObjectDictionary(float loadFactor, int initCapacity) {
    super(loadFactor, initCapacity);
    this.entries = ArrayUtils.create(getEntryType(), initCapacity);
    this.size = new ReusableAtomicInteger(0);
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
  public int getThreshold() {
    return threshold;
  }

  @Override
  protected void setSize(int size) {
    this.size.set(size);
  }

  @Override
  public void setThreshold(int threshold) {
    this.threshold = threshold;
  }

  @Override
  protected int decrementSizeAndGet() {
    return size.decrementAndGet();
  }

  @Override
  protected int incrementSizeAndGet() {
    return size.incrementAndGet();
  }

  @Override
  public final int size() {
    return size.get();
  }
}
