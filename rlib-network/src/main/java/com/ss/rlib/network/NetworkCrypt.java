package com.ss.rlib.network;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

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
        public @Nullable ByteBuffer decrypt(@NotNull ByteBuffer data, int length, @NotNull ByteBuffer toStore) {
            return null;
        }

        @Override
        public @Nullable ByteBuffer encrypt(@NotNull ByteBuffer data, int length, @NotNull ByteBuffer toStore) {
            return null;
        }
    };

    /**
     * Decrypt data.
     *
     * @param data    the buffer with data to decrypt.
     * @param length  the data length.
     * @param toStore the buffer to store decrypted data.
     * @return the buffer with decrypted data or null if don't need to decrypt anything.
     */
    @Nullable ByteBuffer decrypt(@NotNull ByteBuffer data, int length, @NotNull ByteBuffer toStore);

    /**
     * Encrypt data.
     *
     * @param data    the buffer with data to encrypt.
     * @param length  the data length.
     * @param toStore the buffer to store encrypted data.
     * @return the buffer with encrypted data or null if don't need to decrypt encrypt.
     */
    @Nullable ByteBuffer encrypt(@NotNull ByteBuffer data, int length, @NotNull ByteBuffer toStore);
}
