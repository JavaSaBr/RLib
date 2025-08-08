package javasabr.rlib.common.util.array;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import org.jspecify.annotations.NullMarked;

/**
 * The interface to mark an array as read only array.
 *
 * @param <E> the element's type.
 * @author JavaSaBr
 */
@NullMarked
public interface ReadOnlyArray<E> extends Array<E> {

  @Override
  @Deprecated
  void apply(Function<? super E, ? extends E> function);

  @Override
  @Deprecated
  boolean add(E e);

  @Override
  @Deprecated
  boolean addAll(E[] array);

  @Override
  @Deprecated
  boolean addAll(Array<? extends E> array);

  @Override
  @Deprecated
  boolean addAll(Collection<? extends E> c);

  @Override
  @Deprecated
  boolean fastRemove(Object object);

  @Override
  @Deprecated
  E remove(int index);

  @Override
  @Deprecated
  boolean removeIf(Predicate<? super E> filter);

  @Override
  @Deprecated
  boolean retainAll(Collection<?> target);

  @Override
  @Deprecated
  boolean retainAll(Array<?> target);

  @Override
  @Deprecated
  boolean removeAll(Collection<?> target);

  @Override
  @Deprecated
  boolean removeAll(Array<?> target);

  @Override
  @Deprecated
  boolean remove(Object object);

  @Override
  @Deprecated
  void clear();

  @Override
  @Deprecated
  Array<E> sort(ArrayComparator<E> comparator);
}
