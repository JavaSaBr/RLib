package javasabr.rlib.network;

import java.nio.ByteBuffer;
import org.jspecify.annotations.Nullable;

/**
 * The interface to implement a network cryptor.
 *
 * @author JavaSaBr
 */
public interface NetworkCryptor {

  /**
   * Default NULL implementation of the network crypt.
   */
  NetworkCryptor NULL = new NetworkCryptor() {

    @Override
    public @Nullable ByteBuffer decrypt(ByteBuffer data, int length, ByteBuffer toStore) {
      return null;
    }

    @Override
    public @Nullable ByteBuffer encrypt(ByteBuffer data, int length, ByteBuffer toStore) {
      return null;
    }
  };

  /**
   * Decrypt data.
   *
   * @param data the buffer with data to decrypt.
   * @param length the data length.
   * @param toStore the buffer to store decrypted data.
   * @return the buffer with decrypted data or null if don't need to decrypt anything.
   */
  @Nullable ByteBuffer decrypt(ByteBuffer data, int length, ByteBuffer toStore);

  /**
   * Encrypt data.
   *
   * @param data the buffer with data to encrypt.
   * @param length the data length.
   * @param toStore the buffer to store encrypted data.
   * @return the buffer with encrypted data or null if don't need to decrypt encrypt.
   */
  @Nullable ByteBuffer encrypt(ByteBuffer data, int length, ByteBuffer toStore);
}
