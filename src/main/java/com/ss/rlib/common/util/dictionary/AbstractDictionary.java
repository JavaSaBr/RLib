package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.UnsafeArray;
import com.ss.rlib.common.util.pools.PoolFactory;
import com.ss.rlib.common.util.pools.ReusablePool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The base implementation of the {@link Dictionary}.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public abstract class AbstractDictionary<K, V, E extends Entry<E, V>> implements Dictionary<K, V> {

    /**
     * The default size of table in the {@link Dictionary}.
     */
    protected static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
     * The max size of the {@link Dictionary}.
     */
    protected static final int DEFAULT_MAXIMUM_CAPACITY = 1 << 30;

    /**
     * The load factor of the {@link Dictionary}.
     */
    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * Calculate a hash of the hashcode.
     *
     * @param hashcode the hashcode.
     * @return the hash.
     */
    protected static int hash(int hashcode) {
        hashcode ^= hashcode >>> 20 ^ hashcode >>> 12;
        return hashcode ^ hashcode >>> 7 ^ hashcode >>> 4;
    }

    /**
     * Calculate a hash of the long key.
     *
     * @param key the long key.
     * @return the hash.
     */
    protected static int hash(long key) {
        int hash = (int) (key ^ key >>> 32);
        hash ^= hash >>> 20 ^ hash >>> 12;
        return hash ^ hash >>> 7 ^ hash >>> 4;
    }

    /**
     * Get an index of table in the {@link Dictionary}.
     *
     * @param hash   the hash of a key.
     * @param length the length of a table in the {@link Dictionary}.
     * @return the index in the table.
     */
    protected static int indexFor(int hash, int length) {
        return hash & length - 1;
    }

    /**
     * The pool with entries.
     */
    @NotNull
    protected final ReusablePool<E> entryPool;

    /**
     * The load factor.
     */
    protected final float loadFactor;

    protected AbstractDictionary(float loadFactor, int initCapacity) {
        this.loadFactor = loadFactor;
        this.entryPool = PoolFactory.newReusablePool(getEntryType());
        setEntries(ArrayUtils.create(getEntryType(), initCapacity));
        setThreshold((int) (initCapacity * loadFactor));
        setSize(0);
    }

    @Override
    public final void apply(@NotNull Function<? super V, V> function) {
        for (E entry : entries()) {
            while (entry != null) {
                entry.setValue(function.apply(entry.getValue()));
                entry = entry.getNext();
            }
        }
    }

    @Override
    public void clear() {

        E[] entries = entries();
        E next;

        for (E entry : entries) {
            while (entry != null) {
                next = entry.getNext();
                entryPool.put(entry);
                entry = next;
            }
        }

        ArrayUtils.clear(entries);

        setSize(0);
    }

    @Override
    public final boolean containsValue(@Nullable V value) {

        for (E entry : entries()) {
            for (E next = entry; next != null; next = next.getNext()) {
                if (Objects.equals(value, next.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public final void forEach(@NotNull Consumer<? super V> consumer) {
        for (E entry : entries()) {
            while (entry != null) {
                consumer.accept(entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    /**
     * Resize the array of buckets of this dictionary.
     *
     * @param newLength the new size.
     */
    protected final void resize(int newLength) {

        E[] prevEntries = entries();

        int oldLength = prevEntries.length;

        if (oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
            setThreshold(Integer.MAX_VALUE);
            return;
        }

        E[] newEntries = ArrayUtils.create(getEntryType(), newLength);

        transfer(newEntries);
        setEntries(newEntries);
        setThreshold((int) (newLength * loadFactor));
    }

    /**
     * Transfer current entries to new entries.
     *
     * @param newEntries the new array of entries.
     */
    private void transfer(@NotNull E[] newEntries) {

        E[] entries = entries();

        int newCapacity = newEntries.length;

        for (E entry : entries) {

            if (entry == null) {
                continue;
            }

            do {

                E next = entry.getNext();
                int i = indexFor(entry.getHash(), newCapacity);

                entry.setNext(newEntries[i]);
                newEntries[i] = entry;
                entry = next;

            } while (entry != null);
        }
    }

    @Override
    public final @NotNull Array<V> values(@NotNull Array<V> container) {

        UnsafeArray<V> unsafeArray = container.asUnsafe();
        unsafeArray.prepareForSize(container.size() + size());

        for (E entry : entries()) {
            while (entry != null) {
                unsafeArray.unsafeAdd(entry.getValue());
                entry = entry.getNext();
            }
        }

        return container;
    }

    /**
     * Get the entries type.
     *
     * @return the entries type.
     */
    protected abstract @NotNull Class<? super E> getEntryType();

    /**
     * Set new array of entries of this {@link Dictionary}.
     *
     * @param entries the new array of entries.
     */
    protected abstract void setEntries(@NotNull E[] entries);

    /**
     * Get an array of all entries in this dictionary..
     *
     * @return the array of entries.
     */
    protected abstract @NotNull E[] entries();

    /**
     * Set the next size value at which to resize (capacity * load factor).
     *
     * @param threshold the next size.
     */
    protected abstract void setThreshold(int threshold);

    /**
     * Get the threshold.
     *
     * @return the current next size.
     */
    protected abstract int getThreshold();

    /**
     * Set the new size of this {@link Dictionary}.
     *
     * @param size the new size.
     */
    protected abstract void setSize(int size);

    /**
     * Decrement and get the size of this {@link Dictionary}.
     *
     * @return the new size of this {@link Dictionary}.
     */
    protected abstract int decrementSizeAndGet();

    /**
     * Increment and get the size of this {@link Dictionary}.
     *
     * @return the new size of this {@link Dictionary}.
     */
    protected abstract int incrementSizeAndGet();
}
