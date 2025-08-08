package javasabr.rlib.common.util.array.impl;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import javasabr.rlib.common.util.array.Array;
import javasabr.rlib.common.util.array.ArrayComparator;
import javasabr.rlib.common.util.array.ReadOnlyArray;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The read only version of the {@link FastArray}.
 *
 * @param <E> the element's type.
 * @author JavaSaBr
 */
@NullMarked
public final class ReadOnlyFastArray<E> extends FastArray<E> implements ReadOnlyArray<E> {

  public ReadOnlyFastArray(E[] array) {
    super(array);
  }

  @Override
  public void apply(Function<? super E, ? extends E> function) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public boolean add(E object) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public boolean addAll(Collection<? extends E> collection) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public boolean fastRemove(Object object) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public E remove(int index) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public boolean addAll(Array<? extends E> elements) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public boolean addAll(E[] elements) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public E fastRemove(int index) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public boolean unsafeAdd(E object) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public E unsafeGet(int index) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public void unsafeSet(int index, E element) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public void replace(int index, E element) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public void clear() {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public Array<E> sort(ArrayComparator<E> comparator) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public boolean remove(@Nullable Object object) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public boolean removeAll(Array<?> target) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public boolean removeAll(Collection<?> target) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public boolean retainAll(Array<?> target) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public boolean retainAll(Collection<?> target) {
    throw new IllegalStateException("This array is read only.");
  }

  @Override
  public boolean removeIf(Predicate<? super E> filter) {
    throw new IllegalStateException("This array is read only.");
  }
}
