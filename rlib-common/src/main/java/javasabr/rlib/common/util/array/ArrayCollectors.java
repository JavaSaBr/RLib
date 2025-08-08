package javasabr.rlib.common.util.array;

import static java.util.Collections.unmodifiableSet;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import org.jspecify.annotations.NullMarked;

/**
 * The array collectors factory.
 *
 * @author JavaSaBr
 */
@NullMarked
public class ArrayCollectors {

  private static Set<Collector.Characteristics> CH_ID =
      unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

  private static Set<Collector.Characteristics> CH_ID_CONC = unmodifiableSet(EnumSet.of(
      Collector.Characteristics.IDENTITY_FINISH,
      Collector.Characteristics.CONCURRENT));

  /**
   * Get a collector to collect elements into an array.
   *
   * @param type the type of elements.
   * @param arrayFactory the array factory.
   * @param <T> the array element's type.
   * @param <A> the array's type.
   * @return the collector.
   */
  public static <T, A extends Array<T>> Collector<T, A, A> collector(
      Class<? super T> type,
      Function<Class<? super T>, A> arrayFactory) {
    return new Collector<>() {

      private final Supplier<A> supplier = () -> arrayFactory.apply(type);

      @Override
      public Supplier<A> supplier() {
        return supplier;
      }

      @Override
      public BiConsumer<A, T> accumulator() {
        return Collection::add;
      }

      @Override
      public BinaryOperator<A> combiner() {
        return (source, toAdd) -> {
          source.addAll(toAdd);
          return source;
        };
      }

      @Override
      public Function<A, A> finisher() {
        return array -> array;
      }

      @Override
      public Set<Characteristics> characteristics() {
        return CH_ID;
      }
    };
  }

  /**
   * Get a collector to collect elements in a thread safe array.
   *
   * @param type the type of elements.
   * @param arrayFactory the array factory.
   * @param <T> the array element's type.
   * @param <A> the array's type.
   * @return the collector.
   */
  public static <T, A extends ConcurrentArray<T>> Collector<T, A, A> concurrentCollector(
      Class<? super T> type,
      Function<Class<? super T>, A> arrayFactory) {
    return new Collector<>() {

      private final Supplier<A> supplier = () -> arrayFactory.apply(type);

      @Override
      public Supplier<A> supplier() {
        return supplier;
      }

      @Override
      public BiConsumer<A, T> accumulator() {
        return Collection::add;
      }

      @Override
      public BinaryOperator<A> combiner() {
        return (source, toAdd) -> {
          source.runInWriteLock(toAdd, Array::addAll);
          return source;
        };
      }

      @Override
      public Function<A, A> finisher() {
        return array -> array;
      }

      @Override
      public Set<Characteristics> characteristics() {
        return CH_ID_CONC;
      }
    };
  }

  /**
   * Get a collector to collect elements into an array.
   *
   * @param type the type of elements.
   * @param <T> the array element's type.
   * @return the collector.
   */
  public static <T> Collector<T, Array<T>, Array<T>> toArray(Class<? super T> type) {
    return collector(type, ArrayFactory::newArray);
  }

  /**
   * Get a collector to collect elements in a thread safe array.
   *
   * @param type the type of elements.
   * @param <T> the array element's type.
   * @return the collector.
   */
  public static <T> Collector<T, ConcurrentArray<T>, ConcurrentArray<T>> toConcurrentArray(
      Class<? super T> type) {
    return concurrentCollector(type, ArrayFactory::newConcurrentStampedLockArray);
  }
}
