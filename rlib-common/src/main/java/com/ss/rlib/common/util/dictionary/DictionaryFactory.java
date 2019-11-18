package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.StampedLock;

/**
 * The factory for creating new {@link Dictionary}.
 *
 * @author JavaSaBr
 */
public final class DictionaryFactory {

    public static final ObjectDictionary<?, ?> EMPTY_OD = new ReadOnlyFastObjectDictionary<>();
    public static final LongDictionary<?> EMPTY_LD = new ReadOnlyFastLongDictionary<>();

    public static <V> @NotNull ConcurrentIntegerDictionary<V> newConcurrentAtomicIntegerDictionary() {
        return new ConcurrentAtomicARSWLockIntegerDictionary<>();
    }

    public static <V> @NotNull ConcurrentIntegerDictionary<V> newConcurrentAtomicIntegerDictionary(
        float loadFactor,
        int initCapacity
    ) {
        return new ConcurrentAtomicARSWLockIntegerDictionary<>(loadFactor, initCapacity);
    }

    public static <V> @NotNull ConcurrentLongDictionary<V> newConcurrentAtomicLongDictionary() {
        return new ConcurrentAtomicARSWLockLongDictionary<>();
    }

    public static <V> @NotNull ConcurrentLongDictionary<V> newConcurrentAtomicLongDictionary(
        float loadFactor,
        int initCapacity
    ) {
        return new ConcurrentAtomicARSWLockLongDictionary<>(loadFactor, initCapacity);
    }

    public static <K, V> @NotNull ConcurrentObjectDictionary<K, V> newConcurrentAtomicObjectDictionary() {
        return new ConcurrentAtomicARSWLockObjectDictionary<>();
    }

    public static <K, V> @NotNull ConcurrentObjectDictionary<K, V> newConcurrentAtomicObjectDictionary(
        float loadFactor,
        int initCapacity
    ) {
        return new ConcurrentAtomicARSWLockObjectDictionary<>(loadFactor, initCapacity);
    }

    public static <K, V> @NotNull ConcurrentObjectDictionary<K, V> newConcurrentStampedLockObjectDictionary() {
        return new ConcurrentStampedLockObjectDictionary<>();
    }

    public static <K, V> @NotNull ConcurrentObjectDictionary<K, V> newConcurrentStampedLockObjectDictionary(
        float loadFactor,
        int initCapacity
    ) {
        return new ConcurrentStampedLockObjectDictionary<>(loadFactor, initCapacity);
    }

    public static <V> @NotNull IntegerDictionary<V> newIntegerDictionary() {
        return new FastIntegerDictionary<>();
    }

    public static <V> @NotNull IntegerDictionary<V> newIntegerDictionary(float loadFactor, int initCapacity) {
        return new FastIntegerDictionary<>(loadFactor, initCapacity);
    }

    public static <V> @NotNull LongDictionary<V> newLongDictionary() {
        return new FastLongDictionary<>();
    }

    public static <V> @NotNull LongDictionary<V> newLongDictionary(int initCapacity) {
        return new FastLongDictionary<>(initCapacity);
    }

    public static <V> @NotNull LongDictionary<V> newLongDictionary(float loadFactor, int initCapacity) {
        return new FastLongDictionary<>(loadFactor, initCapacity);
    }

    public static <K, V> @NotNull ObjectDictionary<K, V> newObjectDictionary() {
        return new FastObjectDictionary<>();
    }

    public static <K, V> @NotNull ObjectDictionary<K, V> newObjectDictionary(float loadFactor, int initCapacity) {
        return new FastObjectDictionary<>(loadFactor, initCapacity);
    }

    public static <K, V> @NotNull ObjectDictionary<K, V> newReadOnlyObjectDictionary(@NotNull Object... values) {
        return new ReadOnlyFastObjectDictionary<>(values);
    }

    private DictionaryFactory() {
        throw new IllegalArgumentException();
    }
}
