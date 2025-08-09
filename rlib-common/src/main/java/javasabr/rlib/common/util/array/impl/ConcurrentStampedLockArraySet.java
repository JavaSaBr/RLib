package javasabr.rlib.common.util.array.impl;

import java.util.concurrent.locks.StampedLock;
import javasabr.rlib.common.util.array.Array;
import javasabr.rlib.common.util.array.ConcurrentArray;
import org.jspecify.annotations.NullMarked;

/**
 * The concurrent implementation of the array without duplications using {@link StampedLock} for
 * {@link ConcurrentArray#readLock()}* and {@link ConcurrentArray#writeLock()}.
 *
 * @param <E> the array's element type.
 * @author JavaSaBr
 */
@NullMarked
public class ConcurrentStampedLockArraySet<E> extends ConcurrentStampedLockArray<E> {

  private static final long serialVersionUID = -6291504312637658721L;

  public ConcurrentStampedLockArraySet(Class<? super E> type) {
    this(type, 10);
  }

  public ConcurrentStampedLockArraySet(Class<? super E> type, int size) {
    super(type, size);
  }

  @Override
  public boolean add(E element) {
    return !contains(element) && super.add(element);
  }

  @Override
  protected void processAdd(Array<? extends E> elements, int selfSize, int targetSize) {
    var array = elements.array();
    for (int i = 0, length = elements.size(); i < length; i++) {
      E element = array[i];
      if (!contains(element)) {
        unsafeAdd(element);
      }
    }
  }

  @Override
  protected void processAdd(E[] elements, int selfSize, int targetSize) {
    for (E element : elements) {
      if (!contains(element)) {
        unsafeAdd(element);
      }
    }
  }
}
