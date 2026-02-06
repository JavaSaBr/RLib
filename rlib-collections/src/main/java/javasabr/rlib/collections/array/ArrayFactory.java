package javasabr.rlib.collections.array;

import javasabr.rlib.collections.array.impl.CopyOnWriteMutableArray;
import javasabr.rlib.collections.array.impl.DefaultMutableArray;
import javasabr.rlib.collections.array.impl.DefaultMutableIntArray;
import javasabr.rlib.collections.array.impl.DefaultMutableLongArray;
import javasabr.rlib.collections.array.impl.StampedLockBasedArray;
import javasabr.rlib.common.util.ClassUtils;
import lombok.experimental.UtilityClass;

/**
 * Factory for creating various array implementations.
 *
 * @since 10.0.0
 */
@UtilityClass
public class ArrayFactory {

  /**
   * Creates a new mutable array of the specified type.
   *
   * @param <E> the type of elements
   * @param type the component type of the array
   * @return a new mutable array
   * @since 10.0.0
   */
  public static <E> MutableArray<E> mutableArray(Class<? super E> type) {
    return new DefaultMutableArray<>(ClassUtils.unsafeCast(type));
  }

  /**
   * Creates a new mutable int array.
   *
   * @return a new mutable int array
   * @since 10.0.0
   */
  public static MutableIntArray mutableIntArray() {
    return new DefaultMutableIntArray();
  }

  /**
   * Creates a new mutable long array.
   *
   * @return a new mutable long array
   * @since 10.0.0
   */
  public static MutableLongArray mutableLongArray() {
    return new DefaultMutableLongArray();
  }

  /**
   * Creates a new mutable array of the specified type with initial capacity.
   *
   * @param <E> the type of elements
   * @param type the component type of the array
   * @param capacity the initial capacity
   * @return a new mutable array
   * @since 10.0.0
   */
  public static <E> MutableArray<E> mutableArray(Class<? super E> type, int capacity) {
    return new DefaultMutableArray<>(ClassUtils.unsafeCast(type), capacity);
  }


  /**
   * Creates a new copy-on-modify array of the specified type.
   *
   * @param <E> the type of elements
   * @param type the component type of the array
   * @return a new copy-on-modify array
   * @since 10.0.0
   */
  public static <E> MutableArray<E> copyOnModifyArray(Class<? super E> type) {
    return new CopyOnWriteMutableArray<>(type);
  }

  /**
   * Creates a new thread-safe array backed by a stamped lock.
   *
   * @param <E> the type of elements
   * @param type the component type of the array
   * @return a new lockable array
   * @since 10.0.0
   */
  public static <E> LockableArray<E> stampedLockBasedArray(Class<? super E> type) {
    return new StampedLockBasedArray<>(type);
  }
}
