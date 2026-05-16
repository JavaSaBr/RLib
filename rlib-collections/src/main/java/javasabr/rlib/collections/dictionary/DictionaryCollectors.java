package javasabr.rlib.collections.dictionary;

import static java.util.Collections.unmodifiableSet;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import javasabr.rlib.common.util.ObjectUtils;
import lombok.experimental.UtilityClass;

/**
 * Provides {@link Collector} implementations for collecting stream elements into {@link RefToRefDictionary} instances.
 *
 * @since 10.0.0
 */
@UtilityClass
public class DictionaryCollectors {

  static final Set<Characteristics> CH_ID = unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));

  record CollectorImpl<T, A, R>(
      Supplier<A> supplier,
      BiConsumer<A, T> accumulator,
      BinaryOperator<A> combiner,
      Function<A, R> finisher,
      Set<Characteristics> characteristics) implements Collector<T, A, R> {
  
      CollectorImpl(
          Supplier<A> supplier,
          BiConsumer<A, T> accumulator,
          BinaryOperator<A> combiner,
          Set<Characteristics> characteristics) {
        this(supplier, accumulator, combiner, a -> (R) a, characteristics);
      }
    }

  /**
   * Returns a collector that accumulates elements into a dictionary using the element as the value.
   *
   * @param <T> the type of input elements
   * @param <K> the type of keys
   * @param <U> unused type parameter
   * @param keyMapper the function to extract keys
   * @return a collector that collects elements into a dictionary
   * @since 10.0.0
   */
  public static <T, K, U> Collector<T, MutableRefToRefDictionary<K, T>, RefToRefDictionary<K, T>> toRefToRefDictionary(
      Function<? super T, ? extends K> keyMapper) {
    return new CollectorImpl<>(
        DictionaryFactory::mutableRefToRefDictionary,
        uniqKeysAccumulator(keyMapper, Function.identity()),
        MutableRefToRefDictionary::append,
        MutableRefToRefDictionary::toReadOnly,
        CH_ID);
  }

  /**
   * Returns a collector that accumulates elements into a dictionary.
   *
   * @param <T> the type of input elements
   * @param <K> the type of keys
   * @param <U> the type of values
   * @param keyMapper the function to extract keys
   * @param valueMapper the function to extract values
   * @return a collector that collects elements into a dictionary
   * @since 10.0.0
   */
  public static <T, K, U> Collector<T, MutableRefToRefDictionary<K, U>, RefToRefDictionary<K, U>> toRefToRefDictionary(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends U> valueMapper) {
    return new CollectorImpl<>(
        DictionaryFactory::mutableRefToRefDictionary,
        uniqKeysAccumulator(keyMapper, valueMapper),
        MutableRefToRefDictionary::append,
        MutableRefToRefDictionary::toReadOnly,
        CH_ID);
  }

  private static <T, K, V> BiConsumer<MutableRefToRefDictionary<K, V>, T> uniqKeysAccumulator(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends V> valueMapper) {
    return (map, element) -> {

      K key = keyMapper.apply(element);
      V value = ObjectUtils.notNull(valueMapper.apply(element));
      V prev = map.put(key, value);

      if (prev != null) {
        throw duplicateKeyException(key, prev, value);
      }
    };
  }

  private static IllegalStateException duplicateKeyException(Object key, Object left, Object right) {
    return new IllegalStateException(
        String.format("Duplicate key %s (attempted merging values %s and %s)", key, left, right));
  }
}
