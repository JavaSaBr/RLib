package com.ss.rlib.common.util.dictionary;

/**
 * The base implementation of the {@link Dictionary}.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public abstract class AbstractDictionary<K, V> implements Dictionary<K, V> {

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
     * Gets the hash for the hashcode of the key.
     *
     * @param hashcode the hashcode.
     * @return the hash for the {@link Dictionary}.
     */
    protected static int hash(int hashcode) {
        hashcode ^= hashcode >>> 20 ^ hashcode >>> 12;
        return hashcode ^ hashcode >>> 7 ^ hashcode >>> 4;
    }

    /**
     * Gets the hash for the long key of the key.
     *
     * @param key the long key.
     * @return the hash for the {@link Dictionary}.
     */
    protected static int hash(final long key) {
        int hash = (int) (key ^ key >>> 32);
        hash ^= hash >>> 20 ^ hash >>> 12;
        return hash ^ hash >>> 7 ^ hash >>> 4;
    }

    /**
     * Gets the index of table in the {@link Dictionary}.
     *
     * @param hash   the hash of the key.
     * @param length the length of the table in the {@link Dictionary}.
     * @return the index of this hash in the table.
     */
    protected static int indexFor(final int hash, final int length) {
        return hash & length - 1;
    }

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
