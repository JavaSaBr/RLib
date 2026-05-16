package javasabr.rlib.collections.array.impl;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import javasabr.rlib.collections.array.ReversedArgsArrayIterationFunctions;
import javasabr.rlib.collections.array.UnsafeArray;
import javasabr.rlib.functions.TriConsumer;
import org.jspecify.annotations.Nullable;

public record DefaultReversedArgsArrayIterationFunctions<E>(UnsafeArray<E> array) implements
    ReversedArgsArrayIterationFunctions<E> {

  @Override
  public <T> @Nullable E findAny(T arg1, BiPredicate<T, ? super E> filter) {
    @Nullable E[] wrapped = array.wrapped();
    int size = array.size();
    for (int i = 0; i < size; i++) {
      E element = wrapped[i];
      //noinspection DataFlowIssue
      if (filter.test(arg1, element)) {
        return element;
      }
    }
    return null;
  }

  @Override
  public <T> ReversedArgsArrayIterationFunctions<E> forEach(T arg1, BiConsumer<T, ? super E> consumer) {
    @Nullable E[] wrapped = array.wrapped();
    int size = array.size();
    for (int i = 0; i < size; i++) {
      //noinspection DataFlowIssue
      consumer.accept(arg1, wrapped[i]);
    }
    return this;
  }

  @Override
  public <F, S> ReversedArgsArrayIterationFunctions<E> forEach(F arg1, S arg2, TriConsumer<F, S, ? super E> consumer) {
    @Nullable E[] wrapped = array.wrapped();
    int size = array.size();
    for (int i = 0; i < size; i++) {
      //noinspection DataFlowIssue
      consumer.accept(arg1, arg2, wrapped[i]);
    }
    return this;
  }

  @Override
  public <T> boolean anyMatch(T arg1, BiPredicate<T, ? super E> filter) {
    return findAny(arg1, filter) != null;
  }
}
