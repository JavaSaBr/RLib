package javasabr.rlib.collections.array;

import java.util.Collection;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class ArrayBuilder<E> {

  MutableArray<E> elements;

  public ArrayBuilder(Class<? super E> type) {
    this.elements = ArrayFactory.mutableArray(type);
  }

  public ArrayBuilder<E> add(E element) {
    elements.add(element);
    return this;
  }

  @SafeVarargs
  public final ArrayBuilder<E> add(E... other) {
    elements.addAll(other);
    return this;
  }

  public ArrayBuilder<E> add(Collection<E> other) {
    elements.addAll(other);
    return this;
  }

  public ArrayBuilder<E> add(Array<E> other) {
    elements.addAll(other);
    return this;
  }

  public Array<E> build() {
    return Array.copyOf(elements);
  }
}
