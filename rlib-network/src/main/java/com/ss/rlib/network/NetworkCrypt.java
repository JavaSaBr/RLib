package com.ss.rlib.network;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface to implement a network crypt.
 *
 * @author JavaSaBr
 */
public interface NetworkCrypt {

    /**
     * Default NULL implementation of the network crypt.
     */
    @NotNull NetworkCrypt NULL = new NetworkCrypt() {

        @Override
        public @Nullable byte[] decrypt(@NotNull byte[] data, int offset, int length) {
            return null;
        }

        @Override
        public @Nullable byte[] encrypt(@NotNull byte[] data, int offset, int length) {
            return null;
        }

        @Override
        public boolean isNull() {
            return true;
        }
    };

    /**
     * Return true if this crypt doesn't crypt data.
     *
     * @return true if this crypt doesn't crypt data.
     */
    default boolean isNull() {
        return false;
    }

    /**
     * Decrypt the byte array.
     *
     * @param data   the byte array.
     * @param offset the offset.
     * @param length the byte count.
     * @return the decrypted data or null if this crypt implementation does decrypting inside the passed byte array.
     */
    @Nullable byte[] decrypt(@NotNull byte[] data, int offset, int length);

    /**
     * Encrypt the byte array.
     *
     * @param data   the byte array.
     * @param offset the offset.
     * @param length the byte count.
     * @return the encrypted data or null if this crypt implementation does encrypting inside the passed byte array.
     */
    @Nullable byte[] encrypt(@NotNull byte[] data, int offset, int length);
}
