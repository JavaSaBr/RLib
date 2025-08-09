package javasabr.rlib.common.util.array.impl;

import javasabr.rlib.common.concurrent.lock.LockFactory;
import javasabr.rlib.common.util.array.Array;
import javasabr.rlib.common.util.array.ConcurrentArray;
import org.jspecify.annotations.NullMarked;

/**
 * The concurrent implementation of the array without duplications using {@link LockFactory#newAtomicARSWLock()} for
 * {@link ConcurrentArray#readLock()} and {@link ConcurrentArray#writeLock()}.
 *
 * @param <E> the array's element type.
 * @author JavaSaBr
 */
@NullMarked
public class ConcurrentAtomicARSWLockArraySet<E> extends ConcurrentAtomicARSWLockArray<E> {

  private static final long serialVersionUID = -3394386864246350866L;

  public ConcurrentAtomicARSWLockArraySet(Class<E> type) {
    super(type);
  }

  public ConcurrentAtomicARSWLockArraySet(Class<E> type, int size) {
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
