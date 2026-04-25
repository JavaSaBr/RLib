package javasabr.rlib.collections.array.impl;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import javasabr.rlib.collections.array.ArrayIterationFunctions;
import javasabr.rlib.collections.array.ReversedArgsArrayIterationFunctions;
import javasabr.rlib.collections.array.UnsafeArray;
import javasabr.rlib.functions.BiObjLongConsumer;
import javasabr.rlib.functions.ObjIntPredicate;
import javasabr.rlib.functions.TriConsumer;
import org.jspecify.annotations.Nullable;

public record DefaultArrayIterationFunctions<E>(
    UnsafeArray<E> array,
    ReversedArgsArrayIterationFunctions<E> reversedArgs) implements ArrayIterationFunctions<E> {

  @Override
  public <T> @Nullable E findAny(T arg1, BiPredicate<? super E, T> filter) {
    @Nullable E[] wrapped = array.wrapped();
    int size = array.size();

    for (int i = 0; i < size; i++) {
      E element = wrapped[i];
      if (filter.test(element, arg1)) {
        return element;
      }
    }
    return null;
  }

  @Nullable
  @Override
  public E findAny(int arg1, ObjIntPredicate<? super E> filter) {
    @Nullable E[] wrapped = array.wrapped();
    int size = array.size();

    for (int i = 0; i < size; i++) {
      E element = wrapped[i];
      if (filter.test(element, arg1)) {
        return element;
      }
    }
    return null;
  }

  @Override
  public <T> ArrayIterationFunctions<E> forEach(T arg1, BiConsumer<? super E, T> consumer) {
    @Nullable E[] wrapped = array.wrapped();
    int size = array.size();
    for (int i = 0; i < size; i++) {
      consumer.accept(wrapped[i], arg1);
    }
    return this;
  }

  @Override
  public <F, S> ArrayIterationFunctions<E> forEach(F arg1, S arg2, TriConsumer<? super E, F, S> consumer) {
    @Nullable E[] wrapped = array.wrapped();
    int size = array.size();
    for (int i = 0; i < size; i++) {
      consumer.accept(wrapped[i], arg1, arg2);
    }
    return this;
  }

  @Override
  public <F> ArrayIterationFunctions<E> forEach(F arg1, long arg2, BiObjLongConsumer<? super E, F> consumer) {
    @Nullable E[] wrapped = array.wrapped();
    int size = array.size();
    for (int i = 0; i < size; i++) {
      consumer.accept(wrapped[i], arg1, arg2);
    }
    return this;
  }

  @Override
  public <T> boolean anyMatch(T arg1, BiPredicate<? super E, T> filter) {
    return findAny(arg1, filter) != null;
  }
}
