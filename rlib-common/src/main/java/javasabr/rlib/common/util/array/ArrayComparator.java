package javasabr.rlib.common.util.array;

import java.util.Comparator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The interface to implement a comparator for {@link Array}.
 *
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public interface ArrayComparator<T> extends Comparator<T> {

  @Override
  default int compare(@Nullable T first, @Nullable T second) {

    if (first == null) {
      return 1;
    } else if (second == null) {
      return -1;
    }

    return compareImpl(first, second);
  }

  /**
   * Compare the two objects.
   *
   * @param first the first.
   * @param second the second.
   * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
   * than the second.
   */
  int compareImpl(T first, T second);
}
