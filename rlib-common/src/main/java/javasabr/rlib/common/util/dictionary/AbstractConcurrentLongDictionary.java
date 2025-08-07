package javasabr.rlib.common.util.dictionary;

import javasabr.rlib.common.concurrent.atomic.ReusableAtomicInteger;
import javasabr.rlib.common.util.ArrayUtils;
import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of the {@link ConcurrentLongDictionary}.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public abstract class AbstractConcurrentLongDictionary<V> extends AbstractLongDictionary<V> implements
    ConcurrentLongDictionary<V> {

  private final @NotNull ReusableAtomicInteger size;

  private volatile @NotNull LongEntry<V>[] entries;

  private volatile int threshold;

  protected AbstractConcurrentLongDictionary() {
    this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
  }

  protected AbstractConcurrentLongDictionary(float loadFactor) {
    this(loadFactor, DEFAULT_INITIAL_CAPACITY);
  }

  protected AbstractConcurrentLongDictionary(int initCapacity) {
    this(DEFAULT_LOAD_FACTOR, initCapacity);
  }

  protected AbstractConcurrentLongDictionary(float loadFactor, int initCapacity) {
    super(loadFactor, initCapacity);
    this.entries = ArrayUtils.create(getEntryType(), initCapacity);
    this.size = new ReusableAtomicInteger(0);
  }

  @Override
  protected void setEntries(@NotNull LongEntry<V>[] entries) {
    this.entries = entries;
  }

  @Override
  public LongEntry<V> @NotNull [] entries() {
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
