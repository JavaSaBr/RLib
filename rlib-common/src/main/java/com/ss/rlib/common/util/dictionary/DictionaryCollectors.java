package com.ss.rlib.common.util.dictionary;

import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collector.Characteristics;
import com.ss.rlib.common.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * The collectors to {@link Dictionary}
 *
 * @author JavaSaBr
 */
public final class DictionaryCollectors {

    static final Set<Characteristics> CH_ID =
        unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));

    /**
     * Simple implementation class for {@code Collector}.
     *
     * @param <T> the type of elements to be collected
     * @param <R> the type of the result
     */
    static class CollectorImpl<T, A, R> implements Collector<T, A, R> {

        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;

        CollectorImpl(
            @NotNull Supplier<A> supplier,
            @NotNull BiConsumer<A, T> accumulator,
            @NotNull BinaryOperator<A> combiner,
            @NotNull Function<A, R> finisher,
            @NotNull Set<Characteristics> characteristics
        ) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        CollectorImpl(
            @NotNull Supplier<A> supplier,
            @NotNull BiConsumer<A, T> accumulator,
            @NotNull BinaryOperator<A> combiner,
            @NotNull Set<Characteristics> characteristics
        ) {
            this(supplier, accumulator, combiner, a -> (R) a, characteristics);
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }
    }

    public static <T, K, U> @NotNull Collector<T, ?, ObjectDictionary<K, U>> toObjectDictionary(
        @NotNull Function<? super T, ? extends K> keyMapper,
        @NotNull Function<? super T, ? extends U> valueMapper
    ) {
        return new CollectorImpl<>(
            DictionaryFactory::newObjectDictionary,
            uniqKeysAccumulator(keyMapper, valueMapper),
            ObjectDictionary::append,
            CH_ID
        );
    }

    public static <T, U> @NotNull Collector<T, ?, LongDictionary<U>> toLongDictionary(
        @NotNull Function<? super T, Number> keyMapper,
        @NotNull Function<? super T, ? extends U> valueMapper
    ) {
        return new CollectorImpl<>(
            DictionaryFactory::newLongDictionary,
            uniqLongsAccumulator(keyMapper, valueMapper),
            LongDictionary::append,
            CH_ID
        );
    }

    /**
     * {@code BiConsumer<ObjectDictionary, T>} that accumulates (key, value) pairs
     * extracted from elements into the dictionary, throwing {@code IllegalStateException}
     * if duplicate keys are encountered.
     *
     * @param keyMapper a function that dictionaries an element into a key.
     * @param valueMapper a function that dictionaries an element into a value.
     * @param <T> type of elements.
     * @param <K> type of dictionaries keys.
     * @param <V> type of dictionaries values.
     * @return an accumulating consumer.
     */
    private static <T, K, V> BiConsumer<ObjectDictionary<K, V>, T> uniqKeysAccumulator(
        @NotNull Function<? super T, ? extends K> keyMapper,
        @NotNull Function<? super T, ? extends V> valueMapper
    ) {
        return (map, element) -> {

            K key = keyMapper.apply(element);
            V value = ObjectUtils.notNull(valueMapper.apply(element));
            V prev = map.put(key, value);

            if (prev != null) {
                throw duplicateKeyException(key, prev, value);
            }
        };
    }

    /**
     * {@code BiConsumer<LongDictionary, T>} that accumulates (key, value) pairs
     * extracted from elements into the dictionary, throwing {@code IllegalStateException}
     * if duplicate keys are encountered.
     *
     * @param keyMapper   a function that dictionaries an element into a key.
     * @param valueMapper a function that dictionaries an element into a value.
     * @param <T>         type of elements.
     * @param <V>         type of dictionaries values.
     * @return an accumulating consumer.
     */
    private static <T, V> BiConsumer<LongDictionary<V>, T> uniqLongsAccumulator(
        @NotNull Function<? super T, Number> keyMapper,
        @NotNull Function<? super T, ? extends V> valueMapper
    ) {
        return (map, element) -> {

            var key = keyMapper.apply(element);
            var value = ObjectUtils.notNull(valueMapper.apply(element));
            var prevValue = map.put(key.longValue(), value);

            if (prevValue != null) {
                throw duplicateKeyException(key, prevValue, value);
            }
        };
    }

    /**
     * Construct an {@code IllegalStateException} with appropriate message.
     *
     * @param k the duplicate key
     * @param u 1st value to be accumulated/merged
     * @param v 2nd value to be accumulated/merged
     */
    private static IllegalStateException duplicateKeyException(Object k, Object u, Object v) {
        return new IllegalStateException(
            String.format("Duplicate key %s (attempted merging values %s and %s)", k, u, v)
        );
    }

    private DictionaryCollectors() {
        throw new IllegalArgumentException();
    }
}
