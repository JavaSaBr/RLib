package rlib.network;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a network crypt.
 *
 * @author JavaSaBr
 */
public interface NetworkCrypt {

    /**
     * Decrypt a byte array.
     *
     * @param data   the byte array.
     * @param offset the offset.
     * @param length the byte count.
     */
    void decrypt(@NotNull byte[] data, int offset, int length);

    /**
     * Encrypt a byte array.
     *
     * @param data   the byte array.
     * @param offset the offset.
     * @param length the byte count.
     */
    void encrypt(@NotNull byte[] data, int offset, int length);
}
