package javasabr.rlib.common.util.array.impl;

import javasabr.rlib.common.util.array.Array;
import org.jspecify.annotations.NullMarked;

/**
 * The fast implementation of the array with checking on duplicates. This array is not threadsafe.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public class FastArraySet<E> extends FastArray<E> {

  private static final long serialVersionUID = 1L;

  public FastArraySet(Class<? super E> type) {
    super(type);
  }

  public FastArraySet(Class<? super E> type, int size) {
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
