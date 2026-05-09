package javasabr.rlib.collections.array.impl;

import java.util.stream.Stream;
import javasabr.rlib.collections.array.UnsafeArray;
import javasabr.rlib.common.util.ArrayUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class ImmutableArray<E> extends AbstractArray<E> implements UnsafeArray<E> {

  public static <E> ImmutableArray<E> trustWrap(E[] array) {
    return new ImmutableArray<>(array);
  }

  E[] wrapped;

  private ImmutableArray(E[] wrapped) {
    super((Class<? super E>) wrapped.getClass().getComponentType());
    this.wrapped = wrapped;
  }

  public ImmutableArray(Class<? super E> type, E e1) {
    super(type);
    E[] array = ArrayUtils.create(type, 1);
    array[0] = e1;
    this.wrapped = array;
  }

  public ImmutableArray(Class<? super E> type, E e1, E e2) {
    super(type);
    E[] array = ArrayUtils.create(type, 2);
    array[0] = e1;
    array[1] = e2;
    this.wrapped = array;
  }

  public ImmutableArray(Class<? super E> type, E e1, E e2, E e3) {
    super(type);
    E[] array = ArrayUtils.create(type, 3);
    array[0] = e1;
    array[1] = e2;
    array[2] = e3;
    this.wrapped = array;
  }

  public ImmutableArray(Class<? super E> type, E e1, E e2, E e3, E e4) {
    super(type);
    E[] array = ArrayUtils.create(type, 4);
    array[0] = e1;
    array[1] = e2;
    array[2] = e3;
    array[3] = e4;
    this.wrapped = array;
  }

  public ImmutableArray(Class<? super E> type, E e1, E e2, E e3, E e4, E e5) {
    super(type);
    E[] array = ArrayUtils.create(type, 5);
    array[0] = e1;
    array[1] = e2;
    array[2] = e3;
    array[3] = e4;
    array[4] = e5;
    this.wrapped = array;
  }

  public ImmutableArray(Class<? super E> type, E e1, E e2, E e3, E e4, E e5, E e6) {
    super(type);
    E[] array = ArrayUtils.create(type, 6);
    array[0] = e1;
    array[1] = e2;
    array[2] = e3;
    array[3] = e4;
    array[4] = e5;
    array[5] = e6;
    this.wrapped = array;
  }

  @SafeVarargs
  public ImmutableArray(Class<? super E> type, E... elements) {
    super(type);
    if (ArrayUtils.resolveComponentType(elements) == type) {
      this.wrapped = elements;
      return;
    }
    E[] array = ArrayUtils.create(type, elements.length);
    System.arraycopy(elements, 0, array, 0, elements.length);
    this.wrapped = array;
  }

  @Override
  public E unsafeGet(int index) {
    return wrapped[index];
  }

  @Override
  public Stream<E> stream() {
    return Stream.of(wrapped);
  }

  @Override
  public int size() {
    return wrapped.length;
  }

  @Override
  public boolean isEmpty() {
    return wrapped.length < 1;
  }

  @Override
  public E get(int index) {

    if (index < 0 || index >= size()) {
      throw new IndexOutOfBoundsException();
    }

    return wrapped[index];
  }

  @Override
  public @Nullable E[] wrapped() {
    return wrapped;
  }
}
